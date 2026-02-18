package com.seminario.ms_usuarios.service;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.seminario.ms_usuarios.dto.DireccionRequestDTO;
import com.seminario.ms_usuarios.dto.DireccionResponseDTO;
import com.seminario.ms_usuarios.dto.NominatimResponseDTO;
import com.seminario.ms_usuarios.dto.eventos_ms_catalogo.DireccionCatDTO;
import com.seminario.ms_usuarios.dto.eventos_ms_pedidio.DireccionResponseEvent;
import com.seminario.ms_usuarios.exception.RequestException;
import com.seminario.ms_usuarios.mapper.DireccionMapper;
import com.seminario.ms_usuarios.model.Direccion;
import com.seminario.ms_usuarios.model.EstadoDireccion;
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
        direccion.setEstado(EstadoDireccion.ACTIVO);
        direccion.setObservaciones(dto.getObservaciones());
         
        Direccion guardada = direccionRepository.save(direccion);

        return direccionMapper.toResponse(guardada);
 
    }

    public DireccionResponseEvent registrarDireccionParaPedido(DireccionRequestDTO dto, Usuario usuario) {
        if (usuario == null) {
            throw new RequestException("US", 2, HttpStatus.BAD_REQUEST, "El usuario no puede ser nulo");
        }

        Provincia provinciaEntidad = provinciaRepository.findById(dto.getProvincia())
                .orElseThrow(() -> new RequestException("US", 2, HttpStatus.NOT_FOUND, "Provincia no encontrada con ID: " + dto.getProvincia()));

        Localidad localidadEntidad = localidadRepository.findById(dto.getLocalidad())
                .orElseThrow(() -> new RequestException("US", 2, HttpStatus.NOT_FOUND, "Localidad no encontrada con ID: " + dto.getLocalidad()));

        NominatimResponseDTO coordenadas = geocodingService.obtenerCoordenadas(
                dto.getCalle(), 
                dto.getNumero(), 
                localidadEntidad.getNombre(), 
                provinciaEntidad.getNombre()
        );

        if (coordenadas == null) {
            throw new RequestException("US", 2, HttpStatus.BAD_REQUEST, "No se pudieron obtener las coordenadas para la dirección proporcionada");
        }

        Direccion direccion = direccionMapper.toEntity(dto, coordenadas.getLatitud(), coordenadas.getLongitud());
        direccion.setUsuario(usuario);
        direccion.setProvincia(provinciaEntidad); 
        direccion.setLocalidad(localidadEntidad);
        direccion.setEstado(EstadoDireccion.ACTIVO);

        Direccion guardada = direccionRepository.save(direccion);

        return direccionMapper.toPedidoResponse(guardada);
 
    }

    public ResponseEntity<ArrayList<DireccionResponseDTO>> buscarDireccionesPorUsuario(Usuario usuario) {
        
        ArrayList<Direccion> direcciones = direccionRepository.findByUsuario(usuario);
        ArrayList<DireccionResponseDTO> direccionesDTO = new ArrayList<>();

        for (Direccion direccion : direcciones) {
            direccionesDTO.add(direccionMapper.toResponse(direccion));
        }

        return ResponseEntity.ok(direccionesDTO);   
    }

    /*public ArrayList<Direccion> getDireccionesByLocalidadYProvincia(String localidad, String provincia) {
        return direccionRepository.findByLocalidadAndProvincia(localidad, provincia);
    }*/

    public DireccionCatDTO actualizarDireccion(DireccionCatDTO dto, String idUsuario) {
        Direccion direccion = direccionRepository.findByUsuarioId(idUsuario);
        if (direccion==null) new RequestException("US", 2, HttpStatus.NOT_FOUND, "Dirección no encontrada");

        Provincia provinciaEntidad = provinciaRepository.findById(dto.getProvincia()).get();
        if(provinciaEntidad==null) new RequestException("US", 2, HttpStatus.NOT_FOUND, "Provincia no encontrada con ID: " + dto.getProvincia());


        Localidad localidadEntidad = localidadRepository.findById(dto.getLocalidad()).get();
        if(localidadEntidad==null) new RequestException("US", 2, HttpStatus.NOT_FOUND, "Localidad no encontrada con ID: " + dto.getLocalidad());


        NominatimResponseDTO coordenadas = geocodingService.obtenerCoordenadas(dto.getCalle(), dto.getNumero(), localidadEntidad.getNombre(), provinciaEntidad.getNombre());

        if (coordenadas == null) {
            throw new RequestException("US", 2, HttpStatus.BAD_REQUEST, "No se pudieron obtener las coordenadas para la dirección proporcionada");
        }

        direccion.setCalle(dto.getCalle());
        direccion.setNumero(dto.getNumero());
        direccion.setCodigoPostal(dto.getCodigoPostal());
        direccion.setProvincia(provinciaEntidad); 
        direccion.setLocalidad(localidadEntidad);
        direccion.setObservaciones(dto.getObservaciones());
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

    public void eliminarDireccion(String idDireccion){
        Direccion direccion = direccionRepository.findById(idDireccion)
                .orElseThrow(() -> new RequestException("US", 2, HttpStatus.NOT_FOUND, "Dirección no encontrada con ID: " + idDireccion));
        
        direccion.setEstado(EstadoDireccion.INACTIVO);
        direccionRepository.save(direccion);
    }

    public double calcularDistanciaEntreDirecciones(Direccion direccionVendedor, Direccion direccionCliente) {
        if(!direccionVendedor.getProvincia().getId().equals(direccionCliente.getProvincia().getId()) && !direccionVendedor.getLocalidad().getId().equals(direccionCliente.getLocalidad().getId())) {
            throw new RequestException("US", 2, HttpStatus.BAD_REQUEST, "El vendedor y el cliente tienen la misma dirección, no se puede calcular la distancia");
        }
        //se calcula la distancia utilizando la formula de Haversine, que tiene en cuenta la curvatura de la tierra, y devuelve el resultado en kilometros  
        double lon1 = Math.toRadians(direccionVendedor.getLongitud());
        double lat1 = Math.toRadians(direccionVendedor.getLatitud());
        double lat2 = Math.toRadians(direccionCliente.getLatitud());
        double lon2 = Math.toRadians(direccionCliente.getLongitud());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dLon / 2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        double radioTierra = 6371.0; // Radio en kilómetros
        return radioTierra * c;
    }

    }
