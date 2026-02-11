package com.seminario.ms_pedido.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

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
    @CircuitBreaker(name = "usuarioClient", fallbackMethod = "buscarDatosDireccionFallback")
    @Retry(name = "usuarioClient")
    public DireccionResponseDTO buscarDatosDireccion(DireccionRequestDTO event, String clienteId) {
        // Agregamos el clienteId a la URL si es un PathVariable
        String url = usuariosBaseUrl + "/usuariosMs/clientes/registrarDireccion/{usuarioId}";
        
        try {
            ResponseEntity<DireccionResponseDTO> response = restTemplate.postForEntity(
                url, event, DireccionResponseDTO.class, clienteId);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            log.error("Error en microservicio usuarios: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RequestException("USU", e.getStatusCode().value(), (HttpStatus) e.getStatusCode(), "Error en ms-usuarios: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new RequestException("USU", 500, HttpStatus.INTERNAL_SERVER_ERROR, "Error de red: " + e.getMessage());
        }
    }

// El método Fallback
    public DireccionResponseDTO buscarDatosDireccionFallback(DireccionRequestDTO event, String clienteId, Throwable t) {
        log.error("Circuit Breaker activado o reintentos agotados. Razón: {}", t.getMessage());
        // Retornar un objeto por defecto o lanzar una excepción personalizada
        return new DireccionResponseDTO(); 
    }


}
