package com.seminario.ms_catalogo.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.seminario.ms_catalogo.dto.AllProductosResumidoDTO;
import com.seminario.ms_catalogo.model.Vendedor;
import com.seminario.ms_catalogo.repository.VendedorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {
    private final VendedorRepository vendedorRepository;

    public ArrayList<AllProductosResumidoDTO> getAllProductos(String usuarioId) {
        Vendedor vendedor = vendedorRepository.findByUsuarioId(usuarioId).get();
        
        ArrayList<AllProductosResumidoDTO> productos = new ArrayList<AllProductosResumidoDTO>();
        vendedor.getProductos().stream().forEach(producto -> {
            AllProductosResumidoDTO productoResumido = new AllProductosResumidoDTO();
            productoResumido.setId(producto.getId());
            productoResumido.setNombre(producto.getNombre());
            productoResumido.setPrecio(producto.getPrecio());
            productoResumido.setImagen(producto.getImagen());
            productoResumido.setDescripcion(producto.getDescripcion());
            productos.add(productoResumido);
        });
        
        return productos;
    }
}
