package com.seminario.ms_usuarios.dto.georef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeorefProvinciasResponseDTO {
    private List<ProvinciaAPI> provincias;
}
