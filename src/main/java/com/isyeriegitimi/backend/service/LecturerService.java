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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LecturerService {

    private final LecturerRepository lecturerRepository;
    private final StudentsInGroupRepository studentsInGroupRepository;
    private final AuthenticationService authenticationService;

    @Autowired
    public LecturerService(LecturerRepository lecturerRepository, StudentsInGroupRepository studentsInGroupRepository, AuthenticationService authenticationService) {
        this.lecturerRepository = lecturerRepository;
        this.studentsInGroupRepository = studentsInGroupRepository;
        this.authenticationService = authenticationService;
    }

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
           Lecturer existingLecturer= lecturerRepository.findById(lecturerId)
                    .orElseThrow(()-> new ResourceNotFoundException("Lecturer", "lecturerId", lecturerId.toString()));
            Lecturer lecturer = mapToLecturerEntity(lecturerDto);
            lecturer.setLecturerId(lecturerId);
            lecturerRepository.save(lecturer);
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
            StudentGroup studentGroup = studentsInGroupRepository.findByStudent_StudentId(studentId)
                    .orElseThrow(() -> new RuntimeException("Student group not found"))
                    .getStudentGroup();

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
                .password(lecturerDto.getPassword())
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
            authenticationService.save(new UserRequest( lecturerDto.getEmail(), lecturerDto.getPassword(),Role.LECTURER.toString()));
            return lecturer.getLecturerId();
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while saving the lecturer: " + e.getMessage());
        }
    }


}
