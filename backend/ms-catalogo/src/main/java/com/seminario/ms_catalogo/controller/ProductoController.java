package com.seminario.ms_catalogo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_catalogo.dto.consultas_ms_pedido.ProductoResumidoDTO;
import com.seminario.ms_catalogo.mapper.ProductoMapper;
import com.seminario.ms_catalogo.service.ProductoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;
    private final VendedorController vendedorController;

    /* 
    @GetMapping("/mis-productos")
    @Operation(summary = "Obtener los productos de un vendedor logueado")
    public List<ProductoResponseDTO> misProductos(Authentication authentication) {
        String email = authentication.getName();
        return productoService.listarPorVendedor(email);
    }*/


    /*@GetMapping("/getAllProductosFromAllVendedores")
    public ResponseEntity<ArrayList<ProductoResponseDTO>> getAllProductosFromAllVendedores
    (@RequestParam String provincia, @RequestParam String ciudad) {
        return ResponseEntity.ok(productoService.getAllProductosFromAllVendedores(provincia, ciudad));

    }*/


    /*@GetMapping("/updateProducto")
    public ProductoResponseDTO updateProducto(@RequestParam String vendedorId, @RequestParam String productoId,
            @RequestParam ProductoResponseDTO productoRequestDTO) {
        return productoService.updateProducto(vendedorId, productoId, productoRequestDTO);

    }*/

    @GetMapping("/getProductoByIdAndVendedorId")
    public ResponseEntity<ProductoResumidoDTO> getProductoByIdAndVendedorId(@RequestParam String productoId,
            @RequestParam String vendedorId) {
        System.out.println("get recibido");
        return ResponseEntity.ok(ProductoMapper.toResumenDTO(productoService.getProductoByIdAndVendedorId(productoId, vendedorId), vendedorId));
    }
        

}
