package com.isyeriegitimi.backend.dto;

import com.isyeriegitimi.backend.model.Student;
import lombok.Data;

import java.util.UUID;

@Data
public class SkillDto {

    private UUID skillId;
    private UUID studentId;
    private String description;
    private String level;

}
