package com.seminario.ms_catalogo.dto.eventos_ms_usuarios;

import com.seminario.ms_catalogo.dto.DireccionDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendedorRegistradoEvent {
    private String usuarioId;      
    private String email;
    private String nombreNegocio;
    private String nombreResponsable;
    private String apellidoResponsable;
    private String telefono;
    private DireccionDTO direccion;
}