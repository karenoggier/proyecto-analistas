package com.seminario.ms_pedido.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.seminario.ms_pedido.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {
    Optional<Cliente> findByEmail(String email);

    @Query("SELECT c FROM Cliente c LEFT JOIN FETCH c.direccion d " +
           "WHERE c.email = :email AND (d IS NULL OR d.estado = :estado)")
    Optional<Cliente> findByEmailAndDireccionEstado(@Param("email") String email, @Param("estado") String estado);
}
