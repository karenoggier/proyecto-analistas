package com.seminario.ms_usuarios.repository;

import com.seminario.ms_usuarios.model.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, String> {
    
}
