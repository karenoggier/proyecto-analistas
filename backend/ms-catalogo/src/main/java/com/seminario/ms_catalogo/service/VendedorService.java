package com.seminario.ms_catalogo.service;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.seminario.ms_catalogo.client.UsuarioClient;
import com.seminario.ms_catalogo.dto.ProductoRequestDTO;
import com.seminario.ms_catalogo.dto.ProductoResponseDTO;
import com.seminario.ms_catalogo.dto.VendedorRequestDTO;
import com.seminario.ms_catalogo.dto.VendedorResponseDTO;
import com.seminario.ms_catalogo.dto.eventos_ms_usuarios.VendedorRegistradoEvent;
import com.seminario.ms_catalogo.exception.RequestException;
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
    private final UsuarioClient usuarioClient;
    

    public VendedorResponseDTO updateVendedorConDatosLocales(Vendedor vendedor) {
        Vendedor vendedorGuardado = vendedorRepository.save(vendedor);
        return vendedorMapper.toDTO(vendedorGuardado);
    }
    public ResponseEntity<ProductoResponseDTO> agregarProducto(ProductoRequestDTO productoRequestDTO, String vendedorId) {
        Vendedor vendedor = vendedorRepository.findByUsuarioId(vendedorId).orElse(null);
        if (vendedor == null) {
            throw new RequestException("CA", 2, HttpStatus.BAD_REQUEST, "Vendedor no encontrado");
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

    public Vendedor usuarioExistente(String usuarioId) {
        Vendedor vendedor = vendedorRepository.findByUsuarioId(usuarioId).orElse(null);
        if (vendedor == null) {
            //codigoo puesto al azar
            throw new RequestException("CA", 2, HttpStatus.BAD_REQUEST, "Vendedor no encontrado");
        }
        return vendedor;
    }

    public ResponseEntity<VendedorResponseDTO> obtnerVendedorPorUsuarioId(String usuarioId) {
        Vendedor vendedor = vendedorRepository.findByUsuarioId(usuarioId).get();
        if (vendedor == null) {
            return ResponseEntity.notFound().build();
        }

        VendedorResponseDTO vendedorResponseDTO = vendedorMapper.toDTO(vendedor);
        return ResponseEntity.ok(vendedorResponseDTO);
    }

    public ResponseEntity<VendedorResponseDTO> updateVendedor(VendedorRequestDTO vendedorRequestDTO) {
        Vendedor vendedor = vendedorRepository.findByUsuarioId(vendedorRequestDTO.getUsuarioId()).orElse(null);
        if (vendedor == null) {
            return ResponseEntity.notFound().build();
        }
        //aca hay que actualizar el ms-usuarios
        VendedorRegistradoEvent guardado = vendedorMapper.toVendedorRegistradoEvent(vendedorRequestDTO);
        VendedorRegistradoEvent evento = usuarioClient.actualizarVendedor(guardado).getBody();
      
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

    public ArrayList<Vendedor> obtenerVendedoresPorUbicacion(String provincia, String ciudad) {
        ArrayList<Vendedor> vendedores = new ArrayList<Vendedor>();
        if(provincia != null && ciudad != null){
            vendedores = vendedorRepository.findByDireccion_ProvinciaAndDireccion_Ciudad(provincia, ciudad);
        } else if(provincia == null && ciudad != null){
            throw new RequestException("CA", 2, HttpStatus.BAD_REQUEST, "La provincia no puede ser nula");
        } else if(ciudad == null && provincia != null){
            throw new RequestException("CA", 2, HttpStatus.BAD_REQUEST, "La ciudad no puede ser nula");
        } else {
            throw new RequestException("CA", 2, HttpStatus.BAD_REQUEST, "La provincia y la ciudad no pueden ser nulas");    
        }
        return vendedores;
    }

}

