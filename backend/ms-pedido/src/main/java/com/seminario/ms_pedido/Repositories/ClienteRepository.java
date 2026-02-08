package com.seminario.ms_pedido.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seminario.ms_pedido.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {

}
