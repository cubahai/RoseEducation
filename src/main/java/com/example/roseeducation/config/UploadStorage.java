package com.example.roseeducation.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class UploadStorage {

    private static final String UPLOAD_DIR_ENV = "UPLOAD_DIR";

    private UploadStorage() {
    }

    public static Path uploadDir() {
        String uploadDir = System.getenv(UPLOAD_DIR_ENV);
        if (uploadDir != null && !uploadDir.isBlank()) {
            return Paths.get(uploadDir);
        }
        return Paths.get(System.getProperty("java.io.tmpdir"), "roseeducation", "uploads");
    }
}
