package com.seminario.ms_catalogo.model;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Producto {
    @Id
    private String id;
    private String nombre;
    private String descripcion;
    private double precio;
    private Estado estado; 
    private Boolean aptoCeliacos;
    private Estado disponible;
    private String observaciones;
    private Categoria categoria;
    private Subcategoria subcategoria; 

    private byte[] imagen; //to save image as byte array in mongodb

}

