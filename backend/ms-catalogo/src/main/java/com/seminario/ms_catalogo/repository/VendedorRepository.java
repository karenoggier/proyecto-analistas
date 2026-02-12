package com.seminario.ms_catalogo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.seminario.ms_catalogo.model.Estado;
import com.seminario.ms_catalogo.model.Vendedor;

@Repository 
public interface VendedorRepository extends MongoRepository<Vendedor, String> {

    Optional<Vendedor> findByUsuarioId(String usuarioId);

    Optional<Vendedor> findByEmail(String email);

    List<Vendedor> findByEstadoAndDireccion_ProvinciaAndDireccion_Localidad(Estado activo, String provincia,
            String ciudad);

}
