package com.seminario.ms_catalogo.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.seminario.ms_catalogo.dto.eventos_ms_usuarios.VendedorRegistradoEvent;
import com.seminario.ms_catalogo.exception.RequestException;

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

    @CircuitBreaker(name = "usuarioClient", fallbackMethod = "actualizarVendedorFallback")
    @Retry(name = "usuarioClient")
    public ResponseEntity<VendedorRegistradoEvent> actualizarVendedor(VendedorRegistradoEvent vendedorRegistradoEvent) {
        try {
            String url = usuariosBaseUrl + "/usuariosMs/vendedores/actualizar";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<VendedorRegistradoEvent> request = new HttpEntity<>(vendedorRegistradoEvent, headers);
                        
            //envío de mensaje HTTP
            ResponseEntity<VendedorRegistradoEvent> response = restTemplate.postForEntity(url, request, VendedorRegistradoEvent.class);
            
            return response;
            
        } catch (Exception e) {
            throw new RequestException("CAT", 500, HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error al sincronizar con ms-usuario: " + e.getMessage());
        }
    }

    //Fallback method cuando el circuit breaker está abierto
    public ResponseEntity<VendedorRegistradoEvent> actualizarVendedorFallback(VendedorRegistradoEvent vendedorRegistradoEvent, Exception exception) {
        throw new RequestException("CAT", 503, HttpStatus.SERVICE_UNAVAILABLE, 
            "El servicio de usuarios no está disponible. Por favor intente más tarde.");
    }
}
