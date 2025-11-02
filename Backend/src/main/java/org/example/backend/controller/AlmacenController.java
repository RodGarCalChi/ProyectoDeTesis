package org.example.backend.controller;

import org.example.backend.entity.Almacen;
import org.example.backend.repository.AlmacenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/almacenes")
@CrossOrigin(origins = "*")
public class AlmacenController {

    @Autowired
    private AlmacenRepository almacenRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> listarAlmacenes() {
        try {
            List<Almacen> almacenes = almacenRepository.findAll();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", almacenes));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error al listar almacenes: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable UUID id) {
        return almacenRepository.findById(id)
                .map(almacen -> ResponseEntity.ok(Map.of(
                        "success", true,
                        "data", almacen)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody Almacen almacen) {
        try {
            Almacen nuevoAlmacen = almacenRepository.save(almacen);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "Almacén creado exitosamente",
                    "data", nuevoAlmacen));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error al crear almacén: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable UUID id, @Valid @RequestBody Almacen almacen) {
        return almacenRepository.findById(id)
                .map(almacenExistente -> {
                    almacenExistente.setNombre(almacen.getNombre());
                    almacenExistente.setDireccion(almacen.getDireccion());
                    almacenExistente.setTieneAreaControlados(almacen.getTieneAreaControlados());
                    Almacen actualizado = almacenRepository.save(almacenExistente);
                    return ResponseEntity.ok(Map.of(
                            "success", true,
                            "message", "Almacén actualizado exitosamente",
                            "data", actualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
