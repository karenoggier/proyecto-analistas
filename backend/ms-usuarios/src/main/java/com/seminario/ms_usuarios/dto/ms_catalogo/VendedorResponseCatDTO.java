package com.seminario.ms_usuarios.dto.ms_catalogo;

import jakarta.validation.constraints.NotBlank;

public class VendedorResponseCatDTO {
    private String usuarioId;
    private String nombreNegocio;
    private Boolean realizaEnvios;
    private String horarioApertura;
    private String horarioCierre;
    private String tiempoEstimadoEspera;
    private String logo;
    private String banner;


}
