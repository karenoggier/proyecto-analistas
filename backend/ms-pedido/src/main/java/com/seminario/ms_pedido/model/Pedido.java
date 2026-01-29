package com.seminario.ms_pedido.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Pedido")
public class Pedido {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "clienteId")
    private String clienteId;
    @Column(name = "vendedorId")
    private String vendedorId;
    @Column(name = "fechaCreacion")
    private LocalDateTime fechaCreacion;
    @Column(name = "estado")
    private EstadoPedido estado;
    @Column(name = "montoTotal")
    private Double montoTotal;
    @Column(name = "montoTotalProductos")
    private Double montoTotalProductos;
    @Column(name = "metodoEnvio")
    private TipoEnvio metodoEnvio;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idPedido", referencedColumnName = "id")
    private DetalleEnvio detalleEnvio;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idPedido", referencedColumnName = "id")
    private DetallePedido[] detalles;
}
