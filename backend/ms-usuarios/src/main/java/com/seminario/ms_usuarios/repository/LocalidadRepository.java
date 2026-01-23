package com.seminario.ms_usuarios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seminario.ms_usuarios.model.Localidad;

@Repository
public interface LocalidadRepository extends JpaRepository<Localidad, String>{
    List<Localidad> findByProvinciaId(String provinciaId);

    Localidad findByNombre(String localidad);
}
