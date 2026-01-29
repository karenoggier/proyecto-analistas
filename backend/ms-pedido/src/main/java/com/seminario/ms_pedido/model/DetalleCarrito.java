package com.seminario.ms_pedido.model;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class DetalleCarrito {
    private String productoId;
    private Double cantidad;
    private Double montoUnitario;
    private String observaciones;
}
