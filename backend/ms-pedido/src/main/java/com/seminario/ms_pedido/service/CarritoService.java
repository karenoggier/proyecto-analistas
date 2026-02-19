package com.seminario.ms_pedido.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seminario.ms_pedido.client.CatalogoClient;
import com.seminario.ms_pedido.dto.CarritoResponseDTO;
import com.seminario.ms_pedido.dto.ProductoResumidoDTO;
import com.seminario.ms_pedido.exception.RequestException;
import com.seminario.ms_pedido.mapper.CarritoMapper;
import com.seminario.ms_pedido.model.Carrito;
import com.seminario.ms_pedido.model.Cliente;
import com.seminario.ms_pedido.model.DetalleCarrito;
import com.seminario.ms_pedido.repository.CarritoRepository;
import com.seminario.ms_pedido.repository.ClienteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final ClienteRepository clienteRepository;
    private final CarritoMapper carritoMapper;
    private final CatalogoClient catalogoClient;

    public CarritoResponseDTO agregarOModificarItem(String email, String vendedorId, String productoId, Integer cantidad, String observaciones) {
        // 1. Obtener el cliente desde PostgreSQL usando el email del token
        Cliente cliente = clienteRepository.findByEmail(email)
            .orElseThrow(() -> new RequestException("PED", 404, HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        // 2. Buscar si ya existe un carrito para este cliente en este local
        Carrito carrito = carritoRepository.findByClienteIdAndVendedorId(cliente.getId(), vendedorId)
            .orElseGet(() -> {
                Carrito nuevo = new Carrito();
                nuevo.setClienteId(cliente.getId());
                nuevo.setVendedorId(vendedorId);
                return nuevo;
            });

        // 3. Buscar el producto en el ms-catalogo para tener precio actualizado
        ProductoResumidoDTO productoDTO = catalogoClient.buscarProducto(productoId, vendedorId).getBody();

        // 4. Lógica de ítems
        // Buscamos un detalle que coincida en ID Y en la misma observación
        DetalleCarrito detalleExistente = carrito.getDetallesCarrito().stream()
            .filter(d -> d.getProductoId().equals(productoId) && 
                        Objects.equals(d.getObservaciones(), observaciones))
            .findFirst()
            .orElse(null);

        if (detalleExistente != null) {
            // Si coinciden ambos, solo ahí sumamos cantidad
            detalleExistente.setCantidad(detalleExistente.getCantidad() + cantidad);
        } else {
            // Si el producto es igual pero la observación es distinta (o es nuevo), creamos línea nueva
            DetalleCarrito nuevoDetalle = new DetalleCarrito(
                productoId, 
                cantidad, 
                BigDecimal.valueOf(productoDTO.getMontoUnitario()), 
                observaciones
            );
            carrito.addDetalle(nuevoDetalle);
    }

    carrito.calcularMontosTotales();
    return carritoMapper.toResponseDTO(carritoRepository.save(carrito));
    }

    // GET: Un carrito específico de un vendedor
    public CarritoResponseDTO obtenerCarritoPorVendedor(String email, String vendedorId) {
        Cliente cliente = buscarClientePorEmail(email);
        return carritoRepository.findByClienteIdAndVendedorId(cliente.getId(), vendedorId)
        .map(carrito -> {
            carrito.calcularMontosTotales(); 
            return carritoMapper.toResponseDTO(carrito);
        })
        .orElse(CarritoResponseDTO.builder()
                .id(null)
                .vendedorId(vendedorId)
                .items(new ArrayList<>())
                .montoTotalProductos(BigDecimal.ZERO)
                .build());
    }

    // GET: Todos los carritos del cliente (de distintos vendedores)
    public List<CarritoResponseDTO> obtenerTodosLosCarritos(String email) {
        Cliente cliente = buscarClientePorEmail(email);
        List<Carrito> carritos = carritoRepository.findByClienteId(cliente.getId());
        
        return carritos.stream()
            .map(carrito -> {
                carrito.calcularMontosTotales(); // Recalculamos antes de responder
                return carritoMapper.toResponseDTO(carrito);
            })
            .toList();
    }

    // DELETE: Borrar uno o más ítems
    public CarritoResponseDTO eliminarItems(String email, String vendedorId, List<String> itemsIds) {
        Cliente cliente = buscarClientePorEmail(email);
        
        Optional<Carrito> carritoOpt = carritoRepository.findByClienteIdAndVendedorId(cliente.getId(), vendedorId);
        
        if (carritoOpt.isEmpty()) {
            return crearCarritoVacioDTO(vendedorId);
        }

        Carrito carrito = carritoOpt.get();

        carrito.getDetallesCarrito().removeIf(detalle -> itemsIds.contains(detalle.getIdItem()));

        if (carrito.getDetallesCarrito().isEmpty()) {
            carritoRepository.delete(carrito);
            return crearCarritoVacioDTO(vendedorId);
        }

        carrito.calcularMontosTotales();
        return carritoMapper.toResponseDTO(carritoRepository.save(carrito));
    }

    private CarritoResponseDTO crearCarritoVacioDTO(String vendedorId) {
        return CarritoResponseDTO.builder()
                .id(null)
                .vendedorId(vendedorId)
                .items(new ArrayList<>())
                .montoTotalProductos(BigDecimal.ZERO)
                .build();
    }

    private Cliente buscarClientePorEmail(String email) {
        return clienteRepository.findByEmail(email)
            .orElseThrow(() -> new RequestException("PED", 404, HttpStatus.NOT_FOUND, "Cliente no encontrado"));
    }

    @Transactional
    public void eliminarCarritoPorVendedorYCliente(String name, String vendedorId) {
        Cliente cliente = buscarClientePorEmail(name);
        Optional<Carrito> carrito = carritoRepository.findByClienteIdAndVendedorId(cliente.getId(), vendedorId);
        if (carrito.isPresent()) {
            carritoRepository.delete(carrito.get());
        }
    }

   


    /*public ArrayList<Carrito> getCarritoByClienteEmail(String clienteEmail) {
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
        System.out.println("antes");
        ProductoResumidoDTO productoDTO = catalogoClient.buscarProducto(productoId, vendedorId).getBody();
        System.out.println("paso?");
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
        
    }*/
}
