package com.seminario.ms_catalogo.service;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.seminario.ms_catalogo.model.Estado;

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

    public ResponseEntity<VendedorResponseDTO> obtenerVendedorPorUsuarioId(String usuarioId) {
        Vendedor vendedor = vendedorRepository.findByUsuarioId(usuarioId).get();
        if (vendedor == null) {
            return ResponseEntity.notFound().build();
        }

        VendedorResponseDTO vendedorResponseDTO = vendedorMapper.toDTO(vendedor);
        return ResponseEntity.ok(vendedorResponseDTO);
    }

    public VendedorResponseDTO buscarVendedorPorEmail(String email) {
        Vendedor vendedor = vendedorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No se encontró vendedor con email: " + email));
        
        return vendedorMapper.toDTO(vendedor);
    }

    @Transactional
    public ResponseEntity<VendedorResponseDTO> updateVendedor(VendedorRequestDTO dto, String email) {
        Vendedor vendedor = vendedorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado"));

        // Validacion    
        if (Estado.ACTIVO.equals(vendedor.getEstado())) {
            validarTodosLosCampos(dto);
        }
        else {
            validarCamposPrincipales(dto);
        }

        // Preparar datos para el MS-Usuarios
        VendedorRegistradoEvent guardado = vendedorMapper.toVendedorRegistradoEvent(dto);
        guardado.setUsuarioId(vendedor.getUsuarioId());

        // Llamada HTTP al MS-Usuarios (Para Lat/Long)
        VendedorRegistradoEvent respuestaUsuario;
        try {
            // Asumiendo que usuarioClient devuelve ResponseEntity<VendedorRegistradoEvent>
            respuestaUsuario = usuarioClient.actualizarVendedor(guardado).getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error al comunicarse con Microservicio Usuarios: " + e.getMessage());
        }

        // Actualizar campos del Vendedor (Catalogo)
        vendedor.setNombreNegocio(dto.getNombreNegocio());
        vendedor.setTelefono(dto.getTelefono());
        vendedor.setNombreResponsable(dto.getNombreResponsable());
        vendedor.setApellidoResponsable(dto.getApellidoResponsable());
        vendedor.setHorarioApertura(dto.getHorarioApertura());
        vendedor.setHorarioCierre(dto.getHorarioCierre());
        vendedor.setTiempoEstimadoEspera(dto.getTiempoEstimadoEspera());
        vendedor.setRealizaEnvios(dto.getRealizaEnvios());
        vendedor.setLogo(dto.getLogo());
        vendedor.setBanner(dto.getBanner());

        // Actualizamos dirección con lo que devolvió MS-Usuarios (que incluye Lat/Long)
        if (respuestaUsuario != null && respuestaUsuario.getDireccion() != null) {
            vendedor.setDireccion(direccionMapper.toEntity(respuestaUsuario.getDireccion()));
        }

        // Lógica de Cambio de Estado (INCOMPLETO -> ACTIVO)
        if (Estado.INCOMPLETO.equals(vendedor.getEstado()) || vendedor.getEstado() == null) {
            if (esPerfilCompleto(vendedor)) {
                vendedor.setEstado(Estado.ACTIVO);
            }
    }

        // Guardar y Retornar
        vendedorRepository.save(vendedor);
        return ResponseEntity.ok(vendedorMapper.toDTO(vendedor));
    }

    private boolean esPerfilCompleto(Vendedor v) {
        // Verifica si todos los campos obligatorios tienen valor
        return v.getNombreNegocio() != null && !v.getNombreNegocio().isEmpty() &&
            v.getDireccion() != null && 
            v.getDireccion().getCalle() != null && !v.getDireccion().getCalle().isEmpty() &&
            v.getDireccion().getProvincia() != null && !v.getDireccion().getProvincia().isEmpty() &&
            v.getDireccion().getLocalidad() != null && !v.getDireccion().getLocalidad().isEmpty() &&
            v.getDireccion().getNumero() != null && !v.getDireccion().getNumero().isEmpty() &&
            v.getDireccion().getCodigoPostal() != null && !v.getDireccion().getCodigoPostal().isEmpty() &&
            v.getTelefono() != null && !v.getTelefono().isEmpty() &&
            v.getHorarioApertura() != null && !v.getHorarioApertura().isEmpty() &&
            v.getHorarioCierre() != null && !v.getHorarioCierre().isEmpty() &&
            v.getNombreResponsable() != null && !v.getNombreResponsable().isEmpty() &&
            v.getApellidoResponsable() != null && !v.getApellidoResponsable().isEmpty() &&
            v.getRealizaEnvios() != null &&
            v.getTiempoEstimadoEspera() != null && !v.getTiempoEstimadoEspera().isEmpty();
    }

    private void validarTodosLosCampos(VendedorRequestDTO dto) {
        // Si intentan mandar vacío algo importante estando ACTIVO, lanzamos error - FALTA AGREGAR LAS VALIDACIONES DE LONGITUD
        if (dto.getNombreNegocio() == null || dto.getNombreNegocio().isEmpty()) {
            throw new IllegalArgumentException("No puedes dejar el nombre vacío");
        }
        if (dto.getTelefono() == null || dto.getTelefono().isEmpty()) {
            throw new IllegalArgumentException("No puedes dejar el teléfono vacío");
        }
        if (dto.getNombreResponsable() == null || dto.getNombreResponsable().isEmpty()) {
            throw new IllegalArgumentException("No puedes dejar el nombre del responsable vacío");
        }
        if (dto.getApellidoResponsable() == null || dto.getApellidoResponsable().isEmpty()) {
            throw new IllegalArgumentException("No puedes dejar el apellido del responsable vacío");
        }
        if (dto.getHorarioApertura() == null || dto.getHorarioApertura().isEmpty()) {
            throw new IllegalArgumentException("No puedes dejar el horario de apertura vacío");
        }
        if (dto.getHorarioCierre() == null || dto.getHorarioCierre().isEmpty()) {
            throw new IllegalArgumentException("No puedes dejar el horario de cierre vacío");
        }
        if (dto.getTiempoEstimadoEspera() == null || dto.getTiempoEstimadoEspera().isEmpty()) {
            throw new IllegalArgumentException("No puedes dejar el tiempo estimado de espera vacío");
        }
        if (dto.getRealizaEnvios() == null ) {
            throw new IllegalArgumentException("Debe indicar si realiza o no envíos");
        }
        if (dto.getDireccion().getProvincia() == null || dto.getDireccion().getProvincia().isEmpty()) {
            throw new IllegalArgumentException("No puedes dejar la provincia vacía");
        }
        if (dto.getDireccion().getLocalidad() == null || dto.getDireccion().getLocalidad().isEmpty()) {
            throw new IllegalArgumentException("No puedes dejar la localidad vacía");
        }
        if (dto.getDireccion().getCalle() == null || dto.getDireccion().getCalle().isEmpty()) {
            throw new IllegalArgumentException("No puedes dejar la calle vacía");
        }
        if (dto.getDireccion().getNumero() == null || dto.getDireccion().getNumero().isEmpty()) {
            throw new IllegalArgumentException("No puedes dejar el número vacío");
        }
        if (dto.getDireccion().getCodigoPostal() == null || dto.getDireccion().getCodigoPostal().isEmpty()) {
            throw new IllegalArgumentException("No puedes dejar el CP vacío");
        }


    }

    private void validarCamposPrincipales(VendedorRequestDTO dto) {
        // Si intentan mandar vacío algo que ya estaba en el registro, lanzamos error - FALTA AGREGAR LAS VALIDACIONES DE LONGITUD
        if (dto.getNombreNegocio() == null || dto.getNombreNegocio().isEmpty()) {
            throw new IllegalArgumentException("No puedes dejar el nombre vacío");
        }
        if (dto.getTelefono() == null || dto.getTelefono().isEmpty()) {
            throw new IllegalArgumentException("No puedes dejar el teléfono vacío");
        }
        if (dto.getNombreResponsable() == null || dto.getNombreResponsable().isEmpty()) {
            throw new IllegalArgumentException("No puedes dejar el nombre del responsable vacío");
        }
        if (dto.getApellidoResponsable() == null || dto.getApellidoResponsable().isEmpty()) {
            throw new IllegalArgumentException("No puedes dejar el apellido del responsable vacío");
        }
        if (dto.getDireccion().getProvincia() == null || dto.getDireccion().getProvincia().isEmpty()) {
            throw new IllegalArgumentException("No puedes dejar la provincia vacía");
        }
        if (dto.getDireccion().getLocalidad() == null || dto.getDireccion().getLocalidad().isEmpty()) {
            throw new IllegalArgumentException("No puedes dejar la localidad vacía");
        }
        if (dto.getDireccion().getCalle() == null || dto.getDireccion().getCalle().isEmpty()) {
            throw new IllegalArgumentException("No puedes dejar la calle vacía");
        }
        if (dto.getDireccion().getNumero() == null || dto.getDireccion().getNumero().isEmpty()) {
            throw new IllegalArgumentException("No puedes dejar el número vacío");
        }
        if (dto.getDireccion().getCodigoPostal() == null || dto.getDireccion().getCodigoPostal().isEmpty()) {
            throw new IllegalArgumentException("No puedes dejar el CP vacío");
        }
    }
    

   /* public ResponseEntity<VendedorResponseDTO> updateVendedor(VendedorRequestDTO vendedorRequestDTO) {
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
    }*/

    public ArrayList<Vendedor> obtenerVendedoresPorUbicacion(String provincia, String ciudad) {
        ArrayList<Vendedor> vendedores = new ArrayList<Vendedor>();
        if(provincia != null && ciudad != null){
            vendedores = vendedorRepository.findByDireccion_ProvinciaAndDireccion_Localidad(provincia, ciudad);
        } else if(provincia == null && ciudad != null){
            throw new RequestException("CA", 2, HttpStatus.BAD_REQUEST, "La provincia no puede ser nula");
        } else if(ciudad == null && provincia != null){
            throw new RequestException("CA", 2, HttpStatus.BAD_REQUEST, "La localidad no puede ser nula");
        } else {
            throw new RequestException("CA", 2, HttpStatus.BAD_REQUEST, "La provincia y la localidad no pueden ser nulas");    
        }
        return vendedores;
    }

}

