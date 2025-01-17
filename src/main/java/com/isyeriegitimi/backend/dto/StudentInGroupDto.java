package com.isyeriegitimi.backend.dto;

import com.isyeriegitimi.backend.model.Student;
import com.isyeriegitimi.backend.model.StudentGroup;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.UUID;

@Data
public class StudentInGroupDto {

    UUID id;
    private StudentGroup studentGroup;
    private Student student;


}
