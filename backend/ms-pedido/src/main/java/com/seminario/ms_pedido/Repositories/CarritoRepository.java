package com.seminario.ms_pedido.Repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.seminario.ms_pedido.model.Carrito;
import com.seminario.ms_pedido.model.ClienteCarrito;

@Repository
public interface CarritoRepository extends MongoRepository<ClienteCarrito, String> {
    public Optional<ClienteCarrito> findByClienteEmail(String clienteEmail);

    public default Carrito findByClienteEmailAndVendedorId(String clienteEmail, String vendedorId){
        ClienteCarrito cliente = findByClienteEmail(clienteEmail).get();
        Carrito carrito = null;
        if(cliente != null){
            carrito = cliente.encontrarCarritoPorVendedor(vendedorId);
        }
        return carrito;
    }
}
