package com.seminario.ms_pedido.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemCarritoResponseDTO {
    private String productoId;
    private String nombreProducto; 
    private String urlImagen;      
    private Integer cantidad;
    private BigDecimal montoUnitario;
    private BigDecimal subtotal; // (cantidad * montoUnitario)
    private String observaciones;
}
