package com.seminario.ms_usuarios.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.seminario.ms_usuarios.dto.ClienteRequestDTO;
import com.seminario.ms_usuarios.dto.ClienteResponseDTO;
import com.seminario.ms_usuarios.dto.LoginRequestDTO;
import com.seminario.ms_usuarios.exception.RequestException;
import com.seminario.ms_usuarios.mapper.ClienteMapper;
import com.seminario.ms_usuarios.model.Cliente;
import com.seminario.ms_usuarios.model.EstadoUsuario;
import com.seminario.ms_usuarios.model.Usuario;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioService usuarioService; 
    private final ClienteService clienteService; 
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ClienteMapper clienteMapper;

    // --- LOGIN ---
    public String login(LoginRequestDTO loginRequest) {
        Usuario usuario = usuarioService.findByEmail(loginRequest.getEmail());

        if (!passwordEncoder.matches(loginRequest.getPassword(), usuario.getContraseña())) {
            throw new RequestException("US", 2, HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        if (usuario.getEstado() != EstadoUsuario.ACTIVO) {
            throw new RequestException("US", 2, HttpStatus.FORBIDDEN, "El usuario no está activo. Contacte al administrador.");
        }

        return jwtService.generateToken(usuario.getEmail(), usuario.getRol().name());
    }

    // --- REGISTRAR CLIENTE ---
    public ClienteResponseDTO registrarCliente(ClienteRequestDTO dto) {
        
        if (usuarioService.existeEmail(dto.getEmail())) {
             throw new RequestException("US", 2, HttpStatus.CONFLICT, "Credemciales inválidas");
        }

        if (!dto.getPassword().equals(dto.getRepetirPassword())) {
            throw new RequestException("US", 2, HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden");
        }

        Cliente nuevoCliente = clienteMapper.toEntity(dto);

        Cliente guardado = clienteService.guardarCliente(nuevoCliente);

        return clienteMapper.toResponse(guardado);
    }

}
