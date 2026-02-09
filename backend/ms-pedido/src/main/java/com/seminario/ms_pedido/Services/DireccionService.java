package com.seminario.ms_pedido.Services;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seminario.ms_pedido.DTOs.DireccionRequestDTO;
import com.seminario.ms_pedido.DTOs.DireccionResponseDTO;
import com.seminario.ms_pedido.Mapper.DireccionMapper;
import com.seminario.ms_pedido.Repositories.DireccionRepository;
import com.seminario.ms_pedido.client.UsuarioClient;
import com.seminario.ms_pedido.model.Cliente;
import com.seminario.ms_pedido.model.Direccion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class DireccionService {
    private final DireccionRepository direccionRepository;
    private final UsuarioClient usuarioClient;
    private final DireccionMapper direccionMapper;

    @Transactional
    public DireccionResponseDTO registrarDireccion(DireccionRequestDTO direccionRequestDTO, Cliente cliente) {
        DireccionResponseDTO direccionResponseDTO = new DireccionResponseDTO();
        direccionResponseDTO = usuarioClient.buscarDatosDireccion(direccionRequestDTO);
        Direccion direccionNew = new Direccion();
        direccionNew.setProvincia(direccionResponseDTO.getProvincia());
        direccionNew.setLocalidad(direccionResponseDTO.getLocalidad());
        direccionNew.setCalle(direccionRequestDTO.getCalle());
        direccionNew.setNumero(direccionRequestDTO.getNumero());
        direccionNew.setCodigoPostal(direccionRequestDTO.getCodigoPostal());
        direccionNew.setLatitud(direccionResponseDTO.getLatitud());
        direccionNew.setLongitud(direccionResponseDTO.getLongitud());
        direccionNew.setObservaciones(direccionRequestDTO.getObservaciones());
        direccionNew.setCliente(cliente);
        direccionRepository.save(direccionNew);

        return direccionMapper.toResponseDTO(direccionNew);
    }

}
