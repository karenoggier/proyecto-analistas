package com.seminario.ms_pago.repository;

import java.time.LocalDateTime;
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
    
    List<Pago> findByEstadoAndFechaCreacionBefore(EstadoTransaccion estado, LocalDateTime fecha);
    
    @Query("SELECT p.pedidoId FROM Pago p GROUP BY p.pedidoId HAVING COUNT(p.pedidoId) > 1")
    List<String> findPedidoIdsConDuplicados();

}
