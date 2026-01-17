package com.seminario.ms_catalogo.dto.eventos_ms_usuarios;

import lombok.Data;

@Data
public class VendedorResponseUsDTO {
    private String usuarioId;
    private String nombreNegocio;
    private Boolean realizaEnvios;
    private String horarioApertura;
    private String horarioCierre;
    private String tiempoEstimadoEspera;
    private String logo;
    private String banner;
    
}
