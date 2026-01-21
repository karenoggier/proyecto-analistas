package com.seminario.ms_catalogo.dto;

import jakarta.validation.constraints.NotBlank;

public class VendedorRequestDTO {
    @NotBlank(message = "El usuarioId no puede estar vacio")
    private String usuarioId;
    @NotBlank(message = "El nombre del negocio no puede estar vacio")
    private String nombreNegocio;
    private String nombreResponsable;
    private String apellidoResponsable;
    private String telefono;
    private Boolean realizaEnvios;
    private String horarioApertura;
    private String horarioCierre;
    private String tiempoEstimadoEspera;
    private String logo;
    private String banner;

    private DireccionDTO direccion;
}
