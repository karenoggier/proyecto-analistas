package com.seminario.ms_catalogo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendedorResumidoDTO {
    private String nombreNegocio;
    private String logoUrl;
    private Boolean realizaEnvios;
    private String Localidad;
}
