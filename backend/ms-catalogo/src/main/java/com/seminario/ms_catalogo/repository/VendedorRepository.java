package com.seminario.ms_catalogo.repository;

import java.util.ArrayList;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.seminario.ms_catalogo.model.Vendedor;

@Repository 
public interface VendedorRepository extends MongoRepository<Vendedor, String> {

    java.util.Optional<Vendedor> findByUsuarioId(String usuarioId);

    ArrayList<Vendedor> findByDireccion_ProvinciaAndDireccion_Localidad(String provincia, String localidad);

}
