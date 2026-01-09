package com.seminario.ms_usuarios.exception;

import com.seminario.ms_usuarios.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // handle custom errors
    @ExceptionHandler(RequestException.class)
    public ResponseEntity<Map<String, Object>> handleRequestException(RequestException ex) {
        Map<String, Object> error = new HashMap<>();
        
        error.put("ms_code", ex.getMs_code()); 
        error.put("ly_code", ex.getLy_code()); 
        error.put("mensaje", ex.getMessage());
        error.put("status", ex.getStatus().value()); 

        return new ResponseEntity<>(error, ex.getStatus());
    }
    // 2. handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }
    
}
