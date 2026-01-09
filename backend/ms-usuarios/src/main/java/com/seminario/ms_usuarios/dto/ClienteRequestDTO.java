package com.seminario.ms_usuarios.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ClienteRequestDTO {
    private String email;
    private String password;
    private String nombre;
    private String apellido;
    private String telefono;
    private LocalDate fechaNacimiento;
}
