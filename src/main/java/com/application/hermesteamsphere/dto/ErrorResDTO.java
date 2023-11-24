package com.application.hermesteamsphere.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResDTO {
    HttpStatus httpStatus;
    String message;

    public ErrorResDTO(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
