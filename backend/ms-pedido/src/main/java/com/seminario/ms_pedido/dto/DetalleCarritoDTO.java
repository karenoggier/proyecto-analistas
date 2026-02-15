package com.seminario.ms_pedido.dto;

import lombok.Data;

@Data
public class DetalleCarritoDTO {
    private String productoId;
    private Double cantidad;
    private Double montoUnitario;
    private String observaciones;
}
