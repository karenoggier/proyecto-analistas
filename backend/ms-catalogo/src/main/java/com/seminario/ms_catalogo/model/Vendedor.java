package com.seminario.ms_catalogo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "vendedores")
public class Vendedor {
    @Id
    private String id;

    @Indexed(unique = true)
    private String usuarioId;
    
    private String nombreNegocio;
    private String logo;
    private Boolean realizaEnvios;
    private String horarioApertura;
    private String horarioCierre;
    private String tiempoEstimadoEspera;
    private Estado estado;
}
