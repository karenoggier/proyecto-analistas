package com.seminario.ms_pedido.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionDTO {
    private String id;
    private String mensaje;
    private String pedidoId;
    private String fechaHora; 
    private boolean leida;
}