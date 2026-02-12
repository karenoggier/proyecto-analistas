package com.seminario.ms_pedido.Services;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seminario.ms_pedido.DTOs.DireccionRequestDTO;
import com.seminario.ms_pedido.DTOs.DireccionResponseDTO;
import com.seminario.ms_pedido.Mapper.DireccionMapper;
import com.seminario.ms_pedido.Repositories.ClienteRepository;
import com.seminario.ms_pedido.Repositories.DireccionRepository;
import com.seminario.ms_pedido.client.UsuarioClient;
import com.seminario.ms_pedido.exception.RequestException;
import com.seminario.ms_pedido.model.Cliente;
import com.seminario.ms_pedido.model.Direccion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class DireccionService {
    private final DireccionRepository direccionRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioClient usuarioClient;
    private final DireccionMapper direccionMapper;

    @Transactional
    public DireccionResponseDTO agregarDireccion(String email, DireccionRequestDTO dto) {
       Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RequestException("PED", 404, HttpStatus.NOT_FOUND, "Cliente no encontrado con email: " + email));
       
        String clienteId = cliente.getId();

        DireccionResponseDTO direccionValidada = usuarioClient.buscarDatosDireccion(dto, clienteId);

        Direccion nuevaDireccion = direccionMapper.toEntity(direccionValidada);
       
        nuevaDireccion.setCliente(cliente);

        direccionRepository.save(nuevaDireccion);

        return direccionValidada;
    }

    @Transactional
    public void eliminarDireccion(String idDireccion) {
        Direccion direccion = direccionRepository.findById(idDireccion)
                .orElseThrow(() -> new RequestException("PED", 404, HttpStatus.NOT_FOUND, "Dirección no encontrada con ID: " + idDireccion));
        
        usuarioClient.eliminarDireccion(idDireccion);
        direccion.setEstado("INACTIVO");
        direccionRepository.save(direccion);
    }
    
    /* 
    public ArrayList<DireccionResponseDTO> obtenerDireccion(Cliente obtenerPerfil) {
        ArrayList<Direccion> direcciones = direccionRepository.findByCliente(obtenerPerfil);
        ArrayList<DireccionResponseDTO> direccionesResponse = new ArrayList<>();
        for (Direccion direccion : direcciones) {
            direccionesResponse.add(direccionMapper.toResponseDTO(direccion));
        }
        return direccionesResponse;
    }*/

}
