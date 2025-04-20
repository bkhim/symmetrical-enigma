package com.studentportal.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MySuccessResponse {
    private String message;
    private Object data;

    public MySuccessResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }

}
