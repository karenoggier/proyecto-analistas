package com.seminario.ms_pedido.service;

import org.jspecify.annotations.NonNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import com.seminario.ms_pedido.client.CatalogoClient;
import com.seminario.ms_pedido.dto.ProductoResumidoDTO;
import com.seminario.ms_pedido.exception.ProductoNoEncontradoException;
import com.seminario.ms_pedido.exception.ServicioNoDisponibleException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio de integración con el microservicio de Catálogo.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CatalogoService {
    
    private final CatalogoClient catalogoClient;

    /**
     * Busca un producto con resiliencia completa.
     * 
     * CACHE:
     * - Key: "productoId_vendedorId"
     * 
     * OBSERVABILIDAD:
     * - Genera spans automáticos para traces distribuidos
     * - Métricas: latencia, tasa de éxito, cache hit rate
     * - Logs estructurados con contexto
     * 
     * @throws ProductoNoEncontradoException si el producto no existe (404)
     * @throws ServicioNoDisponibleException si el servicio está caído o el circuit breaker está abierto
     */
    @CircuitBreaker(name = "catalogo", fallbackMethod = "buscarProductoFallback")
    @Cacheable(value = "productos", key = "#productoId + '_' + #vendedorId")
    @Observed(
        name = "catalogo.buscar-producto",
        contextualName = "buscar-producto-catalogo"
    )
    public @NonNull ProductoResumidoDTO buscarProducto(@NonNull String productoId, @NonNull String vendedorId) {

        try {
            ProductoResumidoDTO producto = catalogoClient.buscarProducto(productoId, vendedorId);
            
            return producto;
            
        } catch (HttpClientErrorException.NotFound e) {
            // 404: El producto no existe
            throw new ProductoNoEncontradoException(productoId, vendedorId);
            
        } catch (HttpClientErrorException.BadRequest e) {
            // 400: Request mal formado
            throw e;
            
        } catch (HttpClientErrorException e) {
            throw e;
            
        } catch (ResourceAccessException e) {
            // Error de conectividad (timeout, connection refused, etc.)
            log.error("Error de conectividad con MS-Catálogo: {}", e.getMessage());
            throw new ServicioNoDisponibleException("catálogo", e);
        }
    }

    
    private @NonNull ProductoResumidoDTO buscarProductoFallback(@NonNull String productoId, @NonNull String vendedorId, Exception e) {
        
        throw new ServicioNoDisponibleException(
            "catálogo",
            "El servicio de catálogo no está disponible en este momento. " +
            e.getMessage()
        );
    }

    @CircuitBreaker(name = "catalogo", fallbackMethod = "buscarProductoFallback")
    @Cacheable(value = "productos", key = "#productoId + '_' + #vendedorId")
    @Observed(
        name = "catalogo.buscar-producto",
        contextualName = "buscar-producto-catalogo"
    )
    public @NonNull String obtenerIdUsuarioPorVendedorId(@NonNull String vendedorId) {

        try {
            String id = catalogoClient.obtenerIdUsuarioPorVendedorId(vendedorId);
            
            return id;
            
        } catch (HttpClientErrorException.BadRequest e) {
            // 400: Request mal formado
            throw e;
            
        } catch (HttpClientErrorException e) {
            throw e;
            
        } catch (ResourceAccessException e) {
            // Error de conectividad (timeout, connection refused, etc.)
            log.error("Error de conectividad con MS-Catálogo: {}", e.getMessage());
            throw new ServicioNoDisponibleException("catálogo", e);
        }
    }

    
    private @NonNull String obtenerIdUsuarioPorVendedorIdFallback(@NonNull String vendedorId, Exception e) {
        
        throw new ServicioNoDisponibleException(
            "catálogo",
            "El servicio de catálogo no está disponible en este momento. " +
            e.getMessage()
        );
    }

}