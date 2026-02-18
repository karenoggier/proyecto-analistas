package com.seminario.ms_usuarios.service;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.seminario.ms_usuarios.exception.RequestException;
import com.seminario.ms_usuarios.model.Usuario;
import com.seminario.ms_usuarios.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
 
    public boolean existeEmail(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RequestException("US",2, HttpStatus.NOT_FOUND, "Credenciales inválidas"));
    }

    public Optional<Usuario> buscarPorId(String id) {
        return usuarioRepository.findById(id);
    }


   /*  public VendedorResponseDTO actualizarVendedor(VendedorUpdateRequestDTO vendedorDTO) {
        Vendedor vendedor = vendedorService.buscarPorId(vendedorDTO.getUsuarioId())
                .orElseThrow(() -> new RequestException("US", 2, HttpStatus.NOT_FOUND, "Vendedor no encontrado"));
        // Update fields
        vendedor.setNombreNegocio(vendedorDTO.getNombreNegocio());
        vendedor.setNombreResponsable(vendedorDTO.getNombreResponsable());
        vendedor.setApellidoResponsable(vendedorDTO.getApellidoResponsable());
        vendedor.setTelefono(vendedorDTO.getTelefono());
        vendedorService.guardarVendedor(vendedor);

        Direccion direccionUpdate = direccionService.actualizarDireccion(vendedorDTO.getDireccion(), vendedor.getId());
        DireccionResponseDTO direccionDTO = direccionService.registrarDireccion(vendedorDTO.getDireccion(), vendedor);

        //actualiza en el microservicio de catalogo
        VendedorRequestCatDTO vendedorCatDTO = vendedorMapper.toVendedorResponseCatDTO(vendedorDTO, direccionUpdate);
        VendedorResponseCatDTO vendedorResponseCatDTO = vendedorActualizador.enviarActualizacionRequest(vendedorCatDTO);
        return vendedorMapper.toResponse(vendedor,direccionDTO,vendedorResponseCatDTO);
       
    }*/

    /*public VendedorResponseDTO obtenerVendedorPorId(String id) {
        Vendedor vendedor = vendedorService.buscarPorId(id)
                .orElseThrow(() -> new RequestException("US", 2, HttpStatus.NOT_FOUND, "Vendedor no encontrado"));
        DireccionResponseDTO direccionDTO = direccionService.obtenerDireccionPorUsuarioId(vendedor.getId());
        VendedorResponseCatDTO vendedorResponseCatDTO = vendedorActualizador.enviarConsultaVendedorRequest(id);
        return vendedorMapper.toResponse(vendedor, direccionDTO);
        
    }   */

   



    // A useful method for validating logins (searches both clients and sellers simultaneously)
    /*@Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(String id) {
        return usuarioRepository.findById(id);
    }*/
    
}
