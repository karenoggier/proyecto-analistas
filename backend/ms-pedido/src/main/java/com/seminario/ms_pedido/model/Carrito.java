package com.seminario.ms_pedido.model;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "carritos")
public class Carrito {
    @Id
    private String id;
    private String vendedorId;
    private String clienteId;
    private Double montoTotal;
    private Double montoTotalProductos;
    private ArrayList<DetalleCarrito> detallesCarrito;

    public void addDetalle(DetalleCarrito detalle) {
        if (this.detallesCarrito == null) {
            this.detallesCarrito = new ArrayList<>();
        }
        this.detallesCarrito.add(detalle);
    }

    public void calcularMontosTotales() {
        this.montoTotalProductos = 0.0;
        if (this.detallesCarrito != null) {
            for (DetalleCarrito detalle : this.detallesCarrito) {
                this.montoTotalProductos += detalle.getCantidad()*detalle.getMontoUnitario();
            }
        }
        // lógica para calcular descuentos, impuestos, etc.
        this.montoTotal = this.montoTotalProductos; // Por simplicidad, sin descuentos ni impuestos
    }
}
