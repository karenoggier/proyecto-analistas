package com.seminario.ms_usuarios.service;

import com.seminario.ms_usuarios.model.Vendedor;
import com.seminario.ms_usuarios.repository.VendedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class VendedorService {
    
    private final VendedorRepository vendedorRepository;

    // Get all sellers
    @Transactional(readOnly = true)
    public List<Vendedor> listarVendedores() {
        return vendedorRepository.findAll();
    }

    // Find sellers by ID
    @Transactional(readOnly = true)
    public Optional<Vendedor> buscarPorId(String id) {
        return vendedorRepository.findById(id);
    }

    // Save seller
    @Transactional
    public Vendedor guardarVendedor(Vendedor vendedor) {
        // check if the email already exists before save
        return vendedorRepository.save(vendedor);
    }

    // Delete seller
    @Transactional
    public void eliminarVendedor(String id) {
        vendedorRepository.deleteById(id);
    }
}
