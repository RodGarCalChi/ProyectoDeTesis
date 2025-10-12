package org.example.backend.repository;

import org.example.backend.entity.Lote;
import org.example.backend.enumeraciones.EstadoLote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LoteRepository extends JpaRepository<Lote, UUID> {
    
    // Buscar por código de lote
    Optional<Lote> findByCodigoLote(String codigoLote);
    
    // Verificar si existe por código de lote
    boolean existsByCodigoLote(String codigoLote);
    
    // Buscar por estado
    List<Lote> findByEstado(EstadoLote estado);
    
    // Buscar por producto
    @Query("SELECT l FROM Lote l WHERE l.producto.id = :productoId")
    List<Lote> findByProductoId(@Param("productoId") UUID productoId);
    
    // Buscar lotes próximos a vencer
    @Query("SELECT l FROM Lote l WHERE l.fechaVencimiento <= :fechaLimite AND l.estado = 'DISPONIBLE'")
    List<Lote> findLotesProximosAVencer(@Param("fechaLimite") LocalDate fechaLimite);
    
    // Buscar lotes vencidos
    @Query("SELECT l FROM Lote l WHERE l.fechaVencimiento < CURRENT_DATE AND l.estado = 'DISPONIBLE'")
    List<Lote> findLotesVencidos();
    
    // Buscar lotes por rango de fechas de vencimiento
    @Query("SELECT l FROM Lote l WHERE l.fechaVencimiento BETWEEN :fechaInicio AND :fechaFin")
    List<Lote> findByFechaVencimientoBetween(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );
    
    // Búsqueda avanzada con filtros
    @Query("SELECT l FROM Lote l WHERE " +
           "(:codigoLote IS NULL OR LOWER(l.codigoLote) LIKE LOWER(CONCAT('%', :codigoLote, '%'))) AND " +
           "(:estado IS NULL OR l.estado = :estado) AND " +
           "(:productoId IS NULL OR l.producto.id = :productoId)")
    Page<Lote> findLotesWithFilters(
            @Param("codigoLote") String codigoLote,
            @Param("estado") EstadoLote estado,
            @Param("productoId") UUID productoId,
            Pageable pageable
    );
    
    // Contar lotes por estado
    @Query("SELECT l.estado, COUNT(l) FROM Lote l GROUP BY l.estado")
    List<Object[]> countLotesByEstado();
    
    // Lotes disponibles para un producto específico
    @Query("SELECT l FROM Lote l WHERE l.producto.id = :productoId AND l.estado = 'DISPONIBLE' " +
           "ORDER BY l.fechaVencimiento ASC")
    List<Lote> findLotesDisponiblesByProducto(@Param("productoId") UUID productoId);
}