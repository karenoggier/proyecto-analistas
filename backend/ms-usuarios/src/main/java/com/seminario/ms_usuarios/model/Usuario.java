package com.seminario.ms_usuarios.model;

import java.util.List;

import lombok.Data;

@Data
public class Usuario {
    private String id;
    private String email;
    private String telefono;
    private String contraseña;
    private EstadoUsuario estado;
    private List<Direccion> direcciones;
}