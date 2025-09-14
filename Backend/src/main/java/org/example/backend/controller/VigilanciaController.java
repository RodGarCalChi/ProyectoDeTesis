package org.example.backend.controller;

import org.example.backend.entity.Vigilancia;
import org.example.backend.repository.VigilanciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vigilancias")
public class VigilanciaController {
    @Autowired
    private VigilanciaRepository vigilanciaRepository;

    @GetMapping
    public List<Vigilancia> getAll() {
        return vigilanciaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vigilancia> getById(@PathVariable Long id) {
        Optional<Vigilancia> vigilancia = vigilanciaRepository.findById(id);
        return vigilancia.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Vigilancia create(@RequestBody Vigilancia vigilancia) {
        return vigilanciaRepository.save(vigilancia);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vigilancia> update(@PathVariable Long id, @RequestBody Vigilancia vigilanciaDetails) {
        return vigilanciaRepository.findById(id)
                .map(vigilancia -> {
                    vigilancia.setNombre(vigilanciaDetails.getNombre());
                    vigilancia.setApellido(vigilanciaDetails.getApellido());
                    vigilancia.setEmail(vigilanciaDetails.getEmail());
                    vigilancia.setTelefono(vigilanciaDetails.getTelefono());
                    vigilancia.setRol(vigilanciaDetails.getRol());
                    vigilancia.setTurno(vigilanciaDetails.getTurno());
                    return ResponseEntity.ok(vigilanciaRepository.save(vigilancia));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return vigilanciaRepository.findById(id)
                .map(vigilancia -> {
                    vigilanciaRepository.delete(vigilancia);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

