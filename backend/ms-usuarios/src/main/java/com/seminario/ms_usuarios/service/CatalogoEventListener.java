/*package com.seminario.ms_usuarios.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.seminario.ms_usuarios.config.RabbitConfig;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CatalogoEventListener {

    @RabbitListener(queues = RabbitConfig.QUEUE_FROM_CATALOGO)
    public void recibirActualizacionDeCatalogo(Object evento) {
        try {
            log.info("📨 RECIBIDO EVENTO DE CATALOGO: " + evento.getClass().getSimpleName());
            log.info("Contenido: " + evento.toString());
            
            // Aquí puedes procesar eventos que vengan de ms-catalogo
            // Por ejemplo: actualizaciones de vendedores, etc.
            
        } catch (Exception e) {
            log.error("❌ ERROR AL PROCESAR EVENTO DE CATALOGO: " + e.getMessage(), e);
        }
    }
}
*/