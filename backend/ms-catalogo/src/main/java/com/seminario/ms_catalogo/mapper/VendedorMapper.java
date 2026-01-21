package com.seminario.ms_catalogo.mapper;

import org.springframework.stereotype.Component;

import com.seminario.ms_catalogo.dto.VendedorRequestDTO;
import com.seminario.ms_catalogo.dto.VendedorResponseDTO;
import com.seminario.ms_catalogo.dto.eventos_ms_usuarios.VendedorRegistradoEvent;
import com.seminario.ms_catalogo.model.Estado;
import com.seminario.ms_catalogo.model.Vendedor;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VendedorMapper {
    private final DireccionMapper direccionMapper;
    private final ProductoMapper productoMapper;

    public Vendedor toNewEntity (VendedorRegistradoEvent evento) {
        Vendedor vendedor = new Vendedor();
        vendedor.setUsuarioId(evento.getUsuarioId());
        vendedor.setNombreNegocio(evento.getNombreNegocio());
        vendedor.setNombreResponsable(evento.getNombreResponsable());
        vendedor.setApellidoResponsable (evento.getApellidoResponsable());
        vendedor.setTelefono(evento.getTelefono());
        vendedor.setLogo(null);
        vendedor.setBanner(null);
        vendedor.setRealizaEnvios(null);
        vendedor.setHorarioApertura(null);
        vendedor.setHorarioCierre(null);
        vendedor.setTiempoEstimadoEspera(null);
        vendedor.setEstado(Estado.INCOMPLETO);
        vendedor.setProductos(null);
        if (evento.getDireccion() != null) {
            vendedor.setDireccion(direccionMapper.toEntity(evento.getDireccion()));
        }

        return vendedor;
    }

    public VendedorResponseDTO toDTO(Vendedor vendedor) {
        VendedorResponseDTO dto = new VendedorResponseDTO();
        dto.setUsuarioId(vendedor.getUsuarioId());
        dto.setNombreNegocio(vendedor.getNombreNegocio());
        dto.setNombreResponsable(vendedor.getNombreResponsable());
        dto.setApellidoResponsable(vendedor.getApellidoResponsable());
        dto.setTelefono(vendedor.getTelefono());
        dto.setLogo(vendedor.getLogo());
        dto.setBanner(vendedor.getBanner());
        dto.setRealizaEnvios(vendedor.getRealizaEnvios());
        dto.setHorarioApertura(vendedor.getHorarioApertura());
        dto.setHorarioCierre(vendedor.getHorarioCierre());
        dto.setTiempoEstimadoEspera(vendedor.getTiempoEstimadoEspera());
        dto.setEstado(vendedor.getEstado().toString());
        if (vendedor.getDireccion() != null) {
            dto.setDireccion(direccionMapper.toDTO(vendedor.getDireccion()));
        }
        if (vendedor.getProductos() != null) {
            dto.setProductos(productoMapper.toDTOList(vendedor.getProductos()));
        }

        return dto;
    }
    public Vendedor toEntity(VendedorRequestDTO dto) {
        Vendedor vendedor = new Vendedor();
        vendedor.setUsuarioId(dto.getUsuarioId());
        vendedor.setNombreNegocio(dto.getNombreNegocio());
        vendedor.setNombreResponsable(dto.getNombreResponsable());
        vendedor.setApellidoResponsable(dto.getApellidoResponsable());
        vendedor.setTelefono(dto.getTelefono());
        vendedor.setLogo(dto.getLogo());
        vendedor.setBanner(dto.getBanner());
        vendedor.setRealizaEnvios(dto.getRealizaEnvios());
        vendedor.setHorarioApertura(dto.getHorarioApertura());
        vendedor.setHorarioCierre(dto.getHorarioCierre());
        vendedor.setTiempoEstimadoEspera(dto.getTiempoEstimadoEspera());
        if (dto.getDireccion() != null) {
            vendedor.setDireccion(direccionMapper.toEntity(dto.getDireccion()));
        }
        return vendedor;
    }
    

}
