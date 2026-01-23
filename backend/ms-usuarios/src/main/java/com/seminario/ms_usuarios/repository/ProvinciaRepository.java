package com.seminario.ms_usuarios.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seminario.ms_usuarios.model.Provincia;

@Repository
public interface ProvinciaRepository extends JpaRepository<Provincia, String>{
    Provincia findByNombre(String nombre);
    
}
