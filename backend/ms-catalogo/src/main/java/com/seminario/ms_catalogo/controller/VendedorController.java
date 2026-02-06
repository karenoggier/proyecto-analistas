package com.seminario.ms_catalogo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_catalogo.dto.VendedorRequestDTO;
import com.seminario.ms_catalogo.dto.VendedorResponseDTO;
import com.seminario.ms_catalogo.dto.eventos_ms_usuarios.VendedorRegistradoEvent;
import com.seminario.ms_catalogo.service.VendedorService;

import org.springframework.security.core.Authentication;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/vendedores")
@RequiredArgsConstructor
@Slf4j
public class VendedorController {
    private final VendedorService vendedorService;

    /*@PostMapping("/agregar-producto")
    public ResponseEntity<ProductoResponseDTO> agregarProducto(@RequestBody ProductoRequestDTO productoRequestDTO, 
    @RequestParam String vendedorId) {
        return vendedorService.agregarProducto(productoRequestDTO, vendedorId);
    }*/

    /* 
    @GetMapping("/obtener-vendedor-por-usuarioId")
    public ResponseEntity<VendedorResponseDTO> obtenerVendedorPorUsuarioId(@RequestParam String usuarioId) {
        return vendedorService.obtenerVendedorPorUsuarioId(usuarioId);
    }*/

    @PutMapping("/actualizar") 
    public ResponseEntity<VendedorResponseDTO> updateVendedor(
            @RequestBody VendedorRequestDTO vendedorRequestDTO,
            Authentication authentication) { 
        
        String email = authentication.getName();
        
        return vendedorService.updateVendedor(vendedorRequestDTO, email);
    }

    //Endpoint HTTP desde ms-usuarios
    @PostMapping("/registrar")
    public ResponseEntity<Void> registrarVendedor(@RequestBody VendedorRegistradoEvent evento) {
            vendedorService.recibirRegistroVendedor(evento);
            return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/perfil")
    @Operation(summary = "Obtener perfil del vendedor logueado")
    public ResponseEntity<VendedorResponseDTO> obtenerPerfil(Authentication authentication) {
       String usuarioIdentity = authentication.getName();
       return ResponseEntity.ok(vendedorService.buscarVendedorPorEmail(usuarioIdentity));
       
    }
}
