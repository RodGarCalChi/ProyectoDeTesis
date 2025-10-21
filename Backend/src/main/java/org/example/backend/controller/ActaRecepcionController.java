package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.dto.ActaRecepcionDTO;
import org.example.backend.enumeraciones.EstadoDocumento;
import org.example.backend.service.ActaRecepcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/actas-recepcion")
@CrossOrigin(origins = "*")
public class ActaRecepcionController {
    
    @Autowired
    private ActaRecepcionService actaRecepcionService;
    
    // Crear nueva acta de recepción
    @PostMapping
    public ResponseEntity<?> crearActaRecepcion(@Valid @RequestBody ActaRecepcionDTO actaDTO) {
        try {
            ActaRecepcionDTO acta = actaRecepcionService.crearActaRecepcion(actaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "Acta de recepción creada exitosamente",
                    "data", acta
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    // Obtener todas las actas con paginación
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerActas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fechaRecepcion") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ActaRecepcionDTO> actas = actaRecepcionService.obtenerActasPaginadas(pageable);
        
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", actas.getContent(),
                "totalElements", actas.getTotalElements(),
                "totalPages", actas.getTotalPages(),
                "currentPage", actas.getNumber(),
                "size", actas.getSize()
        ));
    }
    
    // Obtener acta por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerActaPorId(@PathVariable UUID id) {
        Optional<ActaRecepcionDTO> acta = actaRecepcionService.obtenerActaPorId(id);
        
        if (acta.isPresent()) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", acta.get()
            ));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Obtener acta por número
    @GetMapping("/numero/{numeroActa}")
    public ResponseEntity<Map<String, Object>> obtenerActaPorNumero(@PathVariable String numeroActa) {
        Optional<ActaRecepcionDTO> acta = actaRecepcionService.obtenerActaPorNumero(numeroActa);
        
        if (acta.isPresent()) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", acta.get()
            ));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Buscar actas con filtros
    @GetMapping("/buscar")
    public ResponseEntity<Map<String, Object>> buscarActas(
            @RequestParam(required = false) String numeroActa,
            @RequestParam(required = false) UUID clienteId,
            @RequestParam(required = false) EstadoDocumento estado,
            @RequestParam(required = false) String responsable,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fechaRecepcion") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ActaRecepcionDTO> actas = actaRecepcionService.buscarActasConFiltros(
                numeroActa, clienteId, estado, responsable, fechaInicio, fechaFin, pageable);
        
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", actas.getContent(),
                "totalElements", actas.getTotalElements(),
                "totalPages", actas.getTotalPages(),
                "currentPage", actas.getNumber(),
                "size", actas.getSize()
        ));
    }
    
    // Obtener actas por cliente
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<Map<String, Object>> obtenerActasPorCliente(@PathVariable UUID clienteId) {
        List<ActaRecepcionDTO> actas = actaRecepcionService.obtenerActasPorCliente(clienteId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", actas
        ));
    }
    
    // Obtener actas por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<Map<String, Object>> obtenerActasPorEstado(@PathVariable EstadoDocumento estado) {
        List<ActaRecepcionDTO> actas = actaRecepcionService.obtenerActasPorEstado(estado);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", actas
        ));
    }
    
    // Obtener actas pendientes de aprobación
    @GetMapping("/pendientes-aprobacion")
    public ResponseEntity<Map<String, Object>> obtenerActasPendientesAprobacion() {
        List<ActaRecepcionDTO> actas = actaRecepcionService.obtenerActasPendientesAprobacion();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", actas
        ));
    }
    
    // Actualizar acta de recepción
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarActaRecepcion(@PathVariable UUID id, 
                                                    @Valid @RequestBody ActaRecepcionDTO actaDTO) {
        try {
            ActaRecepcionDTO acta = actaRecepcionService.actualizarActaRecepcion(id, actaDTO);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Acta de recepción actualizada exitosamente",
                    "data", acta
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    // Cambiar estado del acta
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstadoActa(@PathVariable UUID id,
                                              @RequestBody Map<String, Object> request) {
        try {
            String estadoStr = (String) request.get("estado");
            String actualizadoPor = (String) request.get("actualizadoPor");
            
            if (estadoStr == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "El campo 'estado' es requerido"
                ));
            }
            
            EstadoDocumento estado = EstadoDocumento.valueOf(estadoStr);
            ActaRecepcionDTO acta = actaRecepcionService.cambiarEstadoActa(id, estado, actualizadoPor);
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Estado del acta actualizado exitosamente",
                    "data", acta
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Estado inválido"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    // Aprobar acta
    @PostMapping("/{id}/aprobar")
    public ResponseEntity<?> aprobarActa(@PathVariable UUID id,
                                        @RequestBody Map<String, String> request) {
        try {
            String aprobadoPor = request.get("aprobadoPor");
            if (aprobadoPor == null || aprobadoPor.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "El campo 'aprobadoPor' es requerido"
                ));
            }
            
            ActaRecepcionDTO acta = actaRecepcionService.aprobarActa(id, aprobadoPor);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Acta aprobada exitosamente",
                    "data", acta
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    // Rechazar acta
    @PostMapping("/{id}/rechazar")
    public ResponseEntity<?> rechazarActa(@PathVariable UUID id,
                                         @RequestBody Map<String, String> request) {
        try {
            String rechazadoPor = request.get("rechazadoPor");
            if (rechazadoPor == null || rechazadoPor.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "El campo 'rechazadoPor' es requerido"
                ));
            }
            
            ActaRecepcionDTO acta = actaRecepcionService.rechazarActa(id, rechazadoPor);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Acta rechazada exitosamente",
                    "data", acta
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    // Estadísticas por estado
    @GetMapping("/estadisticas/estado")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasPorEstado() {
        List<Object[]> estadisticas = actaRecepcionService.obtenerEstadisticasPorEstado();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", estadisticas
        ));
    }
    
    // Estadísticas por cliente
    @GetMapping("/estadisticas/cliente")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasPorCliente() {
        List<Object[]> estadisticas = actaRecepcionService.obtenerEstadisticasPorCliente();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", estadisticas
        ));
    }
    
    // Obtener estados disponibles
    @GetMapping("/estados")
    public ResponseEntity<Map<String, Object>> obtenerEstadosDisponibles() {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", EstadoDocumento.values()
        ));
    }
}