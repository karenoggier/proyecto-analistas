package com.seminario.ms_pedido.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DireccionRequestDTO {
    @NotBlank(message = "La provincia es obligatoria")
    private String id_provincia;
    @NotBlank(message = "La localidad es obligatoria")
    private String id_localidad;
    @NotBlank(message = "La calle es obligatoria")
    private String calle;
    @NotBlank(message = "El número es obligatorio")
    private String numero;
    @NotBlank(message = "El código postal es obligatorio")
    private String codigoPostal;
    @NotBlank(message = "Las observaciones son obligatorias")
    private String observaciones;

}
