package com.org.codeverse.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> resourceNotFoundExceptionHandler(DataIntegrityViolationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists!!!");
    }

    @ExceptionHandler(TimeLimitExceededException.class)
    public ResponseEntity<Map<String, String>> timeLimitExceededExceptionHandler(TimeLimitExceededException exception) {
//        System.out.println("TLE");
        Map<String, String> response = new HashMap<>();
        response.put("message", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(CompilationError.class)
    public ResponseEntity<Map<String, String>> compilationErrorHandler(CompilationError e) {
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        response.put("output", e.getError());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
