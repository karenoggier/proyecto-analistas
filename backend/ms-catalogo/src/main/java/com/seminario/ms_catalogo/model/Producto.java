package com.seminario.ms_catalogo.model;

import java.math.BigDecimal;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;

@Data
@Document(collection = "productos")
public class Producto {
    @Id
    private String id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String imagen;
    private Estado estado;
    private String vendedorId; 

    @DBRef
    private Categoria categoria;
}
