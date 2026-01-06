package com.seminario.ms_usuarios.model;

import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "clientes")
public class Cliente extends Usuario {
    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "fechaDeCumpleaños")
    private LocalDate fechaNacimiento;
}
