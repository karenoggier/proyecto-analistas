package com.seminario.ms_pedido.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_pedido.dto.DireccionRequestDTO;
import com.seminario.ms_pedido.dto.DireccionResponseDTO;
import com.seminario.ms_pedido.service.DireccionService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/direcciones")
@RequiredArgsConstructor
@Slf4j
public class DireccionController {
    
    private final DireccionService direccionService;

    @PostMapping
    @Operation(summary = "Registra nueva dirección para un cliente logueado")
    public ResponseEntity<DireccionResponseDTO> registrarDireccion( 
        @Valid @RequestBody DireccionRequestDTO direccionRequestDTO, 
        Authentication authentication) {

        String email = authentication.getName();
        
        return ResponseEntity.ok(direccionService.agregarDireccion(email, direccionRequestDTO));
        
    }


    @DeleteMapping("/{idDireccion}")
    @Operation(summary = "Elimina una dirección para un cliente logueado (baja lógica)")
    public ResponseEntity<Void> eliminarDireccion( 
        @PathVariable("idDireccion") String idDireccion, 
        Authentication authentication) {

        String email = authentication.getName();
        
        direccionService.eliminarDireccion(idDireccion, email);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filtrar")
    @Operation(summary = "Lista direcciones del cliente que coinciden con la localidad del vendedor")
    public ResponseEntity<List<DireccionResponseDTO>> listarPorLocalidad(
            @RequestParam String localidad, 
            Authentication authentication) {

            String email = authentication.getName();
        
        return ResponseEntity.ok(direccionService.filtrarPorLocalidad(email, localidad));
    }

}
