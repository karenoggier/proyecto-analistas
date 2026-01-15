package com.seminario.ms_usuarios.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.seminario.ms_usuarios.config.RabbitConfig;
import com.seminario.ms_usuarios.dto.ms_catalogo.VendedorActualizarDTO;

@Service
public class VendedorActualizador {

    private final RabbitTemplate rabbitTemplate;

    public VendedorActualizador(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarActualizacion(VendedorActualizarDTO actualizacion) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY,
                actualizacion
        );
    }
}
