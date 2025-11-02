package org.example.backend.controller;

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
@CrossOrigin(origins = "*")
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
    public ResponseEntity<Map<String, Object>> crear(@RequestBody Map<String, Object> request) {
        try {
            String codigo = (String) request.get("codigo");
            Integer capacidad = (Integer) request.get("capacidad");
            Float tempMin = request.get("tempObjetivoMin") != null ? 
                ((Number) request.get("tempObjetivoMin")).floatValue() : null;
            Float tempMax = request.get("tempObjetivoMax") != null ? 
                ((Number) request.get("tempObjetivoMax")).floatValue() : null;
            Boolean disponible = (Boolean) request.getOrDefault("disponible", true);
            String zonaIdStr = (String) request.get("zonaId");
            
            UUID zonaId = UUID.fromString(zonaIdStr);
            Zona zona = zonaRepository.findById(zonaId)
                .orElseThrow(() -> new RuntimeException("Zona no encontrada"));
            
            Ubicacion ubicacion = new Ubicacion(codigo, capacidad, tempMin, tempMax, disponible, zona);
            Ubicacion nuevaUbicacion = ubicacionRepository.save(ubicacion);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "message", "Ubicación creada exitosamente",
                "data", nuevaUbicacion
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
}
