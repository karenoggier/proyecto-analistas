package com.seminario.ms_usuarios.dto.eventos_ms_catalogo;
import java.util.ArrayList;
import com.seminario.ms_usuarios.dto.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VendedorRequestCatDTO {
    private String usuarioId;
    private String nombreNegocio;
    private Boolean realizaEnvios;
    private String horarioApertura;
    private String horarioCierre;
    private String tiempoEstimadoEspera;  
    private String logo;
    private String banner; 

    private DireccionCatDTO direccion; 

}
