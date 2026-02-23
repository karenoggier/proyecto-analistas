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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {
    private final NotificacionService notificacionService;

    @GetMapping
    public ResponseEntity<List<NotificacionDTO>> obtenerMisNotificaciones(Authentication auth) {
        return ResponseEntity.ok(notificacionService.listarNotificaciones(auth.getName()));
    }

    @PatchMapping("/leer-todas")
    public ResponseEntity<Void> leerTodas(Authentication auth) {
        notificacionService.marcarComoLeidas(auth.getName());
        return ResponseEntity.noContent().build();
    }
}
