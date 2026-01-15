package com.seminario.ms_catalogo.dto;
import lombok.Data;
@Data
public class ProductoResponseDTO {
    private String nombre;
    private String descripcion;
    private double precio;
    private String estado; 
    private String disponible;
    private String observaciones; 
    private String categoria;
    private String subcategoria;
    private String logo;
    private String banner;
    

}
