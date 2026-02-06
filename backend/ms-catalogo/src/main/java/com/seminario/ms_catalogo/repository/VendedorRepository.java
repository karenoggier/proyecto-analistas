package com.seminario.ms_catalogo.repository;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.seminario.ms_catalogo.model.Vendedor;

@Repository 
public interface VendedorRepository extends MongoRepository<Vendedor, String> {

    Optional<Vendedor> findByUsuarioId(String usuarioId);

    Optional<Vendedor> findByEmail(String email);

    //ArrayList<Vendedor> findByDireccion_ProvinciaAndDireccion_Localidad(String provincia, String localidad);

}
