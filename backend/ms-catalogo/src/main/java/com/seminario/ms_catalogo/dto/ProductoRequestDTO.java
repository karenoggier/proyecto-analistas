package com.seminario.ms_catalogo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
@Data
public class ProductoRequestDTO {
    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;

    @NotBlank(message = "La descripcion no puede estar vacia")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a cero")
    private double precio;

    @NotBlank(message = "La categoria no puede estar vacia")
    private String categoria;  

    @NotBlank(message = "La subcategoria no puede estar vacia")
    private String subcategoria;

    @NotNull(message = "La disponibilidad es obligatoria")
    private Boolean disponible; 

    private String observaciones;

    private String imagen;
}
