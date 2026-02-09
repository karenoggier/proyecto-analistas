package com.seminario.ms_pedido.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_pedido.DTOs.eventos_ms_usuarios.ClienteRegistradoEvent;
import com.seminario.ms_pedido.Services.ClienteService;
import com.seminario.ms_pedido.model.Cliente;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteController {
    private final ClienteService clienteService;
    
    @PostMapping("/registrar")
    public ResponseEntity<Void> registrarCliente(@RequestBody ClienteRegistradoEvent cliente) {
        clienteService.registrarCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @GetMapping("/perfil")
    @Operation(summary = "Obtener perfil del cliente logueado")
    public Cliente obtenerPerfil(org.apache.tomcat.util.net.openssl.ciphers.Authentication authentication) {
       String usuarioIdentity = authentication.name();
       return clienteService.buscarClientePorEmail(usuarioIdentity);
    }




}
