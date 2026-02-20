package com.seminario.ms_pedido.dto;

import lombok.Data;

@Data
public class VendedorResumidoDTO {
    private String nombreNegocio;
    private String logoUrl;
    private Boolean realizaEnvios;
    private String Localidad;
}
