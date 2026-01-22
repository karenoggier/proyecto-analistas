package com.seminario.ms_catalogo.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.seminario.ms_catalogo.dto.ProductoRequestDTO;
import com.seminario.ms_catalogo.dto.ProductoResponseDTO;
import com.seminario.ms_catalogo.dto.VendedorRequestDTO;
import com.seminario.ms_catalogo.dto.VendedorResponseDTO;
import com.seminario.ms_catalogo.dto.eventos_ms_usuarios.VendedorRegistradoEvent;
import com.seminario.ms_catalogo.mapper.DireccionMapper;
import com.seminario.ms_catalogo.mapper.ProductoMapper;
import com.seminario.ms_catalogo.mapper.VendedorMapper;
import com.seminario.ms_catalogo.model.Producto;
import com.seminario.ms_catalogo.model.Vendedor;
import com.seminario.ms_catalogo.repository.VendedorRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class VendedorService {
    private final VendedorRepository vendedorRepository;
    private final ProductoMapper productoMapper;
    private final DireccionMapper direccionMapper;
    private final VendedorMapper vendedorMapper;
    

    public ResponseEntity<ProductoResponseDTO> agregarProducto(ProductoRequestDTO productoRequestDTO, String vendedorId) {
        Vendedor vendedor = vendedorRepository.findById(vendedorId).orElse(null);
        if (vendedor == null) {
            return ResponseEntity.notFound().build();
        }
        Producto producto = productoMapper.toEntity(productoRequestDTO);
        vendedor.getProductos().add(producto);
        vendedorRepository.save(vendedor);
        return ResponseEntity.ok(productoMapper.toDTO(producto));
    }

    //Recibe el registro de vendedor desde ms-usuarios por HTTP sincrónico
    public void recibirRegistroVendedor(VendedorRegistradoEvent evento) {
           
            Vendedor vendedor = vendedorMapper.toNewEntity(evento);
            
            // Guardar vendedor en mongodb
            Vendedor vendedorGuardado = vendedorRepository.save(vendedor);
            
    }

    public ResponseEntity<VendedorResponseDTO> obtnerVendedorPorUsuarioId(String usuarioId) {
        Vendedor vendedor = vendedorRepository.findByUsuarioId(usuarioId);
        if (vendedor == null) {
            return ResponseEntity.notFound().build();
        }

        VendedorResponseDTO vendedorResponseDTO = vendedorMapper.toDTO(vendedor);
        return ResponseEntity.ok(vendedorResponseDTO);
    }

    public ResponseEntity<VendedorResponseDTO> updateVendedor(VendedorRequestDTO vendedorRequestDTO) {
        Vendedor vendedor = vendedorRepository.findById(vendedorRequestDTO.getUsuarioId()).orElse(null);
        if (vendedor == null) {
            return ResponseEntity.notFound().build();
        }
        //aca hay que actualizar el ms-usuarios
        VendedorRegistradoEvent evento = updateVendorEnUsuarios(vendedorRequestDTO);
      
        vendedor.setNombreNegocio(vendedorRequestDTO.getNombreNegocio());
        vendedor.setTelefono(vendedorRequestDTO.getTelefono());
        vendedor.setNombreResponsable(vendedorRequestDTO.getNombreResponsable());
        vendedor.setApellidoResponsable(vendedorRequestDTO.getApellidoResponsable());
        vendedor.setBanner(vendedorRequestDTO.getBanner());
        vendedor.setLogo(vendedorRequestDTO.getLogo());
        vendedor.setHorarioApertura(vendedorRequestDTO.getHorarioApertura());
        vendedor.setHorarioCierre(vendedorRequestDTO.getHorarioCierre());
        vendedor.setRealizaEnvios(vendedorRequestDTO.getRealizaEnvios());
        vendedor.setTiempoEstimadoEspera(vendedorRequestDTO.getTiempoEstimadoEspera());

        if(evento.getDireccion() != null){
            vendedor.setDireccion(direccionMapper.toEntity(evento.getDireccion()));
        }
        vendedorRepository.save(vendedor);
        return ResponseEntity.ok(vendedorMapper.toDTO(vendedor));
    }
 
    private VendedorRegistradoEvent updateVendorEnUsuarios(Object event) {
        //aca tiene que mandar el evento a ms-usuarios y recibir el dto actualizado
       return new VendedorRegistradoEvent();
    }

}

