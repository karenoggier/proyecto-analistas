package com.seminario.ms_pedido.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
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
        
            String url = catalogoBaseUrl + "/catalogoMs/api/productos/resumen?productoId={productoId}&vendedorId={vendedorId}";
                
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
        log.error("CatalogoClient error productoId={} vendedorId={} type={} message={}",
            productoId, vendedorId, t.getClass().getSimpleName(), t.getMessage(), t);

        if (t instanceof RequestException requestException) {
            throw requestException;
        }

        if (t instanceof HttpStatusCodeException httpException) {
            int statusCode = httpException.getStatusCode().value();
            HttpStatus status = HttpStatus.resolve(statusCode);
            if (status == null) {
                status = HttpStatus.SERVICE_UNAVAILABLE;
            }
            String body = httpException.getResponseBodyAsString();
            String message = (body == null || body.isBlank())
                ? "Error al consultar el servicio de catalogo"
                : body;
            throw new RequestException("CAT", statusCode, status, message);
        }

        throw new RequestException("CAT", 503, HttpStatus.SERVICE_UNAVAILABLE,
            "El servicio de catalogo no está disponible. Por favor intente más tarde.");
    }
}
