package com.seminario.ms_catalogo.controller;

import lombok.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.seminario.ms_catalogo.dto.*;
import com.seminario.ms_catalogo.service.VendedorService;

@RestController
@RequestMapping("/vendedor")
@RequiredArgsConstructor
public class VendedorController {
    private final VendedorService vendedorService;

    @PostMapping("/agregarProducto")
    public ResponseEntity<ProductoResponseDTO> agregarProducto(@RequestBody ProductoRequestDTO productoRequestDTO, @RequestParam String vendedorId) {
        // Lógica para agregar un producto
        return vendedorService.agregarProducto(productoRequestDTO, vendedorId);
    }

}
