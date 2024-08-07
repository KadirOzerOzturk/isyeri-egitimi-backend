package com.isyeriegitimi.backend.dto;


import lombok.Data;

@Data
public class StudentLoginDto {
    private Long ogrenciNo;
    private String parola;
}