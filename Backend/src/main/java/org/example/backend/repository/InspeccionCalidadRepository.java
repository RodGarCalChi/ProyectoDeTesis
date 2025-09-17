package org.example.backend.repository;

import org.example.backend.entity.InspeccionCalidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InspeccionCalidadRepository extends JpaRepository<InspeccionCalidad, Long> {
    // Métodos personalizados si son necesarios
}
