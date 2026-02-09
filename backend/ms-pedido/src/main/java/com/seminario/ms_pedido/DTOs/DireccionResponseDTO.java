package com.seminario.ms_pedido.DTOs;

import lombok.Data;

@Data
public class DireccionResponseDTO {
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
