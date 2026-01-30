package com.seminario.ms_pedido.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_pedido.DTOs.CarritoDTO;
import com.seminario.ms_pedido.DTOs.ModificarItemCarritoDTO;
import com.seminario.ms_pedido.Mapper.CarritoMapper;
import com.seminario.ms_pedido.Services.CarritoService;
import com.seminario.ms_pedido.model.Carrito;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    @GetMapping("/view")
    public ResponseEntity<CarritoDTO> viewCarrito(@RequestParam String id) {
        Carrito carrito = carritoService.getCarritoByClienteId(id);
        if (carrito == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(CarritoMapper.toDTO(carrito));
    }

    @PostMapping("/modificarItem")
    public ResponseEntity<CarritoDTO> modificarItem(@RequestBody ModificarItemCarritoDTO dto) {
        return ResponseEntity.ok(CarritoMapper.toDTO(carritoService.modificarItem(dto.getClienteId(), dto.getVendedorId(), dto.getProductoId(), dto.getCantidad())));
    }
}
