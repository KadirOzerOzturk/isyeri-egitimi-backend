package com.isyeriegitimi.backend.dto;

import com.isyeriegitimi.backend.model.Student;
import com.isyeriegitimi.backend.model.StudentGroup;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class StudentInGroupDto {

    int id;
    private StudentGroup studentGroup;
    private Student student;


}
