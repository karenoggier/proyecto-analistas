package com.seminario.ms_usuarios.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.seminario.ms_usuarios.config.RabbitConfig;



@Service
public class RabbitCommunicationService {

    private final RabbitTemplate rabbitTemplate;

    public RabbitCommunicationService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarRegistroVendedorEvent(Object evento) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_FROM_USUARIOS,
                RabbitConfig.ROUTING_KEY_REGISTRAR_USUARIOS,
                evento
        );
    }
    /*public VendedorResponseCatDTO enviarActualizacionRequest(VendedorRequestCatDTO actualizacion) {
        // convertSendAndReceive: Envía el mensaje y SE BLOQUEA esperando la vuelta
        Object respuesta = rabbitTemplate.convertSendAndReceive(
                RabbitConfig.EXCHANGE_TO_CATALOGO,
                RabbitConfig.ROUTING_KEY_TO_CATALOGO,
                actualizacion
        );

        // Verificamos y casteamos la respuesta
        if (respuesta instanceof VendedorResponseCatDTO) {
            return (VendedorResponseCatDTO) respuesta;
        } else {
            // Manejo de error si la respuesta es nula o de otro tipo
            throw new RuntimeException("El servicio de Catálogo no respondió correctamente.");
        }

    }*/

    /*public VendedorResponseCatDTO enviarConsultaVendedorRequest(String id) {
        // convertSendAndReceive: Envía el mensaje y SE BLOQUEA esperando la vuelta
        Object respuesta = rabbitTemplate.convertSendAndReceive(
                RabbitConfig.EXCHANGE_TO_CATALOGO,
                RabbitConfig.ROUTING_KEY_TO_CATALOGO,
                id
        );
        // Verificamos y casteamos la respuesta
        if (respuesta instanceof VendedorResponseCatDTO) {
            return (VendedorResponseCatDTO) respuesta;
        } else {
            // Manejo de error si la respuesta es nula o de otro tipo
            throw new RuntimeException("El servicio de Catálogo no respondió correctamente.");
        }
    }*/
    

    
}
