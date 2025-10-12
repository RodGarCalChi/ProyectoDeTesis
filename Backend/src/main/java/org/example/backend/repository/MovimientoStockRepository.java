package org.example.backend.repository;

import org.example.backend.entity.MovimientoStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MovimientoStockRepository extends JpaRepository<MovimientoStock, UUID> {
    // Métodos personalizados si son necesarios
}
