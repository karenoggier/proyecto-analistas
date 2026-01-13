package com.seminario.ms_catalogo.dto;
import lombok.Data;
@Data
public class ProductoResponseDTO {
    private String nombre;
    private String descripcion;
    private double precio;
    private String estado; 
    private double tamanio;
    private String unidadMedida;
    private Boolean aptoCeliacos;
    private Boolean aptoVeganos;
    private Boolean disponible;
    private String observaciones; 
    private String categoriaNombre;
    private String categoriaDescripcion;

}
