package com.studytrack.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class FileStorageService {

    private final Cloudinary cloudinary;

    public FileStorageService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String storeFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        String contentType = file.getContentType();

        if (contentType == null ||
                (!contentType.startsWith("image/") &&
                        !contentType.startsWith("video/"))) {

            throw new IllegalArgumentException(
                    "Only image or video files are allowed"
            );
        }

        try {

            String resourceType =
                    contentType.startsWith("video/")
                            ? "video"
                            : "image";

            Map<String, Object> uploadOptions = ObjectUtils.asMap(
                    "resource_type", resourceType,
                    "folder", "studytrack/task-proofs"
            );

            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    uploadOptions
            );

            Object secureUrl = uploadResult.get("secure_url");

            if (secureUrl == null) {
                throw new RuntimeException(
                        "Cloudinary did not return a secure URL"
                );
            }

            return secureUrl.toString();

        } catch (IOException e) {

            throw new RuntimeException(
                    "Could not upload file to Cloudinary",
                    e
            );
        }
    }
}