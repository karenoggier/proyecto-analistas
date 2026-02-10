package com.seminario.ms_pedido.Repositories;
import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seminario.ms_pedido.model.Cliente;
import com.seminario.ms_pedido.model.Direccion;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, String> {

    ArrayList<Direccion> findByCliente(Cliente obtenerPerfil);

}