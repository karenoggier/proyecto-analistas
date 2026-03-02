package com.seminario.ms_catalogo.client;

import org.jspecify.annotations.NonNull;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PutExchange;

import com.seminario.ms_catalogo.dto.eventos_ms_usuarios.VendedorRegistradoEvent;

@HttpExchange(url = "/usuariosMs")
public interface UsuarioClient {
    
    @PutExchange(url = "/vendedores/actualizar")
    @NonNull VendedorRegistradoEvent actualizarVendedor(@RequestBody @NonNull VendedorRegistradoEvent vendedorRegistradoEvent);
}
