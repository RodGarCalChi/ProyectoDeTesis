package org.example.backend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.backend.dto.ZonaDTO;
import org.example.backend.dto.ZonaRequest;
import org.example.backend.entity.Almacen;
import org.example.backend.entity.Zona;
import org.example.backend.repository.AlmacenRepository;
import org.example.backend.repository.ZonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/zonas")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "false")
@Tag(name = "3. Zonas", description = "Gestión de zonas de almacenamiento (ULT, Congelado, Refrigerado, Seco)")
public class ZonaController {
    
    @Autowired
    private ZonaRepository zonaRepository;
    
    @Autowired
    private AlmacenRepository almacenRepository;
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarZonas() {
        List<Zona> zonas = zonaRepository.findAll();
        return ResponseEntity.ok(Map.of(
            "success", true,
            "data", zonas
        ));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable UUID id) {
        return zonaRepository.findById(id)
            .map(zona -> ResponseEntity.ok(Map.of(
                "success", true,
                "data", zona
            )))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/almacen/{almacenId}")
    public ResponseEntity<Map<String, Object>> listarPorAlmacen(@PathVariable UUID almacenId) {
        List<Zona> zonas = zonaRepository.findByAlmacenId(almacenId);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "data", zonas
        ));
    }
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody ZonaRequest request) {
        try {
            // Buscar almacén
            Almacen almacen = almacenRepository.findById(request.getAlmacenId())
                .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));
            
            // Crear zona
            Zona zona = new Zona(request.getNombre(), request.getTipo(), almacen);
            
            Zona nuevaZona = zonaRepository.save(zona);
            ZonaDTO zonaDTO = new ZonaDTO(nuevaZona);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "message", "Zona creada exitosamente",
                "data", zonaDTO
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error al crear zona: " + e.getMessage()
            ));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable UUID id, @Valid @RequestBody ZonaRequest request) {
        try {
            return zonaRepository.findById(id)
                .map(zonaExistente -> {
                    zonaExistente.setNombre(request.getNombre());
                    zonaExistente.setTipo(request.getTipo());
                    
                    // Actualizar almacén si cambió
                    if (request.getAlmacenId() != null) {
                        Almacen almacen = almacenRepository.findById(request.getAlmacenId())
                            .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));
                        zonaExistente.setAlmacen(almacen);
                    }
                    
                    Zona actualizada = zonaRepository.save(zonaExistente);
                    return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Zona actualizada exitosamente",
                        "data", actualizada
                    ));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", "Zona no encontrada"
                )));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error al actualizar zona: " + e.getMessage()
            ));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable UUID id) {
        return zonaRepository.findById(id)
            .map(zona -> {
                zonaRepository.delete(zona);
                Map<String, Object> response = Map.of(
                    "success", true,
                    "message", "Zona eliminada exitosamente"
                );
                return ResponseEntity.ok(response);
            })
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "success", false,
                "message", "Zona no encontrada"
            )));
    }
}
