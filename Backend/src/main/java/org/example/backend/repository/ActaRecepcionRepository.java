package org.example.backend.repository;

import org.example.backend.entity.ActaRecepcion;
import org.example.backend.enumeraciones.EstadoDocumento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ActaRecepcionRepository extends JpaRepository<ActaRecepcion, UUID> {
    
    // Buscar por número de acta
    Optional<ActaRecepcion> findByNumeroActa(String numeroActa);
    
    // Verificar si existe un número de acta
    boolean existsByNumeroActa(String numeroActa);
    
    // Buscar por cliente
    List<ActaRecepcion> findByClienteId(UUID clienteId);
    
    // Buscar por estado
    List<ActaRecepcion> findByEstado(EstadoDocumento estado);
    
    // Buscar por responsable de recepción
    List<ActaRecepcion> findByResponsableRecepcion(String responsableRecepcion);
    
    // Buscar por rango de fechas
    List<ActaRecepcion> findByFechaRecepcionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    // Buscar por cliente y estado
    List<ActaRecepcion> findByClienteIdAndEstado(UUID clienteId, EstadoDocumento estado);
    
    // Buscar por cliente y rango de fechas
    List<ActaRecepcion> findByClienteIdAndFechaRecepcionBetween(UUID clienteId, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    // Búsqueda con filtros múltiples
    @Query("SELECT a FROM ActaRecepcion a WHERE " +
           "(:numeroActa IS NULL OR LOWER(a.numeroActa) LIKE LOWER(CONCAT('%', :numeroActa, '%'))) AND " +
           "(:clienteId IS NULL OR a.cliente.id = :clienteId) AND " +
           "(:estado IS NULL OR a.estado = :estado) AND " +
           "(:responsable IS NULL OR LOWER(a.responsableRecepcion) LIKE LOWER(CONCAT('%', :responsable, '%'))) AND " +
           "(:fechaInicio IS NULL OR a.fechaRecepcion >= :fechaInicio) AND " +
           "(:fechaFin IS NULL OR a.fechaRecepcion <= :fechaFin)")
    Page<ActaRecepcion> findWithFilters(@Param("numeroActa") String numeroActa,
                                       @Param("clienteId") UUID clienteId,
                                       @Param("estado") EstadoDocumento estado,
                                       @Param("responsable") String responsable,
                                       @Param("fechaInicio") LocalDateTime fechaInicio,
                                       @Param("fechaFin") LocalDateTime fechaFin,
                                       Pageable pageable);
    
    // Obtener actas por cliente con paginación
    Page<ActaRecepcion> findByClienteId(UUID clienteId, Pageable pageable);
    
    // Obtener actas por estado con paginación
    Page<ActaRecepcion> findByEstado(EstadoDocumento estado, Pageable pageable);
    
    // Contar actas por estado
    @Query("SELECT a.estado, COUNT(a) FROM ActaRecepcion a GROUP BY a.estado")
    List<Object[]> countByEstado();
    
    // Contar actas por cliente
    @Query("SELECT c.razonSocial, COUNT(a) FROM ActaRecepcion a JOIN a.cliente c GROUP BY c.razonSocial")
    List<Object[]> countByCliente();
    
    // Obtener actas recientes
    @Query("SELECT a FROM ActaRecepcion a ORDER BY a.fechaCreacion DESC")
    List<ActaRecepcion> findRecentActas(Pageable pageable);
    
    // Buscar actas pendientes de aprobación
    @Query("SELECT a FROM ActaRecepcion a WHERE a.estado IN ('PENDIENTE', 'PROCESADO') ORDER BY a.fechaRecepcion ASC")
    List<ActaRecepcion> findPendingApproval();
    
    // Obtener actas por mes y año
    @Query("SELECT a FROM ActaRecepcion a WHERE YEAR(a.fechaRecepcion) = :year AND MONTH(a.fechaRecepcion) = :month")
    List<ActaRecepcion> findByYearAndMonth(@Param("year") int year, @Param("month") int month);
}