package com.seminario.ms_usuarios.service;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.seminario.ms_usuarios.dto.DireccionRequestDTO;
import com.seminario.ms_usuarios.dto.DireccionResponseDTO;
import com.seminario.ms_usuarios.dto.NominatimResponseDTO;
import com.seminario.ms_usuarios.exception.RequestException;
import com.seminario.ms_usuarios.mapper.DireccionMapper;
import com.seminario.ms_usuarios.model.Direccion;
import com.seminario.ms_usuarios.model.Usuario;
import com.seminario.ms_usuarios.repository.DireccionRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class DireccionService {

    private final GeocodingService geocodingService;
    private final DireccionRepository direccionRepository;
    private final DireccionMapper direccionMapper;

     // --- REGISTRAR DIRECCION ---

    public DireccionResponseDTO registrarDireccion(DireccionRequestDTO dto, Usuario usuario) {
        if (usuario == null) {
            throw new RequestException("US", 2, HttpStatus.BAD_REQUEST, "El usuario no puede ser nulo");
        }

        NominatimResponseDTO coordenadas = geocodingService.obtenerCoordenadas(dto.getCalle(), dto.getNumero(), dto.getLocalidad(), dto.getProvincia());

        if (coordenadas == null) {
            throw new RequestException("US", 2, HttpStatus.BAD_REQUEST, "No se pudieron obtener las coordenadas para la dirección proporcionada");
        }

        Direccion direccion = direccionMapper.toEntity(dto,coordenadas.getLatitud(), coordenadas.getLongitud());
        direccion.setUsuario(usuario);
        Direccion guardada = direccionRepository.save(direccion);

        return direccionMapper.toResponse(guardada);
 
    }

    public ResponseEntity<ArrayList<DireccionResponseDTO>> buscarDireccionesPorUsuario(Usuario usuario) {
        
        ArrayList<Direccion> direcciones = direccionRepository.findByUsuario(usuario);
        ArrayList<DireccionResponseDTO> direccionesDTO = new ArrayList<>();

        for (Direccion direccion : direcciones) {
            direccionesDTO.add(direccionMapper.toResponse(direccion));
        }

        return ResponseEntity.ok(direccionesDTO);   
    }

}
