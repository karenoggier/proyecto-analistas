package com.seminario.ms_pago.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_pago.service.PagoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/pagos/webhook")
@Slf4j
@RequiredArgsConstructor
public class WebhookController {

    private final PagoService pagoService;

    @PostMapping
    public ResponseEntity<Void> recibirNotificacion(
            @RequestParam(value = "data.id", required = false) String dataId,
            @RequestParam(value = "id", required = false) String id,
            @RequestParam("type") String type) {
        
        String paymentId = (dataId != null) ? dataId : id;
        
        log.info("Notificación recibida de MP: Tipo={}, ID={}", type, paymentId);

        if ("payment".equals(type) && paymentId != null) {
            pagoService.procesarNotificacionPago(paymentId);
        }

        return ResponseEntity.ok().build();
    }
}