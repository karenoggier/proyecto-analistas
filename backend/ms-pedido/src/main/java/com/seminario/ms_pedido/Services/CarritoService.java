package com.seminario.ms_pedido.Services;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.seminario.ms_pedido.DTOs.ProductoResumidoDTO;
import com.seminario.ms_pedido.Repositories.CarritoRepository;
import com.seminario.ms_pedido.client.WebClient;
import com.seminario.ms_pedido.exception.RequestException;
import com.seminario.ms_pedido.model.Carrito;
import com.seminario.ms_pedido.model.DetalleCarrito;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarritoService {
    private final CarritoRepository carritoRepository;

    private final WebClient webClient;

    public ArrayList<Carrito> getCarritoByClienteId(String clienteId) {
        return carritoRepository.findByClienteId(clienteId).orElse(null);
    }

    public Carrito getCarritoByClienteAndVendedorId(String clienteId, String vendedorId) {
        return carritoRepository.findByClienteIdAndVendedorId(clienteId, vendedorId).orElse(null);
    }

    public Carrito modificarItem(String clienteId, String vendedorId, String productoId, Double cantidad) {
        //buscar producto en ms catalogo
        ProductoResumidoDTO productoDTO = webClient.buscarProducto(productoId, vendedorId).getBody();
        
        // Lógica para agregar un producto al carrito
        Carrito carrito = getCarritoByClienteAndVendedorId(clienteId, vendedorId);
        if (carrito == null) {         //si no hay productos de ese vendedor en ningun carrito de ese cliente, crear uno nuevo
            carrito = new Carrito();
            carrito.setClienteId(clienteId);
            carrito.setVendedorId(vendedorId);
            carrito.setMontoTotal(0.0);
            carrito.setMontoTotalProductos(0.0);
            carrito.setDetallesCarrito(new ArrayList<>());
            carrito.addDetalle(new DetalleCarrito(productoId, cantidad, productoDTO.getMontoUnitario(), productoDTO.getObservaciones()));
            carrito.calcularMontosTotales();

        }
        else {
            boolean productoExiste = false;
            for (DetalleCarrito detalle : carrito.getDetallesCarrito()) {     //ver si el producto ya existe en el carrito
                if (detalle.getProductoId().equals(productoId)) {
                    //modificar cantidad
                    detalle.setCantidad(cantidad);
                    carrito.calcularMontosTotales();
                    productoExiste = true;
                    break;
                }
            }
            if (!productoExiste) {
                carrito.addDetalle(new DetalleCarrito(productoId, cantidad, productoDTO.getMontoUnitario(), productoDTO.getObservaciones()));
                carrito.calcularMontosTotales();
            }
        }      

        return carritoRepository.save(carrito);
    }

    public void deleteItem(String clienteId, String vendedorId, String productoId) {
        Carrito carrito = getCarritoByClienteAndVendedorId(clienteId, vendedorId);
        if (carrito == null || !carrito.getDetallesCarrito().stream().anyMatch(detalle -> detalle.getProductoId().equals(productoId))) {
            throw new RequestException(vendedorId, 400, HttpStatus.BAD_REQUEST, "El producto no está en el carrito");
        }
        else{
            carrito.getDetallesCarrito().removeIf(detalle -> detalle.getProductoId().equals(productoId));
            carrito.calcularMontosTotales();
            carritoRepository.save(carrito);
        }
    }
}
