package com.seminario.ms_catalogo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.seminario.ms_catalogo.model.Vendedor;

@Repository 
public interface VendedorRepository extends MongoRepository<Vendedor, String> {

}
