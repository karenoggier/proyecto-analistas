package com.seminario.ms_pago.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PedidoResponseDTO {
    private String id;
    private BigDecimal montoTotal;
}