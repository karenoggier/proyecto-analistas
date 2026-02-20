package com.seminario.ms_pedido.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_pedido.dto.BorrarItemsRequestDTO;
import com.seminario.ms_pedido.dto.CarritoResponseDTO;
import com.seminario.ms_pedido.dto.ItemCarritoRequestDTO;
import com.seminario.ms_pedido.service.CarritoService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    @PostMapping("/items")
    @Operation(summary = "Agrega un nuevo item al carrito y si ya existe lo actualiza")
    public ResponseEntity<CarritoResponseDTO> agregarOModificarItem(@Valid @RequestBody ItemCarritoRequestDTO request, Authentication authentication) {
        
        String email = authentication.getName();

        CarritoResponseDTO carrito = carritoService.agregarOModificarItem(
                email,
                request.getVendedorId(),
                request.getProductoId(),
                request.getCantidad(),
                request.getObservaciones()
        );

        return ResponseEntity.ok(carrito);
    }

    @GetMapping("/todos")
    @Operation(summary = "Obtiene todos los carritos del cliente logueado")
    public ResponseEntity<List<CarritoResponseDTO>> obtenerTodos(Authentication auth) {
        return ResponseEntity.ok(carritoService.obtenerTodosLosCarritos(auth.getName()));
    }

    @GetMapping("/vendedor/{vendedorId}")
    @Operation(summary = "Obtiene el carrito del cliente con un vendedor específico")
    public ResponseEntity<CarritoResponseDTO> obtenerPorVendedor(
            @PathVariable String vendedorId, Authentication auth) {
        return ResponseEntity.ok(carritoService.obtenerCarritoPorVendedor(auth.getName(), vendedorId));
    }

    @DeleteMapping("/items")
    @Operation(summary = "Borra definitivamente items especificos de un carrito")
    public ResponseEntity<CarritoResponseDTO> eliminarItems(
            @Valid @RequestBody BorrarItemsRequestDTO request, Authentication auth) {
        
        CarritoResponseDTO resultado = carritoService.eliminarItems(
            auth.getName(), 
            request.getVendedorId(), 
            request.getItemsIds() 
        );

        return ResponseEntity.ok(resultado);
    }

    @PatchMapping("/items/cantidad")
    @Operation(summary = "Actualiza la cantidad de un item específico (sobrescribe)")
    public ResponseEntity<CarritoResponseDTO> actualizarCantidad(
            @Valid @RequestBody ItemCarritoRequestDTO request, 
            Authentication auth) {
        
        CarritoResponseDTO resultado = carritoService.actualizarCantidad(auth.getName(), request);

        return ResponseEntity.ok(resultado);
    }


}
