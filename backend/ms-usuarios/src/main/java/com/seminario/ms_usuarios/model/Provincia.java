package com.seminario.ms_usuarios.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "provincia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Provincia {
    @Id
    private String id; 
    
    private String nombre;

    public Provincia orElseThrow(Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'orElseThrow'");
    } 
}
