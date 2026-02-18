package com.seminario.ms_pedido.task;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.seminario.ms_pedido.model.EstadoPedido;
import com.seminario.ms_pedido.repository.PedidoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PedidoCleanupTask {
    private final PedidoRepository pedidoRepository;

    // Se ejecuta cada 15 minutos para revisar si hay pedidos viejos
    // (900000 milisegundos = 15 minutos)
    @Scheduled(fixedRate = 900000) 
    @Transactional
    public void borrarPedidosPendientesExpirados() {
        LocalDateTime limite = LocalDateTime.now().minusHours(2);
        
        log.info("Iniciando limpieza de pedidos pendientes anteriores a: {}", limite);
        
        // Buscamos y borramos en una sola consulta
        int eliminados = pedidoRepository.deleteByEstadoAndFechaCreacionBefore(
                EstadoPedido.PENDIENTE, 
                limite
        );
        
        if (eliminados > 0) {
            log.info("Se eliminaron {} pedidos pendientes por expiración de tiempo.", eliminados);
        }
    }
}
