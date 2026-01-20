package com.seminario.ms_usuarios.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;



@Service
public class VendedorActualizador {

    private final RabbitTemplate rabbitTemplate;

    public VendedorActualizador(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
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
