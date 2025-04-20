package com.studentportal.dto;

import lombok.Data;

/**
 * Request object for login containing credentials
 */
@Data
public class LoginRequest {
    private String username; // student_id
    private String password;
}