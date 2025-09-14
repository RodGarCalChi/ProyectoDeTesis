package org.example.backend.repository;

import org.example.backend.entity.Vigilancia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VigilanciaRepository extends JpaRepository<Vigilancia, Long> {
    // Puedes agregar métodos personalizados aquí si lo necesitas
}

