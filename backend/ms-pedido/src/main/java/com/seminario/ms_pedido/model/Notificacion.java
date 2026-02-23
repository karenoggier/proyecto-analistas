package com.seminario.ms_pedido.model;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;



@Data
@Builder
@Document(collection = "notificaciones") 
public class Notificacion {
    @Id
    private String id; 
    private String mensaje;
    private String pedidoId;
    private String email;
    private LocalDateTime fechaHora;
    private boolean leida;
}
