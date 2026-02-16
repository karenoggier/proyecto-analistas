package com.seminario.ms_pedido.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component; 

import com.seminario.ms_pedido.client.CatalogoClient;
import com.seminario.ms_pedido.dto.CarritoResponseDTO;
import com.seminario.ms_pedido.dto.ItemCarritoResponseDTO;
import com.seminario.ms_pedido.dto.ProductoResumidoDTO;
import com.seminario.ms_pedido.model.Carrito;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CarritoMapper {
    private final CatalogoClient catalogoClient;

    public CarritoResponseDTO toResponseDTO(Carrito carrito) {
        List<ItemCarritoResponseDTO> items = carrito.getDetallesCarrito().stream()
            .map(detalle -> {
                // Enriquecemos con datos del ms-catalogo para el front
                ProductoResumidoDTO prod = catalogoClient.buscarProducto(detalle.getProductoId(), carrito.getVendedorId()).getBody();
                
                return ItemCarritoResponseDTO.builder()
                    .productoId(detalle.getProductoId())
                    .nombreProducto(prod.getNombre())
                    .urlImagen(prod.getImagenUrl())
                    .cantidad(detalle.getCantidad())
                    .montoUnitario(BigDecimal.valueOf(detalle.getMontoUnitario()))
                    .subtotal(BigDecimal.valueOf(detalle.getMontoUnitario()).multiply(BigDecimal.valueOf(detalle.getCantidad())))
                    .observaciones(detalle.getObservaciones())
                    .build();
            }).toList();

        return CarritoResponseDTO.builder()
            .id(carrito.getId())
            .vendedorId(carrito.getVendedorId())
            .montoTotalProductos(carrito.getMontoTotalProductos())
            .items(items)
            .build();
    }
    /*public static CarritoDTO toDTO(Carrito carrito) {
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
    }*/
}
