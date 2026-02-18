package com.seminario.ms_pedido.mapper;

import org.springframework.stereotype.Component;

import com.seminario.ms_pedido.dto.PedidoResponseDTO;
import com.seminario.ms_pedido.model.Pedido;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PedidoMapper {
    private final DetallePedidoMapper detallePedidoMapper;
    private final DireccionMapper direccionMapper;

    public PedidoResponseDTO toResponseDTO(Pedido pedido) {
        if (pedido == null) return null;

        return PedidoResponseDTO.builder()
                .id(pedido.getId())
                .clienteId(pedido.getClienteId())
                .vendedorId(pedido.getVendedorId())
                .fechaCreacion(pedido.getFechaCreacion())
                .estado(pedido.getEstado() != null ? pedido.getEstado().name() : null)
                .montoTotalProductos(pedido.getMontoTotalProductos())
                .costoEnvio(pedido.getCostoEnvio())
                .comisionApp(pedido.getComisionApp())
                .montoTotal(pedido.getMontoTotal())
                .metodoEnvio(pedido.getMetodoEnvio() != null ? pedido.getMetodoEnvio().name() : null)
                .direccion(pedido.getDireccion() != null ? 
                        direccionMapper.toResponseDTO(pedido.getDireccion()) : null)
                .detalles(pedido.getDetalles() != null ? 
                        pedido.getDetalles().stream().map(detallePedidoMapper::toDTO).toList() : null)
                .build();
    }
    
}
