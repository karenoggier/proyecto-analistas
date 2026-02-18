package com.seminario.ms_pedido.exception;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * Excepción base para errores de request.
 * 
 * ESTRUCTURA:
 * - ms_code: Código del microservicio (ej: "PED", "CAT")
 * - ly_code: Código numérico específico del error
 * - message: Descripción del error
 * - status: HTTP status code
 */
@Getter
public class RequestException extends RuntimeException {
    
    private final String msCode;
    private final int lyCode;
    private final HttpStatus status;
    
    public RequestException(
            @NonNull String msCode,
            int lyCode,
            @NonNull HttpStatus status,
            @NonNull String message) {
        super(message);
        this.msCode = msCode;
        this.lyCode = lyCode;
        this.status = status;
    }
    
    public RequestException(
            @NonNull String msCode,
            int lyCode,
            @NonNull HttpStatus status,
            @NonNull String message,
            @Nullable Throwable cause) {
        super(message, cause);
        this.msCode = msCode;
        this.lyCode = lyCode;
        this.status = status;
    }
}