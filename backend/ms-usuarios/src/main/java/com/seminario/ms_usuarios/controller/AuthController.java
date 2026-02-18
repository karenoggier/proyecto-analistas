package com.seminario.ms_usuarios.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_usuarios.dto.ClienteRequestDTO;
import com.seminario.ms_usuarios.dto.ClienteResponseDTO;
import com.seminario.ms_usuarios.dto.LoginRequestDTO;
import com.seminario.ms_usuarios.dto.LoginResponseDTO;
import com.seminario.ms_usuarios.dto.VendedorRequestDTO;
import com.seminario.ms_usuarios.dto.VendedorResponseDTO;
import com.seminario.ms_usuarios.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "Registra un nuevo cliente y dispara la creacion de su perfil en ms-pedido")
    @PostMapping("/register/cliente")
    public ResponseEntity<ClienteResponseDTO> registrarCliente(@Valid @RequestBody ClienteRequestDTO dto) {
        return ResponseEntity.ok(authService.registrarCliente(dto));
    }

    @Operation(summary = "Autentica a un usuario e inicia sesión en el sistema")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        LoginResponseDTO response = authService.login(dto);
        return ResponseEntity.ok(response); 
    }

    @Operation(summary = "Registra un nuevo vendedor y dispara la creacion de su perfil en ms-catalogo")
    @PostMapping("/register/vendedor")
    public ResponseEntity<VendedorResponseDTO> registrarVendedor(@Valid @RequestBody VendedorRequestDTO dto) {
        return ResponseEntity.ok(authService.registrarVendedor(dto));
    }

}
