package com.neul.itemexchange.util;

import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
@RequiredArgsConstructor
public class S3Uploader {

  private final S3Client s3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  public String upload(MultipartFile file, String dirName) throws IOException {
    String fileName = dirName + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

    PutObjectRequest putRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(fileName)
        .contentType(file.getContentType())
        .build();

    s3Client.putObject(putRequest, RequestBody.fromBytes(file.getBytes()));

    return s3Client.utilities()
        .getUrl(GetUrlRequest.builder()
            .bucket(bucket)
            .key(fileName)
            .build())
        .toString();
  }
}
