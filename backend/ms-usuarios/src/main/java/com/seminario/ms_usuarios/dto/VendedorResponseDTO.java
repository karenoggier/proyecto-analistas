package com.seminario.ms_usuarios.dto;

import com.seminario.ms_usuarios.dto.eventos_ms_catalogo.*;

import lombok.Data;

@Data
public class VendedorResponseDTO {
    private String nombreNegocio;
    private String nombreResponsable;
    private String apellidoResponsable;
    private String email;
    private String telefono;
    private DireccionResponseDTO direccion;

}
