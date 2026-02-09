package com.seminario.ms_pedido.controller;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_pedido.DTOs.DireccionRequestDTO;
import com.seminario.ms_pedido.DTOs.DireccionResponseDTO;
import com.seminario.ms_pedido.Services.DireccionService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/direcciones")
@RequiredArgsConstructor
@Slf4j
public class DireccionController {
    private final DireccionService direccionService;
    private final ClienteController clienteController;

    @PostMapping("/registrar")
    public  ResponseEntity<DireccionResponseDTO> registrarDireccion( @RequestBody DireccionRequestDTO direccionRequestDTO, Authentication authentication) {
        return ResponseEntity.ok(direccionService.registrarDireccion(direccionRequestDTO, clienteController.obtenerPerfil(authentication)));
        
    }

}
