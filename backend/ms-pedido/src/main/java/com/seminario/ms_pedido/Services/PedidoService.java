package com.seminario.ms_pedido.Services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.seminario.ms_pedido.DTOs.CarritoDTO;
import com.seminario.ms_pedido.DTOs.DetalleCarritoDTO;
import com.seminario.ms_pedido.DTOs.EnvioSeleccionDTO;
import com.seminario.ms_pedido.DTOs.PagoSeleccionDTO;
import com.seminario.ms_pedido.Repositories.PedidoRepository;
import com.seminario.ms_pedido.model.DetalleEnvio;
import com.seminario.ms_pedido.model.DetallePedido;
import com.seminario.ms_pedido.model.EstadoPedido;
import com.seminario.ms_pedido.model.Pedido;
import com.seminario.ms_pedido.model.TipoEnvio;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class PedidoService {
    private final PedidoRepository pedidoRepository;

    public void crearPedido(CarritoDTO carrito, EnvioSeleccionDTO envioSeleccionDTO, PagoSeleccionDTO pagoSeleccionDTO) {
        Pedido pedido = new Pedido();
        
        //datos del pedido
        //pedido.setClienteId(carrito.getClienteId());
        pedido.setVendedorId(carrito.getVendedorId());
        pedido.setFechaCreacion(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setMontoTotalProductos(carrito.getMontoTotalProductos());
        pedido.setMontoTotal(carrito.getMontoTotal());

        //detalles del pedido
        pedido.setDetalles(new java.util.ArrayList<>());
        for (DetalleCarritoDTO item : carrito.getDetallesCarrito()) {
            DetallePedido detalle = new DetallePedido();
            detalle.setIdProducto(item.getProductoId());
            detalle.setCantidad(item.getCantidad());
            detalle.setMontoUnitario(item.getMontoUnitario());
            detalle.setObservaciones(item.getObservaciones());
            pedido.getDetalles().add(detalle);
        }

        //forma de envio
        pedido.setMetodoEnvio(TipoEnvio.valueOf(envioSeleccionDTO.getMetodoEnvio()));
        if(TipoEnvio.valueOf(envioSeleccionDTO.getMetodoEnvio()) == TipoEnvio.ENVIO_A_DOMICILIO) {
            DetalleEnvio detalleEnvio = new DetalleEnvio();
            detalleEnvio.setCalle(envioSeleccionDTO.getDireccion().getCalle());
            detalleEnvio.setNumero(envioSeleccionDTO.getDireccion().getNumero());
            detalleEnvio.setLocalidad(envioSeleccionDTO.getDireccion().getLocalidad());
            detalleEnvio.setProvincia(envioSeleccionDTO.getDireccion().getProvincia());
            detalleEnvio.setCodigoPostal(envioSeleccionDTO.getDireccion().getCodigoPostal());
            detalleEnvio.setLatitud(envioSeleccionDTO.getDireccion().getLatitud());
            detalleEnvio.setLongitud(envioSeleccionDTO.getDireccion().getLongitud());

            pedido.setDetalleEnvio(detalleEnvio);
        }

        //pago

        pedidoRepository.save(pedido);
    }
}
