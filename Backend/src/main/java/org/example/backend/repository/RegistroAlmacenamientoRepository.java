package org.example.backend.repository;

import org.example.backend.entity.RegistroAlmacenamiento;
import org.example.backend.enumeraciones.EstadoAlmacenamiento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RegistroAlmacenamientoRepository extends JpaRepository<RegistroAlmacenamiento, UUID> {
    
    boolean existsByNumeroGuiaRemision(String numeroGuiaRemision);
    
    List<RegistroAlmacenamiento> findByEstado(EstadoAlmacenamiento estado);
    
    @Query("SELECT r FROM RegistroAlmacenamiento r WHERE " +
           "(:numeroGuia IS NULL OR r.numeroGuiaRemision LIKE %:numeroGuia%) AND " +
           "(:estado IS NULL OR r.estado = :estado) AND " +
           "(:clienteId IS NULL OR r.cliente.id = :clienteId) AND " +
           "(:operador IS NULL OR r.operadorResponsable LIKE %:operador%) AND " +
           "(:ubicacion IS NULL OR r.ubicacionAlmacen LIKE %:ubicacion%)")
    Page<RegistroAlmacenamiento> findRegistrosWithFilters(
            @Param("numeroGuia") String numeroGuia,
            @Param("estado") EstadoAlmacenamiento estado,
            @Param("clienteId") UUID clienteId,
            @Param("operador") String operador,
            @Param("ubicacion") String ubicacion,
            Pageable pageable);
    
    @Query("SELECT r FROM RegistroAlmacenamiento r WHERE r.estado = 'PENDIENTE_VERIFICACION'")
    List<RegistroAlmacenamiento> findRegistrosPendientesVerificacion();
    
    @Query("SELECT r FROM RegistroAlmacenamiento r JOIN r.detalles d WHERE d.requiereAtencionEspecial = true")
    List<RegistroAlmacenamiento> findRegistrosConAtencionEspecial();
    
    @Query("SELECT r FROM RegistroAlmacenamiento r WHERE DATE(r.fechaAlmacenamiento) = CURRENT_DATE")
    List<RegistroAlmacenamiento> findRegistrosDelDia();
    
    @Query("SELECT r.estado, COUNT(r) FROM RegistroAlmacenamiento r GROUP BY r.estado")
    List<Object[]> countRegistrosByEstado();
    
    @Query("SELECT c.razonSocial, COUNT(r) FROM RegistroAlmacenamiento r JOIN r.cliente c GROUP BY c.razonSocial")
    List<Object[]> countRegistrosByCliente();
}