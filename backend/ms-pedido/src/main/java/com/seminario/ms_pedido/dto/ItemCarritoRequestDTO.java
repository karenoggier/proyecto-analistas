package com.seminario.ms_pedido.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemCarritoRequestDTO {
    @NotBlank(message = "El ID del vendedor es obligatorio")
    private String vendedorId;

    @NotBlank(message = "El ID del producto es obligatorio")
    private String productoId;

    @NotNull(message = "La cantidad no puede ser nula")
    @Min(value = 1, message = "La cantidad mínima es 1")
    private Integer cantidad;

    private String observaciones; 
}