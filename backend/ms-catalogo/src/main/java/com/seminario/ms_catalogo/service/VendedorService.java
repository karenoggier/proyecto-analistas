package com.seminario.ms_catalogo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import com.seminario.ms_catalogo.exception.ValidationException;
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
    /*private final ProductoMapper productoMapper;*/
    private final DireccionMapper direccionMapper;
    private final VendedorMapper vendedorMapper;
    private final UsuarioClient usuarioClient;
    

    /*public VendedorResponseDTO updateVendedorConDatosLocales(Vendedor vendedor) {
        Vendedor vendedorGuardado = vendedorRepository.save(vendedor);
        return vendedorMapper.toDTO(vendedorGuardado);
    }*/

    /* 
    public ResponseEntity<ProductoResponseDTO> agregarProducto(ProductoRequestDTO productoRequestDTO, String vendedorId) {
        Vendedor vendedor = vendedorRepository.findByUsuarioId(vendedorId).orElse(null);
        if (vendedor == null) {
            throw new RequestException("CA", 2, HttpStatus.BAD_REQUEST, "Vendedor no encontrado");
        }
        Producto producto = productoMapper.toEntity(productoRequestDTO);
        vendedor.getProductos().add(producto);
        vendedorRepository.save(vendedor);
        return ResponseEntity.ok(productoMapper.toDTO(producto));
    }*/

    //Recibe el registro de vendedor desde ms-usuarios por HTTP sincrónico
    public void recibirRegistroVendedor(VendedorRegistradoEvent evento) {
           
            Vendedor vendedor = vendedorMapper.toNewEntity(evento);
            
            // Guardar vendedor en mongodb
            Vendedor vendedorGuardado = vendedorRepository.save(vendedor);
            
    }

    /* 
    public Vendedor usuarioExistente(String usuarioId) {
        Vendedor vendedor = vendedorRepository.findByUsuarioId(usuarioId).orElse(null);
        if (vendedor == null) {
            //codigoo puesto al azar
            throw new RequestException("CA", 2, HttpStatus.BAD_REQUEST, "Vendedor no encontrado");
        }
        return vendedor;
    }*/

    /* 
    public ResponseEntity<VendedorResponseDTO> obtenerVendedorPorUsuarioId(String usuarioId) {
        Vendedor vendedor = vendedorRepository.findByUsuarioId(usuarioId).get();
        if (vendedor == null) {
            return ResponseEntity.notFound().build();
        }

        VendedorResponseDTO vendedorResponseDTO = vendedorMapper.toDTO(vendedor);
        return ResponseEntity.ok(vendedorResponseDTO);
    }*/

    
    public VendedorResponseDTO buscarVendedorPorEmail(String email) {
        Vendedor vendedor = vendedorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No se encontró vendedor con email: " + email));
        
        return vendedorMapper.toDTO(vendedor);
    }

    @Transactional
    public ResponseEntity<VendedorResponseDTO> updateVendedor(VendedorRequestDTO dto, String email) {
        Vendedor vendedor = vendedorRepository.findByEmail(email)
                .orElseThrow(() -> new RequestException("CAT", 404, HttpStatus.NOT_FOUND, "Vendedor no encontrado"));

        Map<String, String> errores = new HashMap<>();

        // Validacion    
        if (Estado.ACTIVO.equals(vendedor.getEstado())) {
            errores = validarTodosLosCampos(dto);
        }
        else {
            errores = validarCamposBasicos(dto);
        }

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
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
            String error = e.getMessage();
            throw new RequestException("US", 2, HttpStatus.BAD_REQUEST, error);
            //throw new RuntimeException("Error al comunicarse con Microservicio Usuarios: " + e.getMessage());
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

    

    private boolean hasText(String text) {
        return text != null && !text.trim().isEmpty();
    }

    private boolean esPerfilCompleto(Vendedor v) {
        // Verifica si todos los campos DE TEXTO tienen valor.
        return hasText(v.getNombreNegocio()) &&
               hasText(v.getTelefono()) &&
               hasText(v.getNombreResponsable()) &&
               hasText(v.getApellidoResponsable()) &&
               hasText(v.getHorarioApertura()) &&
               hasText(v.getHorarioCierre()) &&
               hasText(v.getTiempoEstimadoEspera()) &&
               v.getRealizaEnvios() != null && 
               v.getDireccion() != null &&
               hasText(v.getDireccion().getCalle()) &&
               hasText(v.getDireccion().getNumero()) &&
               hasText(v.getDireccion().getCodigoPostal()) &&
               v.getDireccion().getProvincia() != null && 
               v.getDireccion().getLocalidad() != null;
    }

    private Map<String, String> validarCamposBasicos(VendedorRequestDTO dto) {
        Map<String, String> errores = new HashMap<>();

        if (!hasText(dto.getNombreNegocio())) errores.put("nombreNegocio", "El nombre es obligatorio");
        if (!hasText(dto.getTelefono())) errores.put("telefono", "El teléfono es obligatorio");
        if (!hasText(dto.getNombreResponsable())) errores.put("nombreResponsable", "Responsable requerido");
        if (!hasText(dto.getApellidoResponsable())) errores.put("apellidoResponsable", "Apellido requerido");

        // Dirección siempre obligatoria para que el mapa funcione
        if (dto.getDireccion() != null) {
            if (!hasText(dto.getDireccion().getProvincia())) errores.put("provincia", "Falta provincia");
            if (!hasText(dto.getDireccion().getLocalidad())) errores.put("localidad", "Falta localidad");
            if (!hasText(dto.getDireccion().getCalle())) errores.put("calle", "Falta calle");
            if (!hasText(dto.getDireccion().getNumero())) errores.put("numero", "Falta altura");
            if (!hasText(dto.getDireccion().getCodigoPostal())) errores.put("codigoPostal", "Falta CP");
        } else {
            errores.put("direccion", "Dirección requerida");
        }
        
        return errores;
    }

    private Map<String, String> validarTodosLosCampos(VendedorRequestDTO dto) {
        // 1. Primero chequeamos que tenga lo básico
        Map<String, String> errores = validarCamposBasicos(dto);

        // 2. Sumamos las exigencias para ser ACTIVO
        if (!hasText(dto.getHorarioApertura())) errores.put("horarioApertura", "Requerido para estar Activo");
        if (!hasText(dto.getHorarioCierre())) errores.put("horarioCierre", "Requerido para estar Activo");
        if (!hasText(dto.getTiempoEstimadoEspera())) errores.put("tiempoEstimadoEspera", "Requerido para estar Activo");
        
        if (dto.getRealizaEnvios() == null) errores.put("realizaEnvios", "Debe definir envíos");

        return errores;
    }

    /* 
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
    }*/

}

