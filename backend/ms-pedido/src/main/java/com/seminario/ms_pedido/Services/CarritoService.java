package com.seminario.ms_pedido.Services;

import com.seminario.ms_pedido.model.Carrito;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.seminario.ms_pedido.Repositories.CarritoRepository;
import com.seminario.ms_pedido.client.WebClient;
import com.seminario.ms_pedido.DTOs.ProductoResumidoDTO;
import java.util.ArrayList;
import com.seminario.ms_pedido.model.DetalleCarrito;

@Service
@RequiredArgsConstructor
public class CarritoService {
    private final CarritoRepository carritoRepository;

    private final WebClient webClient;

    public Carrito getCarritoByClienteId(String clienteId) {
        return carritoRepository.findByClienteId(clienteId).orElse(null);
    }

    public Carrito getCarritoByClienteAndVendedorId(String clienteId, String vendedorId) {
        return carritoRepository.findByClienteandVendedorId(clienteId, vendedorId).orElse(null);
    }

    public Carrito modificarItem(String clienteId, String vendedorId, String productoId, Double cantidad) {
        //buscar producto en ms catalogo
        ProductoResumidoDTO productoDTO = webClient.buscarProducto(productoId, vendedorId).getBody();
        
        // Lógica para agregar un producto al carrito
        Carrito carrito = getCarritoByClienteAndVendedorId(clienteId, vendedorId);
        if (carrito == null) {         //si no hay productos de ese vendedor en ningun carrito, crear uno nuevo
            carrito = new Carrito();
            carrito.setClienteId(clienteId);
            carrito.setVendedorId(vendedorId);
            carrito.setMontoTotal(0.0);
            carrito.setMontoTotalProductos(0.0);
            carrito.setDetallesCarrito(new ArrayList<DetalleCarrito>());
            carrito.addDetalle(new DetalleCarrito(productoId, cantidad, productoDTO.getMontoUnitario(), productoDTO.getObservaciones()));

        }
        else {
            for (DetalleCarrito detalle : carrito.getDetallesCarrito()) {     //ver si el producto ya existe en el carrito
                if (detalle.getProductoId().equals(productoId)) {
                    //modificar cantidad
                    detalle.setCantidad(cantidad);
                }
            }
        }      

        return carritoRepository.save(carrito);
    }
}
