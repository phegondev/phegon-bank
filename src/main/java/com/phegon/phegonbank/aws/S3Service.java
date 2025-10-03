package com.phegon.phegonbank.aws;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public String uploadFile(MultipartFile file, String folderName) throws IOException, S3Exception {
        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String newFileName = UUID.randomUUID() + fileExtension;
        String s3Key = folderName + "/" + newFileName;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(s3Key)).toString();
    }

    public boolean deleteFile(String fileUrl) {
        try {
            String key = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key("profile-pictures/" + key)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
            return true;

        } catch (S3Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
}









