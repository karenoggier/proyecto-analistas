package com.seminario.ms_pedido.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.seminario.ms_pedido.client.CatalogoClient;
import com.seminario.ms_pedido.dto.ClienteResponseDTO;
import com.seminario.ms_pedido.dto.PedidoDetalleDTO;
import com.seminario.ms_pedido.dto.PedidoListadoDTO;
import com.seminario.ms_pedido.dto.PedidoResponseDTO;
import com.seminario.ms_pedido.model.Pedido;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PedidoMapper {
    private final DetallePedidoMapper detallePedidoMapper;
    private final DireccionMapper direccionMapper;
    private final CatalogoClient catalogoClient;

    public PedidoResponseDTO toResponseDTO(Pedido pedido) {
        if (pedido == null) return null;
        List<String> datosVendedor = obtenerDatosVendedor(pedido.getVendedorId());

        return PedidoResponseDTO.builder()
                .id(pedido.getId())
                .nombreLocal(datosVendedor.get(0))
                .logo(datosVendedor.get(1))
                //.clienteId(pedido.getClienteId())
                //.vendedorId(pedido.getVendedorId())
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

    public PedidoListadoDTO toListadoDTO(Pedido pedido) {
        if (pedido == null) return null;
        //el primer string es el nombre del local, el segundo el logo
        List<String> datosVendedor = obtenerDatosVendedor(pedido.getVendedorId());

        String nombreLocal = datosVendedor.get(0);
        String logo = datosVendedor.get(1);

        return PedidoListadoDTO.builder()
                .id(pedido.getId())
                .nombreLocal(nombreLocal)
                .logo(logo)
                .fechaCreacion(pedido.getFechaCreacion())
                .estado(pedido.getEstado() != null ? pedido.getEstado().name() : null)
                .montoTotal(pedido.getMontoTotal())
                .cantidadProductos(pedido.getDetalles() != null ? pedido.getDetalles().size() : 0)
                .build();
    }

    public PedidoDetalleDTO toDetalleDTO(Pedido pedido, ClienteResponseDTO cliente) {
        if (pedido == null) return null;
        List<String> datosVendedor = obtenerDatosVendedor(pedido.getVendedorId());

        return PedidoDetalleDTO.builder()
                .id(pedido.getId())
                .nombreLocal(datosVendedor.get(0))
                .fechaCreacion(pedido.getFechaCreacion())
                .estado(pedido.getEstado() != null ? pedido.getEstado().name() : null)
                .cantidadProductos(pedido.getDetalles() != null ? pedido.getDetalles().size() : 0)
                .montoTotalProductos(pedido.getMontoTotalProductos())
                .costoEnvio(pedido.getCostoEnvio())
                .comisionApp(pedido.getComisionApp())
                .montoTotal(pedido.getMontoTotal())
                .metodoEnvio(pedido.getMetodoEnvio() != null ? pedido.getMetodoEnvio().name() : null)
                .detalles(pedido.getDetalles() != null ? 
                        pedido.getDetalles().stream().map(detallePedidoMapper::toDTO).toList() : null)
                .cliente(cliente)
                .build();
    }

   // Método para obtener datos del vendedor desde el ms-catalogo ([0] nombre del local, [1] logo) 
    public List<String> obtenerDatosVendedor(String vendedorId) {
        return catalogoClient.obtenerDatosVendedor(vendedorId);
    }

    
}
