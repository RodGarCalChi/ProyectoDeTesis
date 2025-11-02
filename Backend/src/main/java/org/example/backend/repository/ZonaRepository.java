package org.example.backend.repository;

import org.example.backend.entity.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ZonaRepository extends JpaRepository<Zona, UUID> {
    @Query("SELECT z FROM Zona z WHERE z.almacen.id = :almacenId")
    List<Zona> findByAlmacenId(@Param("almacenId") UUID almacenId);
}
