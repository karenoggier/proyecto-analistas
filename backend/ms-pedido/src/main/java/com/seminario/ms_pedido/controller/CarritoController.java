package com.seminario.ms_pedido.controller;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_pedido.dto.CarritoDTO;
import com.seminario.ms_pedido.dto.DeleteItemDTO;
import com.seminario.ms_pedido.dto.ModificarItemCarritoDTO;
import com.seminario.ms_pedido.mapper.CarritoMapper;
import com.seminario.ms_pedido.model.Carrito;
import com.seminario.ms_pedido.service.CarritoService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    @GetMapping("/view")
    public ResponseEntity<ArrayList<CarritoDTO>> viewCarrito(Authentication authentication) {
        String email = authentication.getName(); // Obtener el email del cliente logueado

        ArrayList<Carrito> carritos = carritoService.getCarritoByClienteEmail(email);
        if (carritos == null || carritos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ArrayList<CarritoDTO> carritoDTOs = new ArrayList<>();
        for (Carrito carrito : carritos) {
            carritoDTOs.add(CarritoMapper.toDTO(carrito));
        }
        return ResponseEntity.ok(carritoDTOs);
    }

    @PutMapping("/modificarItem")
    public ResponseEntity<CarritoDTO> modificarItem(@RequestBody ModificarItemCarritoDTO dto, Authentication authentication) {
        System.out.println(((JwtAuthenticationToken) authentication)
                    .getToken()
                    .getTokenValue()); 

        return ResponseEntity.ok(CarritoMapper.toDTO(carritoService.modificarItem(authentication.getName(), dto.getVendedorId(), dto.getProductoId(), dto.getCantidad())));
    }

    @DeleteMapping("/eliminarItem")
    public ResponseEntity<Void> eliminarItem(@RequestBody DeleteItemDTO dto, Authentication authentication) {
        carritoService.deleteItem(authentication.getName(), dto.getVendedorId(), dto.getProductoId());
        return ResponseEntity.ok().build();
    }
}
