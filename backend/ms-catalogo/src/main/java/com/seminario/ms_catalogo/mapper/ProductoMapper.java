package com.seminario.ms_catalogo.mapper;
import org.springframework.stereotype.Component;

import com.seminario.ms_catalogo.dto.ProductoRequestDTO;
import com.seminario.ms_catalogo.dto.ProductoResponseBusquedaDTO;
import com.seminario.ms_catalogo.dto.ProductoResponseDTO;
import com.seminario.ms_catalogo.dto.ProductoResponsePublicDTO;
import com.seminario.ms_catalogo.dto.consultas_ms_pedido.ProductoResumidoDTO;
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
        producto.setImagen(productoRequestDTO.getImagen());
        producto.setObservaciones(productoRequestDTO.getObservaciones());

        producto.setEstado(Estado.ACTIVO);
        producto.setDisponible(productoRequestDTO.getDisponible());

        producto.setCategoria(Categoria.valueOf(productoRequestDTO.getCategoria().toUpperCase()));
        producto.setSubcategoria(Subcategoria.valueOf(productoRequestDTO.getSubcategoria().toUpperCase()));
        
        return producto;

}
    public ProductoResponseBusquedaDTO toDTO(Producto producto, String vendedorId, String nombreVendedor) {
        if (producto == null) {
            return null;
        }

        ProductoResponseBusquedaDTO productoResponseDTO = new ProductoResponseBusquedaDTO();

        productoResponseDTO.setId(producto.getId());
        productoResponseDTO.setNombre(producto.getNombre());
        productoResponseDTO.setDescripcion(producto.getDescripcion());
        productoResponseDTO.setPrecio(producto.getPrecio());
        productoResponseDTO.setObservaciones(producto.getObservaciones());
        productoResponseDTO.setImagen(producto.getImagen());

        productoResponseDTO.setDisponible(producto.getDisponible());

        
        if (producto.getCategoria() != null) productoResponseDTO.setCategoria(producto.getCategoria().name());
        if (producto.getSubcategoria() != null) productoResponseDTO.setSubcategoria(producto.getSubcategoria().name());

        if (producto.getEstado() != null) productoResponseDTO.setEstado(producto.getEstado().name());

        productoResponseDTO.setIdVendedor(vendedorId);
        productoResponseDTO.setNombreVendedor(nombreVendedor);
    
        return productoResponseDTO;
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
        productoResponseDTO.setObservaciones(producto.getObservaciones());
        productoResponseDTO.setImagen(producto.getImagen());

        productoResponseDTO.setDisponible(producto.getDisponible());

        
        if (producto.getCategoria() != null) productoResponseDTO.setCategoria(producto.getCategoria().name());
        if (producto.getSubcategoria() != null) productoResponseDTO.setSubcategoria(producto.getSubcategoria().name());

        if (producto.getEstado() != null) productoResponseDTO.setEstado(producto.getEstado().name());

        return productoResponseDTO;
    }

    public void updateEntity(Producto productoExistente, ProductoRequestDTO dto) {
        if (productoExistente == null || dto == null) return;

        productoExistente.setNombre(dto.getNombre());
        productoExistente.setDescripcion(dto.getDescripcion());
        productoExistente.setPrecio(dto.getPrecio());
        productoExistente.setObservaciones(dto.getObservaciones());
        
        productoExistente.setImagen(dto.getImagen());

        productoExistente.setDisponible(dto.getDisponible());

        try {
            productoExistente.setCategoria(Categoria.valueOf(dto.getCategoria().toUpperCase()));
            productoExistente.setSubcategoria(Subcategoria.valueOf(dto.getSubcategoria().toUpperCase()));
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new RuntimeException("Categoría o Subcategoría inválida en la edición");
        }
    }

    /*public ArrayList<ProductoResponseDTO> toDTOList(ArrayList<Producto> productos) {
        ArrayList<ProductoResponseDTO> dtoList = new ArrayList<>();
        for (Producto producto : productos) {
            dtoList.add(toDTO(producto));
        }
        return dtoList;
    }*/

    public static ProductoResumidoDTO toResumenDTO(Producto producto, String vendedorId) {
        if (producto == null) {
            return null;
        }
        ProductoResumidoDTO productoResumidoDTO = new ProductoResumidoDTO();
        productoResumidoDTO.setProductoId(producto.getId());
        productoResumidoDTO.setNombre(producto.getNombre());
        productoResumidoDTO.setImagenUrl(producto.getImagen());
        productoResumidoDTO.setVendedorId(vendedorId);
        productoResumidoDTO.setMontoUnitario(producto.getPrecio());
        productoResumidoDTO.setObservaciones(producto.getObservaciones());
        return productoResumidoDTO;
    }
    public ProductoResponsePublicDTO toPublicDTO(Producto producto) {
        if (producto == null) {
            return null;
        }
        ProductoResponsePublicDTO productoResponseDTO = new ProductoResponsePublicDTO();
        productoResponseDTO.setId(producto.getId());
        productoResponseDTO.setNombre(producto.getNombre());
        productoResponseDTO.setDescripcion(producto.getDescripcion());
        productoResponseDTO.setPrecio(producto.getPrecio());
        productoResponseDTO.setImagen(producto.getImagen());
        productoResponseDTO.setCategoria(producto.getCategoria().name());
        productoResponseDTO.setSubcategoria(producto.getSubcategoria().name());
        return productoResponseDTO;
    }

}
