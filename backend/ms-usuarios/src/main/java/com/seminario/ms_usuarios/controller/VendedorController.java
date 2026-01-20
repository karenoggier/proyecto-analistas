package com.seminario.ms_usuarios.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/vendedores")
@RequiredArgsConstructor
public class VendedorController {
    
    
    /*@PostMapping("/actualizar")
    public void actualizarVendedor(@RequestBody VendedorActualizarDTO vendedorDTO) {
        rabbitService.enviarActualizacion(vendedorDTO);
    }*/

}
