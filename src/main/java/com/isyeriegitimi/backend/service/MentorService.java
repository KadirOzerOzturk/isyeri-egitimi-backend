package com.isyeriegitimi.backend.service;


import com.isyeriegitimi.backend.dto.MentorDto;
import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.model.Email;
import com.isyeriegitimi.backend.model.Mentor;
import com.isyeriegitimi.backend.repository.MentorRepository;
import com.isyeriegitimi.backend.security.dto.UserRequest;
import com.isyeriegitimi.backend.security.enums.Role;
import com.isyeriegitimi.backend.security.service.AuthenticationService;
import com.isyeriegitimi.backend.security.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MentorService {




    private MentorRepository mentorRepository;
    private EmailService emailService;
    private UserService userService;
    private AuthenticationService authenticationService;

    public MentorService( AuthenticationService authenticationService, UserService userService, EmailService emailService, MentorRepository mentorRepository) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.emailService = emailService;
        this.mentorRepository = mentorRepository;
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



    private Mentor mapToMentor(MentorDto mentorDto) {
        Mentor mentor = new Mentor();
        mentor.setId(mentorDto.getId());
        mentor.setFirstName(mentorDto.getFirstName());
        mentor.setLastName(mentorDto.getLastName());
        mentor.setCompany(mentorDto.getCompany());
        mentor.setEmail(mentorDto.getEmail());
        mentor.setPhone(mentorDto.getPhone());
        return mentor;

    }

}
