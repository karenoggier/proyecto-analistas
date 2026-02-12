package com.seminario.ms_pedido.Services;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.seminario.ms_pedido.DTOs.ProductoResumidoDTO;
import com.seminario.ms_pedido.Repositories.CarritoRepository;
import com.seminario.ms_pedido.client.WebClient;
import com.seminario.ms_pedido.exception.RequestException;
import com.seminario.ms_pedido.model.Carrito;
import com.seminario.ms_pedido.model.ClienteCarrito;
import com.seminario.ms_pedido.model.DetalleCarrito;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarritoService {
    private final CarritoRepository carritoRepository;

    private final WebClient webClient;

    public ArrayList<Carrito> getCarritoByClienteEmail(String clienteEmail) {
        ClienteCarrito clienteCarrito = carritoRepository.findByClienteEmail(clienteEmail).orElse(null);
        if (clienteCarrito != null) {
            return (ArrayList<Carrito>)clienteCarrito.getCarritos();
        }
        return null;
    }

    public Carrito getCarritoByClienteAndVendedorId(String clienteEmail, String vendedorId) {
        return carritoRepository.findByClienteEmailAndVendedorId(clienteEmail, vendedorId);
    }

    public Carrito modificarItem(String clienteEmail, String vendedorId, String productoId, Double cantidad) {
        //buscar producto en ms catalogo
        ProductoResumidoDTO productoDTO = webClient.buscarProducto(productoId, vendedorId).getBody();
        
        // Lógica para agregar un producto al carrito
        ClienteCarrito clienteCarrito = carritoRepository.findByClienteEmail(clienteEmail).orElse(null);
        Carrito carrito = null;

        if (clienteCarrito == null) {         //si no hay un cliente con carrito, crear uno nuevo
            clienteCarrito = new ClienteCarrito();
            clienteCarrito.setClienteEmail(clienteEmail);

            carrito = new Carrito();
            carrito.setVendedorId(vendedorId);

            carrito.addDetalle(new DetalleCarrito(productoId, cantidad, productoDTO.getMontoUnitario(), productoDTO.getObservaciones()));
            carrito.calcularMontosTotales();

            clienteCarrito.addCarrito(carrito);
        }
        else {  //si hay un cliente con carrito
            carrito = clienteCarrito.encontrarCarritoPorVendedor(vendedorId);

            if (carrito == null) {  //si el cliente no tiene un carrito para ese vendedor
                carrito = new Carrito();
                carrito.setVendedorId(vendedorId);
                carrito.addDetalle(new DetalleCarrito(productoId, cantidad, productoDTO.getMontoUnitario(), productoDTO.getObservaciones()));
                carrito.calcularMontosTotales();
                clienteCarrito.addCarrito(carrito);
            }
            else{
                DetalleCarrito detalleExistente = carrito.encontrarProducto(productoId);

                if (detalleExistente == null){  //si el cliente ya tiene un carrito para ese vendedor pero NO tiene ese producto
                    carrito.addDetalle(new DetalleCarrito(productoId, cantidad, productoDTO.getMontoUnitario(), productoDTO.getObservaciones()));
                    carrito.calcularMontosTotales();
                }
                else{   //si el cliente ya tiene un carrito para ese vendedor y ya tiene ese producto, actualizar cantidad y monto unitario
                    
                    detalleExistente.setCantidad(cantidad);
                    carrito.calcularMontosTotales();          
                }
            }
        }      
        carritoRepository.save(clienteCarrito);

        return carrito;
    }

    public void deleteItem(String clienteEmail, String vendedorId, String productoId) {
        ClienteCarrito clienteCarrito = carritoRepository.findByClienteEmail(clienteEmail).get();
        if (clienteCarrito == null) {
            throw new RequestException(vendedorId, 400, HttpStatus.BAD_REQUEST, "El producto no está en el carrito");
        }

        Carrito carrito = clienteCarrito.encontrarCarritoPorVendedor(vendedorId);
        if (carrito == null || !carrito.getDetallesCarrito().stream().anyMatch(detalle -> detalle.getProductoId().equals(productoId))) {
            throw new RequestException(vendedorId, 400, HttpStatus.BAD_REQUEST, "El producto no está en el carrito");
        }
        
        carrito.getDetallesCarrito().removeIf(detalle -> detalle.getProductoId().equals(productoId));
        carrito.calcularMontosTotales();

        carritoRepository.save(clienteCarrito);
        
    }
}
