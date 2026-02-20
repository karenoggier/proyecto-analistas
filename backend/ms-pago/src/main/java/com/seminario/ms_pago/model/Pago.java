package com.seminario.ms_pago.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "transacciones")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String pedidoId; // El vínculo lógico con ms-pedido
    private String preferenciaId; // El ID que devuelve MP
    private String idMP; // ID de la operación de MP (se llena en el webhook)
    
    private BigDecimal monto;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaAprobacion;

    @Enumerated(EnumType.STRING)
    private MetodoPago metodoDePago;

    @Enumerated(EnumType.STRING)
    private EstadoTransaccion estado;
}
