package org.example.backend.repository;

import org.example.backend.entity.Producto;
import org.example.backend.enumeraciones.TipoProducto;
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
public interface ProductoRepository extends JpaRepository<Producto, UUID> {
    
    // Buscar por código SKU
    Optional<Producto> findByCodigoSKU(String codigoSKU);
    
    // Verificar si existe por código SKU
    boolean existsByCodigoSKU(String codigoSKU);
    
    // Buscar por tipo de producto
    List<Producto> findByTipo(TipoProducto tipo);
    
    // Buscar por nombre (contiene)
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar productos que requieren cadena de frío
    List<Producto> findByRequiereCadenaFrio(Boolean requiereCadenaFrio);
    
    // Buscar por registro sanitario
    Optional<Producto> findByRegistroSanitario(String registroSanitario);
    
    // Búsqueda avanzada con paginación
    @Query("SELECT p FROM Producto p WHERE " +
           "(:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:codigoSKU IS NULL OR LOWER(p.codigoSKU) LIKE LOWER(CONCAT('%', :codigoSKU, '%'))) AND " +
           "(:tipo IS NULL OR p.tipo = :tipo) AND " +
           "(:requiereCadenaFrio IS NULL OR p.requiereCadenaFrio = :requiereCadenaFrio)")
    Page<Producto> findProductosWithFilters(
            @Param("nombre") String nombre,
            @Param("codigoSKU") String codigoSKU,
            @Param("tipo") TipoProducto tipo,
            @Param("requiereCadenaFrio") Boolean requiereCadenaFrio,
            Pageable pageable
    );
    
    // Contar productos por tipo
    @Query("SELECT p.tipo, COUNT(p) FROM Producto p GROUP BY p.tipo")
    List<Object[]> countProductosByTipo();
    
    // Productos próximos a vencer (basado en vida útil)
    @Query("SELECT p FROM Producto p WHERE p.vidaUtilMeses IS NOT NULL AND p.vidaUtilMeses <= :mesesLimite")
    List<Producto> findProductosProximosAVencer(@Param("mesesLimite") Integer mesesLimite);
}