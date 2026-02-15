package com.seminario.ms_pedido.dto;

import lombok.Data;

@Data
public class ProductoResumidoDTO {
    private String productoId;
    private String vendedorId;
    private Double montoUnitario;
    private String observaciones;
}
