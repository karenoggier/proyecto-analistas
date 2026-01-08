package com.seminario.ms_usuarios.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class RequestException  extends RuntimeException {
    private String ms_code; //microservice code that throws the error  
    private int error_code;//code that identify the error
    private HttpStatus status;
    public RequestException(String ms_code, int error_code, HttpStatus status, String message) {
        super(message);
        this.ms_code = ms_code;
        this.error_code = error_code;
        this.status = status;
    }

}
