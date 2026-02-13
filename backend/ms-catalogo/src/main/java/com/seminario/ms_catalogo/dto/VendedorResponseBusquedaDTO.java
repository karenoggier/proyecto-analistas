package com.seminario.ms_catalogo.dto;

import lombok.Data;

@Data
public class VendedorResponseBusquedaDTO {
    private String idVendedor;
    private String nombreNegocio;
    private String telefono;
    private Boolean realizaEnvios;
    private String horarioApertura;
    private String horarioCierre;
    private String tiempoEstimadoEspera;
    private String logo;
    private DireccionDTO direccion;
}
