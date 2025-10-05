package org.example.backend.repository;

import org.example.backend.entity.Usuario;
import org.example.backend.enumeraciones.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    
    Optional<Usuario> findByEmail(String email);
    
    List<Usuario> findByRol(Rol rol);
    
    List<Usuario> findByActivoTrue();
    
    boolean existsByEmail(String email);
    
    boolean existsByDocumento(String documento);
}