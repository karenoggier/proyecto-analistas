package com.seminario.ms_usuarios.client;
import org.jspecify.annotations.NonNull;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import com.seminario.ms_usuarios.dto.eventos_ms_pedidio.ClienteRegistradoEvent;

@HttpExchange(url = "/pedidoMs")
public interface PedidoClient {

    @PostExchange("/clientes/registrar")
    void registrarCliente(@NonNull ClienteRegistradoEvent evento);

}
