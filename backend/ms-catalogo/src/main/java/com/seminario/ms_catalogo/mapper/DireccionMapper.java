package com.seminario.ms_catalogo.mapper;
import org.springframework.stereotype.Component;

import com.seminario.ms_catalogo.dto.*;
import com.seminario.ms_catalogo.model.*;

@Component
public class DireccionMapper {
    public Direccion toEntity(DireccionRequestDTO direccionDTO) {
        if (direccionDTO == null) {
            return null;
        }
        Direccion direccion = new Direccion();
        direccion.setProvincia(direccionDTO.getProvincia());
        direccion.setLocalidad(direccionDTO.getLocalidad());
        direccion.setCalle(direccionDTO.getCalle());
        direccion.setNumero(direccionDTO.getNumero());
        direccion.setCodigoPostal(direccionDTO.getCodigoPostal());
        direccion.setObservaciones(direccionDTO.getObservaciones());
        return direccion;
    }

     public Direccion toEntity(DireccionDTO direccionDTO) {
        if (direccionDTO == null) {
            return null;
        }
        Direccion direccion = new Direccion();
        direccion.setProvincia(direccionDTO.getProvincia());
        direccion.setLocalidad(direccionDTO.getLocalidad());
        direccion.setCalle(direccionDTO.getCalle());
        direccion.setNumero(direccionDTO.getNumero());
        direccion.setCodigoPostal(direccionDTO.getCodigoPostal());
        direccion.setObservaciones(direccionDTO.getObservaciones());
        direccion.setLatitud(direccionDTO.getLatitud());
        direccion.setLongitud(direccionDTO.getLongitud());
        return direccion;
    }


    public DireccionDTO toDTO(Direccion direccion) {
        if (direccion == null) {
            return null;
        }
        DireccionDTO direccionDTO = new DireccionDTO();
        direccionDTO.setProvincia(direccion.getProvincia());
        direccionDTO.setLocalidad(direccion.getLocalidad());
        direccionDTO.setCalle(direccion.getCalle());
        direccionDTO.setNumero(direccion.getNumero());
        direccionDTO.setCodigoPostal(direccion.getCodigoPostal());
        direccionDTO.setObservaciones(direccion.getObservaciones());
        direccionDTO.setLatitud(direccion.getLatitud());
        direccionDTO.setLongitud(direccion.getLongitud());
        return direccionDTO;
    }

}
