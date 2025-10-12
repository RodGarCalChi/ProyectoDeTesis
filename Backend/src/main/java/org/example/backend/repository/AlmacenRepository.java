package org.example.backend.repository;

import org.example.backend.entity.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, UUID> {
}
