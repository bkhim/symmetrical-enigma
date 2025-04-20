package com.studentportal.repository;

import com.studentportal.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Layer for Student entities
 * - Extends JpaRepository for CRUD operations
 * - Includes custom query methods
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

    Optional<Student> findById(Long id);

    /**
     * Find student by studentId (username)
     * @param studentId The unique student identifier
     * @return Optional containing student if found
     */
    Optional<Student> findByStudentId(String studentId);

    /**
     * Find all active students
     * @param status true for active, false for inactive
     * @return List of students matching status
     */
    List<Student> findByStatus(Boolean status);

    /**
     * Soft delete student by setting status to inactive
     * @param id Student ID to deactivate
     */
    @Modifying
    @Query("UPDATE Student s SET s.status = false WHERE s.id = :id")
    void softDelete(@Param("id") Long id);

    /**
     * Count all active students (for dashboard)
     * @return Number of active students
     */
    @Query("SELECT COUNT(s) FROM Student s WHERE s.status = true")
    long countActiveStudents();

    /**
     * Find students by class name
     * @param className The class to filter by
     * @return List of students in specified class
     */
    List<Student> findByClassName(String className);

    /**
     * Find students born between dates (for reporting)
     * @param start Start date range
     * @param end End date range
     * @return List of students in date range
     */
    List<Student> findByDobBetween(LocalDate start, LocalDate end);

    @Query("SELECT DISTINCT s.className FROM Student s")
    List<String> findDistinctClasses();
}