package com.seminario.ms_catalogo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductoRequestDTO {
    @NotBlank(message = "El nombre no puede estar vacio")
    @Size(max = 100, message = "El nombre no puede tener mas de 100 caracteres")
    private String nombre;

    @NotBlank(message = "La descripcion no puede estar vacia")
    @Size(max = 1000, message = "La descripcion no puede tener mas de 1000 caracteres")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a cero")
    private Double precio;

    @NotBlank(message = "La categoria no puede estar vacia")
    private String categoria;

    @NotBlank(message = "La subcategoria no puede estar vacia")
    private String subcategoria;

    @NotNull(message = "La disponibilidad es obligatoria")
    private Boolean disponible;

    private String observaciones;

    private String imagen;
}
