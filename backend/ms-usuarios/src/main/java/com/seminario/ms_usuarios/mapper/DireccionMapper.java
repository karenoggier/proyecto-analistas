package com.seminario.ms_usuarios.mapper;

import org.springframework.stereotype.Component;

import com.seminario.ms_usuarios.dto.DireccionRequestDTO;
import com.seminario.ms_usuarios.dto.DireccionResponseDTO;
import com.seminario.ms_usuarios.dto.eventos_ms_catalogo.DireccionCatDTO;
import com.seminario.ms_usuarios.model.Direccion;
import com.seminario.ms_usuarios.model.Localidad;
import com.seminario.ms_usuarios.model.Provincia;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DireccionMapper {
    // --- DE DTO A ENTIDAD ---
    public Direccion toEntity(DireccionRequestDTO dto, Double latitud, Double longitud) {
        if (dto == null) return null;

        Direccion direccion = new Direccion();
        // direccion.setProvincia(dto.getProvincia());
        // direccion.setLocalidad(dto.getLocalidad());
        
        if (dto.getProvincia() != null) {
            Provincia prov = new Provincia();
            prov.setId(dto.getProvincia()); // Asumiendo que el DTO trae el ID 
            direccion.setProvincia(prov);
        }

        if (dto.getLocalidad() != null) {
            Localidad loc = new Localidad();
            loc.setId(dto.getLocalidad()); // Asumiendo que el DTO trae el ID 
            direccion.setLocalidad(loc);
        }
        
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

    public DireccionCatDTO toDireccionCatDTO(Direccion direccion) {
        if (direccion == null) return null;

        DireccionCatDTO direccionCatDTO = new DireccionCatDTO();
        if (direccion.getProvincia() != null) {
            direccionCatDTO.setProvincia(direccion.getProvincia().getNombre());
        }
        if (direccion.getLocalidad() != null) {
            direccionCatDTO.setLocalidad(direccion.getLocalidad().getNombre());
        }
        direccionCatDTO.setCalle(direccion.getCalle());
        direccionCatDTO.setNumero(direccion.getNumero());
        direccionCatDTO.setCodigoPostal(direccion.getCodigoPostal());
        direccionCatDTO.setObservaciones(direccion.getObservaciones());
        direccionCatDTO.setLatitud(direccion.getLatitud());
        direccionCatDTO.setLongitud(direccion.getLongitud());

        return direccionCatDTO;
    }




}
