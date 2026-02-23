package com.seminario.ms_pago.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_pago.service.PagoService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/pagos/webhook")
@Slf4j
@RequiredArgsConstructor
public class WebhookController {

    private final PagoService pagoService;

    @PostMapping
    @Operation(summary = "Procesa las notificaciones webhooks de Mercado Pago")
    public ResponseEntity<Void> recibirNotificacion(
            @RequestParam(value = "data.id", required = false) String dataId,
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "topic", required = false) String topic) {
        
        String paymentId = (dataId != null) ? dataId : id;
        
        String notificationType = (type != null) ? type : topic;
        

        if (paymentId != null && ("payment".equals(notificationType) || "payment".equals(topic))) {
            pagoService.procesarNotificacionPago(paymentId);
        } 

        return ResponseEntity.ok().build();
    }
}