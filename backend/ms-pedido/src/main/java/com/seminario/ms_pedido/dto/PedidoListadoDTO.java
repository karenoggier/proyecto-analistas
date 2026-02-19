package com.seminario.ms_pedido.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PedidoListadoDTO {
    private String id;
    private String nombreLocal;
    private String logo;
    private LocalDateTime fechaCreacion;
    private String estado; 
    private BigDecimal montoTotal;
    private Integer cantidadProductos;
}
