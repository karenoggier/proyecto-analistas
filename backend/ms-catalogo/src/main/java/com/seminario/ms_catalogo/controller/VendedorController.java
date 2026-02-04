package com.seminario.ms_catalogo.controller;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seminario.ms_catalogo.dto.ProductoRequestDTO;
import com.seminario.ms_catalogo.dto.ProductoResponseDTO;
import com.seminario.ms_catalogo.dto.VendedorRequestDTO;
import com.seminario.ms_catalogo.dto.VendedorResponseDTO;
import com.seminario.ms_catalogo.dto.eventos_ms_usuarios.VendedorRegistradoEvent;
import com.seminario.ms_catalogo.service.VendedorService;

import org.springframework.security.core.Authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/vendedores")
@RequiredArgsConstructor
@Slf4j
public class VendedorController {
    private final VendedorService vendedorService;

    @Value("${jwt.secret}")
    private String secretKey;

    @PostMapping("/agregar-producto")
    public ResponseEntity<ProductoResponseDTO> agregarProducto(@RequestBody ProductoRequestDTO productoRequestDTO, 
    @RequestParam String vendedorId) {
        return vendedorService.agregarProducto(productoRequestDTO, vendedorId);
    }

    @GetMapping("/obtener-vendedor-por-usuarioId")
    public ResponseEntity<VendedorResponseDTO> obtenerVendedorPorUsuarioId(@RequestParam String usuarioId) {
        return vendedorService.obtenerVendedorPorUsuarioId(usuarioId);
    }

    @PutMapping("/actualizar") 
    public ResponseEntity<VendedorResponseDTO> updateVendedor(
            @RequestBody VendedorRequestDTO vendedorRequestDTO,
            Authentication authentication) { 
        
        String email = authentication.getName();
        
        return vendedorService.updateVendedor(vendedorRequestDTO, email);
    }

    //Endpoint HTTP desde ms-usuarios
    @PostMapping("/registrar")
    public ResponseEntity<Void> registrarVendedor(@RequestBody VendedorRegistradoEvent evento) {
            vendedorService.recibirRegistroVendedor(evento);
            return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/perfil")
    public ResponseEntity<?> obtenerPerfil(Authentication authentication) {
       String usuarioIdentity = authentication.getName();
       return ResponseEntity.ok(vendedorService.buscarVendedorPorEmail(usuarioIdentity));
       
        /* try {
            if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o ausente");
            }
            String token = tokenHeader.substring(7);

            String usuarioIdentity = extraerUsuarioDelToken(token);

            return ResponseEntity.ok(vendedorService.buscarVendedorPorEmail(usuarioIdentity));

        } catch (Exception e) {
            log.error("Error al procesar el token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido o expirado");
        }*/
    }
/* 
    private String extraerUsuarioDelToken(String token) {
        return extraerClaims(token).getSubject(); 
    }

    private Claims extraerClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
*/
}
