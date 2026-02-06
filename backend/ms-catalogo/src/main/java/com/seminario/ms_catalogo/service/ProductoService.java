package com.seminario.ms_catalogo.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.seminario.ms_catalogo.dto.ProductoResponseDTO;
import com.seminario.ms_catalogo.exception.RequestException;
import com.seminario.ms_catalogo.mapper.ProductoMapper;
import com.seminario.ms_catalogo.model.Categoria;
import com.seminario.ms_catalogo.model.Estado;
import com.seminario.ms_catalogo.model.Producto;
import com.seminario.ms_catalogo.model.Subcategoria;
import com.seminario.ms_catalogo.model.Vendedor;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {
    private final VendedorService vendedorService;
    private final ProductoMapper productoMapper;

    /* 
    public ProductoResponseDTO updateProducto(String vendedorId, String productoId, ProductoResponseDTO productoRequestDTO) {
        Vendedor vendedor = vendedorService.usuarioExistente(vendedorId);
        if (vendedor == null) {
            throw new RequestException("CA", 2, HttpStatus.BAD_REQUEST, "Vendedor no encontrado");
        }

        for (var producto : vendedor.getProductos()) {
            if (producto.getId().equals(productoId)) {
                producto.setNombre(productoRequestDTO.getNombre());
                producto.setDescripcion(productoRequestDTO.getDescripcion());
                producto.setPrecio(productoRequestDTO.getPrecio());
                producto.setCategoria(Categoria.valueOf(productoRequestDTO.getCategoria()));
                producto.setSubcategoria(Subcategoria.valueOf(productoRequestDTO.getSubcategoria()));
                producto.setEstado(Estado.valueOf(productoRequestDTO.getEstado()));
                producto.setDisponible(Estado.valueOf(productoRequestDTO.getDisponible()));
                producto.setImagen(productoRequestDTO.getImagen());
                vendedorService.updateVendedorConDatosLocales(vendedor);
                return productoRequestDTO;
            }
        }
        throw new RequestException("CA", 2, HttpStatus.BAD_REQUEST, "Producto no encontrado");  
    }
*/
/* 
    public Producto getProductoByIdAndVendedorId(String productoId, String vendedorId) {
        Vendedor vendedor = vendedorService.usuarioExistente(vendedorId);
        
        for (Producto producto : vendedor.getProductos()) {
            if (producto.getId().equals(productoId)) {
                return producto;
            }
        }
        throw new RequestException("CA", 2, HttpStatus.BAD_REQUEST, "Producto no encontrado");  
    }
*/

    

}
