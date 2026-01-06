package com.seminario.ms_usuarios.model;

import java.util.*;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "email")
    private String email;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "contraseña")
    private String contraseña;

    @Column(name = "estado")
    @Enumerated(EnumType.STRING) // Save "Activo" and not 0 or 1
    private EstadoUsuario estado;
   
    //Relation 1 a N with Direcciones (One user have many directions)
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Direccion> direcciones;
}