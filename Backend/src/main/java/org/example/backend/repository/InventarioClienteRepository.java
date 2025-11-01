package org.example.backend.repository;

import org.example.backend.entity.InventarioCliente;
import org.example.backend.enumeraciones.EstadoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InventarioClienteRepository extends JpaRepository<InventarioCliente, UUID> {

    // Buscar por cliente
    List<InventarioCliente> findByClienteId(UUID clienteId);

    // Buscar por estado
    List<InventarioCliente> findByEstado(EstadoInventario estado);

    // Buscar productos pendientes de ubicación
    @Query("SELECT i FROM InventarioCliente i WHERE i.estado = 'PENDIENTE_UBICACION' ORDER BY i.fechaIngreso ASC")
    List<InventarioCliente> findPendientesUbicacion();

    // Buscar por cliente y producto
    @Query("SELECT i FROM InventarioCliente i WHERE i.cliente.id = :clienteId AND i.producto.id = :productoId")
    List<InventarioCliente> findByClienteAndProducto(@Param("clienteId") UUID clienteId,
            @Param("productoId") UUID productoId);

    // Buscar por recepción
    List<InventarioCliente> findByRecepcionId(UUID recepcionId);

    // Buscar por ubicación
    List<InventarioCliente> findByUbicacionId(UUID ubicacionId);

    // Buscar productos próximos a vencer (próximos 30 días)
    @Query("SELECT i FROM InventarioCliente i WHERE i.fechaVencimiento <= DATEADD(day, 30, CURRENT_DATE()) " +
            "AND i.estado = org.example.backend.enumeraciones.EstadoInventario.ALMACENADO " +
            "ORDER BY i.fechaVencimiento ASC")
    List<InventarioCliente> findProximosAVencer();

    // Contar por estado
    @Query("SELECT i.estado, COUNT(i) FROM InventarioCliente i GROUP BY i.estado")
    List<Object[]> countByEstado();
}
