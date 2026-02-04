package com.seminario.ms_catalogo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VendedorRequestDTO {
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
    private DireccionRequestDTO direccion;
}
