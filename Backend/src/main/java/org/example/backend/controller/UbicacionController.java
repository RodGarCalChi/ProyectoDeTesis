package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.dto.UbicacionRequest;
import org.example.backend.entity.Ubicacion;
import org.example.backend.entity.Zona;
import org.example.backend.repository.UbicacionRepository;
import org.example.backend.repository.ZonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/ubicaciones")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "false")
public class UbicacionController {
    
    @Autowired
    private UbicacionRepository ubicacionRepository;
    
    @Autowired
    private ZonaRepository zonaRepository;
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarUbicaciones() {
        List<Ubicacion> ubicaciones = ubicacionRepository.findAll();
        return ResponseEntity.ok(Map.of(
            "success", true,
            "data", ubicaciones
        ));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable UUID id) {
        return ubicacionRepository.findById(id)
            .map(ubicacion -> ResponseEntity.ok(Map.of(
                "success", true,
                "data", ubicacion
            )))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/zona/{zonaId}")
    public ResponseEntity<Map<String, Object>> listarPorZona(@PathVariable UUID zonaId) {
        List<Ubicacion> ubicaciones = ubicacionRepository.findByZonaId(zonaId);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "data", ubicaciones
        ));
    }
    
    @GetMapping("/disponibles")
    public ResponseEntity<Map<String, Object>> listarDisponibles() {
        List<Ubicacion> ubicaciones = ubicacionRepository.findByDisponible(true);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "data", ubicaciones
        ));
    }
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody UbicacionRequest request) {
        try {
            // Buscar zona
            Zona zona = zonaRepository.findById(request.getZonaId())
                .orElseThrow(() -> new RuntimeException("Zona no encontrada"));
            
            // Crear ubicación
            Ubicacion ubicacion = new Ubicacion();
            ubicacion.setCodigo(request.getCodigo());
            ubicacion.setCapacidad(request.getCapacidadMaxima());
            ubicacion.setDisponible(request.getDisponible());
            ubicacion.setZona(zona);
            
            Ubicacion nuevaUbicacion = ubicacionRepository.save(ubicacion);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "message", "Ubicación creada exitosamente",
                "data", nuevaUbicacion
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error al crear ubicación: " + e.getMessage()
            ));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable UUID id, @Valid @RequestBody UbicacionRequest request) {
        try {
            return ubicacionRepository.findById(id)
                .map(ubicacionExistente -> {
                    ubicacionExistente.setCodigo(request.getCodigo());
                    ubicacionExistente.setCapacidad(request.getCapacidadMaxima());
                    ubicacionExistente.setDisponible(request.getDisponible());
                    
                    // Actualizar zona si cambió
                    if (request.getZonaId() != null) {
                        Zona zona = zonaRepository.findById(request.getZonaId())
                            .orElseThrow(() -> new RuntimeException("Zona no encontrada"));
                        ubicacionExistente.setZona(zona);
                    }
                    
                    Ubicacion actualizada = ubicacionRepository.save(ubicacionExistente);
                    return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Ubicación actualizada exitosamente",
                        "data", actualizada
                    ));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", "Ubicación no encontrada"
                )));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error al actualizar ubicación: " + e.getMessage()
            ));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable UUID id) {
        return ubicacionRepository.findById(id)
            .map(ubicacion -> {
                ubicacionRepository.delete(ubicacion);
                Map<String, Object> response = Map.of(
                    "success", true,
                    "message", "Ubicación eliminada exitosamente"
                );
                return ResponseEntity.ok(response);
            })
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "success", false,
                "message", "Ubicación no encontrada"
            )));
    }
}
