package com.seminario.ms_catalogo.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.seminario.ms_catalogo.dto.consultas_ms_pedido.ProductoResumidoDTO;
import com.seminario.ms_catalogo.exception.RequestException;
import com.seminario.ms_catalogo.mapper.ProductoMapper;
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
 
    public ProductoResumidoDTO getProductoResumido(String productoId, String vendedorId) {
        Vendedor vendedor = vendedorService.usuarioExistente(vendedorId);
        
        return vendedor.getProductos().stream()
        .filter(p -> p.getId().equals(productoId))
        .findFirst()
        .map(p -> ProductoMapper.toResumenDTO(p, vendedorId))
        .orElseThrow(() -> new RequestException("CA", 2, HttpStatus.BAD_REQUEST, "Producto no encontrado"));
    }

    public List<String> getNombreImagenProducto(String productoId, String vendedorId) {
        Vendedor vendedor = vendedorService.usuarioExistente(vendedorId);
        return vendedor.getProductos().stream()
                .filter(p -> p.getId().equals(productoId))
                .findFirst()
                .map(p -> List.of(p.getNombre(), p.getImagen()))
                .orElseThrow(() -> new RequestException("CA", 2, HttpStatus.BAD_REQUEST, "Producto no encontrado"));
    }


    
}