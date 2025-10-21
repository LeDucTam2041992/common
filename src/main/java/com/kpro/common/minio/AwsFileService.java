package com.kpro.common.minio;

import com.amazonaws.services.s3.model.CopyObjectResult;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.kpro.common.minio.request.S3DirectoryUploadRequest;
import com.kpro.common.minio.request.S3UploadRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface AwsFileService {
  String getStaticBucket();

  String getBucket();

  void deleteFile(String key);

  InputStream readFile(String bucketName, String keyName) throws IOException;

  String readFile(String key);

  PutObjectResult putFile(String key, String content);

  URL getPresignedURL(String keyName);

  URL getPresignedURL(String keyName, Date expiredDate);

  URL getStaticObjectURL(String keyName);

  PutObjectResult uploadToStaticBucket(
      String keyName, String mediaType, InputStream inputStream, long size);

  Object uploadMediaFile(String fileName, MultipartFile file) throws IOException;

  PutObjectResult upload(String fileName, MultipartFile file) throws IOException;

  S3Object getFile(String fileName);

  void batchDelete(List<String> keys);

  PutObjectResult uploadFile(
      String filePath, InputStream inputStream, String contentType, long size);

  URL getUrl(String bucketName, String key);

  URL getUrl(String key);

  URL getPresignedURL(String bucketName, String keyName);

  boolean existsFile(String bucketName, String filePath);

  void downloadFile(String bucketName, String keyName, String saveLocation) throws IOException;

  PutObjectResult putFile(String bucketName, String key, File file);

  PutObjectResult upload(S3UploadRequest request);

  void downloadFile(String bucketName, String keyName, File destinationFile) throws IOException;

  void downloadDirectory(String bucketName, String keyPrefix, File destinationDirectory);

  List<S3ObjectSummary> listFiles(String bucketName, String keyPrefix);

  void deleteFiles(String bucketName, String keyPrefix);

  void uploadDirectory(S3DirectoryUploadRequest request);

  void uploadListFiles(S3DirectoryUploadRequest request, List<File> files);

  void deleteFiles(String bucketName, List<String> keys);

  CopyObjectResult copy(String sourceName, String destinationName);
}
