package com.neul.itemexchange.controller;

import com.neul.itemexchange.util.S3Uploader;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("test/upload")
public class UploadTestController {

  private final S3Uploader s3Uploader;

  @PostMapping
  public ResponseEntity<String> upload(@RequestParam MultipartFile file) throws IOException {
    String url = s3Uploader.upload(file, "test");
    return ResponseEntity.ok(url);
  }
}
