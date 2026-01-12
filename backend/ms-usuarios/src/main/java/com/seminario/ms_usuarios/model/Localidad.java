package com.seminario.ms_usuarios.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "localidad")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Localidad {
    @Id
    private String id;
    
    private String nombre; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_provincia") 
    private Provincia provincia;
}
