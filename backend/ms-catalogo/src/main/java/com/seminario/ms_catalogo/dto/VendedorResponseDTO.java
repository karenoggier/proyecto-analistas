package com.seminario.ms_catalogo.dto;

import lombok.Data;
import java.util.ArrayList;

@Data
public class VendedorResponseDTO {
    private String usuarioId;
    private String nombreNegocio;
    private Boolean realizaEnvios;
    private String horarioApertura;
    private String horarioCierre;
    private String tiempoEstimadoEspera;
    private String logo;
    private String banner;
    
}
