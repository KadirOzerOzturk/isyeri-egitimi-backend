package com.isyeriegitimi.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String fileName;
    private String fileType;

    private UUID  ownerId;
    private String ownerRole;
    @Column(columnDefinition = "TEXT")
    private String data;
    private Date uploadDate;
    private Date deleteDate;
    private String barcodeNumber;
}
