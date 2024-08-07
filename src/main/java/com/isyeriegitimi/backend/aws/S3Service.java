package com.isyeriegitimi.backend.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Log4j2
public class S3Service {

    private AmazonS3 s3client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public S3Service(AmazonS3 s3client) {
        this.s3client = s3client;
    }

    public void uploadFile(String keyName, MultipartFile file) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        var putObjectResult = s3client.putObject(bucketName, keyName, file.getInputStream(), metadata);
        log.info("Uploaded file with key: " + keyName + " and ETag: " + putObjectResult.getETag());
    }

    public S3Object getFile(String keyName) {
        log.info("Fetching file with key: " + keyName);
        return s3client.getObject(bucketName, keyName);
    }
    public List<String> listAllFolders() {
        Set<String> folders = new HashSet<>();
        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withDelimiter("/");

        ListObjectsV2Result result;

        do {
            result = s3client.listObjectsV2(request);
            folders.addAll(result.getCommonPrefixes());
            for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                String key = objectSummary.getKey();
                int lastSlashIndex = key.lastIndexOf('/');
                if (lastSlashIndex > 0) {
                    folders.add(key.substring(0, lastSlashIndex + 1));
                }
            }
            request.setContinuationToken(result.getNextContinuationToken());
        } while (result.isTruncated());

        return new ArrayList<>(folders);
    }

    public List<String> listFilesInFolder(String folderName) {
        ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName).withPrefix(folderName + "/");
        ListObjectsV2Result result = s3client.listObjectsV2(req);

        return result.getObjectSummaries().stream()
                .map(s -> s.getKey().replace(folderName + "/", "")) // Remove folder prefix
                .collect(Collectors.toList());
    }
}
