package com.seminario.ms_usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VendedorRequestDTO {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @NotBlank(message = "Debe repetir la contraseña")
    private String repetirPassword;

    @NotBlank(message = "El nombre del negocio es obligatorio")
    @Size(max = 150, message = "El nombre del negocio no puede exceder los 150 caracteres")
    private String nombreNegocio;

    @NotBlank(message = "El nombre del responsable es obligatorio")
    @Size(max = 100, message = "El nombre del responsable no puede exceder los 100 caracteres")
    private String nombreResponsable;

    @NotBlank(message = "El apellido del responsable es obligatorio")
    @Size(max = 100, message = "El apellido del responsable no puede exceder los 100 caracteres")
    private String apellidoResponsable;

    // --- TELÉFONO CON MÁSCARA ---
    // Explicación Regex: ^\+54\s9\s = Empieza con "+54 9 "
    // \d{4} = 4 números (característica)
    // - = guion
    // \d{6} = 6 números (resto del número)
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^\\+54\\s9\\s\\d{4}-\\d{6}$", message = "El teléfono debe respetar el formato +54 9 XXXX-XXXXXX")
    private String telefono;

    private DireccionRequestDTO direccion;
}
