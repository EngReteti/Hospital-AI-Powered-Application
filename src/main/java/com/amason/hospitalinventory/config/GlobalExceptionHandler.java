package com.amason.hospitalinventory.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

// @RestControllerAdvice means: "watch every controller in the app, 
// and if any of them throws an error, let ME decide how to respond"
@RestControllerAdvice
public class GlobalExceptionHandler {

    // @ExceptionHandler tells Spring exactly which type of error this 
    // method handles - here, specifically IllegalStateException, which 
    // is what our negative-stock check throws
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalState(IllegalStateException ex) {
        
        // Build a simple, clean JSON body: { "error": "the actual message" }
        Map<String, String> body = new HashMap<>();
        body.put("error", ex.getMessage());

        // 400 Bad Request is the correct status for "the request itself 
        // was invalid" - more accurate than a generic 500
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // This one catches the "Product not found with id: X" error we wrote 
    // in ProductController's getProductById method
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
