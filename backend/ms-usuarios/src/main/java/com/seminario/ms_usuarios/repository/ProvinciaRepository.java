package com.seminario.ms_usuarios.repository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.seminario.ms_usuarios.model.Provincia;

@Repository
public interface ProvinciaRepository extends JpaRepository<Provincia, String>{
    
}
