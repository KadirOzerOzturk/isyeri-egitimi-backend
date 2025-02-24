package com.isyeriegitimi.backend.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.isyeriegitimi.backend.converter.JsonNodeConverter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@Entity
@RequiredArgsConstructor
public class FileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String fileName;
    private String fileType;
    @Column(name = "owners", columnDefinition = "jsonb")
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode owners;
    @Column(name = "signed_by", columnDefinition = "jsonb")
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode signedBy;
    @Column(columnDefinition = "TEXT")
    private String data;
    private String barcodeNumber;


}
