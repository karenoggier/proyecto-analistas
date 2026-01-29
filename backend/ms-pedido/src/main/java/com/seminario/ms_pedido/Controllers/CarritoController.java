package com.seminario.ms_pedido.Controllers;

import org.springframework.web.bind.annotation.*;
import com.seminario.ms_pedido.Services.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import com.seminario.ms_pedido.DTOs.CarritoDTO;
import com.seminario.ms_pedido.Mapper.CarritoMapper;
import com.seminario.ms_pedido.DTOs.ModificarItemCarritoDTO;


@RestController
@RequestMapping("/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    @GetMapping("/view")
    public ResponseEntity<CarritoDTO> viewCarrito(@RequestParam String id) {
        return ResponseEntity.ok(CarritoMapper.toDTO(carritoService.getCarritoByClienteId(id)));
    }

    @PostMapping("/modificarItem")
    public ResponseEntity<CarritoDTO> modificarItem(@RequestBody ModificarItemCarritoDTO dto) {
        return ResponseEntity.ok(CarritoMapper.toDTO(carritoService.modificarItem(dto.getClienteId(), dto.getVendedorId(), dto.getProductoId(), dto.getCantidad())));
    }
}
