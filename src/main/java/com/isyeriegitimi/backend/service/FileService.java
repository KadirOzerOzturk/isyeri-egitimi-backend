package com.isyeriegitimi.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isyeriegitimi.backend.dto.FileInfoDto;
import com.isyeriegitimi.backend.exceptions.InternalServerErrorException;
import com.isyeriegitimi.backend.exceptions.ResourceNotFoundException;
import com.isyeriegitimi.backend.model.FileInfo;
import com.isyeriegitimi.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

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

    public UUID uploadFile(FileInfo fileInfo, MultipartFile file) {
        try {

            // Base64 stringine çevir
            String base64Data = Base64.getEncoder().encodeToString(file.getBytes());
            UUID id = UUID.randomUUID();
            fileInfoRepository.insertFile(
                    id,
                    fileInfo.getFileName(),
                    fileInfo.getFileType(),
                    fileInfo.getOwners().toString(),
                    fileInfo.getSignedBy().toString(),
                    base64Data,
                    fileInfo.getBarcodeNumber()
            );
            return id;
        }catch (Exception e){
            throw new InternalServerErrorException("An error occurred while uploading the file: " + e.getMessage());
        }

    }
    public List<FileInfo> getFiles() {
        try {
            List<FileInfo> files = fileInfoRepository.findAllFiles();
            for (FileInfo file : files) {
                System.out.println("File ID: " + file.getId());
                System.out.println("File Data: " + file.getData()); // Log the Base64 data
            }
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
                    .signedBy(objectMapper.writeValueAsString(fileInfo.getSignedBy()))
                    .data(fileInfo.getData())
                    .barcodeNumber(fileInfo.getBarcodeNumber())
                    .build();
        }catch (Exception e){
            throw new InternalServerErrorException("An error occurred while fetching the file: " + e.getMessage());
        }
    }


}
