package com.seminario.ms_usuarios.dto.georef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocalidadAPI {
    
    private String id;
    private String nombre;

    private ProvinciaAPI provincia;
}
