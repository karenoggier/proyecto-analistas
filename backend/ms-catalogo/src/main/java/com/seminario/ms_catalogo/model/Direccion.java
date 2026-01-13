package com.seminario.ms_catalogo.model;

import lombok.Data;

@Data
public class Direccion {
    //private String id;
    private String provincia;
    private String localidad;
    private String calle;
    private Integer numero;
    private String observaciones;
    private String codigoPostal;
    private String latitud;
    private String longitud;
}
