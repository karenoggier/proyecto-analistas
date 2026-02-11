package com.seminario.ms_usuarios.controller;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_usuarios.dto.DireccionRequestDTO;
import com.seminario.ms_usuarios.dto.DireccionResponseDTO;
import com.seminario.ms_usuarios.dto.eventos_ms_pedidio.DireccionResponseEvent;
import com.seminario.ms_usuarios.model.Cliente;
import com.seminario.ms_usuarios.service.ClienteService;
import com.seminario.ms_usuarios.service.DireccionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/clientes")
public class ClienteController {
    /*private final DireccionService direccionService;
    private final ClienteService clienteService;

    @PostMapping("{usuarioId}/direcciones")
    @Operation(summary = "Registra una nueva dirección para un cliente")    
    public ResponseEntity<DireccionResponseEvent> registrarDireccion(@Valid @RequestBody DireccionRequestDTO dto, @PathVariable String usuarioId) {
        Cliente cliente = clienteService.buscarPorId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + usuarioId));
        return direccionController.registrarDireccionCliente(dto, cliente);
    }*/

    /*@GetMapping("/direcciones")
    @Operation(summary = "Obtiene todas las direcciones de un cliente")
    public ResponseEntity<ArrayList<DireccionResponseDTO>> obtenerDirecciones(@PathVariable String usuarioId) {
        Cliente cliente = clienteService.buscarPorId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + usuarioId));
        return direccionController.obtenerDirecciones(cliente);
    }*/
    

}
