package com.seminario.ms_pedido.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PedidoDetalleDTO {
    private String id;
    private String nombreLocal;
    private LocalDateTime fechaCreacion;
    private String estado; 
    private Integer cantidadProductos;

    private BigDecimal montoTotalProductos;
    private BigDecimal costoEnvio;
    private BigDecimal comisionApp;
    private BigDecimal montoTotal;
    
    private String metodoEnvio;
    private List<DetallePedidoDTO> detalles;
    private ClienteResponseDTO cliente;

}
