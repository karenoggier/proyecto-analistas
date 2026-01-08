package com.seminario.ms_usuarios.controller;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.seminario.ms_usuarios.Excepction.RequestException;
import com.seminario.ms_usuarios.dto.ErrorDTO;

    
@RestControllerAdvice   
public class ControllerAdvice {
    @ExceptionHandler(value = RequestException.class)
    public ResponseEntity<ErrorDTO> runtimeExceptionHandler(RequestException ex) {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .ms_code(ex.getMs_code())
                .error_code(ex.getError_code())
                .message(ex.getMessage())
                .status(ex.getStatus())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorDTO, errorDTO.getStatus());
    }



}
