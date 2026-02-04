package com.seminario.ms_usuarios.service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.seminario.ms_usuarios.dto.NominatimResponseDTO;
import com.seminario.ms_usuarios.exception.RequestException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GeocodingService {

    //private final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search?q={address}&format=json&limit=1";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

   /* public NominatimResponseDTO obtenerCoordenadas(String calle, String numero, String localidad, String provincia) {
        RestTemplate restTemplate = new RestTemplate();
        
        // create the address query
        String addressQuery = String.format("%s %s, %s, %s, %s", calle, numero, localidad, provincia, "Argentina");
        
        // Configurar Headers (REQUISITO DE NOMINATIM)
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "ProyectoTituloIntermedio-UTN-Estudiante"); 
        
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        
        ResponseEntity<NominatimResponseDTO[]> response = restTemplate.exchange(
                NOMINATIM_URL,
                HttpMethod.GET,
                entity,
                NominatimResponseDTO[].class,
                addressQuery
            );

            NominatimResponseDTO[] resultados = response.getBody();
            
            //return new NominatimResponseDTO("-43.44", "1.3234", "addressQuery");
            if (resultados != null && resultados.length > 0) {
                return resultados[0]; // Retornamos el primer resultado encontrado
            }
            else {
                throw new RequestException("GE", 1, HttpStatus.NOT_FOUND, "No se encontraron coordenadas para la dirección proporcionada.");

            }
    }*/

    public NominatimResponseDTO obtenerCoordenadas(String calle, String numero, String localidad, String provincia) {
        
        if (calle == null || numero == null) return null;

        // 1. Armamos una búsqueda simple tipo Google: "Calle Altura, Localidad, Provincia"
        String query = String.format("%s %s, %s, %s, Argentina", calle, numero, localidad, provincia);
        
        // 2. Usamos la API de Photon 
        String url = "https://photon.komoot.io/api/?q={query}&limit=1";

        try {
            // Hacemos la llamada recibiendo un String (JSON crudo) para procesarlo manualmente
            String jsonResponse = restTemplate.getForObject(url, String.class, query);

            // 3. Procesamos el JSON
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode features = root.path("features");

            if (features.isArray() && features.size() > 0) {
                // Photon devuelve GeoJSON: [longitud, latitud] 
                JsonNode coordinates = features.get(0).path("geometry").path("coordinates");
                
                String lon = String.valueOf(coordinates.get(0).asDouble());
                String lat = String.valueOf(coordinates.get(1).asDouble());

                // 4. Devolvemos el DTO
                return new NominatimResponseDTO(lat, lon, query); 
            }

        } catch (Exception e) {
            System.out.println("Error en Photon Geocoding: " + e.getMessage());
            throw new RequestException("GE", 1, HttpStatus.NOT_FOUND, "No se encontraron coordenadas para la dirección proporcionada.");
        }

        return null;
    }
}