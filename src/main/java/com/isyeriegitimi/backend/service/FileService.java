package com.isyeriegitimi.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isyeriegitimi.backend.dto.FileInfoDto;
import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.model.FileInfo;
import com.isyeriegitimi.backend.model.FileLog;
import com.isyeriegitimi.backend.repository.*;
import jakarta.transaction.Transactional;
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
    @Autowired
    private FileLogRepository fileLogRepository;

    public UUID uploadFile(FileInfo fileInfo) {
        try {
            fileInfoRepository.save(fileInfo);
            return  fileInfoRepository.save(fileInfo).getId();
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
                    .ownerId(fileInfo.getOwnerId())
                    .ownerRole(fileInfo.getOwnerRole())
                    .data(fileInfo.getData())
                    .deleteDate(fileInfo.getDeleteDate())
                    .uploadDate(fileInfo.getUploadDate())
                    .barcodeNumber(fileInfo.getBarcodeNumber())
                    .build();
        }catch (Exception e){
            throw new InternalServerErrorException("An error occurred while fetching the file: " + e.getMessage());
        }
    }

    public void updateFile(UUID id, FileInfo fileInfo) {
        try {
            FileInfo file = fileInfoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("File", "id", id.toString()));
            fileInfo.setId(file.getId());
            fileInfoRepository.save(fileInfo);
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while updating the file: " + e.getMessage());
        }
    }

    public FileInfoDto getFile(UUID userId, String userRole, String fileName) {
        try {


            FileInfo fileInfo = fileInfoRepository.findByFileNameAndOwnerIdAndOwnerRole(fileName, userId, userRole)
                    .orElseThrow(() -> new ResourceNotFoundException("File not found by given id: " + userId));

            // Convert to DTO
            return FileInfoDto.builder()
                    .id(fileInfo.getId())
                    .fileName(fileInfo.getFileName())
                    .fileType(fileInfo.getFileType())
                    .ownerId(fileInfo.getOwnerId())
                    .ownerRole(fileInfo.getOwnerRole())
                    .data(fileInfo.getData())
                    .barcodeNumber(fileInfo.getBarcodeNumber())
                    .uploadDate(fileInfo.getUploadDate())
                    .deleteDate(fileInfo.getDeleteDate())
                    .build();
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while fetching the file: " + e.getMessage());
        }
    }

    public List<FileInfo> getFilesByUserId(UUID userId, String userRole) {
        try {


            return fileInfoRepository.findAllByOwnerIdAndOwnerRole(userId, userRole);
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
    @Transactional
    public void deleteFile(UUID id) {
        try {
            FileInfo fileInfo = fileInfoRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("File", "id", id.toString()));
            FileLog fileLog = FileLog.builder()
                    .fileName(fileInfo.getFileName())
                    .fileType(fileInfo.getFileType())
                    .ownerId(fileInfo.getOwnerId())
                    .ownerRole(fileInfo.getOwnerRole())
                    .data(fileInfo.getData())
                    .barcodeNumber(fileInfo.getBarcodeNumber())
                    .uploadDate(fileInfo.getUploadDate())
                    .deleteDate(new Date())
                    .build();
            fileLogRepository.save(fileLog);
            fileInfoRepository.deleteById(id);


        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while deleting the file: " + e.getMessage());
        }
    }
}
