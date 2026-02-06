package com.seminario.ms_catalogo.model;

import java.util.UUID;

import lombok.Data;

@Data
public class Producto {
    private String id = UUID.randomUUID().toString();
    private String nombre;
    private String descripcion;
    private double precio;
    private Estado estado; 
    private Boolean disponible;
    private String observaciones;
    private Categoria categoria;
    private Subcategoria subcategoria; 
    private String imagen;
}

