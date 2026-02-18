package com.seminario.ms_pedido.service;

import java.math.BigDecimal;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.seminario.ms_pedido.client.CatalogoClient;
import com.seminario.ms_pedido.client.UsuarioClient;
import com.seminario.ms_pedido.repository.PedidoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final CatalogoClient catalogoClient;
    private final UsuarioClient usuarioClient;

    public BigDecimal calcularCostoEnvio(String idVendedor, String idDireccionCliente, Authentication authentication) {
        //Se busca el id del vendedor que pertenece a la base de datos de usuarios
        String idVendedorUsuario = catalogoClient.obtenerIdUsuarioPorVendedorId(idVendedor);
        System.out.println("ID Vendedor Usuario: " + idVendedorUsuario);

        Double distancia = usuarioClient.calcularDistanciaEntreDirecciones(idVendedorUsuario, idDireccionCliente, authentication);
        System.out.println("Distancia calculada: " + distancia);

        BigDecimal costoEnvio;

        //Esto hay que redefinirlo
        if (distancia <= 2) {
            costoEnvio = BigDecimal.valueOf(1000);
        } else if (distancia <= 5) {
            costoEnvio = BigDecimal.valueOf(2000);
        } else if (distancia <= 10) {
            costoEnvio = BigDecimal.valueOf(2500);
        }  else {
            costoEnvio = BigDecimal.valueOf(3000);
        }

        return costoEnvio;
        
    }

    /*public void crearPedido(CarritoDTO carrito, EnvioSeleccionDTO envioSeleccionDTO, PagoSeleccionDTO pagoSeleccionDTO) {
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
    }*/
}
