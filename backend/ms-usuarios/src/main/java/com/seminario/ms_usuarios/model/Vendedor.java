package com.seminario.ms_usuarios.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Vendedor extends Usuario {
    private String nombreNegocio;
    private String nombreResponsable;
    private String apellidoResponsable;
}
