package com.seminario.ms_usuarios.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.seminario.ms_usuarios.dto.eventos_ms_catalogo.VendedorRegistradoEvent;
import com.seminario.ms_usuarios.exception.RequestException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CatalogoClient {

    private final RestTemplate restTemplate;

    @Value("${catalogo.ms.url:http://localhost:8081}")
    private String catalogoBaseUrl;

    @CircuitBreaker(name = "catalogoClient", fallbackMethod = "registrarVendedorFallback")
    @Retry(name = "catalogoClient")
    public ResponseEntity<Void> registrarVendedor(VendedorRegistradoEvent evento) {
        try {
            String url = catalogoBaseUrl + "/catalogoMs/api/vendedores/registrar";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<VendedorRegistradoEvent> request = new HttpEntity<>(evento, headers);
                        
            //envío de mensaje HTTP
            ResponseEntity<Void> response = restTemplate.postForEntity(url, request, Void.class);
            
            return response;
            
        } catch (Exception e) {
            log.error("FALLO REAL AL LLAMAR A CATALOGO: " + e.getMessage()); 
            // e.printStackTrace(); // Descomenta si necesitas ver todo
            throw new RequestException("CAT", 500, HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error al sincronizar con ms-catalogo: " + e.getMessage());
        }
    }

    //Fallback method cuando el circuit breaker está abierto
    public ResponseEntity<Void> registrarVendedorFallback(VendedorRegistradoEvent evento, Exception exception) {
        System.out.println(">>> ERROR REAL OCULTO: " + exception.getMessage());
        exception.printStackTrace();
        
        throw new RequestException("CAT", 503, HttpStatus.SERVICE_UNAVAILABLE, 
            "El servicio de catálogo no está disponible. Por favor intente más tarde.");
    }
}
