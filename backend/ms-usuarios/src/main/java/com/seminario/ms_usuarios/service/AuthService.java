package com.seminario.ms_usuarios.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.seminario.ms_usuarios.dto.ClienteRequestDTO;
import com.seminario.ms_usuarios.dto.ClienteResponseDTO;
import com.seminario.ms_usuarios.dto.LoginRequestDTO;
import com.seminario.ms_usuarios.exception.RequestException;
import com.seminario.ms_usuarios.model.Cliente;
import com.seminario.ms_usuarios.model.EstadoUsuario;
import com.seminario.ms_usuarios.model.RolUsuario;
import com.seminario.ms_usuarios.model.Usuario;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioService usuarioService; 
    private final ClienteService clienteService; 
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // --- LOGIN ---
    public String login(LoginRequestDTO loginRequest) {
        Usuario usuario = usuarioService.findByEmail(loginRequest.getEmail());

        if (!passwordEncoder.matches(loginRequest.getPassword(), usuario.getContraseña())) {
            throw new RequestException("US", 2, HttpStatus.UNAUTHORIZED, "Contraseña incorrecta");
        }

        return jwtService.generateToken(usuario.getEmail(), usuario.getRol().name());
    }

    // --- REGISTRAR CLIENTE ---
    public ClienteResponseDTO registrarCliente(ClienteRequestDTO dto) {
        
        if (usuarioService.existeEmail(dto.getEmail())) {
             throw new RequestException("US", 2, HttpStatus.CONFLICT, "El email ya existe");
        }

        Cliente nuevoCliente = mapToClienteEntity(dto);

        Cliente guardado = clienteService.guardarCliente(nuevoCliente);

        return mapToClienteResponseDTO(guardado);
    }

    // --- MAPEOS ---
    private Cliente mapToClienteEntity(ClienteRequestDTO dto) {
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

    private ClienteResponseDTO mapToClienteResponseDTO(Cliente cliente) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setNombre(cliente.getNombre());
        dto.setApellido(cliente.getApellido());
        dto.setEmail(cliente.getEmail());
        dto.setRol(cliente.getRol().name());
        dto.setFechaNacimiento(cliente.getFechaNacimiento());
        return dto;
    }
}
