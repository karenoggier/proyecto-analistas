package com.seminario.ms_pedido.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.seminario.ms_pedido.dto.exceptions_dtos.ErrorResponseDTO;

import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;

/**
 * Manejador global de excepciones.
 * 
 * MEJORAS SOBRE VERSION ANTERIOR:
 * - Usa DTOs en lugar de Maps
 * - Logging estructurado
 * - Trace IDs para debugging
 * - Timestamps y paths
 * - Maneja errores de servicios externos
 * - Catch-all para errores inesperados
 * - Diferencia entre dev y prod
 * 
 * @author Tu Nombre
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    private final Tracer tracer;
    
    @Value("${spring.profiles.active:dev}")
    private String activeProfile;
    

    @ExceptionHandler(RequestException.class)
    public ResponseEntity<ErrorResponseDTO> handleRequestException(
            RequestException ex,
            WebRequest request) {
        
        // Log estructurado
        log.error("RequestException: ms_code={}, ly_code={}, status={}, mensaje={}, path={}", 
                 ex.getMsCode(),
                 ex.getLyCode(),
                 ex.getStatus().value(),
                 ex.getMessage(),
                 getPath(request));
        
        ErrorResponseDTO error = ErrorResponseDTO.builder()
            .timestamp(Instant.now())
            .status(ex.getStatus().value())
            .msCode(ex.getMsCode())
            .lyCode(ex.getLyCode())
            .message(ex.getMessage())
            .path(getPath(request))
            .traceId(getTraceId())
            .build();
        
        return ResponseEntity.status(ex.getStatus()).body(error);
    }
    
    /**
     * Maneja errores de validación (@Valid).
     * con:
     * - Timestamp
     * - Path
     * - Estructura consistente
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        
        log.warn("Errores de validación en {}: {} errores", 
                getPath(request),
                ex.getBindingResult().getErrorCount());
        
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", Instant.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("code", "VALIDATION_ERROR");
        response.put("mensaje", "Errores de validación en el request");
        response.put("path", getPath(request));
        response.put("traceId", getTraceId());
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        response.put("errors", errors);
        
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ProductoNoEncontradoException.class)
    public ResponseEntity<ErrorResponseDTO> handleProductoNoEncontrado(
            ProductoNoEncontradoException ex,
            WebRequest request) {
        
        log.warn("Producto no encontrado: productoId={}, vendedorId={}, path={}", 
                ex.getProductoId(), 
                ex.getVendedorId(),
                getPath(request));
        
        ErrorResponseDTO error = ErrorResponseDTO.builder()
            .timestamp(Instant.now())
            .status(HttpStatus.NOT_FOUND.value())
            .msCode("PED")  // Código de tu microservicio
            .lyCode(1001)    
            .code("PRODUCTO_NO_ENCONTRADO")  
            .message(ex.getMessage())
            .path(getPath(request))
            .traceId(getTraceId())
            .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(ServicioNoDisponibleException.class)
    public ResponseEntity<ErrorResponseDTO> handleServicioNoDisponible(
            ServicioNoDisponibleException ex,
            WebRequest request) {
        
        log.error("Servicio no disponible: servicio={}, path={}", 
                 ex.getServicio(),
                 getPath(request), 
                 ex);
        
        ErrorResponseDTO error = ErrorResponseDTO.builder()
            .timestamp(Instant.now())
            .status(HttpStatus.SERVICE_UNAVAILABLE.value())
            .msCode("PED")
            .lyCode(1002)
            .code("SERVICIO_NO_DISPONIBLE")
            .message(ex.getMessage())
            .path(getPath(request))
            .traceId(getTraceId())
            .build();
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(
            Exception ex,
            WebRequest request) {
        
        log.error("Error inesperado en la aplicación: path={}", 
                 getPath(request), 
                 ex);
        
        // En producción, NO exponer detalles internos
        String message = isProductionProfile()
            ? "Ha ocurrido un error inesperado. Contacte a soporte con el traceId."
            : ex.getMessage();
        
        ErrorResponseDTO error = ErrorResponseDTO.builder()
            .timestamp(Instant.now())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .msCode("SYS")  // System
            .lyCode(500)
            .code("INTERNAL_SERVER_ERROR")
            .message(message)
            .path(getPath(request))
            .traceId(getTraceId())
            .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    
    // ========== HELPER METHODS ==========
    
    private String getPath(WebRequest request) {
        if (request instanceof ServletWebRequest servletRequest) {
            return servletRequest.getRequest().getRequestURI();
        }
        return request.getDescription(false).replace("uri=", "");
    }
    
    @Nullable
    private String getTraceId() {
        try {
            var span = tracer.currentSpan();
            return span != null ? span.context().traceId() : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    private boolean isProductionProfile() {
        return "prod".equalsIgnoreCase(activeProfile) || 
               "production".equalsIgnoreCase(activeProfile);
    }

    public GlobalExceptionHandler(@Autowired(required = false) @Nullable Tracer tracer) {
        this.tracer = tracer;
        if (tracer == null) {
            log.info("Micrometer Tracer no configurado - Trace IDs no disponibles");
        }
    }
}
