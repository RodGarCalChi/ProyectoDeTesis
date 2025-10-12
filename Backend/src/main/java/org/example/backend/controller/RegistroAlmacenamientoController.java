package org.example.backend.controller;

import org.example.backend.dto.RegistroAlmacenamientoDTO;
import org.example.backend.enumeraciones.EstadoAlmacenamiento;
import org.example.backend.service.RegistroAlmacenamientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/almacenamiento")
@CrossOrigin(origins = "http://localhost:3000")
public class RegistroAlmacenamientoController {
    
    @Autowired
    private RegistroAlmacenamientoService almacenamientoService;
    
    // Crear nuevo registro de almacenamiento
    @PostMapping
    public ResponseEntity<RegistroAlmacenamientoDTO> crearRegistro(@Valid @RequestBody RegistroAlmacenamientoDTO registroDTO) {
        try {
            RegistroAlmacenamientoDTO nuevoRegistro = almacenamientoService.crearRegistro(registroDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoRegistro);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Obtener todos los registros con paginación
    @GetMapping
    public ResponseEntity<Page<RegistroAlmacenamientoDTO>> obtenerRegistros(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fechaCreacion") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<RegistroAlmacenamientoDTO> registros = almacenamientoService.obtenerRegistrosPaginados(pageable);
        
        return ResponseEntity.ok(registros);
    }
    
    // Obtener registro por ID
    @GetMapping("/{id}")
    public ResponseEntity<RegistroAlmacenamientoDTO> obtenerRegistroPorId(@PathVariable UUID id) {
        Optional<RegistroAlmacenamientoDTO> registro = almacenamientoService.obtenerRegistroPorId(id);
        return registro.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    // Buscar registros con filtros
    @GetMapping("/buscar")
    public ResponseEntity<Page<RegistroAlmacenamientoDTO>> buscarRegistros(
            @RequestParam(required = false) String numeroGuia,
            @RequestParam(required = false) EstadoAlmacenamiento estado,
            @RequestParam(required = false) UUID clienteId,
            @RequestParam(required = false) String operador,
            @RequestParam(required = false) String ubicacion,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fechaCreacion") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<RegistroAlmacenamientoDTO> registros = almacenamientoService.buscarRegistrosConFiltros(
                numeroGuia, estado, clienteId, operador, ubicacion, pageable);
        
        return ResponseEntity.ok(registros);
    }
    
    // Obtener registros por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<RegistroAlmacenamientoDTO>> obtenerRegistrosPorEstado(@PathVariable EstadoAlmacenamiento estado) {
        List<RegistroAlmacenamientoDTO> registros = almacenamientoService.obtenerRegistrosPorEstado(estado);
        return ResponseEntity.ok(registros);
    }
    
    // Iniciar proceso de almacenamiento
    @PutMapping("/{id}/iniciar")
    public ResponseEntity<RegistroAlmacenamientoDTO> iniciarAlmacenamiento(@PathVariable UUID id) {
        try {
            RegistroAlmacenamientoDTO registro = almacenamientoService.iniciarAlmacenamiento(id);
            return ResponseEntity.ok(registro);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Completar almacenamiento
    @PutMapping("/{id}/completar")
    public ResponseEntity<RegistroAlmacenamientoDTO> completarAlmacenamiento(@PathVariable UUID id) {
        try {
            RegistroAlmacenamientoDTO registro = almacenamientoService.completarAlmacenamiento(id);
            return ResponseEntity.ok(registro);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Enviar a verificación administrativa
    @PutMapping("/{id}/enviar-verificacion")
    public ResponseEntity<RegistroAlmacenamientoDTO> enviarAVerificacion(@PathVariable UUID id) {
        try {
            RegistroAlmacenamientoDTO registro = almacenamientoService.enviarAVerificacion(id);
            return ResponseEntity.ok(registro);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Verificar por área administrativa
    @PutMapping("/{id}/verificar")
    public ResponseEntity<RegistroAlmacenamientoDTO> verificarPorAdmin(
            @PathVariable UUID id,
            @RequestBody Map<String, String> request) {
        try {
            String verificador = request.get("verificador");
            if (verificador == null || verificador.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            RegistroAlmacenamientoDTO registro = almacenamientoService.verificarPorAdmin(id, verificador);
            return ResponseEntity.ok(registro);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Rechazar registro
    @PutMapping("/{id}/rechazar")
    public ResponseEntity<RegistroAlmacenamientoDTO> rechazarRegistro(
            @PathVariable UUID id,
            @RequestBody Map<String, String> request) {
        try {
            String motivo = request.get("motivo");
            String verificador = request.get("verificador");
            
            if (motivo == null || motivo.trim().isEmpty() || 
                verificador == null || verificador.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            RegistroAlmacenamientoDTO registro = almacenamientoService.rechazarRegistro(id, motivo, verificador);
            return ResponseEntity.ok(registro);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Reubicar mercadería
    @PutMapping("/{id}/reubicar")
    public ResponseEntity<RegistroAlmacenamientoDTO> reubicarMercaderia(
            @PathVariable UUID id,
            @RequestBody Map<String, String> request) {
        try {
            String nuevaUbicacion = request.get("nuevaUbicacion");
            String operador = request.get("operador");
            
            if (nuevaUbicacion == null || nuevaUbicacion.trim().isEmpty() || 
                operador == null || operador.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            RegistroAlmacenamientoDTO registro = almacenamientoService.reubicarMercaderia(id, nuevaUbicacion, operador);
            return ResponseEntity.ok(registro);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Obtener registros pendientes de verificación
    @GetMapping("/pendientes-verificacion")
    public ResponseEntity<List<RegistroAlmacenamientoDTO>> obtenerRegistrosPendientesVerificacion() {
        List<RegistroAlmacenamientoDTO> registros = almacenamientoService.obtenerRegistrosPendientesVerificacion();
        return ResponseEntity.ok(registros);
    }
    
    // Obtener registros con atención especial
    @GetMapping("/atencion-especial")
    public ResponseEntity<List<RegistroAlmacenamientoDTO>> obtenerRegistrosConAtencionEspecial() {
        List<RegistroAlmacenamientoDTO> registros = almacenamientoService.obtenerRegistrosConAtencionEspecial();
        return ResponseEntity.ok(registros);
    }
    
    // Obtener registros del día
    @GetMapping("/del-dia")
    public ResponseEntity<List<RegistroAlmacenamientoDTO>> obtenerRegistrosDelDia() {
        List<RegistroAlmacenamientoDTO> registros = almacenamientoService.obtenerRegistrosDelDia();
        return ResponseEntity.ok(registros);
    }
    
    // Obtener estadísticas por estado
    @GetMapping("/estadisticas/por-estado")
    public ResponseEntity<List<Object[]>> obtenerEstadisticasPorEstado() {
        List<Object[]> estadisticas = almacenamientoService.obtenerEstadisticasPorEstado();
        return ResponseEntity.ok(estadisticas);
    }
    
    // Obtener estadísticas por cliente
    @GetMapping("/estadisticas/por-cliente")
    public ResponseEntity<List<Object[]>> obtenerEstadisticasPorCliente() {
        List<Object[]> estadisticas = almacenamientoService.obtenerEstadisticasPorCliente();
        return ResponseEntity.ok(estadisticas);
    }
    
    // Obtener estados disponibles
    @GetMapping("/estados")
    public ResponseEntity<EstadoAlmacenamiento[]> obtenerEstados() {
        return ResponseEntity.ok(EstadoAlmacenamiento.values());
    }
}