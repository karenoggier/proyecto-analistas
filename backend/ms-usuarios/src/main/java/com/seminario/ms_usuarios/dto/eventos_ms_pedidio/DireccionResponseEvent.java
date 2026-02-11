package com.seminario.ms_usuarios.dto.eventos_ms_pedidio;

import lombok.Data;


@Data
public class DireccionResponseEvent {
    private String id;
    private String provincia;
    private String localidad;
    private String calle;
    private String numero; 
    private String codigoPostal; 
    private String observaciones;
    private Double latitud;
    private Double longitud;

}
