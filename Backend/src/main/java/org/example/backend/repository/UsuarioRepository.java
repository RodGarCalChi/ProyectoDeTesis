package org.example.backend.repository;

import org.example.backend.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Buscar por email (único)
    Optional<Usuario> findByEmail(String email);
    
    // Verificar si existe un email
    boolean existsByEmail(String email);
    
    // Buscar usuarios activos
    List<Usuario> findByActivoTrue();
    
    // Buscar usuarios por rol
    List<Usuario> findByRol(String rol);
    
    // Buscar usuarios activos por rol
    List<Usuario> findByRolAndActivoTrue(String rol);
    
    // Buscar por nombre o apellido (búsqueda parcial)
    @Query("SELECT u FROM Usuario u WHERE " +
           "(LOWER(u.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(u.apellido) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :busqueda, '%'))) AND " +
           "u.activo = true")
    List<Usuario> buscarUsuarios(@Param("busqueda") String busqueda);
    
    // Buscar usuarios con paginación
    Page<Usuario> findByActivoTrue(Pageable pageable);
    
    // Buscar usuarios por rol con paginación
    Page<Usuario> findByRolAndActivoTrue(String rol, Pageable pageable);
    
    // Contar usuarios activos
    long countByActivoTrue();
    
    // Contar usuarios por rol
    long countByRol(String rol);
    
    // Buscar usuarios creados en un rango de fechas
    @Query("SELECT u FROM Usuario u WHERE u.fechaCreacion BETWEEN :fechaInicio AND :fechaFin")
    List<Usuario> findByFechaCreacionBetween(
        @Param("fechaInicio") java.time.LocalDateTime fechaInicio,
        @Param("fechaFin") java.time.LocalDateTime fechaFin
    );
}