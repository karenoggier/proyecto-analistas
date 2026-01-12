package com.seminario.ms_usuarios.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.seminario.ms_usuarios.model.Provincia;
import com.seminario.ms_usuarios.model.Localidad;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DireccionResponseDTO {
    private Provincia provincia;
    private Localidad localidad;
    private String calle;
    private String numero;
    private String codigoPostal;
    private String observaciones;
    private Double latitud;
    private Double longitud;

}
