package com.studentportal.dto;

import lombok.Data;
import java.util.List;

/**
 * Standardized API response for student operations
 * - Includes pagination info for list responses
 */
@Data
public class StudentResponse {
    private List<StudentDTO> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;

    /**
     * Constructor for single student response
     * @param student The student DTO
     */
    public StudentResponse(StudentDTO student) {
        this.content = List.of(student);
        this.page = 0;
        this.size = 1;
        this.totalElements = 1;
        this.totalPages = 1;
        this.last = true;
    }

    /**
     * Constructor for paginated list response
     * @param content List of student DTOs
     * @param page Current page number
     * @param size Page size
     * @param totalElements Total items in database
     */
    public StudentResponse(List<StudentDTO> content, int page,
                           int size, long totalElements) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / size);
        this.last = (page + 1) >= totalPages;
    }
}