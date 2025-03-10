package com.isyeriegitimi.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isyeriegitimi.backend.dto.FileInfoDto;
import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.model.FileInfo;
import com.isyeriegitimi.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class FileService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FileInfoRepository fileInfoRepository;
    @Autowired
    private LecturerRepository lecturerRepository;
    @Autowired
    private CommissionRepository commissionRepository;
    @Autowired
    private CompanyRepository companyRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public UUID uploadFile(FileInfo fileInfo) {
        try {

            Set<UUID> uniqueOwnerIds = new HashSet<>();

            if (fileInfo.getOwners().isArray()) {
                for (JsonNode ownerNode : fileInfo.getOwners()) {
                    if (ownerNode.isObject() && ownerNode.has("id")) {
                        String idStr = ownerNode.get("id").asText();
                        UUID owner = UUID.fromString(idStr);

                        if (!uniqueOwnerIds.add(owner)) {
                            throw new IllegalArgumentException("Founded duplicated id in owners. Duplicated id : " + owner);
                        }

                        if (studentRepository.findById(owner).isEmpty() &&
                                lecturerRepository.findById(owner).isEmpty() &&
                                commissionRepository.findById(owner).isEmpty() &&
                                companyRepository.findById(owner).isEmpty()) {
                            throw new ResourceNotFoundException("Owner", "id", owner.toString());
                        }
                    } else {
                        throw new IllegalArgumentException("Owners field should be an array of objects with id field.");
                    }
                }
            } else {
                throw new IllegalArgumentException("Owners field should be an json array.");
            }

            UUID id = UUID.randomUUID();
            fileInfoRepository.insertFile(
                    id,
                    fileInfo.getFileName(),
                    fileInfo.getFileType(),
                    objectMapper.writeValueAsString(fileInfo.getOwners()),
                    fileInfo.getData(),
                    fileInfo.getBarcodeNumber()
            );
            return id;
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while uploading the file: " + e.getMessage());

        }
    }

    public List<FileInfo> getFiles() {
        try {
            List<FileInfo> files = fileInfoRepository.findAll();
            return files;
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while fetching the files: " + e.getMessage());
        }
    }
    public FileInfoDto getFileById(UUID id){
        try {
            FileInfo fileInfo = fileInfoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("File", "id", id.toString()));
            return FileInfoDto.builder()
                    .id(fileInfo.getId())
                    .fileName(fileInfo.getFileName())
                    .fileType(fileInfo.getFileType())
                    .owners(objectMapper.writeValueAsString(fileInfo.getOwners()))
                    .data(fileInfo.getData())
                    .barcodeNumber(fileInfo.getBarcodeNumber())
                    .build();
        }catch (Exception e){
            throw new InternalServerErrorException("An error occurred while fetching the file: " + e.getMessage());
        }
    }

    public void updateFile(UUID id, FileInfo fileInfo) {
        try {
            FileInfo file = fileInfoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("File", "id", id.toString()));
            file.setFileName(fileInfo.getFileName());
            file.setFileType(fileInfo.getFileType());
            file.setOwners(fileInfo.getOwners());
            file.setData(fileInfo.getData());
            file.setBarcodeNumber(fileInfo.getBarcodeNumber());
            fileInfoRepository.save(file);
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while updating the file: " + e.getMessage());
        }
    }

    public FileInfoDto getFile(UUID userId, String userRole, String fileName) {
        try {
            // JSON formatında owner bilgisini oluştur
            String ownerJson = String.format("{\"id\": \"%s\"}", userId);

            // Fetch FileInfo from DB
            FileInfo fileInfo = fileInfoRepository.findByOwnersContainingAndFileName(ownerJson, fileName)
                    .orElseThrow(() -> new ResourceNotFoundException("File not found by given id: " + userId));

            // Convert to DTO
            return FileInfoDto.builder()
                    .id(fileInfo.getId())
                    .fileName(fileInfo.getFileName())
                    .fileType(fileInfo.getFileType())
                    .owners(String.valueOf(fileInfo.getOwners()))
                    .data(fileInfo.getData())
                    .barcodeNumber(fileInfo.getBarcodeNumber())
                    .build();
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while fetching the file: " + e.getMessage());
        }
    }
}
