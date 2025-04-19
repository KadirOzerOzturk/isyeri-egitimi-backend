package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.Lecturer;
import com.isyeriegitimi.backend.model.StudentGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LecturerRepository extends JpaRepository<Lecturer,UUID> {


    Optional<Lecturer> findByLecturerNumber(String lecturerNo);

    Optional<Lecturer> findByLecturerId(UUID lecturerId);
    List<StudentGroup> findAllByLecturerId(UUID id);

    Optional<Lecturer> findByEmail(String email);

    List<Lecturer> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String searchText, String searchText1);
}
