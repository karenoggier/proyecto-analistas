package com.seminario.ms_pedido.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
    private List<DetallePedido> detalles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCliente", nullable = true) // this line specifies the foreign key column  idUsuario in the "direccion" table
    
    @ToString.Exclude // important to avoid circular references in toString() method
    private Cliente cliente;
}
