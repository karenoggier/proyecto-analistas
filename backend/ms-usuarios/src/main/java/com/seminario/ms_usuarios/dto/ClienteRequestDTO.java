package com.seminario.ms_usuarios.dto;

import lombok.Data;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

@Data
public class ClienteRequestDTO {
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @NotBlank(message = "Debe repetir la contraseña")
    private String repetirPassword;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede superar los 100 caracteres")
    private String apellido;

    // --- TELÉFONO CON MÁSCARA ---
    // Explicación Regex: ^\+54\s9\s = Empieza con "+54 9 "
    // \d{4} = 4 números (característica)
    // - = guion
    // \d{6} = 6 números (resto del número)
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^\\+54\\s9\\s\\d{4}-\\d{6}$", message = "El teléfono debe respetar el formato +54 9 XXXX-XXXXXX")
    private String telefono;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    private LocalDate fechaNacimiento;
}
