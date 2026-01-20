package com.seminario.ms_usuarios.service;

import java.util.List;
import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seminario.ms_usuarios.config.RabbitConfig;
import com.seminario.ms_usuarios.mapper.DireccionMapper;
import com.seminario.ms_usuarios.model.Vendedor;
import com.seminario.ms_usuarios.repository.VendedorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VendedorService {
    
    private final VendedorRepository vendedorRepository;
    private final DireccionService direccionService;
    private final DireccionMapper direccionMapper;
    

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

    @RabbitListener(queues = RabbitConfig.QUEUE_TO_USUARIOS)
    public void recibirActualizacionVendedor() {
        
        
    }
}
