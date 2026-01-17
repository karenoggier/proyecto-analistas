package com.seminario.ms_usuarios.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_usuarios.dto.VendedorResponseDTO;
import com.seminario.ms_usuarios.dto.VendedorUpdateRequestDTO;
import com.seminario.ms_usuarios.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    
   /*  @PostMapping("/actualizarVendedor")
    public ResponseEntity<VendedorResponseDTO> actualizarVendedor(@RequestBody VendedorUpdateRequestDTO vendedorDTO) {
        return ResponseEntity.ok(usuarioService.actualizarVendedor(vendedorDTO));
    }*/

    @GetMapping("/vendedorPorId")
    public VendedorResponseDTO getVendedorPorId(@RequestParam String id) {
        return usuarioService.obtenerVendedorPorId(id);  
    }

   


    //@GetMapping("/vendedorPorUbicacion")
    //public ArrayList<VendedorFiltradoParaCatalogoDTO> getVendedorPorUbicacion( @RequestParam String provincia, @RequestParam String localidad) {
      //  return usuarioService.obtenerVendedorPorUbicacion(provincia, localidad);}

}
