package com.seminario.ms_usuarios.client;

import org.jspecify.annotations.NonNull;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import com.seminario.ms_usuarios.dto.eventos_ms_catalogo.VendedorRegistradoEvent;

@HttpExchange(url = "/catalogoMs/api")
public interface CatalogoClient {

    @PostExchange("/vendedores/registrar")
    void registrarVendedor(@NonNull VendedorRegistradoEvent evento);

}