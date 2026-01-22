package com.seminario.ms_catalogo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_catalogo.dto.ProductoRequestDTO;
import com.seminario.ms_catalogo.dto.ProductoResponseDTO;
import com.seminario.ms_catalogo.dto.VendedorRequestDTO;
import com.seminario.ms_catalogo.dto.VendedorResponseDTO;
import com.seminario.ms_catalogo.dto.eventos_ms_usuarios.VendedorRegistradoEvent;
import com.seminario.ms_catalogo.service.VendedorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/vendedores")
@RequiredArgsConstructor
@Slf4j
public class VendedorController {
    private final VendedorService vendedorService;

    @PostMapping("/agregarProducto")
    public ResponseEntity<ProductoResponseDTO> agregarProducto(@RequestBody ProductoRequestDTO productoRequestDTO, 
    @RequestParam String vendedorId) {
        return vendedorService.agregarProducto(productoRequestDTO, vendedorId);
    }
    @GetMapping("/obtnerVendedorPorUsuarioId")
    public ResponseEntity<VendedorResponseDTO> obtnerVendedorPorUsuarioId(@RequestParam String usuarioId) {
        return vendedorService.obtnerVendedorPorUsuarioId(usuarioId);
    }
    @PostMapping("/updateVendedor")
    public ResponseEntity<VendedorResponseDTO> updateVendedor(@RequestBody VendedorRequestDTO vendedorRequestDTO) {
        return vendedorService.updateVendedor(vendedorRequestDTO);
    }

    //Endpoint HTTP desde ms-usuarios
    @PostMapping("/registrar")
    public ResponseEntity<Void> registrarVendedor(@RequestBody VendedorRegistradoEvent evento) {
            vendedorService.recibirRegistroVendedor(evento);
            return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
