package com.kpro.common.minio.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.kpro.common.exception.BaseException;
import com.kpro.common.minio.AwsFileService;
import com.kpro.common.minio.config.AwsS3Config;
import com.kpro.common.minio.config.FileHandlerUtils;
import com.kpro.common.minio.config.Helper;
import com.kpro.common.minio.request.S3DirectoryUploadRequest;
import com.kpro.common.minio.request.S3UploadRequest;
import jakarta.annotation.PostConstruct;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.beans.ConstructorProperties;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class AwsFileServiceImpl implements AwsFileService {
    private static final Logger log = LoggerFactory.getLogger(AwsFileServiceImpl.class);
    private final AwsS3Config awsS3Config;
    private final AmazonS3 amazonS3;
    private static final String STATIC_RESOURCE = "-statics-resource";
    private TransferManager transferManager;

    @ConstructorProperties({"awsS3Config", "amazonS3"})
    public AwsFileServiceImpl(final AwsS3Config awsS3Config, final AmazonS3 amazonS3) {
        this.awsS3Config = awsS3Config;
        this.amazonS3 = amazonS3;
    }

    public AwsS3Config getAwsS3Config() {
        return this.awsS3Config;
    }

    public AmazonS3 getAmazonS3() {
        return this.amazonS3;
    }

    public TransferManager getTransferManager() {
        return this.transferManager;
    }

    @PostConstruct
    private void postConstruct() {
        this.transferManager = TransferManagerBuilder.standard().withS3Client(this.amazonS3).build();
    }

    public String getStaticBucket() {
        String bucketName = this.awsS3Config.getBucket() + "-statics-resource";
        if (!this.amazonS3.doesBucketExistV2(bucketName)) {
            throw new BaseException("S3_BUCKET_NOT_EXISTS", bucketName);
        } else {
            return bucketName;
        }
    }

    public String getBucket() {
        return this.awsS3Config.getBucket();
    }

    public void downloadDirectory(String bucketName, String keyPrefix, File destinationDirectory) {
        try {
            this.transferManager.downloadDirectory(bucketName, keyPrefix, destinationDirectory).waitForCompletion();
        } catch (Exception var5) {
            log.error("Cannot downloadDirectory from S3", var5);
        }

    }

    public void downloadFile(String bucketName, String keyName, File destinationFile) throws IOException {
        S3Object s3object = this.amazonS3.getObject(bucketName, keyName);
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        FileUtils.copyInputStreamToFile(inputStream, destinationFile);
    }

    public InputStream readFile(String bucketName, String keyName) throws IOException {
        S3Object s3object = this.amazonS3.getObject(bucketName, keyName);
        return s3object.getObjectContent();
    }

    public String readFile(String key) {
        log.info("Read file from bucket");
        return this.amazonS3.getObjectAsString(this.awsS3Config.getBucket(), key);
    }

    public void deleteFile(String key) {
        DeleteObjectRequest request = (new DeleteObjectRequest(this.awsS3Config.getBucket(), key)).withRequesterPays(true);
        this.amazonS3.deleteObject(request);
    }

    public PutObjectResult putFile(String key, String content) {
        try {
            this.amazonS3.putObject(this.awsS3Config.getBucket(), key, content);
            InputStream is = new ByteArrayInputStream(content.getBytes());
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentType("plain/text");
            return this.amazonS3.putObject(this.awsS3Config.getBucket(), key, is, meta);
        } catch (SdkClientException var5) {
            log.error(var5.getMessage());
            return null;
        }
    }

    public URL getPresignedURL(String keyName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = (new GeneratePresignedUrlRequest(this.awsS3Config.getBucket(), keyName)).withMethod(HttpMethod.GET).withExpiration(Helper.nextDays(1));
        return this.amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
    }

    public URL getPresignedURL(String keyName, Date expiredDate) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = (new GeneratePresignedUrlRequest(this.awsS3Config.getBucket(), keyName)).withMethod(HttpMethod.GET).withExpiration(expiredDate);
        return this.amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
    }

    public URL getStaticObjectURL(String keyName) {
        String staticBucketName = this.awsS3Config.getBucket().concat("-statics-resource");
        return this.amazonS3.getUrl(staticBucketName, keyName);
    }

    public PutObjectResult uploadToStaticBucket(String keyName, String mediaType, InputStream inputStream) {
        String staticBucketName = this.awsS3Config.getBucket().concat("-statics-resource");
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType(mediaType);
        PutObjectRequest putObjectRequest = (new PutObjectRequest(staticBucketName, keyName, inputStream, meta)).withCannedAcl(CannedAccessControlList.PublicRead);
        return this.amazonS3.putObject(putObjectRequest);
    }

    public Object uploadMediaFile(String fileName, MultipartFile file) throws IOException {
        InputStream is = file.getInputStream();
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType("application/octet-stream");
        return this.amazonS3.putObject(this.awsS3Config.getBucket(), fileName, is, meta);
    }

    public PutObjectResult upload(String fileName, MultipartFile file) throws IOException {
        InputStream is = file.getInputStream();
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType("image/jpeg");
        meta.setContentLength(file.getSize());
        return this.amazonS3.putObject(this.awsS3Config.getBucket(), fileName, is, meta);
    }

    public void batchDelete(List<String> keys) {
        if (!keys.isEmpty()) {
            ;
        }
    }

    public CopyObjectResult copy(String sourceName, String destinationName) {
        return this.amazonS3.copyObject(this.awsS3Config.getBucket(), sourceName, this.awsS3Config.getBucket(), destinationName);
    }

    public PutObjectResult uploadFile(String fileName, InputStream is, String contentType, long size) {
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType(contentType);
        meta.setContentLength(size);
        return this.amazonS3.putObject(this.awsS3Config.getBucket(), fileName, is, meta);
    }

    public S3Object getFile(String fileName) {
        GetObjectRequest req = new GetObjectRequest(this.awsS3Config.getBucket(), fileName);
        return this.amazonS3.getObject(req);
    }

    public URL getUrl(String bucketName, String key) {
        return this.amazonS3.getUrl(bucketName, key);
    }

    public URL getUrl(String key) {
        return this.amazonS3.getUrl(this.awsS3Config.getBucket(), key);
    }

    public URL getPresignedURL(String bucketName, String keyName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = (new GeneratePresignedUrlRequest(bucketName, keyName)).withMethod(HttpMethod.GET).withExpiration(Helper.nextDays(1));
        return this.amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
    }

    public boolean existsFile(String bucketName, String filePath) {
        return this.amazonS3.doesObjectExist(bucketName, filePath);
    }

    public void downloadFile(String bucketName, String keyName, String saveLocation) throws IOException {
        S3Object s3object = this.amazonS3.getObject(bucketName, keyName);
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        FileUtils.copyInputStreamToFile(inputStream, FileHandlerUtils.validateCanonicalPath(saveLocation));
    }

    public PutObjectResult putFile(String bucketName, String key, File file) {
        return this.amazonS3.putObject(bucketName, key, file);
    }

    public PutObjectResult upload(S3UploadRequest request) {
        return this.amazonS3.putObject(request.toPutObjectRequest());
    }

    public List<S3ObjectSummary> listFiles(String bucketName, String keyPrefix) {
        ObjectListing objectListing = this.amazonS3.listObjects(bucketName, keyPrefix);
        ArrayList<S3ObjectSummary> objectSummaryList = new ArrayList();
        objectSummaryList.addAll(objectListing.getObjectSummaries());

        while(objectListing.isTruncated()) {
            objectListing = this.amazonS3.listNextBatchOfObjects(objectListing);
            objectSummaryList.addAll(objectListing.getObjectSummaries());
        }

        return objectSummaryList;
    }

    public void deleteFiles(String bucketName, String keyPrefix) {
        List<S3ObjectSummary> objectSummaryList = this.listFiles(bucketName, keyPrefix);
        List<String> keys = (List)objectSummaryList.stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
        this.deleteFiles(bucketName, keys);
    }

    public void uploadDirectory(S3DirectoryUploadRequest request) {
        try {
            this.transferManager.uploadDirectory(request.getBucketName(), request.getVirtualDirectoryKeyPrefix(), request.getUploadDirectory(), true).waitForCompletion();
        } catch (Exception var3) {
            log.error("Cannot uploadDirectory to S3/MinIO", var3);
        }

    }

    private void listFiles(File dir, List<File> results, boolean includeSubDirectories) {
        File[] found = dir.listFiles();
        if (found != null) {
            File[] var5 = found;
            int var6 = found.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                File f = var5[var7];
                if (f.isDirectory()) {
                    if (includeSubDirectories) {
                        this.listFiles(f, results, includeSubDirectories);
                    }
                } else {
                    results.add(f);
                }
            }
        }

    }

    public void uploadListFiles(S3DirectoryUploadRequest request, List<File> files) {
        try {
            if ("aws-s3".equalsIgnoreCase(this.awsS3Config.getProvider())) {
                this.transferManager.uploadFileList(request.getBucketName(), request.getVirtualDirectoryKeyPrefix(), request.getUploadDirectory(), files).waitForCompletion();
            } else if ("minio".equalsIgnoreCase(this.awsS3Config.getProvider())) {
                String virtualDirectoryKeyPrefix = request.getVirtualDirectoryKeyPrefix();
                if (virtualDirectoryKeyPrefix != null && virtualDirectoryKeyPrefix.length() != 0) {
                    if (!virtualDirectoryKeyPrefix.endsWith("/")) {
                        virtualDirectoryKeyPrefix = virtualDirectoryKeyPrefix + "/";
                    }
                } else {
                    virtualDirectoryKeyPrefix = "";
                }

                Iterator var4 = files.iterator();

                while(var4.hasNext()) {
                    File file = (File)var4.next();
                    log.info("Uploading file to {} of bucket {}", virtualDirectoryKeyPrefix + file.getName(), request.getBucketName());
                    this.amazonS3.putObject(request.getBucketName(), virtualDirectoryKeyPrefix + file.getName(), file);
                }
            }
        } catch (Exception var6) {
            log.error("Cannot uploadDirectory to S3/MinIO", var6);
        }
    }

    public void deleteFiles(String bucketName, List<String> keys) {
        if (keys != null && !keys.isEmpty()) {
            this.amazonS3.deleteObjects((new DeleteObjectsRequest(bucketName)).withKeys((String[])keys.toArray(new String[0])));
        }
    }
}
