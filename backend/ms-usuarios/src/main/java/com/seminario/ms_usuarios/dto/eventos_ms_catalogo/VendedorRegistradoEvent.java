package com.seminario.ms_usuarios.dto.eventos_ms_catalogo;

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
    private DireccionCatDTO direccion;
}