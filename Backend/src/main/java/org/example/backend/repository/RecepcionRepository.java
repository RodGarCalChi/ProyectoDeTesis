package org.example.backend.repository;

import org.example.backend.entity.Recepcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecepcionRepository extends JpaRepository<Recepcion, Long> {
    // Puedes agregar métodos personalizados aquí si lo necesitas
}

