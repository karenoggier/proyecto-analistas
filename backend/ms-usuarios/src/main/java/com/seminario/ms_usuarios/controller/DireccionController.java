package com.seminario.ms_usuarios.controller;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_usuarios.dto.DireccionRequestDTO;
import com.seminario.ms_usuarios.dto.DireccionResponseDTO;
import com.seminario.ms_usuarios.dto.eventos_ms_pedidio.DireccionResponseEvent;
import com.seminario.ms_usuarios.mapper.DireccionMapper;
import com.seminario.ms_usuarios.model.Usuario;
import com.seminario.ms_usuarios.service.DireccionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/direcciones")
@RequiredArgsConstructor
public class DireccionController {

    private final DireccionService direccionService;
    private final DireccionMapper direccionMapper;


    public ResponseEntity<DireccionResponseDTO> registrarDireccion(DireccionRequestDTO dto, Usuario usuario) {
        return ResponseEntity.ok(direccionService.registrarDireccion(dto, usuario));
    }

    //este metodo se usa para llamar a la funcion registrarDireccion desde el clienteController, para que devuelva un DireccionResponseEvent en vez de un DireccionResponseDTO, ya que el clienteController es el que se encarga de enviar el evento al ms-pedido
    public ResponseEntity<DireccionResponseEvent> registrarDireccionCliente(DireccionRequestDTO dto, Usuario usuario) { 
        return ResponseEntity.ok(direccionMapper.toResponseEvent(direccionService.registrarDireccion(dto, usuario)));
    }
    public ResponseEntity<ResponseEntity<ArrayList<DireccionResponseDTO>>> registrarDireccion( Usuario usuario) {
        return ResponseEntity.ok(direccionService.buscarDireccionesPorUsuario(usuario));
    }
    
    public ResponseEntity<ArrayList<DireccionResponseDTO>> obtenerDirecciones(Usuario usuario) {
        return direccionService.buscarDireccionesPorUsuario(usuario);
    }

}