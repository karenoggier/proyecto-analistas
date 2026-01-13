package com.seminario.ms_catalogo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "categorias")
public class Categoria {
    @Id
    private String id;
    private String nombre;
    private String descripcion;
}
