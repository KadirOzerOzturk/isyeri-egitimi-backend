package com.isyeriegitimi.backend.dto;

import com.isyeriegitimi.backend.security.enums.Role;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FormDto {
    private UUID id;
    private String title;
    private String description;
    private List<Role> roles;


}
