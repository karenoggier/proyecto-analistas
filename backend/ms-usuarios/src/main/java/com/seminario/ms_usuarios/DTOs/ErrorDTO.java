package com.seminario.ms_usuarios.DTOs;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDTO {
    //see the meaning of each field in the error dictionary 
    private String ms_code; //microservice code that throws the error  
    private int error_code;//code that identify the error
    private String message; //description of the error --> tThis field should contain the same message that is recorded in the error dictionary.
    private HttpStatus status; //identify the http status of the error
    private LocalDateTime timestamp; //time when the error happened
}

