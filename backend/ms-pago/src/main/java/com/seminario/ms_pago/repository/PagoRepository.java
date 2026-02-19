package com.seminario.ms_pago.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seminario.ms_pago.model.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, String> {
    Optional<Pago> findByPedidoId(String pedidoId);
    Optional<Pago> findByPreferenciaId(String preferenciaId);
}
