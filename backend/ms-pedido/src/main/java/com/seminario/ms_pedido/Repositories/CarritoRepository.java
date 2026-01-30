package com.seminario.ms_pedido.Repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.seminario.ms_pedido.model.Carrito;

@Repository
public interface CarritoRepository extends MongoRepository<Carrito, String> {
    public Optional<Carrito> findByClienteId(String clienteId);

    public Optional<Carrito> findByClienteIdAndVendedorId(String clienteId, String vendedorId);
}
