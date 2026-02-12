package com.seminario.ms_pedido.Services;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.seminario.ms_pedido.DTOs.ClienteResponseDTO;
import com.seminario.ms_pedido.DTOs.eventos_ms_usuarios.ClienteRegistradoEvent;
import com.seminario.ms_pedido.Mapper.ClienteMapper;
import com.seminario.ms_pedido.Repositories.ClienteRepository;
import com.seminario.ms_pedido.exception.RequestException;
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

    public ClienteResponseDTO obtenerPerfilPorEmail(String email) {

    // Busca el cliente por email y las direcciones que no estén eliminadas lógicamente
        Cliente cliente = clienteRepository.findByEmail(email)
        .orElseThrow(() -> new RequestException("PE", 404, HttpStatus.NOT_FOUND, "Cliente no encontrado"));
        // Filtra la lista en una sola línea de forma segura
        cliente.getDireccion().removeIf(direccion -> "INACTIVO".equals(direccion.getEstado()));
        
        return clienteMapper.toResponseDTO(cliente);
    }

}
