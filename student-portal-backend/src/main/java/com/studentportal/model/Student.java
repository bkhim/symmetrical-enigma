package com.studentportal.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

/**
 * Represents a Student entity in the database
 * - Maps to 'students' table
 * - Contains all student-related fields
 * - Uses Lombok for boilerplate code reduction
 */
@Data
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique student identifier (used as username in authentication)
     * - Must be unique and cannot be null
     */
    @Column(unique = true, nullable = false)
    private String studentId;

    /**
     * Encrypted password (using BCrypt)
     * - Required field
     */
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    /**
     * Date of birth
     * - Randomly generated between 2000-2010 for demo data
     */
    private LocalDate dob;

    /**
     * Class assignment (one of: Class1, Class2, Class3, Class4, Class5)
     */
    private String className;

    /**
     * Academic score (55-85 range for demo data)
     */
    private Integer score;

    /**
     * Account status
     * - true = active (default)
     * - false = inactive (soft delete)
     */
    private Boolean status = true;

    /**
     * Path to student photo
     * - Default photo path is empty or set to "default.jpg"
     * - Updated when photo is uploaded
     */
    private String photoPath = "default.jpg";  // Default photo path

    public Student() {}

    public Student(String studentId, String password, String firstName,
                   String lastName, String className, String photoPath) {
        this.studentId = studentId;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.className = className;
        this.photoPath = photoPath;
    }
}
