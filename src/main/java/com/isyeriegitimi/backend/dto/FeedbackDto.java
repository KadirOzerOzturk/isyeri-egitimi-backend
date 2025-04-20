package com.isyeriegitimi.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDto {
    private String name;
    private String surname;
    private String email;
    private String subject;
    private String message;
}
