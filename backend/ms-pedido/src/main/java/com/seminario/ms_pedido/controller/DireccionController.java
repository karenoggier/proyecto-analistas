package com.seminario.ms_pedido.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_pedido.DTOs.DireccionRequestDTO;
import com.seminario.ms_pedido.DTOs.DireccionResponseDTO;
import com.seminario.ms_pedido.Services.DireccionService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/direcciones")
@RequiredArgsConstructor
@Slf4j
public class DireccionController {
    
    private final DireccionService direccionService;
    private final ClienteController clienteController;

    @PostMapping
    @Operation(summary = "Registra nueva dirección para un cliente logueado")
    public ResponseEntity<DireccionResponseDTO> registrarDireccion( 
        @Valid @RequestBody DireccionRequestDTO direccionRequestDTO, 
        Authentication authentication) {

        String email = authentication.getName();
        
        return ResponseEntity.ok(direccionService.agregarDireccion(email, direccionRequestDTO));
        
    }


    @DeleteMapping("/eliminar")
    @Operation(summary = "Elimina una dirección para un cliente logueado")
    public ResponseEntity<Void> eliminarDireccion( 
        @PathParam("idDireccion") String idDireccion, 
        Authentication authentication) {

        String email = authentication.getName();
        
        direccionService.eliminarDireccion(idDireccion);
        return ResponseEntity.noContent().build();
    }

    /*@GetMapping("/obtener")
    @Operation(summary = "Obtener dirección del cliente logueado")
    public ResponseEntity<ArrayList<DireccionResponseDTO>> obtenerDireccion(Authentication authentication) {
        return ResponseEntity.ok(direccionService.obtenerDireccion(clienteController.obtenerPerfil(authentication)));
    }*/

}
