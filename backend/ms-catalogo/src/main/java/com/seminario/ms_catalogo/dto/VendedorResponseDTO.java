package com.seminario.ms_catalogo.dto;

import java.util.ArrayList;

import lombok.Data;

@Data
public class VendedorResponseDTO {
    private String usuarioId;
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
    private ArrayList<ProductoResponseDTO> productos;
}
