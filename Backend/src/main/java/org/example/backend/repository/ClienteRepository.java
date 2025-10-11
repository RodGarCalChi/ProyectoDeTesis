package org.example.backend.repository;

import org.example.backend.entity.Cliente;
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
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    
    // Buscar por RUC/DNI
    Optional<Cliente> findByRucDni(String rucDni);
    
    // Verificar si existe por RUC/DNI
    boolean existsByRucDni(String rucDni);
    
    // Buscar por email
    Optional<Cliente> findByEmail(String email);
    
    // Buscar clientes activos
    List<Cliente> findByActivoTrue();
    
    // Buscar clientes inactivos
    List<Cliente> findByActivoFalse();
    
    // Buscar por tipo de cliente
    List<Cliente> findByTipoCliente(String tipoCliente);
    
    // Buscar por distrito
    List<Cliente> findByDistrito(String distrito);
    
    // Buscar por razón social (contiene)
    List<Cliente> findByRazonSocialContainingIgnoreCase(String razonSocial);
    
    // Búsqueda avanzada con paginación
    @Query("SELECT c FROM Cliente c WHERE " +
           "(:razonSocial IS NULL OR LOWER(c.razonSocial) LIKE LOWER(CONCAT('%', :razonSocial, '%'))) AND " +
           "(:rucDni IS NULL OR c.rucDni LIKE CONCAT('%', :rucDni, '%')) AND " +
           "(:distrito IS NULL OR LOWER(c.distrito) LIKE LOWER(CONCAT('%', :distrito, '%'))) AND " +
           "(:tipoCliente IS NULL OR c.tipoCliente = :tipoCliente) AND " +
           "(:activo IS NULL OR c.activo = :activo)")
    Page<Cliente> findClientesWithFilters(
            @Param("razonSocial") String razonSocial,
            @Param("rucDni") String rucDni,
            @Param("distrito") String distrito,
            @Param("tipoCliente") String tipoCliente,
            @Param("activo") Boolean activo,
            Pageable pageable
    );
    
    // Contar clientes por tipo
    @Query("SELECT c.tipoCliente, COUNT(c) FROM Cliente c WHERE c.activo = true GROUP BY c.tipoCliente")
    List<Object[]> countClientesByTipo();
    
    // Contar clientes por distrito
    @Query("SELECT c.distrito, COUNT(c) FROM Cliente c WHERE c.activo = true GROUP BY c.distrito")
    List<Object[]> countClientesByDistrito();
    
    // Clientes con email válido
    @Query("SELECT c FROM Cliente c WHERE c.email IS NOT NULL AND c.email != '' AND c.activo = true")
    List<Cliente> findClientesWithEmail();
}