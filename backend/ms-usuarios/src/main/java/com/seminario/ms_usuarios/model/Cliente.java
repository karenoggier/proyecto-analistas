package com.seminario.ms_usuarios.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "clientes")
public class Cliente extends Usuario {
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
}
