package com.seminario.ms_pedido.dto;

import com.seminario.ms_pedido.model.TipoEnvio;

import lombok.Data;

@Data
public class ConfirmarEnvioRequestDTO {
    private String vendedorId;
    private String idDireccion; // ID de la dirección elegida por el cliente
    private TipoEnvio metodoEnvio; // ENVIO_A_DOMICILIO o RETIRO_EN_LOCAL
}
