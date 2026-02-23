package com.seminario.ms_pago.controller;

import com.seminario.ms_pago.service.PagoService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {
    @Autowired
    private PagoService pagoService;

    @PostMapping("/create-preference/{pedidoId}")
    @Operation(summary = "Crea la preferencia de pago en Mercado Pago")
    public ResponseEntity<Map<String, String>> crear(@PathVariable String pedidoId) {
        return ResponseEntity.ok(pagoService.crearPreferencia(pedidoId));
    }

    @GetMapping("/estado/{pedidoId}")
    @Operation(summary = "Obtiene el estado de la transacción por ID de pedido")
    public ResponseEntity<Map<String, Object>> obtenerEstadoPago(@PathVariable String pedidoId) {
        return ResponseEntity.ok(pagoService.obtenerEstadoPago(pedidoId));
    }
}
