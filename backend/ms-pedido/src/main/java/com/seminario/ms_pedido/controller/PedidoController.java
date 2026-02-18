package com.seminario.ms_pedido.controller;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_pedido.service.PedidoService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {
    private final PedidoService pedidoService;

    @GetMapping("/costo-envio/{idVendedor}/{idDireccionCliente}")
    public ResponseEntity<BigDecimal> calcularDistanciaEntreDirecciones(@PathVariable String idVendedor, @PathVariable String idDireccionCliente, Authentication authentication){
        return ResponseEntity.ok(pedidoService.calcularCostoEnvio(idVendedor, idDireccionCliente, authentication));
    }

    /*@PostMapping("/crear")    
    public ResponseEntity<Void> crearPedido(@RequestBody CrearPedidoDTO crearPedidoDTO) {
        pedidoService.crearPedido(crearPedidoDTO.getPedidoRequestDTO(), crearPedidoDTO.getEnvioSeleccionDTO(), crearPedidoDTO.getPagoSeleccionDTO());
        return ResponseEntity.ok().build();
    }*/
}
