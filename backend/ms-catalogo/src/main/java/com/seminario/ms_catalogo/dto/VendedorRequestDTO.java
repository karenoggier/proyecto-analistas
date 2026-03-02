package com.seminario.ms_catalogo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VendedorRequestDTO {
    @NotBlank(message = "El nombre del negocio es obligatorio")
    @Size(max = 150, message = "El nombre del negocio no puede exceder los 150 caracteres")
    private String nombreNegocio;
    @NotBlank(message = "El nombre del responsable es obligatorio")
    @Size(max = 100, message = "El nombre del responsable no puede exceder los 100 caracteres")
    private String nombreResponsable;
    @NotBlank(message = "El apellido del responsable es obligatorio")
    @Size(max = 100, message = "El apellido del responsable no puede exceder los 100 caracteres")
    private String apellidoResponsable;
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^\\+54\\s9\\s\\d{4}-\\d{6}$", message = "El teléfono debe respetar el formato +54 9 XXXX-XXXXXX")
    private String telefono;
    private Boolean realizaEnvios;
    private String horarioApertura;
    // debe ser después de la hora de apertura
    @Past(message = "La hora de cierre debe ser una hora pasada")
    private String horarioCierre;
    private String tiempoEstimadoEspera;
    private String logo;
    private String banner;
    private DireccionRequestDTO direccion;
}
