package com.seminario.ms_pedido.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleCarrito {
    private String idItem;
    private String productoId;
    private Integer cantidad;
    private Double montoUnitario;
    private String observaciones;

    public DetalleCarrito(String productoId, Integer cantidad, Double montoUnitario, String observaciones) {
        this.idItem = UUID.randomUUID().toString(); 
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.montoUnitario = montoUnitario;
        this.observaciones = observaciones;
    }
}
