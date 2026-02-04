package com.seminario.ms_catalogo.dto;

import lombok.Data;
@Data
public class DireccionRequestDTO {
    private String provincia;
    private String localidad;
    private String calle;
    private String numero;
    private String codigoPostal;
    private String observaciones;
}
