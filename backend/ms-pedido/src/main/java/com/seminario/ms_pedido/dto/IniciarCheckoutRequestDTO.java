package com.seminario.ms_pedido.dto;

import lombok.Data;

@Data
public class IniciarCheckoutRequestDTO {
    private String clienteId;
    private String vendedorId;
}
