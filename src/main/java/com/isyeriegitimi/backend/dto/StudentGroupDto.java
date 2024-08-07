package com.isyeriegitimi.backend.dto;

import com.isyeriegitimi.backend.model.Lecturer;
import lombok.Data;

@Data
public class StudentGroupDto {
    private int grupId;

    private Lecturer izleyici;

}
