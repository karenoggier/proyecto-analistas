package com.seminario.ms_pedido.repository;

import com.seminario.ms_pedido.model.Notificacion;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface NotificacionRepository extends MongoRepository<Notificacion, String> {
    List<Notificacion> findByEmailOrderByFechaHoraDesc(String email);

    long countByEmailAndLeidaFalse(String email);

    List<Notificacion> findByEmailAndLeidaFalse(String email);

    boolean existsByEmailAndPedidoIdAndMensajeAndFechaHoraAfter(
        String email,
        String pedidoId,
        String mensaje,
        java.time.LocalDateTime fechaHora
    );
}
