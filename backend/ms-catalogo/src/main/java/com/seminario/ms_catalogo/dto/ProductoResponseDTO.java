package com.seminario.ms_catalogo.dto;
import lombok.Data;
@Data
public class ProductoResponseDTO {
    private String id;
    private String nombre;
    private String descripcion;
    private double precio;
    private String estado; 
    private Boolean disponible;
    private String observaciones; 
    private String categoria;
    private String subcategoria;
    private String imagen;
    

}
