package com.seminario.ms_catalogo.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.seminario.ms_catalogo.config.RabbitConfig;
import com.seminario.ms_catalogo.dto.ProductoRequestDTO;
import com.seminario.ms_catalogo.dto.ProductoResponseDTO;
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
    private final RabbitTemplate rabbitTemplate;
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

    @RabbitListener(queues = RabbitConfig.QUEUE_FROM_USUARIOS)
    public void recibirRegistroVendedor(VendedorRegistradoEvent evento) {
                
        try {
            log.info("Recibido evento de registro para: {}", evento.getNombreNegocio());

            Vendedor vendedor = vendedorMapper.toNewEntity(evento);
            
            // Guardar vendedor
            Vendedor vendedorGuardado = vendedorRepository.save(vendedor);
            
            log.info("VENDEDOR REGISTRADO EN CATALOGO: " + vendedorGuardado.getId());
            log.info(" Vendedor guardado en MongoDB con ID Usuario: {}", evento.getUsuarioId());
            
        } catch (Exception e) {
            log.error("ERROR AL REGISTRAR VENDEDOR EN CATALOGO: " + e.getMessage(), e);
        }
    }

    public ResponseEntity<VendedorResponseDTO> obtnerVendedorPorUsuarioId(String usuarioId) {
        Vendedor vendedor = vendedorRepository.findByUsuarioId(usuarioId);
        if (vendedor == null) {
            return ResponseEntity.notFound().build();
        }

        VendedorResponseDTO vendedorResponseDTO = vendedorMapper.toDTO(vendedor);
        return ResponseEntity.ok(vendedorResponseDTO);
    }
    
}


