package com.seminario.ms_pedido.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoResponseDTO {
    private String id;
    private String nombreLocal;
    private String logo;
    private LocalDateTime fechaCreacion;
    private String estado; 
    
    private BigDecimal montoTotalProductos;
    private BigDecimal costoEnvio;
    private BigDecimal comisionApp;
    private BigDecimal montoTotal;
    
    private String metodoEnvio;
    private DireccionResponseDTO direccion; 
    private List<DetallePedidoDTO> detalles; 
   // private String clienteId;
   // private String vendedorId;
}
