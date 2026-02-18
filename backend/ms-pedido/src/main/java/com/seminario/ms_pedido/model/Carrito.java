package com.seminario.ms_pedido.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import lombok.Data;

@Data
@Document(collection = "carritos")
public class Carrito {
    @Id
    private String id;

    @Indexed
    private String clienteId;

    @Indexed
    private String vendedorId;

    private BigDecimal montoTotal;

    private BigDecimal montoTotalProductos;

    private BigDecimal comisionApp;

    private List<DetalleCarrito> detallesCarrito = new ArrayList<>();;

    @CreatedDate // Se setea automáticamente al insertar el documento
    @Indexed(name = "expire_after_2_hours", expireAfterSeconds = 7200)
    private LocalDateTime fechaCreacion;

    public void addDetalle(DetalleCarrito detalle) {
        if (this.detallesCarrito == null) {
            this.detallesCarrito = new ArrayList<>();
        }
        this.detallesCarrito.add(detalle);
    }

    public void calcularMontosTotales() {
        // 1. Sumamos los subtotales de cada detalle
        this.montoTotalProductos = detallesCarrito.stream()
            .map(d -> d.getMontoUnitario().multiply(BigDecimal.valueOf(d.getCantidad())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        this.comisionApp = this.montoTotalProductos.multiply(new BigDecimal("0.05"));

        this.montoTotal = this.montoTotalProductos.add(this.comisionApp);
    }

    public DetalleCarrito encontrarProducto(String productoId) {
        if (this.detallesCarrito == null || this.detallesCarrito.isEmpty()) {
            return null;
        }
        
        return this.detallesCarrito.stream()
                .filter(detalle -> detalle.getProductoId().equals(productoId))
                .findFirst()
                .orElse(null);
        }
}
