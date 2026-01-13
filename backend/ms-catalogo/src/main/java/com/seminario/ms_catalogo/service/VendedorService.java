package com.seminario.ms_catalogo.service;

import org.springframework.http.ResponseEntity;

import com.seminario.ms_catalogo.dto.ProductoRequestDTO;
import com.seminario.ms_catalogo.dto.ProductoResponseDTO;

import org.springframework.stereotype.Service;

import com.seminario.ms_catalogo.mapper.ProductoMapper;
import com.seminario.ms_catalogo.model.Producto;
import com.seminario.ms_catalogo.model.Vendedor;
import com.seminario.ms_catalogo.repository.VendedorRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class VendedorService {
    private VendedorRepository vendedorRepository;
    private ProductoMapper productoMapper;
    

    public ResponseEntity<ProductoResponseDTO> agregarProducto(ProductoRequestDTO productoRequestDTO, String vendedorId) {
        Vendedor vendedor = vendedorRepository.findById(vendedorId).orElse(null);
        if (vendedor == null) {
            return ResponseEntity.notFound().build();
        }
        Producto producto = productoMapper.toEntity(productoRequestDTO);

        

        return null;
    }

}
