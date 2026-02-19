package com.seminario.ms_pedido.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_pedido.dto.ConfirmarEnvioRequestDTO;
import com.seminario.ms_pedido.dto.IniciarCheckoutRequestDTO;
import com.seminario.ms_pedido.dto.PedidoListadoDTO;
import com.seminario.ms_pedido.dto.PedidoResponseDTO;
import com.seminario.ms_pedido.service.PedidoService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {
    private final PedidoService pedidoService;

    @GetMapping("/costo-envio/{idVendedor}/{idDireccionCliente}")
    @Operation(summary = "Obtiene el costo de envio desde el local de un vendedor a la direccion del cliente")
    public ResponseEntity<BigDecimal> calcularDistanciaEntreDirecciones(@PathVariable String idVendedor, @PathVariable String idDireccionCliente, Authentication authentication){
        return ResponseEntity.ok(pedidoService.calcularCostoEnvio(idVendedor, idDireccionCliente, authentication));
    }

    // CONTINUAR PASO 1: Se dispara al salir del Carrito hacia Direcciones
    @PostMapping("/iniciar-checkout/{vendedorId}")
    @Operation(summary = "Crea el pedido en estado PENDIENTE y copia los items del carrito")
    public ResponseEntity<PedidoResponseDTO> iniciarCheckout(
            @PathVariable String vendedorId,
            Authentication auth) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pedidoService.crearBorradorPedido(vendedorId, auth));
    }

    // CONTINUAR PASO 2: Se dispara al elegir dirección y querer ir a Datos del cliente
    @PatchMapping("/confirmar-envio")
    @Operation(summary = "Actualiza el pedido guardando la direccion y el costo de envio")
    public ResponseEntity<PedidoResponseDTO> confirmarEnvio(
            @RequestBody ConfirmarEnvioRequestDTO dto, 
            Authentication auth) {

        return ResponseEntity.ok(pedidoService.confirmarOActualizarEnvio(dto, auth));
    }

    @GetMapping("/listado-pedidos")
    @Operation(summary = "Listado de pedidos (con estado distinto de PENDIENTE) del cliente autenticado")
    public ResponseEntity<List<PedidoListadoDTO>> obtenerListadoPedidos(Authentication auth) {
        return ResponseEntity.ok(pedidoService.obtenerListadoPedidos(auth.getName()));
    }

}
