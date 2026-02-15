package com.seminario.ms_pedido.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_pedido.dto.CrearPedidoDTO;
import com.seminario.ms_pedido.service.PedidoService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {
    private final PedidoService pedidoService;

    @PostMapping("/crear")    
    public ResponseEntity<Void> crearPedido(@RequestBody CrearPedidoDTO crearPedidoDTO/*, Authentication authentication*/) {
        pedidoService.crearPedido(crearPedidoDTO.getPedidoRequestDTO(), crearPedidoDTO.getEnvioSeleccionDTO(), crearPedidoDTO.getPagoSeleccionDTO());
        return ResponseEntity.ok().build();
    }
}
