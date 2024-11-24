package com.isyeriegitimi.backend.security.dto;

import com.isyeriegitimi.backend.dto.StudentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String token;
    private Object userDto;

}
