package com.seminario.ms_pedido.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.seminario.ms_pedido.DTOs.ProductoResumidoDTO;
import com.seminario.ms_pedido.exception.RequestException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebClient {
    
    private final RestTemplate restTemplate;

    @Value("${catalogo.ms.url:http://localhost:8081}")
    private String catalogoBaseUrl;

    @CircuitBreaker(name = "catalogoClient", fallbackMethod = "buscarproductoFallback")
    @Retry(name = "catalogoClient")
    public ResponseEntity<ProductoResumidoDTO> buscarProducto(String productoId, String vendedorId) {
        try {
            String url = catalogoBaseUrl + "/catalogoMs/api/productos/getProductoByIdAndVendedorId?productoId={productoId}&vendedorId={vendedorId}";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
                                    
            //envío de mensaje HTTP
            ResponseEntity<?> response = restTemplate.getForEntity(url, ProductoResumidoDTO.class, productoId, vendedorId);
            
            //si devuelve un error personalizado del microservicio de catálogo
            if(response.getBody().getClass() != ProductoResumidoDTO.class) {
                Map<String, Object> error = (Map<String, Object>) response.getBody();
                throw new RequestException(error.get("ms_code").toString(),
                                           (int) error.get("ly_code"),
                                           HttpStatus.valueOf((int) error.get("status")),
                                           error.get("mensaje").toString());
            }

            return (ResponseEntity<ProductoResumidoDTO>) response;
            
        } catch (Exception e) {
            throw new RequestException("CAT", 500, HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error al sincronizar con ms-catalogo: " + e.getMessage());
        }
    }

    //Fallback method cuando el circuit breaker está abierto
    public ResponseEntity<ProductoResumidoDTO> buscarproductoFallback(String productoId, String vendedorId, Throwable t) {
        throw new RequestException("CAT", 503, HttpStatus.SERVICE_UNAVAILABLE, 
            "El servicio de catalogo no está disponible. Por favor intente más tarde.");
    }
}
