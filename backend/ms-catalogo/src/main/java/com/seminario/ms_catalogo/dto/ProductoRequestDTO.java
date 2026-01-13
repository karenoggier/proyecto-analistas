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
    private String idCategoria;  
    @NotBlank(message = "El tamaño no puede estar vacio")
    private double tamanio;
    @NotBlank(message = "La unidad de medida no puede estar vacia")
    private String unidadMedida;
    @NotBlank(message= "el campo aptoCeliacos no puede estar vacio")
    private Boolean aptoCeliacos;
    @NotBlank(message= "el campo aptoVeganos no puede estar vacio")
    private Boolean aptoVeganos;
    @NotBlank(message= "el campo disponible no puede estar vacio")
    private Boolean disponible; 
    private String observaciones;

}
