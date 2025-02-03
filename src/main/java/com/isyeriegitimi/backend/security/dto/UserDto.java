package com.isyeriegitimi.backend.security.dto;

import com.isyeriegitimi.backend.security.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID id;
    private String nameSurname;
    private String username;
    private String password;
    private String title;
    private Role role;
}
