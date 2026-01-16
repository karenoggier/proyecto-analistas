package com.seminario.ms_usuarios.dto;

import java.util.ArrayList;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data

public class VendedorUpdateRequestDTO {
    private String usuarioId;
    private String nombreNegocio;
    private String nombreResponsable;
    private String apellidoResponsable;
    private String telefono;
    private DireccionRequestDTO direccion;
    //informacion que se guarda en el ms-catalogo
    private Boolean realizaEnvios;
    private String horarioApertura;
    private String horarioCierre;
    private String tiempoEstimadoEspera;
    private String logo;
    private String banner;   
}
