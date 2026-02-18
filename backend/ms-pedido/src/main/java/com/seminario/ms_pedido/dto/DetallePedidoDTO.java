package com.seminario.ms_pedido.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetallePedidoDTO {
    private String id; 
    private String productoId;
    private Integer cantidad;
    private BigDecimal montoUnitario;
    private String observaciones;
    private BigDecimal subtotal;
}
