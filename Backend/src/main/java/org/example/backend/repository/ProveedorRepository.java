package org.example.backend.repository;

import org.example.backend.entity.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, UUID> {
    
    // Buscar por RUC
    Optional<Proveedor> findByRuc(String ruc);
    
    // Verificar si existe por RUC
    boolean existsByRuc(String ruc);
    
    // Buscar por email
    Optional<Proveedor> findByEmail(String email);
    
    // Buscar proveedores habilitados
    List<Proveedor> findByHabilitadoTrue();
    
    // Buscar proveedores deshabilitados
    List<Proveedor> findByHabilitadoFalse();
    
    // Buscar por razón social (contiene)
    List<Proveedor> findByRazonSocialContainingIgnoreCase(String razonSocial);
    
    // Buscar por contacto
    List<Proveedor> findByContactoContainingIgnoreCase(String contacto);
    
    // Búsqueda avanzada con filtros
    @Query("SELECT p FROM Proveedor p WHERE " +
           "(:razonSocial IS NULL OR LOWER(p.razonSocial) LIKE LOWER(CONCAT('%', :razonSocial, '%'))) AND " +
           "(:ruc IS NULL OR p.ruc LIKE CONCAT('%', :ruc, '%')) AND " +
           "(:contacto IS NULL OR LOWER(p.contacto) LIKE LOWER(CONCAT('%', :contacto, '%'))) AND " +
           "(:habilitado IS NULL OR p.habilitado = :habilitado)")
    Page<Proveedor> findProveedoresWithFilters(
            @Param("razonSocial") String razonSocial,
            @Param("ruc") String ruc,
            @Param("contacto") String contacto,
            @Param("habilitado") Boolean habilitado,
            Pageable pageable
    );
    
    // Proveedores con email válido
    @Query("SELECT p FROM Proveedor p WHERE p.email IS NOT NULL AND p.email != '' AND p.habilitado = true")
    List<Proveedor> findProveedoresWithEmail();
    
    // Contar proveedores habilitados/deshabilitados
    @Query("SELECT p.habilitado, COUNT(p) FROM Proveedor p GROUP BY p.habilitado")
    List<Object[]> countProveedoresByEstado();
}