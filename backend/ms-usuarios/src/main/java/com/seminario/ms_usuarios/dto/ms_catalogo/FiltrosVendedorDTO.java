package com.seminario.ms_usuarios.dto.ms_catalogo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FiltrosVendedorDTO {
    @NotBlank(message = "La provincia es obligatoria")
    private String provincia;
    @NotBlank(message = "La localidad es obligatoria")
    private String localidad;
    private String nombreNegocio;
}
