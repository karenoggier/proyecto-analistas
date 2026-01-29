package com.seminario.ms_pedido.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "DetalleEnvio")
public class DetalleEnvio {
    @Id
    private String id;
    @Column(name = "idPedido")
    private String idPedido;
    private String provincia;
    private String localidad;
    private String calle;
    private String codigoPostal;
    private String numero;
    private String latitud;
    private String longitud;
    private String obs;
}
