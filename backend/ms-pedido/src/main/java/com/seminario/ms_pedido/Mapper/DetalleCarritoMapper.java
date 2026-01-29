package com.seminario.ms_pedido.Mapper;

import com.seminario.ms_pedido.model.DetalleCarrito;
import com.seminario.ms_pedido.DTOs.DetalleCarritoDTO;

public class DetalleCarritoMapper {
    public static DetalleCarritoDTO toDTO(DetalleCarrito detalleCarrito) {
        DetalleCarritoDTO dto = new DetalleCarritoDTO();

        dto.setProductoId(detalleCarrito.getProductoId());
        dto.setCantidad(detalleCarrito.getCantidad());
        dto.setMontoUnitario(detalleCarrito.getMontoUnitario());
        dto.setObservaciones(detalleCarrito.getObservaciones());
        
        return dto;
    }
}
