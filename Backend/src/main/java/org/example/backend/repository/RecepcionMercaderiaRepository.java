package org.example.backend.repository;

import org.example.backend.entity.RecepcionMercaderia;
import org.example.backend.enumeraciones.EstadoRecepcion;
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
public interface RecepcionMercaderiaRepository extends JpaRepository<RecepcionMercaderia, UUID> {
    
    // Buscar por número de orden de compra
    Optional<RecepcionMercaderia> findByNumeroOrdenCompra(String numeroOrdenCompra);
    
    // Buscar por número de guía de remisión
    Optional<RecepcionMercaderia> findByNumeroGuiaRemision(String numeroGuiaRemision);
    
    // Verificar si existe por número de orden
    boolean existsByNumeroOrdenCompra(String numeroOrdenCompra);
    
    // Buscar por estado
    List<RecepcionMercaderia> findByEstado(EstadoRecepcion estado);
    
    // Buscar por cliente
    @Query("SELECT r FROM RecepcionMercaderia r WHERE r.cliente.id = :clienteId")
    List<RecepcionMercaderia> findByClienteId(@Param("clienteId") UUID clienteId);
    
    // Buscar por responsable de recepción
    List<RecepcionMercaderia> findByResponsableRecepcionContainingIgnoreCase(String responsable);
    
    // Buscar por rango de fechas
    @Query("SELECT r FROM RecepcionMercaderia r WHERE r.fechaRecepcion BETWEEN :fechaInicio AND :fechaFin")
    List<RecepcionMercaderia> findByFechaRecepcionBetween(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );
    
    // Búsqueda avanzada con filtros
    @Query("SELECT r FROM RecepcionMercaderia r WHERE " +
           "(:numeroOrden IS NULL OR LOWER(r.numeroOrdenCompra) LIKE LOWER(CONCAT('%', :numeroOrden, '%'))) AND " +
           "(:numeroGuia IS NULL OR LOWER(r.numeroGuiaRemision) LIKE LOWER(CONCAT('%', :numeroGuia, '%'))) AND " +
           "(:estado IS NULL OR r.estado = :estado) AND " +
           "(:clienteId IS NULL OR r.cliente.id = :clienteId) AND " +
           "(:responsable IS NULL OR LOWER(r.responsableRecepcion) LIKE LOWER(CONCAT('%', :responsable, '%')))")
    Page<RecepcionMercaderia> findRecepcionesWithFilters(
            @Param("numeroOrden") String numeroOrden,
            @Param("numeroGuia") String numeroGuia,
            @Param("estado") EstadoRecepcion estado,
            @Param("clienteId") UUID clienteId,
            @Param("responsable") String responsable,
            Pageable pageable
    );
    
    // Recepciones pendientes de verificación
    @Query("SELECT r FROM RecepcionMercaderia r WHERE r.estado IN ('PENDIENTE', 'EN_VERIFICACION') " +
           "ORDER BY r.fechaRecepcion ASC")
    List<RecepcionMercaderia> findRecepcionesPendientesVerificacion();
    
    // Recepciones en cuarentena
    @Query("SELECT r FROM RecepcionMercaderia r WHERE r.estado = 'EN_CUARENTENA' " +
           "ORDER BY r.fechaRecepcion ASC")
    List<RecepcionMercaderia> findRecepcionesEnCuarentena();
    
    // Estadísticas por estado
    @Query("SELECT r.estado, COUNT(r) FROM RecepcionMercaderia r GROUP BY r.estado")
    List<Object[]> countRecepcionesByEstado();
    
    // Recepciones del día
    @Query("SELECT r FROM RecepcionMercaderia r WHERE DATE(r.fechaRecepcion) = CURRENT_DATE " +
           "ORDER BY r.fechaRecepcion DESC")
    List<RecepcionMercaderia> findRecepcionesDelDia();
    
    // Recepciones que requieren aprobación de calidad
    @Query("SELECT r FROM RecepcionMercaderia r WHERE r.aprobadoPorCalidad = false " +
           "AND r.estado IN ('EN_VERIFICACION', 'EN_CUARENTENA') " +
           "ORDER BY r.fechaRecepcion ASC")
    List<RecepcionMercaderia> findRecepcionesPendientesAprobacionCalidad();
}