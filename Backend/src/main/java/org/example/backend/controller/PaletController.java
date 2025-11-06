package org.example.backend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.backend.dto.PaletRequest;
import org.example.backend.entity.Lote;
import org.example.backend.entity.Palet;
import org.example.backend.entity.Ubicacion;
import org.example.backend.repository.LoteRepository;
import org.example.backend.repository.PaletRepository;
import org.example.backend.repository.UbicacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/palets")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "false")
@Tag(name = "6. Palets", description = "Gestión de palets dentro de lotes")
public class PaletController {

    @Autowired
    private PaletRepository paletRepository;
    
    @Autowired
    private LoteRepository loteRepository;
    
    @Autowired
    private UbicacionRepository ubicacionRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> listarPalets() {
        try {
            List<Palet> palets = paletRepository.findAll();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", palets));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error al listar palets: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable UUID id) {
        return paletRepository.findById(id)
                .map(palet -> ResponseEntity.ok(Map.of(
                        "success", true,
                        "data", palet)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Palet no encontrado")));
    }

    @GetMapping("/lote/{loteId}")
    public ResponseEntity<Map<String, Object>> listarPorLote(@PathVariable UUID loteId) {
        try {
            List<Palet> palets = paletRepository.findByLoteId(loteId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", palets));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error al listar palets por lote: " + e.getMessage()));
        }
    }

    @GetMapping("/disponibles")
    public ResponseEntity<Map<String, Object>> listarDisponibles() {
        try {
            List<Palet> palets = paletRepository.findByDisponible(true);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", palets));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error al listar palets disponibles: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody PaletRequest request) {
        try {
            // Buscar lote
            Lote lote = loteRepository.findById(request.getLoteId())
                    .orElseThrow(() -> new RuntimeException("Lote no encontrado"));
            
            // Crear palet
            Palet palet = new Palet();
            palet.setCodigo(request.getCodigo());
            palet.setLote(lote);
            
            // Buscar ubicación si se proporcionó
            if (request.getUbicacionId() != null) {
                Ubicacion ubicacion = ubicacionRepository.findById(request.getUbicacionId())
                        .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));
                palet.setUbicacion(ubicacion);
            }
            
            palet.setCapacidadMaxima(request.getCapacidadMaxima());
            palet.setCajasActuales(request.getCajasActuales());
            palet.setPesoMaximoKg(request.getPesoMaximoKg());
            palet.setPesoActualKg(request.getPesoActualKg());
            palet.setDisponible(request.getDisponible());
            palet.setObservaciones(request.getObservaciones());
            
            Palet nuevoPalet = paletRepository.save(palet);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "Palet creado exitosamente",
                    "data", nuevoPalet));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error al crear palet: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable UUID id, @Valid @RequestBody Palet palet) {
        return paletRepository.findById(id)
                .map(paletExistente -> {
                    if (palet.getCodigo() != null) paletExistente.setCodigo(palet.getCodigo());
                    if (palet.getCapacidadMaxima() != null) paletExistente.setCapacidadMaxima(palet.getCapacidadMaxima());
                    if (palet.getCajasActuales() != null) paletExistente.setCajasActuales(palet.getCajasActuales());
                    if (palet.getPesoMaximoKg() != null) paletExistente.setPesoMaximoKg(palet.getPesoMaximoKg());
                    if (palet.getPesoActualKg() != null) paletExistente.setPesoActualKg(palet.getPesoActualKg());
                    if (palet.getDisponible() != null) paletExistente.setDisponible(palet.getDisponible());
                    if (palet.getObservaciones() != null) paletExistente.setObservaciones(palet.getObservaciones());
                    Palet actualizado = paletRepository.save(paletExistente);
                    return ResponseEntity.ok(Map.of(
                            "success", true,
                            "message", "Palet actualizado exitosamente",
                            "data", actualizado));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Palet no encontrado")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable UUID id) {
        return paletRepository.findById(id)
                .map(palet -> {
                    paletRepository.delete(palet);
                    Map<String, Object> response = Map.of(
                            "success", true,
                            "message", "Palet eliminado exitosamente");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Palet no encontrado")));
    }
}
