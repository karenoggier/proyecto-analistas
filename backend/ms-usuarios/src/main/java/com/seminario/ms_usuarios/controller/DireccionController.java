package com.seminario.ms_usuarios.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_usuarios.dto.DireccionRequestDTO;
import com.seminario.ms_usuarios.dto.DireccionResponseDTO;
import com.seminario.ms_usuarios.service.DireccionService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/direcciones")
@RequiredArgsConstructor
public class DireccionController {
    private DireccionService direccionService;


    @PostMapping("/register/direccion")
    public ResponseEntity<DireccionResponseDTO> registrarDireccion(@Valid @RequestBody DireccionRequestDTO dto){

        return ResponseEntity.ok(direccionService.registrarDireccion(dto));
        
    }
    

}
