package com.seminario.ms_usuarios.service;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.seminario.ms_usuarios.dto.DireccionRequestDTO;
import com.seminario.ms_usuarios.dto.DireccionResponseDTO;
import com.seminario.ms_usuarios.dto.NominatimResponseDTO;
import com.seminario.ms_usuarios.dto.eventos_ms_catalogo.DireccionCatDTO;
import com.seminario.ms_usuarios.exception.RequestException;
import com.seminario.ms_usuarios.mapper.DireccionMapper;
import com.seminario.ms_usuarios.model.Direccion;
import com.seminario.ms_usuarios.model.Localidad;
import com.seminario.ms_usuarios.model.Provincia;
import com.seminario.ms_usuarios.model.Usuario;
import com.seminario.ms_usuarios.repository.DireccionRepository;
import com.seminario.ms_usuarios.repository.LocalidadRepository;
import com.seminario.ms_usuarios.repository.ProvinciaRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class DireccionService {

    private final GeocodingService geocodingService;
    private final DireccionRepository direccionRepository;
    private final DireccionMapper direccionMapper;
    private final ProvinciaRepository provinciaRepository;
    private final LocalidadRepository localidadRepository;

    // --- REGISTRAR DIRECCION ---
    public DireccionResponseDTO registrarDireccion(DireccionRequestDTO dto, Usuario usuario) {
        if (usuario == null) {
            throw new RequestException("US", 2, HttpStatus.BAD_REQUEST, "El usuario no puede ser nulo");
        }

        Provincia provinciaEntidad = null;
        String nombreProvinciaParaGeo = dto.getProvincia(); 

        if (dto.getProvincia() != null) {
            provinciaEntidad = provinciaRepository.findById(dto.getProvincia())
                .orElseThrow(() -> new RequestException("US", 2, HttpStatus.NOT_FOUND, "Provincia no encontrada con ID: " + dto.getProvincia()));
            
            nombreProvinciaParaGeo = provinciaEntidad.getNombre();
        }

        Localidad localidadEntidad = null;
        String nombreLocalidadParaGeo = dto.getLocalidad();

        if (dto.getLocalidad() != null) {
            localidadEntidad = localidadRepository.findById(dto.getLocalidad())
                .orElseThrow(() -> new RequestException("US", 2, HttpStatus.NOT_FOUND, "Localidad no encontrada con ID: " + dto.getLocalidad()));
            
            nombreLocalidadParaGeo = localidadEntidad.getNombre();
        }

        NominatimResponseDTO coordenadas = geocodingService.obtenerCoordenadas(dto.getCalle(), dto.getNumero(), nombreLocalidadParaGeo, nombreProvinciaParaGeo);

        if (coordenadas == null) {
            throw new RequestException("US", 2, HttpStatus.BAD_REQUEST, "No se pudieron obtener las coordenadas para la dirección proporcionada");
        }
 
        Direccion direccion = direccionMapper.toEntity(dto,coordenadas.getLatitud(), coordenadas.getLongitud());
        direccion.setUsuario(usuario);
        direccion.setProvincia(provinciaEntidad); 
        direccion.setLocalidad(localidadEntidad);
         
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

    public ArrayList<Direccion> getDireccionesByLocalidadYProvincia(String localidad, String provincia) {
        return direccionRepository.findByLocalidadAndProvincia(localidad, provincia);
    }

    public DireccionCatDTO actualizarDireccion(DireccionCatDTO dto, String idUsuario) {
        Direccion direccion = direccionRepository.findByUsuarioId(idUsuario);
        if (direccion==null) new RequestException("US", 2, HttpStatus.NOT_FOUND, "Dirección no encontrada");

        Provincia provinciaEntidad = provinciaRepository.findByNombre(dto.getProvincia());
        if(provinciaEntidad==null) new RequestException("US", 2, HttpStatus.NOT_FOUND, "Provincia no encontrada con ID: " + dto.getProvincia());


        Localidad localidadEntidad = localidadRepository.findByNombre(dto.getLocalidad());
        if(localidadEntidad==null) new RequestException("US", 2, HttpStatus.NOT_FOUND, "Localidad no encontrada con ID: " + dto.getLocalidad());


        NominatimResponseDTO coordenadas = geocodingService.obtenerCoordenadas(dto.getCalle(), dto.getNumero(), dto.getLocalidad(), dto.getProvincia());

        if (coordenadas == null) {
            throw new RequestException("US", 2, HttpStatus.BAD_REQUEST, "No se pudieron obtener las coordenadas para la dirección proporcionada");
        }

        direccion.setCalle(dto.getCalle());
        direccion.setNumero(dto.getNumero());
        direccion.setCodigoPostal(dto.getCodigoPostal());
        direccion.setProvincia(provinciaEntidad); 
        direccion.setLocalidad(localidadEntidad);
        direccion.setLatitud(coordenadas.getLatitud());
        direccion.setLongitud(coordenadas.getLongitud());

        return direccionMapper.toDireccionCatDTO(direccionRepository.save(direccion));
    }

    public DireccionResponseDTO obtenerDireccionPorUsuarioId(String id) {
        Direccion direccion = direccionRepository.findByUsuarioId(id);
        if (direccion == null) {
            throw new RequestException("US", 2, HttpStatus.NOT_FOUND, "Dirección no encontrada para el usuario con ID: " + id);
        }
        return direccionMapper.toResponse(direccion);
    }


}
