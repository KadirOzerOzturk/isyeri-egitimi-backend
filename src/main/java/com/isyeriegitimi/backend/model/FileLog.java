package com.isyeriegitimi.backend.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.isyeriegitimi.backend.converter.JsonNodeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FileLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String fileName;
    private String fileType;

    private UUID ownerId;
    private String ownerRole;
    @Column(columnDefinition = "TEXT")
    private String data;
    private String barcodeNumber;
    private Date uploadDate;
    private Date deleteDate;


}
