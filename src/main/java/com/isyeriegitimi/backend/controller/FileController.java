package com.isyeriegitimi.backend.controller;


import com.isyeriegitimi.backend.model.File;
import com.isyeriegitimi.backend.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/file")
@RestController
public class FileController {

    private FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveFile(@RequestBody File file){
        fileService.save(file);
        return ResponseEntity.ok("Successfully saved");
    }

    @GetMapping("")
    public ResponseEntity<?> getAllFiles(){

        return ResponseEntity.ok( fileService.getAll());
    }

}
