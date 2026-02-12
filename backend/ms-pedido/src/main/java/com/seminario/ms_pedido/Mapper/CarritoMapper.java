package com.seminario.ms_pedido.Mapper;

import java.util.ArrayList;
import java.util.stream.Collectors;

import com.seminario.ms_pedido.DTOs.CarritoDTO;
import com.seminario.ms_pedido.model.Carrito;

public class CarritoMapper {
    public static CarritoDTO toDTO(Carrito carrito) {
        CarritoDTO dto = new CarritoDTO();
        
        dto.setVendedorId(carrito.getVendedorId());
        dto.setMontoTotal(carrito.getMontoTotal());
        dto.setMontoTotalProductos(carrito.getMontoTotalProductos());

        dto.setDetallesCarrito(
            carrito.getDetallesCarrito()
            .stream()
            .map(DetalleCarritoMapper::toDTO)
            .collect(Collectors.toCollection(ArrayList::new))
        );

        return dto;
    }
}
