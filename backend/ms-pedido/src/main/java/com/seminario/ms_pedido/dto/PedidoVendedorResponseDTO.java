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
public class PedidoVendedorResponseDTO {
    private String id;
    private LocalDateTime fechaCreacion;
    private String estado; 
    
    // --- DATOS DEL CLIENTE ---
    private String nombreCliente;
    private String apellidoCliente;
    private String emailCliente;
    private String telefonoCliente;
    
    // --- DATOS DE DINERO ---
    private BigDecimal montoTotalProductos;
    private BigDecimal costoEnvio;
    private BigDecimal montoTotal;
    
    // --- ENTREGA Y DETALLE ---
    private String metodoEnvio;
    private DireccionResponseDTO direccion; 
    private List<DetallePedidoDTO> detalles; 
}