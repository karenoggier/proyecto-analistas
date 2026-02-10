package com.seminario.ms_pedido.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seminario.ms_pedido.DTOs.DireccionRequestDTO;
import com.seminario.ms_pedido.DTOs.DireccionResponseDTO;
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

    @Value("${usuarios.ms.url:http://localhost:8080}")
    private String usuariosBaseUrl;

    /*@CircuitBreaker(name = "usuarioClient", fallbackMethod = "BuscarDatosDireccionFallback")
    @Retry(name = "usuarioClient")
    public DireccionResponseDTO BuscarDatosDireccion(DireccionRequestDTO event) {
        try {
            String url = usuariosBaseUrl + "/usuariosMs/direcciones/obtener";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
                                    
            //envío de mensaje HTTP
            ResponseEntity<?> response = restTemplate.postForEntity(url, event, DireccionResponseDTO.class);
            
            //si devuelve un error personalizado del microservicio de usuarios
            if(response.getBody().getClass() != DireccionResponseDTO.class) {
                Map<String, Object> error = (Map<String, Object>) response.getBody();
                throw new RequestException(error.get("ms_code").toString(),
                                           (int) error.get("ly_code"),
                                           HttpStatus.valueOf((int) error.get("status")),
                                           error.get("mensaje").toString());
            }

            return (DireccionResponseDTO) response.getBody();
            
        } catch (Exception e) {
            throw new RequestException("USU", 500, HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error al sincronizar con ms-usuario: " + e.getMessage());
        }
    }*/
    @CircuitBreaker(name = "usuarioClient", fallbackMethod = "BuscarDatosDireccionFallback")
    @Retry(name = "usuarioClient")
    public DireccionResponseDTO buscarDatosDireccion(DireccionRequestDTO event) {
    String url = usuariosBaseUrl + "/usuariosMs/direcciones/obtener";
    try {
        ResponseEntity<DireccionResponseDTO> response = restTemplate.postForEntity(url, event, DireccionResponseDTO.class);
        return response.getBody();

    } catch (HttpStatusCodeException e) {
        // 1. Log the raw error for internal debugging
        log.error("Error calling {}: Status {} - Body {}", url, e.getStatusCode(), e.getResponseBodyAsString());

        // 2. Try to extract the remote message, otherwise use a default
        String remoteMessage = extractMessage(e.getResponseBodyAsString());
        
        throw new RequestException(
            "USU", 
            e.getStatusCode().value(), 
            (HttpStatus) e.getStatusCode(), 
            remoteMessage
        );
    } catch (Exception e) {
        throw new RequestException("USU", 500, HttpStatus.INTERNAL_SERVER_ERROR, 
            "Error de comunicación: " + e.getMessage());
    }
}

// Helper method to parse the JSON error body
private String extractMessage(String body) {
    try {
        // Use ObjectMapper to read the "mensaje" field from the JSON string
        return new ObjectMapper().readTree(body).get("mensaje").asText();
    } catch (Exception e) {
        return "Error remoto sin descripción específica.";
    }
}
       


}
