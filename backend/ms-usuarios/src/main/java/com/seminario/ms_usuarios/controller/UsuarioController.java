package com.seminario.ms_usuarios.controller;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_usuarios.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;


    //@GetMapping("/vendedorPorUbicacion")
    //public ArrayList<VendedorFiltradoParaCatalogoDTO> getVendedorPorUbicacion( @RequestParam String provincia, @RequestParam String localidad) {
      //  return usuarioService.obtenerVendedorPorUbicacion(provincia, localidad);}

}
