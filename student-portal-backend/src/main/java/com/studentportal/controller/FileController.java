package com.studentportal.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/uploads")
public class FileController {

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws MalformedURLException {
        String uploadDir = "C:/var/log/applications/API/uploads/";
        Path filePath = Paths.get(uploadDir).resolve(filename);
        Resource file = new UrlResource(filePath.toUri());

        if (file.exists() || file.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Or detect from extension
                    .body(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
