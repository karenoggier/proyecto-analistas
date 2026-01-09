package com.seminario.ms_usuarios.service;

import com.seminario.ms_usuarios.dto.LoginRequestDTO;
import com.seminario.ms_usuarios.dto.ClienteRequestDTO;
import com.seminario.ms_usuarios.dto.ClienteResponseDTO;
import com.seminario.ms_usuarios.model.Cliente;
import com.seminario.ms_usuarios.model.EstadoUsuario;
import com.seminario.ms_usuarios.model.Usuario;
import com.seminario.ms_usuarios.model.RolUsuario;
import com.seminario.ms_usuarios.repository.ClienteRepository;
import com.seminario.ms_usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService; 

    // --- LOGIN ---
    public String login(LoginRequestDTO loginRequest) {
        Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), usuario.getContraseña())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return jwtService.generateToken(usuario.getEmail(), usuario.getClass().getSimpleName());
    }

    // --- REGISTRO DE CLIENTE ---
    public ClienteResponseDTO registrarCliente(ClienteRequestDTO dto) {
        if(usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setEmail(dto.getEmail());
        nuevoCliente.setTelefono(dto.getTelefono());
        
        nuevoCliente.setContraseña(passwordEncoder.encode(dto.getPassword()));
        
        nuevoCliente.setEstado(EstadoUsuario.ACTIVO);
        nuevoCliente.setNombre(dto.getNombre());
        nuevoCliente.setApellido(dto.getApellido());
        nuevoCliente.setFechaNacimiento(dto.getFechaNacimiento());
        nuevoCliente.setRol(RolUsuario.CLIENTE);

        Cliente clienteGuardado = clienteRepository.save(nuevoCliente);

        ClienteResponseDTO clienteResponse = new ClienteResponseDTO();
        clienteResponse.setNombre(clienteGuardado.getNombre());
        clienteResponse.setApellido(clienteGuardado.getApellido());
        clienteResponse.setEmail(clienteGuardado.getEmail());
        clienteResponse.setRol(clienteGuardado.getRol().name());
        clienteResponse.setFechaNacimiento(clienteGuardado.getFechaNacimiento());

        return clienteResponse;

    }


    // A useful method for validating logins (searches both clients and sellers simultaneously)
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(String id) {
        return usuarioRepository.findById(id);
    }
    
}
