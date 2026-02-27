package com.seminario.ms_pago.client;

import org.jspecify.annotations.NonNull;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PatchExchange;

import com.seminario.ms_pago.dto.PedidoResponseDTO;

@HttpExchange(url = "/pedidoMs/pedidos")
public interface PedidoClient {

    @GetExchange("/{id}")
    @NonNull PedidoResponseDTO obtenerPedidoPorId(@PathVariable("id") @NonNull String id);

    @PatchExchange("/{id}/confirmar-pago")
    void confirmarPago(@PathVariable("id") String id);
    
}
