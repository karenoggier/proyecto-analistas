package com.seminario.ms_pedido.DTOs;

import lombok.Data;

@Data
public class DeleteItemDTO {
    private String clienteId;
    private String vendedorId;
    private String productoId;
}
