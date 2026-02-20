package com.seminario.ms_pago.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.seminario.ms_pago.model.Pago;
import com.seminario.ms_pago.model.EstadoTransaccion;

@Repository
public interface PagoRepository extends JpaRepository<Pago, String> {
    Optional<Pago> findByPedidoId(String pedidoId);
    Optional<Pago> findByPreferenciaId(String preferenciaId);
    List<Pago> findByPedidoIdAndEstadoOrderByFechaCreacionAsc(String pedidoId, EstadoTransaccion estado);
    List<Pago> findAllByPedidoIdOrderByFechaCreacionDesc(String pedidoId);
	Optional<Pago> findByIdMP(String paymentId);
}
