package com.seminario.ms_pedido.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class BorrarItemsRequestDTO {
    @NotBlank(message = "El ID del vendedor es obligatorio")
    private String vendedorId;
    
    @NotEmpty(message = "La lista de IDs de items no puede estar vacía")
    private List<String> itemsIds;
}