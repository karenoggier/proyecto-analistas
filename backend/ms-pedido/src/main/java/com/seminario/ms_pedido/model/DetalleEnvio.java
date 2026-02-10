package com.seminario.ms_pedido.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "DetalleEnvio")
public class DetalleEnvio {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "idPedido")
    private String idPedido;
    private String provincia;
    private String localidad;
    private String calle;
    private String codigoPostal;
    private String numero;
    private Double latitud;
    private Double longitud;
    private String obs;
}
