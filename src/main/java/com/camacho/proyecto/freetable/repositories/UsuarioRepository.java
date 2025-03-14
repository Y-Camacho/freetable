package com.camacho.proyecto.freetable.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.camacho.proyecto.freetable.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    Optional<Usuario> findByUsername(String username);
}