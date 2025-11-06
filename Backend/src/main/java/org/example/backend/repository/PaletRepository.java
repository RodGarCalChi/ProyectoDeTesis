package org.example.backend.repository;

import org.example.backend.entity.Palet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaletRepository extends JpaRepository<Palet, UUID> {
    Optional<Palet> findByCodigo(String codigo);
    List<Palet> findByLoteId(UUID loteId);
    List<Palet> findByDisponible(Boolean disponible);
}
