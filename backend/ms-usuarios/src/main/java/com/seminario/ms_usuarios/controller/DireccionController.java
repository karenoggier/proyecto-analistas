package com.seminario.ms_usuarios.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_usuarios.dto.DireccionRequestDTO;
import com.seminario.ms_usuarios.dto.eventos_ms_pedidio.DireccionResponseEvent;
import com.seminario.ms_usuarios.exception.RequestException;
import com.seminario.ms_usuarios.model.Usuario;
import com.seminario.ms_usuarios.service.DireccionService;
import com.seminario.ms_usuarios.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/direcciones")
@RequiredArgsConstructor
public class DireccionController {

    private final UsuarioService usuarioService;
    private final DireccionService direccionService;

    @PostMapping("/{usuarioId}") 
    @Operation(summary = "Valida y guarda una nueva dirección para un usuario. Llamado internamente por ms-pedido")
    public ResponseEntity<DireccionResponseEvent> agregarDireccion(
            @PathVariable String usuarioId, 
            @RequestBody DireccionRequestDTO dto) {
        
        Usuario usuario = usuarioService.buscarPorId(usuarioId)
                .orElseThrow(() -> new RequestException("US", 404, HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        
        return ResponseEntity.ok(direccionService.registrarDireccionParaPedido(dto, usuario));
        
    }
    @DeleteMapping("/{direccionId}")
    @Operation(summary = "Elimina una dirección por su ID (baja lógica). Llamado internamente por ms-pedido")
    public ResponseEntity<Void> eliminarDireccion(@PathVariable("direccionId") String direccionId) {
        direccionService.eliminarDireccion(direccionId);
        return ResponseEntity.noContent().build();
    }


    /* 
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
    }*/

}