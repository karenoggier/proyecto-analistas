package com.seminario.ms_pedido.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.seminario.ms_pedido.dto.ProductoResumidoDTO;
import com.seminario.ms_pedido.exception.RequestException;

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

    @CircuitBreaker(name = "catalogoClient", fallbackMethod = "buscarProductoFallback")
    @Retry(name = "catalogoClient")
    public ResponseEntity<ProductoResumidoDTO> buscarProducto(String productoId, String vendedorId) {
        
            String url = catalogoBaseUrl + "/catalogoMs/api/productos/getProductoByIdAndVendedorId?productoId={productoId}&vendedorId={vendedorId}";
                
            ResponseEntity<ProductoResumidoDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null, // El interceptor agregará el header Authorization automáticamente
                ProductoResumidoDTO.class,
                productoId,
                vendedorId
            );

            return response;
    }

    //Fallback method cuando el circuit breaker está abierto
    public ResponseEntity<ProductoResumidoDTO> buscarProductoFallback(String productoId, String vendedorId, Throwable t) {
        throw new RequestException("CAT", 503, HttpStatus.SERVICE_UNAVAILABLE, 
            "El servicio de catalogo no está disponible. Por favor intente más tarde.");
    }
}
