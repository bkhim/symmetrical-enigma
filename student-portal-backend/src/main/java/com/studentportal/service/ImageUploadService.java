package com.studentportal.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageUploadService {

    private final Path imageStorageLocation;

    public ImageUploadService() {
        this.imageStorageLocation = Paths.get("C:/var/log/applications/API/uploads").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.imageStorageLocation); // Ensure the directory exists
        } catch (Exception ex) {
            throw new RuntimeException("Could not create image upload directory", ex);
        }
    }

    public String uploadImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("No image file provided");
        }

        // Create a unique file name based on the current timestamp
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path targetLocation = this.imageStorageLocation.resolve(fileName);

        // Copy the file to the target location
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // Return the relative file path to be saved in the database
        return fileName;
    }
}
