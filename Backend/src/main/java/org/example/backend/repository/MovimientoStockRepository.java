package org.example.backend.repository;

import org.example.backend.entity.MovimientoStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientoStockRepository extends JpaRepository<MovimientoStock, Long> {
    // Métodos personalizados si son necesarios
}
