package com.seminario.ms_pedido.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.seminario.ms_pedido.model.Carrito;

@Repository
public interface CarritoRepository extends MongoRepository<Carrito, String> {
    Optional<Carrito> findByClienteIdAndVendedorId(String clienteId, String vendedorId);
    List<Carrito> findByClienteId(String clienteId);
    
    //public Optional<ClienteCarrito> findByClienteEmail(String clienteEmail);

    /*public default Carrito findByClienteEmailAndVendedorId(String clienteEmail, String vendedorId){
        ClienteCarrito cliente = findByClienteEmail(clienteEmail).get();
        Carrito carrito = null;
        if(cliente != null){
            carrito = cliente.encontrarCarritoPorVendedor(vendedorId);
        }
        return carrito;
    }*/
}
