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

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
            // Check if the file is an image
            if (fileInfo.getFileType().startsWith("image")) {
                byte[] imageBytes = Base64.getDecoder().decode(fileInfo.getData());
                ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
                BufferedImage originalImage = ImageIO.read(inputStream);

                // Proceed only if the image was successfully read
                if (originalImage == null) {
                    throw new IllegalStateException("Invalid image data");
                }

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                // Find an image writer for JPG format
                Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
                if (!writers.hasNext()) throw new IllegalStateException("No writer found for jpg");

                ImageWriter writer = writers.next();
                ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream);
                writer.setOutput(ios);

                ImageWriteParam param = writer.getDefaultWriteParam();
                if (param.canWriteCompressed()) {
                    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    param.setCompressionQuality(0.5f); // Compression quality
                }

                writer.write(null, new IIOImage(originalImage, null, null), param);
                ios.close();
                writer.dispose();

                // Convert the compressed image to Base64
                String compressedBase64 = Base64.getEncoder().encodeToString(outputStream.toByteArray());
                fileInfo.setData(compressedBase64);
            }

            // Save the file and return its ID (without modification if it's not an image)
            return fileInfoRepository.save(fileInfo).getId();

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
       try  {
            FileInfo existingFileInfo = fileInfoRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("File", "id", id.toString()));
            existingFileInfo.setFileName(fileInfo.getFileName());
            existingFileInfo.setFileType(fileInfo.getFileType());
            existingFileInfo.setOwnerId(fileInfo.getOwnerId());
            existingFileInfo.setOwnerRole(fileInfo.getOwnerRole());
            existingFileInfo.setData(fileInfo.getData());
            existingFileInfo.setBarcodeNumber(fileInfo.getBarcodeNumber());
            existingFileInfo.setUploadDate(fileInfo.getUploadDate());

            fileInfoRepository.save(existingFileInfo);
        }catch (Exception e){
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

    public List<FileInfo> getCompanyPhotos() {
        try {
            List<FileInfo> companyPhotos = fileInfoRepository.findAllByFileNameAndOwnerRole("profilePhoto","COMPANY");
            return companyPhotos;
        }catch (Exception e){
            throw new InternalServerErrorException("An error occurred while fetching the files: " + e.getMessage());
        }
    }
}
