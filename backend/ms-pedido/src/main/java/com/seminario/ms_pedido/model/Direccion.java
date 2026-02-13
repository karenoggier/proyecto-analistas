/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.seminario.ms_pedido.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "direccion")
public class Direccion {
     @Id
    //@GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCliente", nullable = false) // this line specifies the foreign key column  idUsuario in the "direccion" table
    
    @ToString.Exclude // important to avoid circular references in toString() method
    private Cliente cliente;

    @Column (name = "provincia")
    private String provincia;

    @Column (name = "localidad")
    private String localidad;

    @Column(name = "calle")
    private String calle;

    @Column(name = "numero")
    private String numero;
 
    @Column(name = "codigoPostal")
    private String codigoPostal;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "latitud")
    private Double latitud;

    @Column(name = "longitud")
    private Double longitud;
    @Column(name = "estado")
    @Enumerated(EnumType.STRING)
    private EstadoDireccion estado;

}
