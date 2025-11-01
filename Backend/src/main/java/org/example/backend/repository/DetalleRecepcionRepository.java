package org.example.backend.repository;

import org.example.backend.entity.DetalleRecepcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DetalleRecepcionRepository extends JpaRepository<DetalleRecepcion, UUID> {
    
    List<DetalleRecepcion> findByRecepcionId(UUID recepcionId);
}
