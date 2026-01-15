package com.seminario.ms_usuarios.dto;

import com.seminario.ms_usuarios.dto.ms_catalogo.*;

import lombok.Data;

@Data
public class VendedorResponseDTO {
    private String nombreNegocio;
    private String nombreResponsable;
    private String apellidoResponsable;
    private String email;
    private String telefono;
    private DireccionResponseDTO direccion;
    private VendedorResponseCatDTO vendedorResponseCatDTO;

}
