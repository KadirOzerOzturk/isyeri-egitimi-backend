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

            validateOwners(fileInfo.getOwners());

            // Generate a random UUID for the new file
            UUID id = UUID.randomUUID();

            // Insert the file info into the repository
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

    private void validateOwners(JsonNode owners) {
        if (!owners.isArray()) {
            throw new IllegalArgumentException("Owners field should be a JSON array.");
        }

        Set<UUID> uniqueOwnerIds = new HashSet<>();

        for (JsonNode ownerNode : owners) {
            validateOwner(ownerNode, uniqueOwnerIds);
        }
    }

    private void validateOwner(JsonNode ownerNode, Set<UUID> uniqueOwnerIds) {
        if (!ownerNode.isObject() || !ownerNode.has("id")) {
            throw new IllegalArgumentException("Each owner should be an object with an 'id' field.");
        }

        String idStr = ownerNode.get("id").asText();
        String role= ownerNode.get("role").asText();
        UUID ownerId = UUID.fromString(idStr);

        // Check for duplicate owner ID
        if (!uniqueOwnerIds.add(ownerId)) {
            throw new IllegalArgumentException("Found duplicated id in owners. Duplicated id: " + ownerId);
        }

        // Validate that the owner exists in one of the repositories
        if (!role.equalsIgnoreCase("announcement")  &&  isOwnerNotFound(ownerId)) {
            throw new ResourceNotFoundException("Owner", "id", ownerId.toString());
        }
    }

    private boolean isOwnerNotFound(UUID ownerId) {
        return studentRepository.findById(ownerId).isEmpty() &&
                lecturerRepository.findById(ownerId).isEmpty() &&
                commissionRepository.findById(ownerId).isEmpty() &&
                companyRepository.findById(ownerId).isEmpty();
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

            fileInfoRepository.updateFile(
                    id,
                    fileInfo.getFileName(),
                    fileInfo.getFileType(),
                    objectMapper.writeValueAsString(fileInfo.getOwners()),
                    fileInfo.getData(),
                    fileInfo.getBarcodeNumber()
            );
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

    public List<FileInfo> getFilesByUserId(UUID userId, String userRole) {
        try {
            String ownerRoleJson = String.format("[{\"id\": \"%s\", \"role\": \"%s\"}]", userId, userRole);

            return fileInfoRepository.findByOwner(ownerRoleJson);
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while fetching the files: " + e.getMessage());
        }
    }


    public List<FileInfo> getFilesByName(String fileName) {
        try {
            return fileInfoRepository.findAllByFileName(fileName);
        }catch (Exception e){
            throw new InternalServerErrorException("An error occurred while fetching the files: " + e.getMessage());

        }

    }
}
