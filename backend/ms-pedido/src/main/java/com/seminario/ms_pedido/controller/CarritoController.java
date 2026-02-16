package com.seminario.ms_pedido.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    /*@GetMapping("/view")
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
    }*/
}
