package org.example.backend.controller;

import org.example.backend.entity.Recepcion;
import org.example.backend.repository.RecepcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recepciones")
public class RecepcionController {
    @Autowired
    private RecepcionRepository recepcionRepository;

    @GetMapping
    public List<Recepcion> getAll() {
        return recepcionRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recepcion> getById(@PathVariable Long id) {
        Optional<Recepcion> recepcion = recepcionRepository.findById(id);
        return recepcion.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Recepcion create(@RequestBody Recepcion recepcion) {
        return recepcionRepository.save(recepcion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recepcion> update(@PathVariable Long id, @RequestBody Recepcion recepcionDetails) {
        return recepcionRepository.findById(id)
                .map(recepcion -> {
                    recepcion.setNombre(recepcionDetails.getNombre());
                    recepcion.setApellido(recepcionDetails.getApellido());
                    recepcion.setEmail(recepcionDetails.getEmail());
                    recepcion.setTelefono(recepcionDetails.getTelefono());
                    recepcion.setRol(recepcionDetails.getRol());
                    recepcion.setAreaAsignada(recepcionDetails.getAreaAsignada());
                    return ResponseEntity.ok(recepcionRepository.save(recepcion));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return recepcionRepository.findById(id)
                .map(recepcion -> {
                    recepcionRepository.delete(recepcion);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

