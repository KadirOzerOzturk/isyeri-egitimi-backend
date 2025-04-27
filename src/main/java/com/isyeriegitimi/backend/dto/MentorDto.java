package com.isyeriegitimi.backend.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MentorDto {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UUID companyId;
    private String about;
    private String title;


}