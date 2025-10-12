package org.example.backend.repository;

import org.example.backend.entity.InspeccionCalidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InspeccionCalidadRepository extends JpaRepository<InspeccionCalidad, UUID> {
    // Métodos personalizados si son necesarios
}
