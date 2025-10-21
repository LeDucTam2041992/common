package com.kpro.common.minio.request;

import java.io.File;

public class S3DirectoryUploadRequest {
  private String bucketName;
  private String virtualDirectoryKeyPrefix;
  private File uploadDirectory;
  private boolean publicReadable;

  private S3DirectoryUploadRequest(
      String bucketName, String virtualDirectoryKeyPrefix, File uploadDirectory) {
    this.bucketName = bucketName;
    this.virtualDirectoryKeyPrefix = virtualDirectoryKeyPrefix;
    this.uploadDirectory = uploadDirectory;
  }

  public static S3DirectoryUploadRequest fromDirectory(
      String bucketName, String virtualDirectoryKeyPrefix, File uploadDirectory) {
    return new S3DirectoryUploadRequest(bucketName, virtualDirectoryKeyPrefix, uploadDirectory);
  }

  public S3DirectoryUploadRequest withBucketName(String bucketName) {
    this.bucketName = bucketName;
    return this;
  }

  public S3DirectoryUploadRequest withVirtualDirectoryKeyPrefix(String virtualDirectoryKeyPrefix) {
    this.virtualDirectoryKeyPrefix = virtualDirectoryKeyPrefix;
    return this;
  }

  public S3DirectoryUploadRequest withUploadDirectory(File uploadDirectory) {
    this.uploadDirectory = uploadDirectory;
    return this;
  }

  public S3DirectoryUploadRequest withPublicReadable(boolean publicReadable) {
    this.publicReadable = publicReadable;
    return this;
  }

  public String getBucketName() {
    return this.bucketName;
  }

  public void setBucketName(final String bucketName) {
    this.bucketName = bucketName;
  }

  public String getVirtualDirectoryKeyPrefix() {
    return this.virtualDirectoryKeyPrefix;
  }

  public void setVirtualDirectoryKeyPrefix(final String virtualDirectoryKeyPrefix) {
    this.virtualDirectoryKeyPrefix = virtualDirectoryKeyPrefix;
  }

  public File getUploadDirectory() {
    return this.uploadDirectory;
  }

  public void setUploadDirectory(final File uploadDirectory) {
    this.uploadDirectory = uploadDirectory;
  }

  public boolean isPublicReadable() {
    return this.publicReadable;
  }

  public void setPublicReadable(final boolean publicReadable) {
    this.publicReadable = publicReadable;
  }
}
