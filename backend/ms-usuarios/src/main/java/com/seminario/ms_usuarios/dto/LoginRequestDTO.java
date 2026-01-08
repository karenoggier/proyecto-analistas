package com.seminario.ms_usuarios.dto;

import lombok.Data;

@Data
public abstract class LoginRequestDTO {
    private String email;
    private String password;
}
