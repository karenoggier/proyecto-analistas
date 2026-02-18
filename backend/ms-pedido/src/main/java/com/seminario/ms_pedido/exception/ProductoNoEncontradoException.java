package com.seminario.ms_pedido.exception;

import org.jspecify.annotations.NonNull;

import lombok.Value;

/**
 * Excepción lanzada cuando un producto no existe en el catálogo.
 * 
 * Corresponde a un error HTTP 404 NOT FOUND.
 */
@Value
public class ProductoNoEncontradoException extends RuntimeException {
    private String productoId;
    private String vendedorId;

    public ProductoNoEncontradoException(@NonNull String productoId, @NonNull String vendedorId) {
        
        super(String.format(
            "Producto no encontrado: productoId=%s, vendedorId=%s", 
            productoId, vendedorId
        ));
        this.productoId = productoId;
        this.vendedorId = vendedorId;
    }
}