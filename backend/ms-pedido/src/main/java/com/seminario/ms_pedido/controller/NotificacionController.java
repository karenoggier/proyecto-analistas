package com.seminario.ms_pedido.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_pedido.dto.NotificacionDTO;
import com.seminario.ms_pedido.model.Notificacion;
import com.seminario.ms_pedido.service.NotificacionService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {
    private final NotificacionService notificacionService;

    @GetMapping
    @Operation(summary = "Obtiene las notificaciones del usuario")
    public ResponseEntity<List<NotificacionDTO>> obtenerMisNotificaciones(Authentication auth) {
        return ResponseEntity.ok(notificacionService.listarNotificaciones(auth.getName()));
    }

    @PatchMapping("/leer-todas")
    @Operation(summary = "Actualiza el estado de todas las notificaciones pendientes del usuario")
    public ResponseEntity<Void> leerTodas(Authentication auth) {
        notificacionService.marcarComoLeidas(auth.getName());
        return ResponseEntity.noContent().build();
    }
}
