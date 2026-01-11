package com.seminario.ms_usuarios.controller;

import com.seminario.ms_usuarios.dto.*;
import com.seminario.ms_usuarios.dto.LoginResponseDTO;
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
    public ResponseEntity<ClienteResponseDTO> registrarCliente(@Valid @RequestBody ClienteRequestDTO dto) {
        return ResponseEntity.ok(authService.registrarCliente(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        String token = authService.login(dto);
        LoginResponseDTO respuesta = new LoginResponseDTO(token);
        return ResponseEntity.ok(respuesta); 
    }

    @PostMapping("/register/vendedor")
    public ResponseEntity<VendedorResponseDTO> registrarVendedor(@Valid @RequestBody VendedorRequestDTO dto) {
        return ResponseEntity.ok(authService.registrarVendedor(dto));
    }
}
