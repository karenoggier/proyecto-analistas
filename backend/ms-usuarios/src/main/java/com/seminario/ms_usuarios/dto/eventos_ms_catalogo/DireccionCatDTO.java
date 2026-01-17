package com.seminario.ms_usuarios.dto.eventos_ms_catalogo;

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
