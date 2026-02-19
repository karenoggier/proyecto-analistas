package com.seminario.ms_pedido.client;

import java.util.List;

import org.jspecify.annotations.NonNull;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import com.seminario.ms_pedido.dto.ProductoResumidoDTO;
/**
 * Cliente HTTP declarativo para el microservicio de Catálogo.
 * 
 * Spring Boot 4 genera automáticamente la implementación usando RestClient.
 * No necesitas escribir código de conexión HTTP manualmente.
 * 
 * Características:
 * - Propagación automática de JWT (vía interceptor)
 * - Retry automático en caso de fallos transitorios
 * - HTTP/2 y Virtual Threads nativos
 * - Null-safety con JSpecify
 */
@HttpExchange(url = "/catalogoMs/api")
public interface CatalogoClient {
    
    /**
     * Busca un producto por ID y vendedor.
     * @throws org.springframework.web.client.HttpClientErrorException.NotFound si el producto no existe
     * @throws org.springframework.web.client.ResourceAccessException si hay problemas de conectividad
     */
    @GetExchange("/productos/resumen")
    @NonNull ProductoResumidoDTO buscarProducto(
        @RequestParam @NonNull String productoId,
        @RequestParam @NonNull String vendedorId
    );

    @GetExchange("/vendedores/id-usuario/{vendedorId}")
    @NonNull String obtenerIdUsuarioPorVendedorId(@PathVariable("vendedorId") @NonNull String vendedorId);

    @GetExchange("/vendedores/nombre-logo/{vendedorId}")
    @NonNull List<String> obtenerDatosVendedor(@PathVariable("vendedorId") @NonNull String vendedorId);
}