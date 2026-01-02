package com.seminario.ms_usuarios.model;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Cliente extends Usuario {
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
}
