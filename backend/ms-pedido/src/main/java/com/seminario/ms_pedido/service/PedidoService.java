package com.seminario.ms_pedido.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seminario.ms_pedido.client.CatalogoClient;
import com.seminario.ms_pedido.client.UsuarioClient;
import com.seminario.ms_pedido.dto.CarritoResponseDTO;
import com.seminario.ms_pedido.dto.ConfirmarEnvioRequestDTO;
import com.seminario.ms_pedido.dto.PedidoListadoDTO;
import com.seminario.ms_pedido.dto.PedidoResponseDTO;
import com.seminario.ms_pedido.exception.RequestException;
import com.seminario.ms_pedido.mapper.PedidoMapper;
import com.seminario.ms_pedido.model.Cliente;
import com.seminario.ms_pedido.model.DetallePedido;
import com.seminario.ms_pedido.model.Direccion;
import com.seminario.ms_pedido.model.EstadoPedido;
import com.seminario.ms_pedido.model.Pedido;
import com.seminario.ms_pedido.repository.PedidoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final DireccionService direccionService; 
    private final CarritoService carritoService;
    private final ClienteService clienteService;
    private final PedidoMapper pedidoMapper;
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

    @Transactional
    public PedidoResponseDTO crearBorradorPedido(String vendedorId, Authentication auth) {
        String emailCliente = auth.getName();

        CarritoResponseDTO carrito = carritoService.obtenerCarritoPorVendedor(emailCliente, vendedorId);

        if (carrito.getItems().isEmpty()) {
            throw new RequestException("PED", 400, HttpStatus.BAD_REQUEST, "No se puede iniciar el checkout con un carrito vacío");
        }

        Cliente cliente = clienteService.obtenerClientePorEmail(emailCliente);

        // Buscamos o creamos el Pedido en PostgreSQL
        Pedido pedido = pedidoRepository
            .findByClienteIdAndVendedorIdAndEstado(cliente.getId(), vendedorId, EstadoPedido.PENDIENTE)
            .orElse(new Pedido());

        // 3. Mapeo de cabecera
        pedido.setClienteId(cliente.getId());
        pedido.setCliente(cliente);
        pedido.setVendedorId(vendedorId);
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setFechaCreacion(LocalDateTime.now());
        pedido.setMontoTotalProductos(carrito.getMontoTotalProductos());
        pedido.setComisionApp(carrito.getComisionApp());

        // 4. Mapeo de ítems (de CarritoItem a DetallePedido)
        if (pedido.getDetalles() != null) {
            pedido.getDetalles().clear();
        } else {
            pedido.setDetalles(new ArrayList<>());
        }

        carrito.getItems().forEach(item -> {
            DetallePedido detalle = new DetallePedido();
            detalle.setIdProducto(item.getProductoId());
            detalle.setCantidad(item.getCantidad()); 
            detalle.setMontoUnitario(item.getMontoUnitario());
            detalle.setObservaciones(item.getObservaciones());
            detalle.setPedido(pedido); 
            pedido.getDetalles().add(detalle);
        });

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        return pedidoMapper.toResponseDTO(pedidoGuardado);
    }

    @Transactional
    public PedidoResponseDTO confirmarOActualizarEnvio(ConfirmarEnvioRequestDTO dto, Authentication auth) {
        String emailCliente = auth.getName();
        Cliente cliente = clienteService.obtenerClientePorEmail(emailCliente);
        
        // 1. Recuperar o crear borrador
        Pedido pedido = pedidoRepository
            .findByClienteIdAndVendedorIdAndEstado(cliente.getId(), dto.getVendedorId(), EstadoPedido.PENDIENTE)
            .orElse(new Pedido());

        if (pedido.getId() == null) {
            pedido.setEstado(EstadoPedido.PENDIENTE);
            pedido.setFechaCreacion(LocalDateTime.now());
            pedido.setClienteId(cliente.getId());
            pedido.setCliente(cliente);
            pedido.setVendedorId(dto.getVendedorId());
            
        }

        // 2. Vincular dirección mediante el Service
        Direccion dir = direccionService.obtenerEntidadPorId(dto.getIdDireccion());
        pedido.setDireccion(dir);
        pedido.setMetodoEnvio(dto.getMetodoEnvio());

        // 3. Cálculo de montos
        BigDecimal costoEnvio = calcularCostoEnvio(dto.getVendedorId(), String.valueOf(dto.getIdDireccion()), auth);
        pedido.setCostoEnvio(costoEnvio);
   
        pedido.setMontoTotal(pedido.getMontoTotalProductos()
                .add(pedido.getComisionApp())
                .add(pedido.getCostoEnvio()));

        return pedidoMapper.toResponseDTO(pedidoRepository.save(pedido));
    }

    public List<PedidoListadoDTO> obtenerListadoPedidos(String emailCliente) {
        Cliente cliente = clienteService.obtenerClientePorEmail(emailCliente);
        
        return pedidoRepository.findByClienteIdAndEstadoNot(cliente.getId(), EstadoPedido.PENDIENTE)
                .stream()
                .map(pedidoMapper::toListadoDTO)
                .toList();
    }


}
