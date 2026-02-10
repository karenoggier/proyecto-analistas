package com.seminario.ms_usuarios.controller;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_usuarios.dto.DireccionRequestDTO;
import com.seminario.ms_usuarios.dto.DireccionResponseDTO;
import com.seminario.ms_usuarios.dto.eventos_ms_pedidio.DireccionRequestEvent;
import com.seminario.ms_usuarios.dto.eventos_ms_pedidio.DireccionResponseEvent;
import com.seminario.ms_usuarios.model.Usuario;
import com.seminario.ms_usuarios.service.DireccionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/direcciones")
@RequiredArgsConstructor
public class DireccionController {

    private final DireccionService direccionService;

    public ResponseEntity<DireccionResponseDTO> registrarDireccion(DireccionRequestDTO dto, Usuario usuario) {
        return ResponseEntity.ok(direccionService.registrarDireccion(dto, usuario));
    }
    public ResponseEntity<ResponseEntity<ArrayList<DireccionResponseDTO>>> registrarDireccion( Usuario usuario) {
        return ResponseEntity.ok(direccionService.buscarDireccionesPorUsuario(usuario));
    }
    
    public ResponseEntity<ArrayList<DireccionResponseDTO>> obtenerDirecciones(Usuario usuario) {
        return direccionService.buscarDireccionesPorUsuario(usuario);
    }

    @GetMapping("/obtener")
    @Operation(summary = "Obtener datos de provincia, localidad y cordenadas")
    public ResponseEntity<DireccionResponseEvent> obtenerDireccion(@RequestBody DireccionRequestEvent dto) {
        return ResponseEntity.ok(direccionService.obtenerDireccion(dto));
    }
}