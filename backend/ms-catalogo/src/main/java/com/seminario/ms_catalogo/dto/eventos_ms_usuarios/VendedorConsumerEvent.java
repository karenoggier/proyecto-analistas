/*package com.seminario.ms_catalogo.dto.eventos_ms_usuarios;


import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.seminario.ms_catalogo.config.RabbitConfig;
import com.seminario.ms_catalogo.mapper.DireccionMapper;
import com.seminario.ms_catalogo.repository.VendedorRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class VendedorConsumerEvent {
    private final VendedorRepository repository;
    private final DireccionMapper direccionMapper;

    @RabbitListener(queues = RabbitConfig.QUEUE_FROM_USUARIOS)
    public void recibirCrudo(Message message) {
        String jsonBody = new String(message.getBody());
        System.out.println("🔥🔥🔥 ¡LLEGÓ UN MENSAJE A LA COLA! 🔥🔥🔥");
        System.out.println("Contenido JSON: " + jsonBody);

    }

   /*  @RabbitListener(queues = "vendedor.queue") 
    public void recibirRegistro(VendedorRegistradoEvent evento) {
       log.info("Recibido evento de registro para: {}", evento.getNombreNegocio());

        Vendedor vendedor = new Vendedor();
        vendedor.setUsuarioId(evento.getUsuarioId());
        vendedor.setNombreNegocio(evento.getNombreNegocio());
        vendedor.setNombreResponsable(evento.getNombreResponsable());
        vendedor.setApellidoResponsable (evento.getApellidoResponsable());
        vendedor.setTelefono(evento.getTelefono());
        vendedor.setLogo(null);
        vendedor.setBanner(null);
        vendedor.setRealizaEnvios(null);
        vendedor.setHorarioApertura(null);
        vendedor.setHorarioCierre(null);
        vendedor.setTiempoEstimadoEspera(null);
        vendedor.setEstado(Estado.INCOMPLETO);

        if (evento.getDireccion() != null) {
            vendedor.setDireccion(direccionMapper.toEntity(evento.getDireccion()));
        }

        vendedor.setProductos(null);

        repository.save(vendedor);
        log.info(" Vendedor guardado en MongoDB con ID Usuario: {}", evento.getUsuarioId());

        
    }
}*/
