package com.seminario.ms_catalogo.mapper;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.seminario.ms_catalogo.dto.ProductoRequestDTO;
import com.seminario.ms_catalogo.dto.ProductoResponseDTO;
import com.seminario.ms_catalogo.model.Categoria;
import com.seminario.ms_catalogo.model.Estado;
import com.seminario.ms_catalogo.model.Producto;
import com.seminario.ms_catalogo.model.Subcategoria;
@Component
public class ProductoMapper {
    public Producto toEntity(ProductoRequestDTO productoRequestDTO) {
        if (productoRequestDTO == null) {
            return null;
        }
        Producto producto = new Producto();
        producto.setNombre(productoRequestDTO.getNombre());
        producto.setDescripcion(productoRequestDTO.getDescripcion());
        producto.setPrecio(productoRequestDTO.getPrecio());
        producto.setEstado(Estado.ACTIVO);
        producto.setDisponible(Estado.valueOf(productoRequestDTO.getDisponible()));
        producto.setObservaciones(productoRequestDTO.getObservaciones());
        producto.setCategoria(Categoria.valueOf(productoRequestDTO.getCategoria()));
        producto.setSubcategoria(Subcategoria.valueOf(productoRequestDTO.getSubcategoria()));
        producto.setImagen(productoRequestDTO.getImagen());
     
        return producto;

}
    public ProductoResponseDTO toDTO(Producto producto) {
        if (producto == null) {
            return null;
        }
        ProductoResponseDTO productoResponseDTO = new ProductoResponseDTO();
        productoResponseDTO.setId(producto.getId());
        productoResponseDTO.setNombre(producto.getNombre());
        productoResponseDTO.setDescripcion(producto.getDescripcion());
        productoResponseDTO.setPrecio(producto.getPrecio());
        productoResponseDTO.setCategoria(producto.getCategoria().toString());
        productoResponseDTO.setSubcategoria(producto.getSubcategoria().toString());
        productoResponseDTO.setDisponible(producto.getDisponible().toString());
        productoResponseDTO.setObservaciones(producto.getObservaciones());
        productoResponseDTO.setImagen(producto.getImagen());

        
        return productoResponseDTO;
    }
    public ArrayList<ProductoResponseDTO> toDTOList(ArrayList<Producto> productos) {
        ArrayList<ProductoResponseDTO> dtoList = new ArrayList<>();
        for (Producto producto : productos) {
            dtoList.add(toDTO(producto));
        }
        return dtoList;
    }

}
