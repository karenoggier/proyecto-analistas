package com.seminario.ms_usuarios.service;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.seminario.ms_usuarios.exception.RequestException;
import com.seminario.ms_usuarios.model.Usuario;
import com.seminario.ms_usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
 

    public boolean existeEmail(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RequestException("US",2, HttpStatus.NOT_FOUND, "Credenciales inválidas"));
    }

    // A useful method for validating logins (searches both clients and sellers simultaneously)
    /*@Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(String id) {
        return usuarioRepository.findById(id);
    }*/
    
}
