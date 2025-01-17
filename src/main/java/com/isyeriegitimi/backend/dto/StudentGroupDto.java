package com.isyeriegitimi.backend.dto;

import com.isyeriegitimi.backend.model.Lecturer;
import lombok.Data;

import java.util.UUID;

@Data
public class StudentGroupDto {
    private UUID groupId;

    private Lecturer lecturer;

}
