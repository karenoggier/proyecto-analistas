package com.seminario.ms_usuarios.dto.eventos_ms_pedidio;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ClienteRegistradoEvent {
    private String usuarioId;      
    private String email;
    private String nombre;
    private String apellido;
    private String telefono;
    private LocalDate fechaNacimiento;
}
