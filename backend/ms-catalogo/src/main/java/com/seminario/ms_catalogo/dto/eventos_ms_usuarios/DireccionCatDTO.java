package com.seminario.ms_catalogo.dto.eventos_ms_usuarios;

import lombok.Data;

@Data
public class DireccionCatDTO {
    private String provincia;
    private String localidad;
    private String calle;
    private String numero;
    private String codigoPostal;
    private String observaciones;
    private Double latitud;
    private Double longitud;
}
