package com.seminario.ms_pedido.service;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.seminario.ms_pedido.dto.ClienteResponseDTO;
import com.seminario.ms_pedido.dto.eventos_ms_usuarios.ClienteRegistradoEvent;
import com.seminario.ms_pedido.exception.RequestException;
import com.seminario.ms_pedido.mapper.ClienteMapper;
import com.seminario.ms_pedido.model.Cliente;
import com.seminario.ms_pedido.model.EstadoDireccion;
import com.seminario.ms_pedido.repository.ClienteRepository;

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

    public ClienteResponseDTO obtenerPerfilPorEmail(String email) {

    // Busca el cliente por email y las direcciones que no estén eliminadas lógicamente
        Cliente cliente = clienteRepository.findByEmail(email)
        .orElseThrow(() -> new RequestException("PE", 404, HttpStatus.NOT_FOUND, "Cliente no encontrado"));
        // Filtra la lista en una sola línea de forma segura
        cliente.getDireccion().removeIf(direccion -> EstadoDireccion.INACTIVO.equals(direccion.getEstado()));
        
        return clienteMapper.toResponseDTO(cliente);
    }

    public Cliente obtenerClientePorEmail(String email) {
        return clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RequestException("PE", 404, HttpStatus.NOT_FOUND, "Cliente no encontrado"));
    }

}
