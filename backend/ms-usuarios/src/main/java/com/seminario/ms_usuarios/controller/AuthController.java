package com.seminario.ms_usuarios.controller;

import com.seminario.ms_usuarios.dto.LoginRequestDTO;
import com.seminario.ms_usuarios.dto.ClienteRequestDTO;
import com.seminario.ms_usuarios.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UsuarioService usuarioService;

    @PostMapping("/register/cliente")
    public ResponseEntity<?> registrarCliente(@RequestBody ClienteRequestDTO dto) {
        return ResponseEntity.ok(usuarioService.registrarCliente(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO dto) {
        String token = usuarioService.login(dto);
        return ResponseEntity.ok(token); 
    }
}
