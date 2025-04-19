package com.isyeriegitimi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResultDto {

    UUID id;
    String name;
    String role;

}
