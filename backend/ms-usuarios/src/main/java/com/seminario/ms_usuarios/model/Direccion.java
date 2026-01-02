package com.seminario.ms_usuarios.model;

import lombok.Data;

@Data
public class Direccion {
    private String id;
    private String calle;
    private String numero;
    private String localidad;
    private String provincia;
    private String codigoPostal;
    private String observaciones;
    private Double latitud;
    private Double longitud;
}
