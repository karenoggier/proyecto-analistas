package com.seminario.ms_usuarios.dto.ms_catalogo;
import java.util.ArrayList;
import com.seminario.ms_usuarios.dto.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VendedornRequestCastDTO {
    @NotBlank(message = "El usuario es obligatorio")
    private String usuarioId;
    @NotBlank(message = "El nombre del negocio es obligatorio")
    private String nombreNegocio;
    private Boolean realizaEnvios;
    private String horarioApertura;
    private String horarioCierre;
    private String tiempoEstimadoEspera;  
    private String logo;
    private String banner; 

    private DireccionCatDTO direccion; 

}
