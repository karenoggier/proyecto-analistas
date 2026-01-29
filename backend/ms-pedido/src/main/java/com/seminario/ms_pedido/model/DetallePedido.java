package com.seminario.ms_pedido.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "DetallePedido")
public class DetallePedido {
    @Id
    private String id;
    @Column(name = "idPedido")
    private String idPedido;
    private String idProducto;
    private Double cantidad;
    private Double montoUnitario;
    private String observaciones;
}
