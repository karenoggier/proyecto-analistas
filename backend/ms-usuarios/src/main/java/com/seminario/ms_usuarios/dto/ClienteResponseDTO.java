package com.seminario.ms_usuarios.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ClienteResponseDTO {
    private String nombre;
    private String apellido;
    private String email;
    private String rol;
    private LocalDate fechaNacimiento;
}
