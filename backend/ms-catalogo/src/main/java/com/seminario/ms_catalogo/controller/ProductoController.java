package com.seminario.ms_catalogo.controller;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_catalogo.dto.AllProductosResumidoDTO;
import com.seminario.ms_catalogo.service.ProductoService;
import com.seminario.ms_catalogo.service.VendedorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;
    private final VendedorService vendedorService;

    @GetMapping("/getAllProductos")
    public ResponseEntity<ArrayList<AllProductosResumidoDTO>> getAllProductos(@RequestParam String usuarioId) {
        vendedorService.usuarioExistente(usuarioId);
        return ResponseEntity.ok(productoService.getAllProductos(usuarioId));
    }
}
