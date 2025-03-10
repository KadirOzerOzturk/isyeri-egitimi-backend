package com.isyeriegitimi.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownloadFormRequest {

    private UUID formId;
    private UUID userId;
    private String userRole;


}
