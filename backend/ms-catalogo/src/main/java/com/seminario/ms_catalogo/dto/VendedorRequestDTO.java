package com.seminario.ms_catalogo.dto;

import jakarta.validation.constraints.NotBlank;

public class VendedorRequestDTO {
    @NotBlank(message = "El usuarioId no puede estar vacio")
    private String usuarioId;
    @NotBlank(message = "El nombre del negocio no puede estar vacio")
    private String nombreNegocio;
    @NotBlank(message = "El logo no puede estar vacio")
    private Boolean realizaEnvios;
    @NotBlank(message = "El horario de apertura no puede estar vacio")
    private String horarioApertura;
    @NotBlank(message = "El horario de cierre no puede estar vacio")
    private String horarioCierre;
    @NotBlank(message = "El tiempo estimado de espera no puede estar vacio")
    private String tiempoEstimadoEspera;
}
