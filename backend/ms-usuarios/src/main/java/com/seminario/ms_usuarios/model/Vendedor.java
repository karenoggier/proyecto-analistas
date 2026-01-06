package com.seminario.ms_usuarios.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "vendedor")
@Data
@EqualsAndHashCode(callSuper = true)
public class Vendedor extends Usuario {
    @Column(name = "nombreNegocio")
    private String nombreNegocio;
    @Column(name = "nombreResponsable")
    private String nombreResponsable;
    @Column(name = "apellidoResponsable")
    private String apellidoResponsable;
}
