package com.seminario.ms_pedido.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seminario.ms_pedido.dto.DireccionRequestDTO;
import com.seminario.ms_pedido.dto.DireccionResponseDTO;
import com.seminario.ms_pedido.exception.RequestException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@RequiredArgsConstructor
@Slf4j
public class UsuarioClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${usuarios.ms.url:http://localhost:8080}")
    private String usuariosBaseUrl;

    @CircuitBreaker(name = "usuarioClient", fallbackMethod = "buscarDatosDireccionFallback")
    @Retry(name = "usuarioClient")
    public DireccionResponseDTO buscarDatosDireccion(DireccionRequestDTO event, String clienteId) {

        String url = usuariosBaseUrl + "/usuariosMs/direcciones/{usuarioId}";
        
        try {
            ResponseEntity<DireccionResponseDTO> response = restTemplate.exchange(
                url, 
                HttpMethod.POST, 
                new HttpEntity<>(event), 
                DireccionResponseDTO.class, 
                clienteId
            );
            return response.getBody();

        } catch (HttpStatusCodeException e) {
            log.error("Error HTTP desde ms-usuarios: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            
            String mensajeReal = "Error de validación en ms-usuarios";
            try {
                JsonNode jsonNode = objectMapper.readTree(e.getResponseBodyAsString());
                
                if (jsonNode.has("message")) {
                    mensajeReal = jsonNode.get("message").asText();
                } else if (jsonNode.has("mensaje")) {
                    mensajeReal = jsonNode.get("mensaje").asText();
                }
            } catch (Exception ex) {
                mensajeReal = e.getResponseBodyAsString(); 
            }

            throw new RequestException("USU", 400, HttpStatus.BAD_REQUEST, mensajeReal);
            
        } catch (Exception e) {
            throw new RuntimeException("Error de conexión pura: " + e.getMessage(), e);
        }
    }

// El método Fallback
    public DireccionResponseDTO buscarDatosDireccionFallback(DireccionRequestDTO event, String clienteId, Throwable t) {
        log.error("Circuit Breaker activado o reintentos agotados. Razón: {}", t.getMessage());
        if (t instanceof RequestException) {
            throw (RequestException) t; 
        }
        throw new RequestException("US", 503, HttpStatus.SERVICE_UNAVAILABLE, "Servicio de validación de direcciones temporalmente inactivo.");
    }

    @CircuitBreaker(name = "usuarioClient", fallbackMethod = "eliminarDireccionFallback")
    @Retry(name = "usuarioClient")
    public void eliminarDireccion(String idDireccion) {
        String url = usuariosBaseUrl + "/usuariosMs/direcciones/{idDireccion}";
        
        try {
            restTemplate.exchange(
                url, 
                HttpMethod.DELETE, 
                null, 
                Void.class, 
                idDireccion
            );

        } catch (HttpStatusCodeException e) {
            log.error("Error HTTP desde ms-usuarios: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            
            String mensajeReal = "Error de validación en ms-usuarios";
            try {
                JsonNode jsonNode = objectMapper.readTree(e.getResponseBodyAsString());
                
                if (jsonNode.has("message")) {
                    mensajeReal = jsonNode.get("message").asText();
                } else if (jsonNode.has("mensaje")) {
                    mensajeReal = jsonNode.get("mensaje").asText();
                }
            } catch (Exception ex) {
                mensajeReal = e.getResponseBodyAsString(); 
            }

            throw new RequestException("USU", 400, HttpStatus.BAD_REQUEST, mensajeReal);
            
        } catch (Exception e) {
            throw new RuntimeException("Error de conexión pura: " + e.getMessage(), e);
        }
    }

    public void eliminarDireccionFallback(String idDireccion, Throwable t) {
        log.error("Circuit Breaker activado o reintentos agotados. Razón: {}", t.getMessage());
        if (t instanceof RequestException) {
            throw (RequestException) t; 
        }
        throw new RequestException("US", 503, HttpStatus.SERVICE_UNAVAILABLE, "Servicio de eliminación de direcciones temporalmente inactivo.");
    }


}
