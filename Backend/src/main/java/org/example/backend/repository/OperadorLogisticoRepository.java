package org.example.backend.repository;

import org.example.backend.entity.OperadorLogistico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OperadorLogisticoRepository extends JpaRepository<OperadorLogistico, UUID> {
    Optional<OperadorLogistico> findByRuc(String ruc);
}
