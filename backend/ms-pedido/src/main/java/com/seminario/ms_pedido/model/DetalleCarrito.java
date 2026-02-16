package com.seminario.ms_pedido.model;

import lombok.Data;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class DetalleCarrito {
    private String productoId;
    private Integer cantidad;
    private Double montoUnitario;
    private String observaciones;
}
