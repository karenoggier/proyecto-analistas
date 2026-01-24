package com.seminario.ms_usuarios.controller;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_usuarios.dto.DireccionRequestDTO;
import com.seminario.ms_usuarios.dto.DireccionResponseDTO;
import com.seminario.ms_usuarios.model.Usuario;
import com.seminario.ms_usuarios.service.DireccionService;

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

}
