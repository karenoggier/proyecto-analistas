package com.seminario.ms_usuarios.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.seminario.ms_usuarios.model.Localidad;
import java.util.List;

@Repository
public interface LocalidadRepository extends JpaRepository<Localidad, String>{
    List<Localidad> findByProvinciaId(String provinciaId);
}
