package com.seminario.ms_catalogo.dto.eventos_ms_usuarios;
import com.seminario.ms_catalogo.dto.DireccionDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendedorRequestUsDTO {
    private String usuarioId;
    private String nombreNegocio;
    private Boolean realizaEnvios;
    private String horarioApertura;
    private String horarioCierre;
    private String tiempoEstimadoEspera;
    private String logo;
    private String banner;

    private DireccionDTO direccion;
}
