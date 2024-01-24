package com.kpro.common.minio.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

public class FileHandlerUtils {
    private static final Logger log = LoggerFactory.getLogger(FileHandlerUtils.class);

    private FileHandlerUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static File validateCanonicalPath(String inputPath) throws IOException {
        Path filePath = FileSystems.getDefault().getPath(inputPath);
        if (!filePath.toFile().getCanonicalPath().contains(inputPath)) {
            throw new InvalidPathException(inputPath, "The path does not match with Canonical path");
        } else {
            return filePath.toFile();
        }
    }
}