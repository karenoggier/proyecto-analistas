package com.seminario.ms_catalogo.dto.ms_usuarios;
import com.seminario.ms_catalogo.dto.DireccionDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VendedorRequestUsDTO {
    @NotBlank(message = "El usuarioId no puede estar vacio")
    private String usuarioId;
    @NotBlank(message = "El nombre del negocio no puede estar vacio")
    private String nombreNegocio;
   
    private Boolean realizaEnvios;
    private String horarioApertura;
    private String horarioCierre;
    private String tiempoEstimadoEspera;
    private String logo;
    private String banner;

    private DireccionDTO direccion;
}
