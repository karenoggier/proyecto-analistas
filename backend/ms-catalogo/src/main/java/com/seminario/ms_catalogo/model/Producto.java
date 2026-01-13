package com.seminario.ms_catalogo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.Data;

@Data
public class Producto {
    @Id
    private String id;
    private String nombre;
    private String descripcion;
    private double precio;
    private Estado estado; 
    private double tamanio;
    private String unidadMedida;
    private Boolean aptoCeliacos;
    private Boolean aptoVeganos;
    private Boolean disponible;
    private String observaciones;

    @DBRef
    private Categoria categoria;
}
