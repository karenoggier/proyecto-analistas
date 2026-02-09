package com.seminario.ms_pedido.DTOs;

import lombok.Data;

@Data
public class EnvioSeleccionDTO {
    public String metodoEnvio;
    public DireccionResponseDTO direccion;
}
