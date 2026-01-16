package com.seminario.ms_usuarios.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.seminario.ms_usuarios.dto.DireccionResponseDTO;
import com.seminario.ms_usuarios.dto.VendedorRequestDTO;
import com.seminario.ms_usuarios.dto.VendedorResponseDTO;
import com.seminario.ms_usuarios.dto.VendedorUpdateRequestDTO;
import com.seminario.ms_usuarios.dto.ms_catalogo.VendedorRequestCatDTO;
import com.seminario.ms_usuarios.dto.ms_catalogo.VendedorResponseCatDTO;
import com.seminario.ms_usuarios.model.Direccion;
import com.seminario.ms_usuarios.model.EstadoUsuario;
import com.seminario.ms_usuarios.model.RolUsuario;
import com.seminario.ms_usuarios.model.Vendedor;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VendedorMapper {
    private final PasswordEncoder passwordEncoder;  
    // --- DE DTO A ENTIDAD ---
    public Vendedor toEntity(VendedorRequestDTO dto) {
        if (dto == null) return null;

        Vendedor vendedor = new Vendedor();
        vendedor.setEmail(dto.getEmail());
        vendedor.setContraseña(passwordEncoder.encode(dto.getPassword())); 
        vendedor.setNombreNegocio(dto.getNombreNegocio());
        vendedor.setTelefono(dto.getTelefono());
        vendedor.setNombreNegocio(dto.getNombreNegocio());
        vendedor.setNombreResponsable(dto.getNombreResponsable());
        vendedor.setApellidoResponsable(dto.getApellidoResponsable());
        vendedor.setEstado(EstadoUsuario.ACTIVO);
        vendedor.setRol(RolUsuario.VENDEDOR);
        
        return vendedor;
    }
    // --- DE ENTIDAD A RESPONSE ---
    public VendedorResponseDTO toResponse(Vendedor entity, DireccionResponseDTO direccion, VendedorResponseCatDTO vendedorResponseCatDTO) {
        if (entity == null) return null;

        VendedorResponseDTO dto = new VendedorResponseDTO();
        dto.setNombreNegocio(entity.getNombreNegocio());
        dto.setNombreResponsable(entity.getNombreResponsable());
        dto.setApellidoResponsable(entity.getApellidoResponsable());
        dto.setEmail(entity.getEmail());
        dto.setTelefono(entity.getTelefono());
        dto.setDireccion(direccion);
        dto.setVendedorResponseCatDTO(vendedorResponseCatDTO);

        
        return dto;
    }
    public  VendedorRequestCatDTO toVendedorResponseCatDTO(VendedorUpdateRequestDTO dtoRequest, Direccion direccion) {
        if (dtoRequest == null) return null;
        VendedorRequestCatDTO vendedorCatDTO = new VendedorRequestCatDTO();
        vendedorCatDTO.setUsuarioId(dtoRequest.getUsuarioId());
        vendedorCatDTO.setNombreNegocio(dtoRequest.getNombreNegocio());
        if (dtoRequest.getRealizaEnvios() != null) {
            vendedorCatDTO.setRealizaEnvios(dtoRequest.getRealizaEnvios());
        }
        if (dtoRequest.getHorarioApertura() != null) {
            vendedorCatDTO.setHorarioApertura(dtoRequest.getHorarioApertura());
        }
        if (dtoRequest.getHorarioCierre() != null) {
            vendedorCatDTO.setHorarioCierre(dtoRequest.getHorarioCierre());
        }   
        if (dtoRequest.getTiempoEstimadoEspera() != null) {
            vendedorCatDTO.setTiempoEstimadoEspera(dtoRequest.getTiempoEstimadoEspera());
        }
        if (dtoRequest.getLogo() != null) {
            vendedorCatDTO.setLogo(dtoRequest.getLogo());
        }
        if (dtoRequest.getBanner() != null) {
            vendedorCatDTO.setBanner(dtoRequest.getBanner());
        }
        DireccionMapper direccionMapper = new DireccionMapper();
        vendedorCatDTO.setDireccion(direccionMapper.toDireccionCatDTO(direccion));
        return vendedorCatDTO;
    }
    public VendedorRequestCatDTO toVendedorRequestCatDTO(VendedorRequestDTO dto,
            DireccionResponseDTO direccionResponseDTO, String usuarioId) {
        if (dto == null) return null;
        VendedorRequestCatDTO vendedorCatDTO = new VendedorRequestCatDTO();
        vendedorCatDTO.setUsuarioId(usuarioId);
        vendedorCatDTO.setNombreNegocio(dto.getNombreNegocio());
        vendedorCatDTO.getDireccion().setCalle(direccionResponseDTO.getCalle());
        vendedorCatDTO.getDireccion().setNumero(direccionResponseDTO.getNumero());
        vendedorCatDTO.getDireccion().setCodigoPostal(direccionResponseDTO.getCodigoPostal());
        vendedorCatDTO.getDireccion().setLocalidad(direccionResponseDTO.getLocalidad().getNombre());
        vendedorCatDTO.getDireccion().setProvincia(direccionResponseDTO.getProvincia().getNombre());
        vendedorCatDTO.getDireccion().setObservaciones(direccionResponseDTO.getObservaciones());
        vendedorCatDTO.getDireccion().setLatitud(direccionResponseDTO.getLatitud());
        vendedorCatDTO.getDireccion().setLongitud(direccionResponseDTO.getLongitud());
        return vendedorCatDTO;
    }

}
