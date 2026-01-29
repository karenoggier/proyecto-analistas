package com.seminario.ms_pedido.DTOs;

import lombok.Data;

@Data
public class ModificarItemCarritoDTO {
    private String clienteId;
    private String vendedorId;
    private String productoId;
    private Double cantidad;
}
