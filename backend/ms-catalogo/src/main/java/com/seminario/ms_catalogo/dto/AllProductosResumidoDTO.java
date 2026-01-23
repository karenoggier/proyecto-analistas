package com.seminario.ms_catalogo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllProductosResumidoDTO {
    private String id;
    private String nombre;
    private double precio;
    private String imagen;
    private String descripcion;
}
