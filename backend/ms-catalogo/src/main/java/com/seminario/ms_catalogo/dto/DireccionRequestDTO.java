package com.seminario.ms_catalogo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DireccionRequestDTO {
    @NotBlank(message = "La provincia es obligatoria")
    private String provincia;
    @NotBlank(message = "La localidad es obligatoria")
    private String localidad;
    @NotBlank(message = "La calle es obligatoria")
    @Size(max = 200, message = "La calle no puede exceder los 200 caracteres")
    private String calle;
    @NotBlank(message = "El número es obligatorio")
    private String numero;
    @NotBlank(message = "El código postal es obligatorio")
    @Size(max = 5, message = "El código postal no puede exceder los 5 caracteres")
    private String codigoPostal;
    @Size(max = 150, message = "Las observaciones no pueden exceder los 150 caracteres")
    private String observaciones;
}
