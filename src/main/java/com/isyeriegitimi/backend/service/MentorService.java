package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.dto.MentorDto;
import com.isyeriegitimi.backend.dto.StudentDto;
import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.model.Email;
import com.isyeriegitimi.backend.model.Mentor;
import com.isyeriegitimi.backend.model.Student;
import com.isyeriegitimi.backend.repository.MentorRepository;
import com.isyeriegitimi.backend.repository.StudentRepository;
import com.isyeriegitimi.backend.security.dto.UserRequest;
import com.isyeriegitimi.backend.security.enums.Role;
import com.isyeriegitimi.backend.security.service.AuthenticationService;
import com.isyeriegitimi.backend.security.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MentorService {

    private MentorRepository mentorRepository;
    private EmailService emailService;
    private UserService userService;
    private AuthenticationService authenticationService;
    private StudentRepository studentRepository;

    public MentorService(MentorRepository mentorRepository, EmailService emailService, UserService userService, AuthenticationService authenticationService, StudentRepository studentRepository) {
        this.mentorRepository = mentorRepository;
        this.emailService = emailService;
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.studentRepository = studentRepository;
    }

    @Transactional
    public UUID createMentor(MentorDto mentorDto) {
        try {
            Mentor mentor = mentorRepository.save(mapToMentor(mentorDto));

            String generatedPassword = UserService.generatePassword(6);
            UserRequest userRequest = new UserRequest();
            userRequest.setFirstName(mentor.getFirstName());
            userRequest.setLastName(mentor.getLastName());
            userRequest.setUsername(mentor.getEmail());
            userRequest.setTitle(Role.MENTOR.toString());
            userRequest.setPassword(generatedPassword);
            authenticationService.save(userRequest);
            Email email = new Email();
            email.setTo(mentorDto.getEmail());
            email.setSubject(mentorDto.getFirstName() + " " + mentorDto.getLastName());
            email.setMessage("Kaydınız firmanız tarafından oluşturulmuştur. Lütfen tek kullanımlık şifrenizi değiştiriniz.\n Şifreniz:\n "+generatedPassword );
            emailService.sendEmail(email);
            return mentor.getId();
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while creating the mentor. Error: " + e.getMessage());
        }
    }
   @Transactional
   public void updateMentor(MentorDto mentorDto, UUID mentorId) {
       try {
           Optional<Mentor> mentor = mentorRepository.findById(mentorId);
           if (mentor.isPresent()) {
               Mentor existingMentor = mentor.get();
               String oldEmail = existingMentor.getEmail();
               String newEmail = mentorDto.getEmail();
               existingMentor.setFirstName(mentorDto.getFirstName());
               existingMentor.setLastName(mentorDto.getLastName());
               existingMentor.setEmail(mentorDto.getEmail());
               existingMentor.setCompany(mentorDto.getCompany());
               existingMentor.setPhone(mentorDto.getPhone());
               existingMentor.setAbout(mentorDto.getAbout());



               if (newEmail != null && !oldEmail.equals(newEmail)) {
                   System.out.println("Updating user email from " + oldEmail + " to " + newEmail);
                   userService.updateUsernameByEmail(oldEmail, newEmail);
               }

               mentorRepository.save(existingMentor);
           } else {
               throw new ResourceNotFoundException("Mentor", "ID", mentorId.toString());
           }
       } catch (Exception e) {
           throw new InternalServerErrorException("An error occurred while updating the mentor: " + e.getMessage());
       }
   }
    public List<StudentDto> getStudentsByMentor(UUID mentorId) {
        try{
            List< Student > studentList = studentRepository.findByMentorId(mentorId);
            List<StudentDto> studentDtoList = List.of();
            if (studentList.isEmpty()) {
                return studentDtoList;
            }
            studentDtoList = studentList.stream()
                    .map(student -> StudentService.mapToDto(student))
                    .collect(Collectors.toList());
            return studentDtoList;
        }catch (Exception e){
            throw new InternalServerErrorException("An error occurred while fetching the students: " + e.getMessage());
        }
    }
    public List<MentorDto> getMentorsByCompanyId(UUID companyId) {
        try{
            List<Mentor> mentors = mentorRepository.findByCompanyCompanyId(companyId);
            List<MentorDto> mentorDtoList = List.of();
            if (mentors.isEmpty()) {
                return mentorDtoList;
            }
            mentorDtoList = mentors.stream().map(
                    mentor -> mapToDto(mentor))
                    .collect(Collectors.toList());
            return mentorDtoList;
        }
        catch (Exception e){
            throw new InternalServerErrorException("An error occurred while fetching the students: " + e.getMessage());
        }
    }
    private Mentor mapToMentor(MentorDto mentorDto) {
        Mentor mentor = new Mentor();
        mentor.setId(mentorDto.getId());
        mentor.setFirstName(mentorDto.getFirstName());
        mentor.setLastName(mentorDto.getLastName());
        mentor.setCompany(mentorDto.getCompany());
        mentor.setEmail(mentorDto.getEmail());
        mentor.setPhone(mentorDto.getPhone());
        mentor.setAbout(mentorDto.getAbout());
        return mentor;

    }
    private MentorDto mapToDto(Mentor mentor) {
        MentorDto mentorDto = new MentorDto();
        mentorDto.setId(mentor.getId());
        mentorDto.setFirstName(mentor.getFirstName());
        mentorDto.setLastName(mentor.getLastName());
        mentorDto.setCompany(mentor.getCompany());
        mentorDto.setEmail(mentor.getEmail());
        mentorDto.setPhone(mentor.getPhone());
        mentorDto.setAbout(mentor.getAbout());
        return mentorDto;
    }


    public MentorDto getMentorById(UUID id) {
        try {
            Optional<Mentor> mentor = mentorRepository.findById(id);
            if (mentor.isPresent()) {
                return mapToDto(mentor.get());
            } else {
                throw new ResourceNotFoundException("Mentor", "ID", id.toString());
            }
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while fetching the mentor: " + e.getMessage());
        }
    }
}
