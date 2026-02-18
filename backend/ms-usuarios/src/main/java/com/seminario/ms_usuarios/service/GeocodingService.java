package com.seminario.ms_usuarios.service;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seminario.ms_usuarios.dto.NominatimResponseDTO;
import com.seminario.ms_usuarios.exception.RequestException;

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

    public double calcularDistancia(Double latitud, Double longitud, Double latitud2, Double longitud2) {
        return obtenerDistanciaPorCalle(String.valueOf(latitud), String.valueOf(longitud), String.valueOf(latitud2), String.valueOf(longitud2));
       
    }
    public double obtenerDistanciaPorCalle(String lat1, String lon1, String lat2, String lon2) {
    // El formato de OSRM es longitud,latitud;longitud,latitud
    String coords = String.format("%s,%s;%s,%s", lon1, lat1, lon2, lat2);
    String url = "http://router.project-osrm.org/route/v1/driving/" + coords + "?overview=false";

    try {
        String jsonResponse = restTemplate.getForObject(url, String.class);
        JsonNode root = objectMapper.readTree(jsonResponse);
        
        // Verificamos que la ruta sea exitosa
        if ("Ok".equals(root.path("code").asText())) {
            // La distancia viene en metros en: routes[0].distance
            double distanciaMetros = root.path("routes").get(0).path("distance").asDouble();
            
            // Convertimos a kilómetros
            return distanciaMetros / 1000.0;
        }
    } catch (Exception e) {
        System.err.println("Error calculando ruta: " + e.getMessage());
    }
    return -1; // O manejar el error según tu lógica
}
}