package com.seminario.ms_usuarios.service;

import com.seminario.ms_usuarios.dto.georef.GeorefLocalidadesResponseDTO;
import com.seminario.ms_usuarios.dto.georef.GeorefProvinciasResponseDTO;
import com.seminario.ms_usuarios.model.Localidad;
import com.seminario.ms_usuarios.model.Provincia;
import com.seminario.ms_usuarios.repository.LocalidadRepository;
import com.seminario.ms_usuarios.repository.ProvinciaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UbicacionService {
    private final ProvinciaRepository provinciaRepository;
    private final LocalidadRepository localidadRepository;
    
    private final RestTemplate restTemplate = new RestTemplate(); 

    // URLs de la API del Gobierno
    // max=24 trae todas las provincias
    private static final String URL_PROVINCIAS = "https://apis.datos.gob.ar/georef/api/provincias?campos=id,nombre&max=24";
    // max=5000 asegura traer todas las localidades del país
    private static final String URL_LOCALIDADES = "https://apis.datos.gob.ar/georef/api/localidades?campos=id,nombre,provincia&max=5000";

    @Transactional 
    public void cargarDatosGeograficos() {
        
        if (provinciaRepository.count() > 0) {
            System.out.println("La base de datos ya tiene información geográfica. Se omite la carga.");
            return;
        }

        System.out.println("Iniciando carga de Provincias y Localidades desde Georef...");

        try {
            // --- CARGAR PROVINCIAS ---
            GeorefProvinciasResponseDTO responseProv = restTemplate.getForObject(URL_PROVINCIAS, GeorefProvinciasResponseDTO.class);
            
            if (responseProv != null && responseProv.getProvincias() != null) {
                // Mapeamos de DTO (API) a Entidad (BD)
                List<Provincia> provinciasEntities = responseProv.getProvincias().stream().map(dto -> {
                    return new Provincia(dto.getId(), dto.getNombre());
                }).collect(Collectors.toList());

                provinciaRepository.saveAll(provinciasEntities);
                System.out.println(provinciasEntities.size() + " Provincias guardadas.");
            }

            // --- CARGAR LOCALIDADES ---
            GeorefLocalidadesResponseDTO responseLoc = restTemplate.getForObject(URL_LOCALIDADES, GeorefLocalidadesResponseDTO.class);
            
            if (responseLoc != null && responseLoc.getLocalidades() != null) {
                // Mapeamos de DTO (API) a Entidad (BD)
                List<Localidad> localidadesEntities = responseLoc.getLocalidades().stream().map(dto -> {
                    Localidad localidad = new Localidad();
                    localidad.setId(dto.getId());
                    localidad.setNombre(dto.getNombre());
                    
                    // Relación: Solo necesitamos crear una instancia de Provincia con el ID
                    // No hace falta buscarla en la BD, Hibernate entiende la referencia por ID.
                    Provincia provRef = new Provincia();
                    provRef.setId(dto.getProvincia().getId()); // ID que viene de la API
                    localidad.setProvincia(provRef);
                    
                    return localidad;
                }).collect(Collectors.toList());

                localidadRepository.saveAll(localidadesEntities);
                System.out.println(localidadesEntities.size() + " Localidades guardadas.");
            }

        } catch (Exception e) {
            System.err.println("Error al cargar datos de Georef: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // --- MÉTODOS PARA EL CONTROLLER ---
    
    public List<Provincia> obtenerTodasLasProvincias() {
        return provinciaRepository.findAll();
    }
    
    public List<Localidad> obtenerLocalidadesPorProvincia(String idProvincia) {
        return localidadRepository.findByProvinciaIdOrderByNombreAsc(idProvincia);
    }
}
