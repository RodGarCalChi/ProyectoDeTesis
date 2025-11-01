package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.dto.AprobarActaRequest;
import org.example.backend.dto.AsignarUbicacionRequest;
import org.example.backend.dto.InventarioClienteDTO;
import org.example.backend.service.InventarioClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "*")
public class InventarioClienteController {

    @Autowired
    private InventarioClienteService inventarioService;

    /**
     * FASE 2: Aprobar acta y registrar productos en inventario
     * POST /api/inventario/aprobar-acta/{recepcionId}
     */
    @PostMapping("/aprobar-acta/{recepcionId}")
    public ResponseEntity<?> aprobarActaYRegistrar(
            @PathVariable UUID recepcionId,
            @Valid @RequestBody AprobarActaRequest request) {
        try {
            List<InventarioClienteDTO> inventarios = inventarioService.aprobarActaYRegistrar(
                    recepcionId,
                    request.getUsuarioNombre(),
                    request.getObservaciones()
            );

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Acta aprobada y " + inventarios.size() + " producto(s) registrado(s) en inventario",
                    "data", inventarios
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * FASE 3: Asignar ubicación física a un producto
     * PUT /api/inventario/{inventarioId}/asignar-ubicacion
     */
    @PutMapping("/{inventarioId}/asignar-ubicacion")
    public ResponseEntity<?> asignarUbicacion(
            @PathVariable UUID inventarioId,
            @Valid @RequestBody AsignarUbicacionRequest request) {
        try {
            InventarioClienteDTO inventario = inventarioService.asignarUbicacion(
                    inventarioId,
                    request.getUbicacionId(),
                    request.getCodigoBarras(),
                    request.getUsuarioNombre(),
                    request.getObservaciones()
            );

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Ubicación asignada exitosamente",
                    "data", inventario
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * Obtener productos pendientes de ubicación
     * GET /api/inventario/pendientes-ubicacion
     */
    @GetMapping("/pendientes-ubicacion")
    public ResponseEntity<?> obtenerPendientesUbicacion() {
        try {
            List<InventarioClienteDTO> inventarios = inventarioService.obtenerPendientesUbicacion();

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", inventarios,
                    "total", inventarios.size()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * Obtener inventario por cliente
     * GET /api/inventario/cliente/{clienteId}
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<?> obtenerPorCliente(@PathVariable UUID clienteId) {
        try {
            List<InventarioClienteDTO> inventarios = inventarioService.obtenerPorCliente(clienteId);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", inventarios,
                    "total", inventarios.size()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * Obtener inventario por ID
     * GET /api/inventario/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable UUID id) {
        try {
            InventarioClienteDTO inventario = inventarioService.obtenerPorId(id);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", inventario
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}
