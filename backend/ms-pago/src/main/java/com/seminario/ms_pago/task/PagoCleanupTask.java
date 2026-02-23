package com.seminario.ms_pago.task;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.seminario.ms_pago.model.EstadoTransaccion;
import com.seminario.ms_pago.model.Pago;
import com.seminario.ms_pago.repository.PagoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PagoCleanupTask {
    private final PagoRepository pagoRepository;

    // Se ejecuta cada 15 minutos
    // (900000 milisegundos = 15 minutos)
    @Scheduled(fixedDelay = 900000) 
    @Transactional
    public void limpiarPagosDuplicadosYAntiguos() {
        // 1. Limpiar pendientes de más de 2 horas ...
        LocalDateTime haceDosHoras = LocalDateTime.now().minusHours(2);

        List<Pago> pendientesAntiguos = pagoRepository.findByEstadoAndFechaCreacionBefore(
                EstadoTransaccion.PENDIENTE, haceDosHoras);
        
        if (!pendientesAntiguos.isEmpty()) {
            log.info("Borrando {} pagos pendientes antiguos.", pendientesAntiguos.size());
            pagoRepository.deleteAll(pendientesAntiguos);
        }

        // 2. Lógica para dejar solo 1 registro por pedido si ya están aprobados/rechazados
        List<String> pedidoIdsDuplicados = pagoRepository.findPedidoIdsConDuplicados();
    
        for (String pedidoId : pedidoIdsDuplicados) {
            List<Pago> todosLosPagos = pagoRepository.findAllByPedidoIdOrderByFechaCreacionDesc(pedidoId);
            
            if (todosLosPagos.size() <= 1) continue;

            // Buscamos el mejor pago para conservar
            // 2.1. Prioridad: APROBADO
            // 2.2. Si no hay aprobado: El más reciente (que ya está primero por el OrderBy)
            Pago pagoAConservar = todosLosPagos.stream()
                .filter(p -> EstadoTransaccion.APROBADO.equals(p.getEstado()))
                .findFirst()
                .orElse(todosLosPagos.get(0));

            // Filtramos la lista para obtener los que vamos a borrar
            List<Pago> pagosABorrar = todosLosPagos.stream()
                .filter(p -> !p.getId().equals(pagoAConservar.getId()))
                .toList();

            log.info("Pedido {}: Conservando pago ID {}, borrando {} duplicados.", 
                    pedidoId, pagoAConservar.getId(), pagosABorrar.size());
            
            pagoRepository.deleteAll(pagosABorrar);
           
        }
        log.info("Limpieza de pagos finalizada.");
    }
}
