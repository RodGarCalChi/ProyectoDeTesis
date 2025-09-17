package org.example.backend.repository;

import org.example.backend.entity.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoteRepository extends JpaRepository<Lote, Long> {
    // Métodos personalizados si son necesarios
}
