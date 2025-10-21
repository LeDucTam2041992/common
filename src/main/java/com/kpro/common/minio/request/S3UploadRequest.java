package com.kpro.common.minio.request;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.web.multipart.MultipartFile;

public class S3UploadRequest {
  private String bucketName;
  private String key;
  private InputStream inputStream;
  private String contentType;
  private Long contentLength;
  private boolean publicReadable;

  private S3UploadRequest(String bucketName, String key, InputStream inputStream) {
    this.bucketName = bucketName;
    this.key = key;
    this.inputStream = inputStream;
  }

  public static S3UploadRequest fromInputStream(
      String bucketName, String key, InputStream inputStream, String contentType) {
    return (new S3UploadRequest(bucketName, key, inputStream)).withContentType(contentType);
  }

  public static S3UploadRequest fromMultipartFile(String bucketName, String key, MultipartFile file)
      throws IOException {
    String contentType = file.getContentType();
    long contentLength = file.getSize();
    InputStream inputStream = file.getInputStream();
    return (new S3UploadRequest(bucketName, key, inputStream))
        .withContentType(contentType)
        .withContentLength(contentLength);
  }

  public static S3UploadRequest fromFile(String bucketName, String key, File file)
      throws IOException {
    Path filePath = file.toPath();
    String contentType = Files.probeContentType(filePath);
    long contentLength = Files.size(filePath);
    InputStream inputStream = Files.newInputStream(filePath);
    return (new S3UploadRequest(bucketName, key, inputStream))
        .withContentType(contentType)
        .withContentLength(contentLength);
  }

  public S3UploadRequest withBucketName(String bucketName) {
    this.bucketName = bucketName;
    return this;
  }

  public S3UploadRequest withKey(String key) {
    this.key = key;
    return this;
  }

  public S3UploadRequest withInputStream(InputStream inputStream) {
    this.inputStream = inputStream;
    return this;
  }

  public S3UploadRequest withContentType(String contentType) {
    this.contentType = contentType;
    return this;
  }

  public S3UploadRequest withContentLength(Long contentLength) {
    this.contentLength = contentLength;
    return this;
  }

  public S3UploadRequest withPublicReadable(boolean publicReadable) {
    this.publicReadable = publicReadable;
    return this;
  }

  public PutObjectRequest toPutObjectRequest() {
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType(this.contentType);
    if (this.contentLength != null) {
      metadata.setContentLength(this.contentLength);
    }

    return (new PutObjectRequest(this.bucketName, this.key, this.inputStream, metadata))
        .withCannedAcl(
            this.publicReadable
                ? CannedAccessControlList.PublicRead
                : CannedAccessControlList.Private);
  }

  public String getBucketName() {
    return this.bucketName;
  }

  public void setBucketName(final String bucketName) {
    this.bucketName = bucketName;
  }

  public String getKey() {
    return this.key;
  }

  public void setKey(final String key) {
    this.key = key;
  }

  public InputStream getInputStream() {
    return this.inputStream;
  }

  public void setInputStream(final InputStream inputStream) {
    this.inputStream = inputStream;
  }

  public String getContentType() {
    return this.contentType;
  }

  public void setContentType(final String contentType) {
    this.contentType = contentType;
  }

  public Long getContentLength() {
    return this.contentLength;
  }

  public void setContentLength(final Long contentLength) {
    this.contentLength = contentLength;
  }

  public boolean isPublicReadable() {
    return this.publicReadable;
  }

  public void setPublicReadable(final boolean publicReadable) {
    this.publicReadable = publicReadable;
  }
}
