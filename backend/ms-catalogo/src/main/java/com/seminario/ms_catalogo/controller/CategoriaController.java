package com.seminario.ms_catalogo.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_catalogo.model.Categoria;
import com.seminario.ms_catalogo.model.Subcategoria;

import io.swagger.v3.oas.annotations.Operation;



@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @GetMapping
    @Operation(summary = "Obtiene las categorias con sus subcategorias")
    public ResponseEntity<Map<String, List<String>>> obtenerCategorias() {
        Map<String, List<String>> mapaCategorias = new HashMap<>();

        for (Categoria cat : Categoria.values()) {
            List<String> subcategorias = Arrays.stream(Subcategoria.values())
                    .filter(sub -> sub.getCategoriaPadre().equals(cat))
                    .map(Enum::name) 
                    .collect(Collectors.toList());

            mapaCategorias.put(cat.name(), subcategorias);
        }

        return ResponseEntity.ok(mapaCategorias);
    }
    
}
