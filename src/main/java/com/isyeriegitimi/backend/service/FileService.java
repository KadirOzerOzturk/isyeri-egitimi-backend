package com.isyeriegitimi.backend.service;

import com.isyeriegitimi.backend.model.File;
import com.isyeriegitimi.backend.repository.FileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    private FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public void save(File file){
        fileRepository.save(file);
    }

    public List<File> getAll() {
        return fileRepository.findAll();
    }
}
