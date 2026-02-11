package com.seminario.ms_usuarios.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_usuarios.dto.VendedorRequestDTO;
import com.seminario.ms_usuarios.dto.eventos_ms_catalogo.VendedorRegistradoEvent;
import com.seminario.ms_usuarios.mapper.VendedorMapper;
import com.seminario.ms_usuarios.model.Vendedor;
import com.seminario.ms_usuarios.service.VendedorService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/vendedores")
@RequiredArgsConstructor
@Slf4j
public class VendedorController {
    private final VendedorService vendedorService;

    @PostMapping("/actualizar")
     @Operation(summary = "Actualiza un vendedor y su dirección. Llamado internamente por ms-catalogo")
    public ResponseEntity<VendedorRegistradoEvent> actualizarVendedor(@RequestBody VendedorRegistradoEvent vendedorDTO) {

        return ResponseEntity.ok(vendedorService.guardarVendedor(vendedorDTO));

    }

}
