package com.seminario.ms_pedido.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarritoResponseDTO {
    private String id;
    private String vendedorId;
    private BigDecimal montoTotalProductos; // Suma de los ítems
    private BigDecimal comisionApp;
    private BigDecimal montoTotal;          // Suma final (Productos + Comisión)
    private List<ItemCarritoResponseDTO> items;
}
