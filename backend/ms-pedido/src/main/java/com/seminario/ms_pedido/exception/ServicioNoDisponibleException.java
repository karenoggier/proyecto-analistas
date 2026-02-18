package com.seminario.ms_pedido.exception;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import lombok.Value;


/**
 * Excepción lanzada cuando un servicio externo no está disponible.
 * 
 * Corresponde a un error HTTP 503 SERVICE UNAVAILABLE.
 */
@Value
public class ServicioNoDisponibleException extends RuntimeException {
    private String servicio;

    public ServicioNoDisponibleException(@NonNull String servicio, @Nullable String mensaje) {
        super(String.format("Servicio '%s' no disponible%s", 
                           servicio, 
                           mensaje != null ? ": " + mensaje : ""));
        this.servicio = servicio;
    }
    
    public ServicioNoDisponibleException(@NonNull String servicio, Throwable causa) {
        super(String.format("Servicio '%s' no disponible", servicio), causa);
        this.servicio = servicio;
    }
}