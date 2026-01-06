package com.seminario.ms_usuarios.service;

import com.seminario.ms_usuarios.model.Cliente;
import com.seminario.ms_usuarios.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ClienteService {
    
    private final ClienteRepository clienteRepository;
    
    // Get all clients
    @Transactional(readOnly = true)
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    // Find client by ID
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorId(String id) {
        return clienteRepository.findById(id);
    }

    // Save client
    @Transactional
    public Cliente guardarCliente(Cliente cliente) {
        // check if the email already exists before save
        return clienteRepository.save(cliente);
    }

    // delete client
    @Transactional
    public void eliminarCliente(String id) {
        clienteRepository.deleteById(id);
    }
    
}
