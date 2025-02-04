package com.isyeriegitimi.backend.dto;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FormDto {
    private UUID id;
    private String title;
    private String description;

}
