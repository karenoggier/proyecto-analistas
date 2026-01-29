package com.seminario.ms_pedido.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import java.util.ArrayList;

@Data
@Document(collection = "carritos")
public class Carrito {
    @Id
    private String id;
    private String vendedorId;
    @Indexed(unique = true)
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
}
