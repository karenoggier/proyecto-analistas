package com.seminario.ms_pedido.model;

import java.util.ArrayList;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.Data;

@Data
@Document(collection = "carritos")
public class ClienteCarrito {
    @Id
    private String id;
    private String clienteEmail;
    private String clienteId;
    private ArrayList<Carrito> carritos;

    public Carrito encontrarCarritoPorVendedor(String vendedorId) {
        if (this.carritos != null & !this.carritos.isEmpty()) {
            for (Carrito carrito : this.carritos) {
                if (carrito.getVendedorId().equals(vendedorId)) {
                    return carrito;
                }
            }
        }
        return null;
    }

    public void addCarrito(Carrito carrito) {
        if (this.carritos == null) {
            this.carritos = new ArrayList<>();
        }
        this.carritos.add(carrito);
    }
}

