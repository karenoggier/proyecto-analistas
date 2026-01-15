package com.seminario.ms_catalogo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class ProductoRequestDTO {
    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;
    @NotBlank(message = "La descripcion no puede estar vacia")
    private String descripcion;
    @NotBlank(message = "La imagen no puede estar vacia")
    private double precio;
    @NotBlank(message = "La categoria no puede estar vacia")
    private String Categoria;  
    @NotBlank(message = "La subcategoria no puede estar vacia")
    private String subcategoria;
    @NotBlank(message= "el campo disponible no puede estar vacio")
    private String disponible; 
    private String observaciones;
    private String logo;
    private String banner;
}
