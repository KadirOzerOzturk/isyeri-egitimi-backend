package com.isyeriegitimi.backend.service;

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
    public List<FileInfo> getFiles(){
        try {
            return fileInfoRepository.findAll();
        }catch (Exception e){
            throw new InternalServerErrorException("An error occurred while fetching the files: " + e.getMessage());
        }
    }


}
