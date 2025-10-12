package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.dto.RecepcionMercaderiaDTO;
import org.example.backend.enumeraciones.EstadoRecepcion;
import org.example.backend.service.RecepcionMercaderiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/recepciones")
public class RecepcionMercaderiaController {
    
    @Autowired
    private RecepcionMercaderiaService recepcionService;
    
    // Crear nueva recepción
    @PostMapping
    public ResponseEntity<?> crearRecepcion(@Valid @RequestBody RecepcionMercaderiaDTO recepcionDTO) {
        try {
            RecepcionMercaderiaDTO recepcion = recepcionService.crearRecepcion(recepcionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "Recepción creada exitosamente",
                    "data", recepcion
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    // Obtener todas las recepciones
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerRecepciones(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fechaRecepcion") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<RecepcionMercaderiaDTO> recepciones = recepcionService.obtenerRecepcionesPaginadas(pageable);
        
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", recepciones.getContent(),
                "totalElements", recepciones.getTotalElements(),
                "totalPages", recepciones.getTotalPages(),
                "currentPage", recepciones.getNumber(),
                "size", recepciones.getSize()
        ));
    }
    
    // Obtener recepción por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerRecepcionPorId(@PathVariable UUID id) {
        Optional<RecepcionMercaderiaDTO> recepcion = recepcionService.obtenerRecepcionPorId(id);
        
        if (recepcion.isPresent()) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", recepcion.get()
            ));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Buscar recepciones con filtros
    @GetMapping("/buscar")
    public ResponseEntity<Map<String, Object>> buscarRecepciones(
            @RequestParam(required = false) String numeroOrden,
            @RequestParam(required = false) String numeroGuia,
            @RequestParam(required = false) EstadoRecepcion estado,
            @RequestParam(required = false) UUID proveedorId,
            @RequestParam(required = false) String responsable,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fechaRecepcion") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<RecepcionMercaderiaDTO> recepciones = recepcionService.buscarRecepcionesConFiltros(
                numeroOrden, numeroGuia, estado, proveedorId, responsable, pageable);
        
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", recepciones.getContent(),
                "totalElements", recepciones.getTotalElements(),
                "totalPages", recepciones.getTotalPages(),
                "currentPage", recepciones.getNumber(),
                "size", recepciones.getSize()
        ));
    }
    
    // Obtener recepciones por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<Map<String, Object>> obtenerRecepcionesPorEstado(@PathVariable EstadoRecepcion estado) {
        List<RecepcionMercaderiaDTO> recepciones = recepcionService.obtenerRecepcionesPorEstado(estado);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", recepciones
        ));
    }
    
    // Obtener recepciones pendientes
    @GetMapping("/pendientes")
    public ResponseEntity<Map<String, Object>> obtenerRecepcionesPendientes() {
        List<RecepcionMercaderiaDTO> recepciones = recepcionService.obtenerRecepcionesPendientes();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", recepciones
        ));
    }
    
    // Obtener recepciones en cuarentena
    @GetMapping("/cuarentena")
    public ResponseEntity<Map<String, Object>> obtenerRecepcionesEnCuarentena() {
        List<RecepcionMercaderiaDTO> recepciones = recepcionService.obtenerRecepcionesEnCuarentena();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", recepciones
        ));
    }
    
    // Iniciar verificación
    @PostMapping("/{id}/iniciar-verificacion")
    public ResponseEntity<?> iniciarVerificacion(@PathVariable UUID id, 
                                                @RequestBody Map<String, String> request) {
        try {
            String inspector = request.get("inspector");
            if (inspector == null || inspector.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "El inspector es requerido"
                ));
            }
            
            RecepcionMercaderiaDTO recepcion = recepcionService.iniciarVerificacion(id, inspector);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Verificación iniciada exitosamente",
                    "data", recepcion
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    // Completar verificación documental
    @PostMapping("/{id}/verificacion-documental")
    public ResponseEntity<?> completarVerificacionDocumental(@PathVariable UUID id,
                                                           @RequestBody Map<String, Boolean> request) {
        try {
            Boolean aprobado = request.get("aprobado");
            if (aprobado == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "El campo 'aprobado' es requerido"
                ));
            }
            
            RecepcionMercaderiaDTO recepcion = recepcionService.completarVerificacionDocumental(id, aprobado);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", aprobado ? "Documentos aprobados" : "Documentos rechazados",
                    "data", recepcion
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    // Completar verificación física
    @PostMapping("/{id}/verificacion-fisica")
    public ResponseEntity<?> completarVerificacionFisica(@PathVariable UUID id,
                                                        @RequestBody Map<String, Boolean> request) {
        try {
            Boolean aprobado = request.get("aprobado");
            if (aprobado == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "El campo 'aprobado' es requerido"
                ));
            }
            
            RecepcionMercaderiaDTO recepcion = recepcionService.completarVerificacionFisica(id, aprobado);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", aprobado ? "Verificación física aprobada" : "Verificación física rechazada",
                    "data", recepcion
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    // Completar verificación de temperatura
    @PostMapping("/{id}/verificacion-temperatura")
    public ResponseEntity<?> completarVerificacionTemperatura(@PathVariable UUID id,
                                                             @RequestBody Map<String, Boolean> request) {
        try {
            Boolean aprobado = request.get("aprobado");
            if (aprobado == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "El campo 'aprobado' es requerido"
                ));
            }
            
            RecepcionMercaderiaDTO recepcion = recepcionService.completarVerificacionTemperatura(id, aprobado);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", aprobado ? "Temperatura aprobada" : "Temperatura rechazada",
                    "data", recepcion
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    // Enviar a cuarentena
    @PostMapping("/{id}/cuarentena")
    public ResponseEntity<?> enviarACuarentena(@PathVariable UUID id) {
        try {
            RecepcionMercaderiaDTO recepcion = recepcionService.enviarACuarentena(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Recepción enviada a cuarentena",
                    "data", recepcion
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    // Aprobar por control de calidad
    @PostMapping("/{id}/aprobar-calidad")
    public ResponseEntity<?> aprobarPorCalidad(@PathVariable UUID id,
                                              @RequestBody Map<String, String> request) {
        try {
            String inspector = request.get("inspector");
            if (inspector == null || inspector.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "El inspector es requerido"
                ));
            }
            
            RecepcionMercaderiaDTO recepcion = recepcionService.aprobarPorCalidad(id, inspector);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Recepción aprobada por control de calidad",
                    "data", recepcion
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    // Rechazar por control de calidad
    @PostMapping("/{id}/rechazar-calidad")
    public ResponseEntity<?> rechazarPorCalidad(@PathVariable UUID id,
                                               @RequestBody Map<String, String> request) {
        try {
            String inspector = request.get("inspector");
            String motivo = request.get("motivo");
            
            if (inspector == null || inspector.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "El inspector es requerido"
                ));
            }
            
            if (motivo == null || motivo.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "El motivo de rechazo es requerido"
                ));
            }
            
            RecepcionMercaderiaDTO recepcion = recepcionService.rechazarPorCalidad(id, inspector, motivo);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Recepción rechazada por control de calidad",
                    "data", recepcion
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    // Almacenar productos
    @PostMapping("/{id}/almacenar")
    public ResponseEntity<?> almacenarProductos(@PathVariable UUID id) {
        try {
            RecepcionMercaderiaDTO recepcion = recepcionService.almacenarProductos(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Productos almacenados exitosamente",
                    "data", recepcion
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
        List<Object[]> estadisticas = recepcionService.obtenerEstadisticasPorEstado();
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
                "data", EstadoRecepcion.values()
        ));
    }
}