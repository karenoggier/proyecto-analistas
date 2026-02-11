package com.seminario.ms_usuarios.controller;

import com.seminario.ms_usuarios.model.Localidad;
import com.seminario.ms_usuarios.model.Provincia;
import com.seminario.ms_usuarios.service.UbicacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ubicacion")
@RequiredArgsConstructor
public class UbicacionController {
    private final UbicacionService ubicacionService;

    @GetMapping("/provincias")
    @Operation(summary = "Lista todas las provincias")
    public ResponseEntity<List<Provincia>> listarProvincias() {
        return ResponseEntity.ok(ubicacionService.obtenerTodasLasProvincias());
    }

    @GetMapping("/localidades/{idProvincia}")
    @Operation(summary = "Lista localidades filtradas por ID de provincia")
    public ResponseEntity<List<Localidad>> listarLocalidadesPorProvincia(@PathVariable String idProvincia) {
        return ResponseEntity.ok(ubicacionService.obtenerLocalidadesPorProvincia(idProvincia));
    }
}
