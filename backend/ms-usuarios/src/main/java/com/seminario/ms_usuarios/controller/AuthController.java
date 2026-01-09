package com.seminario.ms_usuarios.controller;

import com.seminario.ms_usuarios.dto.LoginRequestDTO;
import com.seminario.ms_usuarios.dto.ClienteRequestDTO;
import com.seminario.ms_usuarios.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register/cliente")
    public ResponseEntity<?> registrarCliente(@Valid @RequestBody ClienteRequestDTO dto) {
        return ResponseEntity.ok(authService.registrarCliente(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO dto) {
        String token = authService.login(dto);
        return ResponseEntity.ok(token); 
    }
}
