package com.studentportal.exception;

import lombok.Getter;

import java.util.Date;

@Getter
public class ErrorDetails {
    // Getters
    private Date timestamp;
    private String message;
    private String details;

    // Constructor, getters and setters
    public ErrorDetails(Date timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

}