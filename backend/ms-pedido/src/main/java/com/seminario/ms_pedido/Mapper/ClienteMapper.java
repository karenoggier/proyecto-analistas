package com.seminario.ms_pedido.Mapper;

import org.springframework.stereotype.Component;

import com.seminario.ms_pedido.DTOs.eventos_ms_usuarios.ClienteRegistradoEvent;
import com.seminario.ms_pedido.model.Cliente;

@Component
public class ClienteMapper {
    public Cliente toEntity(ClienteRegistradoEvent event) {
        Cliente cliente = new Cliente();
        cliente.setId(event.getUsuarioId());
        cliente.setNombre(event.getNombre());
        cliente.setApellido(event.getApellido());
        cliente.setEmail(event.getEmail());
        cliente.setFechaNacimiento(event.getFechaNacimiento());
        cliente.setTelefono(event.getTelefono());
        cliente.setDireccion(null);
        cliente.setPedidos(null);
        return cliente;
    }

    

}
