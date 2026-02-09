package com.seminario.ms_pedido.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.seminario.ms_pedido.DTOs.DireccionRequestDTO;
import com.seminario.ms_pedido.DTOs.DireccionResponseDTO;
import com.seminario.ms_pedido.exception.RequestException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@RequiredArgsConstructor
@Slf4j
public class UsuarioClient {
    private final RestTemplate restTemplate;

    @Value("${usuarios.ms.url:http://localhost:8080}")
    private String usuariosBaseUrl;

    /*@CircuitBreaker(name = "usuarioClient", fallbackMethod = "BuscarDatosDireccionFallback")
    @Retry(name = "usuarioClient")
    public DireccionResponseDTO BuscarDatosDireccion(DireccionRequestDTO event) {
        try {
            String url = usuariosBaseUrl + "/usuariosMs/direcciones/obtener";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
                                    
            //envío de mensaje HTTP
            ResponseEntity<?> response = restTemplate.postForEntity(url, event, DireccionResponseDTO.class);
            
            //si devuelve un error personalizado del microservicio de usuarios
            if(response.getBody().getClass() != DireccionResponseDTO.class) {
                Map<String, Object> error = (Map<String, Object>) response.getBody();
                throw new RequestException(error.get("ms_code").toString(),
                                           (int) error.get("ly_code"),
                                           HttpStatus.valueOf((int) error.get("status")),
                                           error.get("mensaje").toString());
            }

            return (DireccionResponseDTO) response.getBody();
            
        } catch (Exception e) {
            throw new RequestException("USU", 500, HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error al sincronizar con ms-usuario: " + e.getMessage());
        }
    }*/
    public DireccionResponseDTO buscarDatosDireccion(DireccionRequestDTO event) {
        String url = usuariosBaseUrl + "/usuariosMs/direcciones/obtener";
        try {
            ResponseEntity<DireccionResponseDTO> response = restTemplate.postForEntity(url, event, DireccionResponseDTO.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }
            
            throw new RequestException("USU", 500, HttpStatus.INTERNAL_SERVER_ERROR, "Respuesta vacía del servidor");

        } catch (HttpStatusCodeException e) {
            // Errores HTTP específicos del microservicio de usuarios
            throw new RequestException("USU", e.getStatusCode().value(), (HttpStatus) e.getStatusCode(), 
                "Error remoto: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            // Errores de timeout o conexión
            throw new RequestException("USU", 500, HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error de comunicación: " + e.getMessage());
        }
    }
    //Fallback method cuando el circuit breaker está abierto
    public DireccionResponseDTO BuscarDatosDireccionFallback(DireccionRequestDTO event, Throwable exception) {
        System.err.println("Fallback activado por: " + exception.getMessage());
        throw new RequestException(
            "USU", 
            503, 
            HttpStatus.SERVICE_UNAVAILABLE, 
            "El servicio de gestión de usuarios/direcciones no responde. Intente nuevamente en unos minutos."
        );
    }

    
       


}
