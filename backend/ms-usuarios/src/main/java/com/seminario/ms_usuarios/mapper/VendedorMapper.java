package com.seminario.ms_usuarios.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.seminario.ms_usuarios.dto.DireccionResponseDTO;
import com.seminario.ms_usuarios.dto.VendedorRequestDTO;
import com.seminario.ms_usuarios.dto.VendedorResponseDTO;
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
    public VendedorResponseDTO toResponse(Vendedor entity, DireccionResponseDTO direccion) {
        if (entity == null) return null;

        VendedorResponseDTO dto = new VendedorResponseDTO();
        dto.setNombreNegocio(entity.getNombreNegocio());
        dto.setNombreResponsable(entity.getNombreResponsable());
        dto.setApellidoResponsable(entity.getApellidoResponsable());
        dto.setEmail(entity.getEmail());
        dto.setTelefono(entity.getTelefono());
        dto.setDireccion(direccion);
        
        return dto;
    }

}
