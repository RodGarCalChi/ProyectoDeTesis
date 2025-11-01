package org.example.backend.repository;

import org.example.backend.entity.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, UUID> {
    
    Optional<Ubicacion> findByCodigo(String codigo);
}
