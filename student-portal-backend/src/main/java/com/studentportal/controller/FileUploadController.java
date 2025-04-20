package com.studentportal.controller;

import com.studentportal.exception.MyErrorResponse;
import com.studentportal.exception.MySuccessResponse;
import com.studentportal.model.Student;
import com.studentportal.repository.StudentRepository;
import com.studentportal.service.FileStorageService;
import com.studentportal.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/students")
public class FileUploadController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FileStorageService fileStorageService;
    FileUploadService fileUploadService;
    

    @PostMapping("/upload")
    public ResponseEntity<?> uploadExcelFile(
            @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(new MyErrorResponse("File is empty"));
            }

            if (!file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
                return ResponseEntity.badRequest().body(new MyErrorResponse("Only Excel files are allowed"));
            }

            int processedCount = fileUploadService.processExcelFile(file);
            return ResponseEntity.ok().body(new MySuccessResponse("Successfully processed " + processedCount + " student records", + processedCount));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(new MyErrorResponse("Error processing file: " + e.getMessage()));
        }
    }

    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<?> uploadStudentPhoto(@PathVariable String id,
                                                @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(new MyErrorResponse("No file uploaded."));
            }

            // Store file and get the filename
            String storedFileName = fileStorageService.storeFile(file);

            // Update student record
            Student student = studentRepository.findByStudentId(id)
                    .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));

            // Just store the filename (relative path)
            student.setPhotoPath(storedFileName);
            studentRepository.save(student);

            return ResponseEntity.ok(new MySuccessResponse("Photo uploaded successfully", storedFileName));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new MyErrorResponse("Failed to upload photo: " + e.getMessage()));
        }
    }
}
