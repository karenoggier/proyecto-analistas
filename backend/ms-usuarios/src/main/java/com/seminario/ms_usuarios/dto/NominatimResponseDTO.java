package com.seminario.ms_usuarios.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
public class NominatimResponseDTO{
    
    @JsonProperty("lat")
    private String lat;
    
    @JsonProperty("lon")
    private String lon;
    
    @JsonProperty("display_name")
    private String displayName;

    public double getLatitud() {
        return Double.parseDouble(lat);
    }

    public double getLongitud() {
        return Double.parseDouble(lon);
    }

}