package com.seminario.ms_usuarios.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.seminario.ms_usuarios.dto.ClienteRequestDTO;
import com.seminario.ms_usuarios.dto.ClienteResponseDTO;
import com.seminario.ms_usuarios.dto.eventos_ms_pedidio.ClienteRegistradoEvent;
import com.seminario.ms_usuarios.model.Cliente;
import com.seminario.ms_usuarios.model.EstadoUsuario;
import com.seminario.ms_usuarios.model.RolUsuario;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ClienteMapper {
    private final PasswordEncoder passwordEncoder;

    // --- DE DTO A ENTIDAD ---
    public Cliente toEntity(ClienteRequestDTO dto) {
        if (dto == null) return null;

        Cliente cliente = new Cliente();
        cliente.setEmail(dto.getEmail());
        cliente.setContraseña(passwordEncoder.encode(dto.getPassword())); 
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setTelefono(dto.getTelefono());
        cliente.setFechaNacimiento(dto.getFechaNacimiento());
        cliente.setEstado(EstadoUsuario.ACTIVO);
        cliente.setRol(RolUsuario.CLIENTE);
        
        return cliente;
    }

    // --- DE ENTIDAD A RESPONSE ---
    public ClienteResponseDTO toResponse(Cliente entity) {
        if (entity == null) return null;

        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setEmail(entity.getEmail());
        dto.setRol(entity.getRol().name());
        dto.setFechaNacimiento(entity.getFechaNacimiento());
        
        return dto;
    }

    public ClienteRegistradoEvent toClienteRegistrado(Cliente guardado) {
        if (guardado == null) return null;

        ClienteRegistradoEvent evento = new ClienteRegistradoEvent();
        evento.setUsuarioId(guardado.getId());
        evento.setEmail(guardado.getEmail());
        evento.setNombre(guardado.getNombre());
        evento.setApellido(guardado.getApellido());
        evento.setTelefono(guardado.getTelefono());
        evento.setFechaNacimiento(guardado.getFechaNacimiento());
        
        return evento;
    }
}
