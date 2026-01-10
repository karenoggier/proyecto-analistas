package com.seminario.ms_usuarios.service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.seminario.ms_usuarios.dto.NominatimResponseDTO;
import com.seminario.ms_usuarios.exception.RequestException;

@Service
public class GeocodingService {

    private final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search?q={address}&format=json&limit=1";

    public NominatimResponseDTO obtenerCoordenadas(String calle, String numero, String localidad) {
        RestTemplate restTemplate = new RestTemplate();
        
        // create the address query
        String addressQuery = String.format("%s %s, %s", calle, numero, localidad);

        // Configurar Headers (REQUISITO DE NOMINATIM)
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "AppPedidosGastronomicos-UTN-Estudiante"); // Pon un nombre identificativo
        
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        
        ResponseEntity<NominatimResponseDTO[]> response = restTemplate.exchange(
                NOMINATIM_URL,
                HttpMethod.GET,
                entity,
                NominatimResponseDTO[].class,
                addressQuery
            );

            NominatimResponseDTO[] resultados = response.getBody();
            
            if (resultados != null && resultados.length > 0) {
                return resultados[0]; // Retornamos el primer resultado encontrado
            }
            else {
                throw new RequestException("GE", 1, HttpStatus.NOT_FOUND, "No se encontraron coordenadas para la dirección proporcionada.");

            }
    }
}