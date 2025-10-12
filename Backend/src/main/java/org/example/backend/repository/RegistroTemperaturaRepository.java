package org.example.backend.repository;

import org.example.backend.entity.RegistroTemperatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RegistroTemperaturaRepository extends JpaRepository<RegistroTemperatura, UUID> {
    // Métodos personalizados si son necesarios
}
