package com.isyeriegitimi.backend.aws;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.isyeriegitimi.backend.dto.PostPhotosDto;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/s3-bucket")
public class S3Controller {

    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }


    @PostMapping(path = "/upload/{folder}/{userId}/{category}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String uploadFile(@RequestParam("file") MultipartFile file,@PathVariable String category,@PathVariable String userId,@PathVariable String folder) throws IOException {
        //s3Service.uploadFile(file.getOriginalFilename(), file);
        s3Service.uploadFile(folder+"/"+userId+"/"+category, file);
        return "File uploaded";
    }


    @PostMapping(path = "/upload/posts/{postId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String uploadPostPhotos(@RequestParam("file") MultipartFile file,@PathVariable Long postId) throws IOException {
        s3Service.uploadFile("posts/"+postId, file);
        return "File uploaded";
    }


    @PostMapping(path = "/upload/forms/{userId}/{studentName}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> uploadForms(
            @RequestParam("form1") MultipartFile form1,
            @RequestParam("form8") MultipartFile form8,
            @RequestParam("form2") MultipartFile form2,
            @RequestParam("idCopy") MultipartFile idCopy,
            @RequestParam("eligibility") MultipartFile eligibility,
            @RequestParam("reportForm") MultipartFile reportForm,
            @PathVariable String userId,
            @PathVariable String studentName) throws IOException {

        try {
            s3Service.uploadFile(userId + "/form1-" + studentName, form1);
            s3Service.uploadFile(userId + "/form8-" + studentName, form8);
            s3Service.uploadFile(userId + "/form2-" + studentName, form2);
            s3Service.uploadFile(userId + "/idCopy-" + studentName, idCopy);
            s3Service.uploadFile(userId + "/eligibility-" + studentName, eligibility);
            s3Service.uploadFile(userId + "/reportForm-" + studentName, reportForm);

            return ResponseEntity.ok("Files uploaded successfully");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/download/{studentNo}/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, @PathVariable Long studentNo) {
        S3Object s3Object = s3Service.getFile(studentNo.toString() + "/" + fileName);
        String contentType = s3Object.getObjectMetadata().getContentType();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(new InputStreamResource(s3Object.getObjectContent()));
    }

    @GetMapping("/view/{folder}/{userId}/{category}")
    public ResponseEntity<InputStreamResource> viewFile(@PathVariable String category,@PathVariable String userId,@PathVariable String folder) {
        String  fileName=folder+"/"+userId+"/"+category;
        System.out.println("fileName : "+fileName);

            var s3Object = s3Service.getFile(fileName);
            var content = s3Object.getObjectContent();
            String contentType = s3Object.getObjectMetadata().getContentType();

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                    .body(new InputStreamResource(content));
    }

    @GetMapping("/list-files/{folderName}")
    public List<String> listFiles(@PathVariable String folderName) {

        return s3Service.listFilesInFolder(folderName);

    }

    @GetMapping("/list-all-folders")
    public ResponseEntity<List<String>> listAllFolders() {
        List<String> folders = s3Service.listAllFolders();
        return ResponseEntity.ok(folders);
    }


    @GetMapping("/view/posts/{postId}")
    public ResponseEntity<InputStreamResource> getPostPhotos(@PathVariable Long  postId) {
        String  fileName="posts/"+postId;

        var s3Object = s3Service.getFile(fileName);
        var content = s3Object.getObjectContent();
        String contentType = s3Object.getObjectMetadata().getContentType();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(new InputStreamResource(content));
    }




}