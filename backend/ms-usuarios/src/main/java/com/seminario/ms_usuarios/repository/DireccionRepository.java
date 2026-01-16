package com.seminario.ms_usuarios.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seminario.ms_usuarios.model.Direccion;
import com.seminario.ms_usuarios.model.Usuario;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, String> {

    ArrayList<Direccion> findByUsuario(Usuario usuario);

    ArrayList<Direccion> findByLocalidadAndProvincia(String localidad, String provincia);

    Direccion findByUsuarioId (String idUsuario);

}
