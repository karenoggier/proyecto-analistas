package com.seminario.ms_catalogo.mapper;
import org.springframework.stereotype.Component;

import com.seminario.ms_catalogo.dto.ProductoRequestDTO;
import com.seminario.ms_catalogo.dto.ProductoResponseDTO;
import com.seminario.ms_catalogo.model.Estado;
import com.seminario.ms_catalogo.model.Producto;
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
        producto.setTamanio(productoRequestDTO.getTamanio());
        producto.setUnidadMedida(productoRequestDTO.getUnidadMedida());
        producto.setAptoCeliacos(productoRequestDTO.getAptoCeliacos());
        producto.setAptoVeganos(productoRequestDTO.getAptoVeganos());
        producto.setDisponible(productoRequestDTO.getDisponible());
        producto.setObservaciones(productoRequestDTO.getObservaciones());

        return producto;

}
    public ProductoResponseDTO toDTO(Producto producto) {
        if (producto == null) {
            return null;
        }
        ProductoResponseDTO productoResponseDTO = new ProductoResponseDTO();
        productoResponseDTO.setNombre(producto.getNombre());
        productoResponseDTO.setDescripcion(producto.getDescripcion());
        productoResponseDTO.setPrecio(producto.getPrecio());
        productoResponseDTO.setCategoriaNombre(producto.getCategoria().getNombre());
        productoResponseDTO.setCategoriaDescripcion(producto.getCategoria().getDescripcion());
        productoResponseDTO.setTamanio(producto.getTamanio());
        productoResponseDTO.setUnidadMedida(producto.getUnidadMedida());
        productoResponseDTO.setAptoCeliacos(producto.getAptoCeliacos());
        productoResponseDTO.setAptoVeganos(producto.getAptoVeganos());
        productoResponseDTO.setDisponible(producto.getDisponible());
        productoResponseDTO.setObservaciones(producto.getObservaciones());
        return productoResponseDTO;
    }

}
