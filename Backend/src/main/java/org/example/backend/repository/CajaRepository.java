package org.example.backend.repository;

import org.example.backend.entity.Caja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CajaRepository extends JpaRepository<Caja, UUID> {
    Optional<Caja> findByCodigo(String codigo);
    List<Caja> findByPaletId(UUID paletId);
    List<Caja> findByClienteId(UUID clienteId);
    List<Caja> findByProductoId(UUID productoId);
    
    @Query("SELECT c FROM Caja c WHERE c.fechaVencimiento <= :fecha")
    List<Caja> findProximasAVencer(@Param("fecha") LocalDate fecha);
}
