package com.seminario.ms_usuarios.mapper;

import org.springframework.stereotype.Component;

import com.seminario.ms_usuarios.dto.DireccionRequestDTO;
import com.seminario.ms_usuarios.dto.DireccionResponseDTO;
import com.seminario.ms_usuarios.model.Direccion;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DireccionMapper {
    // --- DE DTO A ENTIDAD ---
    public Direccion toEntity(DireccionRequestDTO dto, Double latitud, Double longitud) {
        if (dto == null) return null;

        Direccion direccion = new Direccion();
        direccion.setProvincia(dto.getProvincia());
        direccion.setLocalidad(dto.getLocalidad());
        direccion.setCalle(dto.getCalle());
        direccion.setNumero(dto.getNumero());
        direccion.setCodigoPostal(dto.getCodigoPostal());
        direccion.setObservaciones(dto.getObservaciones());
        direccion.setLatitud(latitud);
        direccion.setLongitud(longitud);
        
        return direccion;
    }

    // --- DE ENTIDAD A RESPONSE ---
    public DireccionResponseDTO toResponse(Direccion entity) {
        if (entity == null) return null;
        DireccionResponseDTO dto = new DireccionResponseDTO();
        dto.setProvincia(entity.getProvincia());
        dto.setLocalidad(entity.getLocalidad());
        dto.setCalle(entity.getCalle());
        dto.setNumero(entity.getNumero());
        dto.setCodigoPostal(entity.getCodigoPostal());
        dto.setObservaciones(entity.getObservaciones());
        dto.setLatitud(entity.getLatitud());
        dto.setLongitud(entity.getLongitud());

        return dto;
    }


}
