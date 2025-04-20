package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.dto.LecturerDto;
import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.model.Lecturer;
import com.isyeriegitimi.backend.model.StudentGroup;
import com.isyeriegitimi.backend.repository.LecturerRepository;
import com.isyeriegitimi.backend.repository.StudentGroupRepository;
import com.isyeriegitimi.backend.repository.StudentsInGroupRepository;
import com.isyeriegitimi.backend.security.dto.UserRequest;
import com.isyeriegitimi.backend.security.enums.Role;
import com.isyeriegitimi.backend.security.service.AuthenticationService;
import com.isyeriegitimi.backend.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LecturerService {
    @Autowired
    private  LecturerRepository lecturerRepository;
    @Autowired
    private  StudentsInGroupRepository studentsInGroupRepository;
    @Autowired
    private UserService userService;



    public Optional<Lecturer> getLecturerByLecturerNumber(String lecturerNumber) {
        try {
           Lecturer lecturer = lecturerRepository.findByLecturerNumber(lecturerNumber)
                    .orElseThrow(() -> new ResourceNotFoundException("Lecturer", "lecturerNumber", lecturerNumber));
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while fetching the lecturer: " + e.getMessage());
        }
        return lecturerRepository.findByLecturerNumber(lecturerNumber);
    }

    public Optional<Lecturer> getLecturerByLecturerId(UUID lecturerId) {
        try {
            Lecturer lecturer = lecturerRepository.findById(lecturerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Lecturer", "lecturerId", lecturerId.toString()));
            return Optional.of(lecturer);
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while fetching the lecturer: " + e.getMessage());

        }
    }

    public void updateLecturer(UUID lecturerId, LecturerDto lecturerDto) {
        try {
           Optional<Lecturer> existingLecturer= lecturerRepository.findById(lecturerId);
           if (existingLecturer.isPresent()) {
               Lecturer lecturer = existingLecturer.get();
               String oldEmail = lecturer.getEmail();
               String newEmail = lecturerDto.getEmail();
               lecturer.setEmail(newEmail);
               lecturer.setAbout(lecturerDto.getAbout());
               lecturer.setLecturerNumber(lecturerDto.getLecturerNumber());
               lecturer.setFirstName(lecturerDto.getFirstName());
               lecturer.setLastName(lecturerDto.getLastName());
               lecturer.setFaculty(lecturerDto.getFaculty());
               lecturerRepository.save(lecturer);
               if (!oldEmail.equals(newEmail)) {
                   userService.updateUsernameByEmail(oldEmail, newEmail);
               }
           }else {
               throw new ResourceNotFoundException("Lecturer", "lecturerId", lecturerId.toString());
           }
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while updating the lecturer: " + e.getMessage());

        }
    }
    public Optional<Lecturer> getLecturerOfStudentByStudentNumber(String studentNumber) {
        try {
            StudentGroup studentGroup = studentsInGroupRepository.findByStudent_StudentNumber(studentNumber)
                    .orElseThrow(() -> new RuntimeException("Student group not found"))
                    .getStudentGroup();

            return Optional.ofNullable(studentGroup.getLecturer());
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while fetching the lecturer: " + e.getMessage());
        }
    }
    public Optional<Lecturer> getLecturerOfStudentByStudentId(UUID studentId) {
        try {

            if (studentsInGroupRepository.findByStudent_StudentId(studentId).isEmpty()) {
                return  Optional.empty();
            }
            StudentGroup studentGroup = studentsInGroupRepository.findByStudent_StudentId(studentId).get().getStudentGroup();

            return Optional.ofNullable(studentGroup.getLecturer());
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while fetching the lecturer: " + e.getMessage());
        }
    }
    public List<LecturerDto> getAllLecturers() {
        try {
            List<Lecturer> lecturers = lecturerRepository.findAll();

            return lecturers.stream().map(this::mapToDto).collect(Collectors.toList());
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while fetching the lecturers: " + e.getMessage());
        }
    }
    public Lecturer mapToLecturerEntity(LecturerDto lecturerDto) {
        return Lecturer.builder()
                .lecturerId(lecturerDto.getLecturerId())
                .firstName(lecturerDto.getFirstName())
                .lastName(lecturerDto.getLastName())
                .email(lecturerDto.getEmail())
                .faculty(lecturerDto.getFaculty())
                .about(lecturerDto.getAbout())
                .lecturerNumber(lecturerDto.getLecturerNumber())
                .build();
    }

    public LecturerDto mapToDto(Lecturer lecturer) {
        return LecturerDto.builder()
                .lecturerId(lecturer.getLecturerId())
                .firstName(lecturer.getFirstName())
                .lastName(lecturer.getLastName())
                .email(lecturer.getEmail())
                .faculty(lecturer.getFaculty())
                .about(lecturer.getAbout())
                .lecturerNumber(lecturer.getLecturerNumber())
                .build();
    }

    public UUID save(LecturerDto lecturerDto) {
        try {
            Lecturer lecturer = mapToLecturerEntity(lecturerDto);
            lecturerRepository.save(lecturer);
            return lecturer.getLecturerId();
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while saving the lecturer: " + e.getMessage());
        }
    }


}
