package com.seminario.ms_catalogo.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.seminario.ms_catalogo.config.RabbitConfig;

@Service
public class RabbitCommunicationService {
    private final RabbitTemplate RabbitTemplate;

    public RabbitCommunicationService(RabbitTemplate rabbitTemplate) {
        this.RabbitTemplate = rabbitTemplate;
    }

    public void enviarActualizacion(Object evento) {
        RabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_TO_USUARIOS,
                RabbitConfig.ROUTING_KEY_ACTUALIZAR_USUARIOS,
                evento
        );
    }
}
