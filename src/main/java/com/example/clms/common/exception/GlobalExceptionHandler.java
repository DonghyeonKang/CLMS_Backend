package com.example.clms.common.exception;

import com.example.clms.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicateEmailException(DuplicateEmailException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ApiResponse.duplicateEmailException(ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ApiResponse.userNotFoundException(ex.getMessage()));
    }

    @ExceptionHandler(AuthNumNotEqualException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthNumNotEqualException(AuthNumNotEqualException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ApiResponse.authNumNotEqualException(ex.getMessage()));
    }
}
