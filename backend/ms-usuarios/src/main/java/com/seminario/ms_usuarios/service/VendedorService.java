package com.seminario.ms_usuarios.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seminario.ms_usuarios.dto.eventos_ms_catalogo.DireccionCatDTO;
import com.seminario.ms_usuarios.dto.eventos_ms_catalogo.VendedorRegistradoEvent;
import com.seminario.ms_usuarios.model.Vendedor;
import com.seminario.ms_usuarios.repository.VendedorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VendedorService {
    
    private final VendedorRepository vendedorRepository;
    private final DireccionService direccionService;
    
    

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

    public VendedorRegistradoEvent guardarVendedor(VendedorRegistradoEvent vendedorDTO) {
        Vendedor vendedor = vendedorRepository.findById(vendedorDTO.getUsuarioId()).orElse(null);
        if (vendedor == null) {
            throw new RuntimeException("Vendedor no encontrado con ID: " + vendedorDTO.getUsuarioId());
        }
        DireccionCatDTO direccionDTO = direccionService.actualizarDireccion(vendedorDTO.getDireccion(), vendedor.getId());
        if (direccionDTO == null) {
            throw new RuntimeException("Error al actualizar la dirección para el vendedor con ID: " + vendedorDTO.getUsuarioId());
        }
        
        //actualizar otros campos del vendedor si es necesario
        vendedor.setNombreNegocio(vendedorDTO.getNombreNegocio());
        vendedor.setNombreResponsable(vendedorDTO.getNombreResponsable());
        vendedor.setApellidoResponsable(vendedorDTO.getApellidoResponsable());
        vendedor.setTelefono(vendedorDTO.getTelefono());
        
        vendedorRepository.save(vendedor);

        vendedorDTO.setDireccion(direccionDTO);
        return vendedorDTO;
    
    }
}
