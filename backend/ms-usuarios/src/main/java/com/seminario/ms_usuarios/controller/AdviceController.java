package com.seminario.ms_usuarios.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.seminario.ms_usuarios.dto.ErrorDTO;
import com.seminario.ms_usuarios.exception.RequestException;

    
@RestControllerAdvice   
public class AdviceController {
    @ExceptionHandler(value = RequestException.class)
    public ResponseEntity<ErrorDTO> runtimeExceptionHandler(RequestException ex) {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .ms_code(ex.getMs_code())
                .error_code(ex.getLy_code())
                .message(ex.getMessage())
                .status(ex.getStatus())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorDTO, errorDTO.getStatus());
    }



}
