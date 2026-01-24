package com.seminario.ms_usuarios.controller;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_usuarios.dto.DireccionRequestDTO;
import com.seminario.ms_usuarios.dto.DireccionResponseDTO;
import com.seminario.ms_usuarios.model.Cliente;
import com.seminario.ms_usuarios.service.ClienteService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private final DireccionController direccionController;
    private final ClienteService clienteService;

    @PostMapping("/registrarDireccion")
    public ResponseEntity<DireccionResponseDTO> registrarDireccion(DireccionRequestDTO dto, String usuarioId) {
        Cliente cliente = clienteService.buscarPorId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + usuarioId));
        return direccionController.registrarDireccion(dto, cliente);
    }

    @GetMapping("/listarDirecciones")
    public ResponseEntity<ArrayList<DireccionResponseDTO>> obtenerDirecciones(String usuarioId) {
        Cliente cliente = clienteService.buscarPorId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + usuarioId));
        return direccionController.obtenerDirecciones(cliente);
    }
    

}
