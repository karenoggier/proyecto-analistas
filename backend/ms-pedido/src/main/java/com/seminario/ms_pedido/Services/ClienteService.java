package com.seminario.ms_pedido.Services;
import org.springframework.stereotype.Service;

import com.seminario.ms_pedido.DTOs.eventos_ms_usuarios.ClienteRegistradoEvent;
import com.seminario.ms_pedido.Mapper.ClienteMapper;
import com.seminario.ms_pedido.Repositories.ClienteRepository;
import com.seminario.ms_pedido.model.Cliente;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    
    public void registrarCliente(ClienteRegistradoEvent cliente) {
        Cliente clienteEntity = clienteMapper.toEntity(cliente);
        clienteRepository.save(clienteEntity);
    }

    public Cliente buscarClientePorEmail(String usuarioIdentity) {
        return (clienteRepository.findByEmail(usuarioIdentity))
                 .orElseThrow(() -> new RuntimeException("No se encontró cliente con email: " + usuarioIdentity)); 
    }

}
