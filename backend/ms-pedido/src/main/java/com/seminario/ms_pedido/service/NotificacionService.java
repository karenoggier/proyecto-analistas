package com.seminario.ms_pedido.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seminario.ms_pedido.dto.NotificacionDTO;
import com.seminario.ms_pedido.mapper.NotificacionMapper;
import com.seminario.ms_pedido.model.Notificacion;
import com.seminario.ms_pedido.repository.NotificacionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificacionService {
    private final NotificacionRepository notificacionRepository;
    private final NotificacionMapper notificacionMapper;
    private final SimpMessagingTemplate messagingTemplate;

    public List<NotificacionDTO> listarNotificaciones(String email) {
        // Obtener todas las notificaciones no leídas
        List<Notificacion> noLeidas = notificacionRepository.findByEmailAndLeidaFalseOrderByFechaHoraDesc(email);
        
        // Si hay más de 10 no leídas, retornar todas las no leídas
        if (noLeidas.size() > 10) {
            return noLeidas.stream()
                    .map(notificacionMapper::toDTO)
                    .toList();
        }
        
        // Si hay 10 o menos no leídas, retornar las 10 más recientes (leídas y no leídas combinadas)
        return notificacionRepository.findByEmailOrderByFechaHoraDesc(email)
                .stream()
                .limit(10)
                .map(notificacionMapper::toDTO)
                .toList();
    }

    @Transactional
    public void crearYEnviarNotificacion(String email, String mensaje, String pedidoId) {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(2);
        boolean isDuplicate = notificacionRepository.existsByEmailAndPedidoIdAndMensajeAndFechaHoraAfter(
            email,
            pedidoId,
            mensaje,
            cutoff
        );
        if (isDuplicate) {
            return;
        }

        Notificacion entidad = Notificacion.builder()
                .email(email)
                .mensaje(mensaje)
                .pedidoId(pedidoId)
                .fechaHora(LocalDateTime.now())
                .leida(false)
                .build();
        
        notificacionRepository.save(entidad);

        NotificacionDTO dto = notificacionMapper.toDTO(entidad);

        messagingTemplate.convertAndSendToUser(email, "/queue/updates", dto);
    }

    public void marcarComoLeidas(String email) {
        List<Notificacion> noLeidas = notificacionRepository.findByEmailAndLeidaFalse(email);
        noLeidas.forEach(n -> n.setLeida(true));
        notificacionRepository.saveAll(noLeidas);
    }
}
