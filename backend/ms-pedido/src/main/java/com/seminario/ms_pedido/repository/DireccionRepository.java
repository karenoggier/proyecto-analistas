package com.seminario.ms_pedido.repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seminario.ms_pedido.model.Cliente;
import com.seminario.ms_pedido.model.Direccion;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, String> {

    ArrayList<Direccion> findByCliente(Cliente obtenerPerfil);
    Optional<Direccion> findByIdAndCliente(String id, Cliente obtenerPerfil);
    List<Direccion> findByClienteIdAndLocalidadIgnoreCase(String clienteId, String localidad);
}