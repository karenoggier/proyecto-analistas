package com.seminario.ms_pedido.model;

import java.util.ArrayList;

import lombok.Data;

@Data
public class Carrito {
    private String vendedorId;
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

    public DetalleCarrito encontrarProducto(String productoId) {
        if (this.detallesCarrito != null & !this.detallesCarrito.isEmpty()) {
            for (DetalleCarrito detalle : this.detallesCarrito) {
                if (detalle.getProductoId().equals(productoId)) {
                    return detalle;
                }
            }
        }
        return null;
    }
}
