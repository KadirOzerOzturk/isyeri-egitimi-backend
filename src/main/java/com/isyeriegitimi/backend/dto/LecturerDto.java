package com.isyeriegitimi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LecturerDto {
    private UUID lecturerId;
    private String firstName;
    private String lastName;
    private String email;
    private String faculty;
    private String about;
    private String lecturerNumber;
    private String password;
}
