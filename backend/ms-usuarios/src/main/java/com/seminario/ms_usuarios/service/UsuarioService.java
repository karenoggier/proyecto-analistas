package com.seminario.ms_usuarios.service;

import com.seminario.ms_usuarios.model.Usuario;
import com.seminario.ms_usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // A useful method for validating logins (searches both clients and sellers simultaneously)
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(String id) {
        return usuarioRepository.findById(id);
    }
    
}
