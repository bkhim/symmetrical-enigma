package com.studentportal.controller;

import com.studentportal.model.Student;
import com.studentportal.repository.StudentRepository;
import com.studentportal.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public Page<Student> getAllStudents(Pageable pageable) {
        return studentService.getAllStudents(pageable); // Pagination applied here
    }

    @GetMapping("/classes")
    public List<String> getClasses() {
        return studentService.getClasses(); // Calls the service method to fetch the classes
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.saveStudent(student);
    }

    @PostMapping("/{id}/photo")
    public ResponseEntity<String> uploadPhoto(@PathVariable("id") Long studentId, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file uploaded.");
        }

        try {
            // Generate a file path to store the image
            String uploadDir = "uploads/students/" + studentId + "/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();  // Create directories if not exist
            }

            Path path = Paths.get(uploadDir + file.getOriginalFilename());
            Files.write(path, file.getBytes());  // Save the file to the server

            String photoUrl = "/uploads/students/" + studentId + "/" + file.getOriginalFilename();

            Optional<Student> studentOptional = studentService.getStudentById(studentId);
            if (studentOptional.isPresent()) {
                Student student = studentOptional.get();
                student.setPhotoPath(photoUrl);
                studentService.saveStudent(student);  // Save updated student
            }

            return ResponseEntity.ok("Photo uploaded successfully: " + photoUrl);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable("id") Long id, @RequestBody Student studentDetails) {
        Optional<Student> studentOptional = studentService.getStudentById(id);

        if (!studentOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Student student = studentOptional.get();
        student.setFirstName(studentDetails.getFirstName());
        student.setLastName(studentDetails.getLastName());
        student.setDob(studentDetails.getDob());
        student.setClassName(studentDetails.getClassName());
        student.setScore(studentDetails.getScore());
        student.setStatus(studentDetails.getStatus());
        student.setPhotoPath(studentDetails.getPhotoPath());

        studentService.saveStudent(student);

        return ResponseEntity.ok(student);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Optional<Student> student = studentService.getStudentById(id);
        return student.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id); // Using Long for ID
        return ResponseEntity.noContent().build();  // Returns 204 No Content for successful delete
    }

    @GetMapping("/count")
    public long getStudentCount() {
        return studentService.getActiveStudentCount();
    }
}
