package com.seminario.ms_pedido.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
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
@CircuitBreaker(name = "usuarioClient", fallbackMethod = "calcularDistanciaFallback")
@Retry(name = "usuarioClient")
public Double calcularDistanciaEntreDirecciones(String idVendedorUsuario, String idDireccionCliente, Authentication authentication) {
    
    // 1. Extraer el token JWT del objeto authentication
    String tokenValue;
    try {
        // En Spring Security con OAuth2, el principal suele ser el objeto Jwt
        Jwt jwt = (Jwt) authentication.getPrincipal();
        tokenValue = jwt.getTokenValue();
    } catch (Exception e) {
        log.error("Error al extraer el token JWT: {}", e.getMessage());
        throw new RequestException("PED", 401, HttpStatus.UNAUTHORIZED, "No se pudo recuperar la sesión del usuario");
    }

    // 2. Configurar cabeceras con el token
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(tokenValue);
    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    // 3. Definir la URL
    // Nota: Verifica si "/usuariosMs" es realmente parte del path o el context-path
    String url = usuariosBaseUrl + "/usuariosMs/direcciones/calcular-distancia/{idVendedor}/{idDireccionCliente}";

    try {
        log.info("Solicitando distancia a ms-usuarios. Vendedor: {}, Cliente: {}, Usuario: {}", 
            idVendedorUsuario, idDireccionCliente, authentication.getName());

        ResponseEntity<Double> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            requestEntity, // Enviamos el header con el token
            Double.class,
            idVendedorUsuario,
            idDireccionCliente
        );

        return response.getBody();

    } catch (HttpStatusCodeException e) {
        log.error("Error HTTP desde ms-usuarios al calcular distancia: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
        
        String mensajeReal = "Error al calcular distancia";
        try {
            JsonNode jsonNode = objectMapper.readTree(e.getResponseBodyAsString());
            mensajeReal = jsonNode.path("message").asText(jsonNode.path("mensaje").asText(mensajeReal));
        } catch (Exception ex) {
            mensajeReal = e.getResponseBodyAsString();
        }

        throw new RequestException("USU", e.getStatusCode().value(), (HttpStatus) e.getStatusCode(), mensajeReal);

    } catch (Exception e) {
        log.error("Error inesperado de comunicación: {}", e.getMessage());
        throw new RequestException("USU", 503, HttpStatus.SERVICE_UNAVAILABLE, "El servicio de usuarios no responde.");
    }
}

// IMPORTANTE: El método Fallback debe tener la MISMA firma que el original + la excepción
public Double calcularDistanciaFallback(String idVendedorUsuario, String idDireccionCliente, Authentication authentication, Throwable t) {
    log.error("Fallback activado para calcularDistancia. Usuario: {}. Motivo: {}", authentication.getName(), t.getMessage());
    
    if (t instanceof RequestException) {
        throw (RequestException) t;
    }
    
    throw new RequestException("US", 503, HttpStatus.SERVICE_UNAVAILABLE, 
        "El servicio de usuarios no está disponible para calcular la distancia.");
}
}