package com.isyeriegitimi.backend.dto;

import com.isyeriegitimi.backend.model.Student;
import lombok.Data;

@Data
public class SkillDto {

    private Long skillId;

    private Student ogrenci;
    private String aciklama;
    private String seviye;

}
