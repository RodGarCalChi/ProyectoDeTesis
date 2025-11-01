package org.example.backend.repository;

import org.example.backend.entity.HistorialUbicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HistorialUbicacionRepository extends JpaRepository<HistorialUbicacion, UUID> {
    
    // Buscar historial por inventario cliente
    List<HistorialUbicacion> findByInventarioClienteIdOrderByFechaMovimientoDesc(UUID inventarioClienteId);
    
    // Buscar por usuario
    List<HistorialUbicacion> findByUsuarioIdOrderByFechaMovimientoDesc(UUID usuarioId);
}
