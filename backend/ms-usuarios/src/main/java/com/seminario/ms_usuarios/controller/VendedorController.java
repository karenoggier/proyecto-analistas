package com.seminario.ms_usuarios.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_usuarios.dto.ms_catalogo.VendedorActualizarDTO;
import com.seminario.ms_usuarios.service.VendedorActualizador;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/vendedores")
@RequiredArgsConstructor
public class VendedorController {
    
    private final VendedorActualizador vendedorActualizador;
    
    @PostMapping("/actualizar")
    public void actualizarVendedor(@RequestBody VendedorActualizarDTO vendedorDTO) {
        vendedorActualizador.enviarActualizacion(vendedorDTO);
    }

}
