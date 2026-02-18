package com.seminario.ms_pedido.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.seminario.ms_pedido.dto.DetallePedidoDTO;
import com.seminario.ms_pedido.model.DetallePedido;

@Component
public class DetallePedidoMapper {
    public DetallePedidoDTO toDTO(DetallePedido detalle) {
        return DetallePedidoDTO.builder()
                .id(detalle.getId())
                .productoId(detalle.getIdProducto())
                .cantidad(detalle.getCantidad())
                .montoUnitario(detalle.getMontoUnitario())
                .subtotal(detalle.getMontoUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())))
                .observaciones(detalle.getObservaciones())
                .build();
    }
}
