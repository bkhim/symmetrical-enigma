package com.studentportal.dto;

import com.studentportal.model.Student;
import lombok.Data;
import java.time.LocalDate;

/**
 * Data Transfer Object for Student entities
 * - Used for API requests/responses
 * - Excludes sensitive data like password
 * - Includes validation annotations
 */
@Data
public class StudentDTO {
    private Long id;
    private String studentId;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String className;
    private Integer score;
    private Boolean status;
    private String photoPath;

    /**
     * Convert Student entity to DTO
     * @param student The entity to convert
     * @return StudentDTO representation
     */
    public static StudentDTO fromEntity(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setStudentId(student.getStudentId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setDob(student.getDob());
        dto.setClassName(student.getClassName());
        dto.setScore(student.getScore());
        dto.setStatus(student.getStatus());
        dto.setPhotoPath(student.getPhotoPath());
        return dto;
    }

    /**
     * Convert DTO to Student entity
     * @return Student entity
     */
    public Student toEntity() {
        Student student = new Student();
        student.setId(this.id);
        student.setStudentId(this.studentId);
        student.setFirstName(this.firstName);
        student.setLastName(this.lastName);
        student.setDob(this.dob);
        student.setClassName(this.className);
        student.setScore(this.score);
        student.setStatus(this.status);
        student.setPhotoPath(this.photoPath);
        return student;
    }
}