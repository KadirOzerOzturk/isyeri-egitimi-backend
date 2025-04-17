package com.isyeriegitimi.backend.dto;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileInfoDto {
    private UUID id;
    private String fileName;
    private String fileType;
    private UUID ownerId;
    private String ownerRole;
    private String data;
    private String barcodeNumber;
    private Date uploadDate;
    private Date deleteDate;
}
