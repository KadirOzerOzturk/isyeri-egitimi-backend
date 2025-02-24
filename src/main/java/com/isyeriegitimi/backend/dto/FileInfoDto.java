package com.isyeriegitimi.backend.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileInfoDto {
    private UUID id;
    private String fileName;
    private String fileType;
    private String owners;
    private String signedBy;
    private String data;
    private String barcodeNumber;
}
