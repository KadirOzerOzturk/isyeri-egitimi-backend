package com.isyeriegitimi.backend.security.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @Valid
    private String username;
    @Valid
    private String password;
    private String firstName;
    private String lastName;
    @Valid
    private String title;
    private String name;
}
