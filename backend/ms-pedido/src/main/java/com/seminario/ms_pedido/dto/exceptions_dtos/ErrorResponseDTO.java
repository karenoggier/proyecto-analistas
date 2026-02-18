package com.seminario.ms_pedido.dto.exceptions_dtos;

import java.time.Instant;

import org.jspecify.annotations.Nullable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Value;

/**
 * DTO para respuestas de error.
 */
@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDTO {
    
    Instant timestamp;
    int status;
    
    //Código del microservicio (ej: "CAT", "PED")
    @JsonProperty("ms_code")
    String msCode;
    
    //Código de la capa lógica ¿?
    @JsonProperty("ly_code")
    Integer lyCode;
    
    //Código de error semántico (ej: "PRODUCTO_NO_ENCONTRADO")
    @Nullable
    String code;
    
    //Mensaje legible para humanos
    @JsonProperty("mensaje")
    String message;
    
    //Path del endpoint que generó el error
    @Nullable
    String path;
    
    //Trace ID para debugging distribuido
    @Nullable
    String traceId;
}
