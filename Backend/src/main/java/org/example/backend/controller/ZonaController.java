package org.example.backend.controller;

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
    public ResponseEntity<Map<String, Object>> crear(@RequestBody Map<String, Object> request) {
        try {
            String nombre = (String) request.get("nombre");
            String tipo = (String) request.get("tipo");
            String almacenIdStr = (String) request.get("almacenId");
            
            UUID almacenId = UUID.fromString(almacenIdStr);
            Almacen almacen = almacenRepository.findById(almacenId)
                .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));
            
            Zona zona = new Zona(nombre, tipo, almacen);
            Zona nuevaZona = zonaRepository.save(zona);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "message", "Zona creada exitosamente",
                "data", nuevaZona
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
}
