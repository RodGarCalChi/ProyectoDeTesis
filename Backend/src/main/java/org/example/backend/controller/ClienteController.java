package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.dto.ClienteCreateDTO;
import org.example.backend.dto.ClienteDTO;
import org.example.backend.dto.ClienteUpdateDTO;
import org.example.backend.service.ClienteService;
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
@RequestMapping("/api/clientes")
public class ClienteController {
    
    @Autowired
    private ClienteService clienteService;
    
    // Crear cliente
    @PostMapping
    public ResponseEntity<?> crearCliente(@Valid @RequestBody ClienteCreateDTO createDTO) {
        try {
            ClienteDTO cliente = clienteService.crearCliente(createDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "Cliente creado exitosamente",
                    "data", cliente
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    // Obtener todos los clientes
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerClientes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "razonSocial") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "true") Boolean soloActivos) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        if (soloActivos) {
            // Para clientes activos, usar búsqueda con filtros
            Page<ClienteDTO> clientes = clienteService.buscarClientesConFiltros(
                    null, null, null, null, true, pageable);
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", clientes.getContent(),
                    "totalElements", clientes.getTotalElements(),
                    "totalPages", clientes.getTotalPages(),
                    "currentPage", clientes.getNumber(),
                    "size", clientes.getSize()
            ));
        } else {
            Page<ClienteDTO> clientes = clienteService.obtenerClientesPaginados(pageable);
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", clientes.getContent(),
                    "totalElements", clientes.getTotalElements(),
                    "totalPages", clientes.getTotalPages(),
                    "currentPage", clientes.getNumber(),
                    "size", clientes.getSize()
            ));
        }
    }
    
    // Obtener cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerClientePorId(@PathVariable UUID id) {
        Optional<ClienteDTO> cliente = clienteService.obtenerClientePorId(id);
        
        if (cliente.isPresent()) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", cliente.get()
            ));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Obtener cliente por RUC/DNI
    @GetMapping("/ruc/{rucDni}")
    public ResponseEntity<Map<String, Object>> obtenerClientePorRucDni(@PathVariable String rucDni) {
        Optional<ClienteDTO> cliente = clienteService.obtenerClientePorRucDni(rucDni);
        
        if (cliente.isPresent()) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", cliente.get()
            ));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Buscar clientes con filtros
    @GetMapping("/buscar")
    public ResponseEntity<Map<String, Object>> buscarClientes(
            @RequestParam(required = false) String razonSocial,
            @RequestParam(required = false) String rucDni,
            @RequestParam(required = false) String distrito,
            @RequestParam(required = false) String tipoCliente,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "razonSocial") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ClienteDTO> clientes = clienteService.buscarClientesConFiltros(
                razonSocial, rucDni, distrito, tipoCliente, activo, pageable);
        
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", clientes.getContent(),
                "totalElements", clientes.getTotalElements(),
                "totalPages", clientes.getTotalPages(),
                "currentPage", clientes.getNumber(),
                "size", clientes.getSize()
        ));
    }
    
    // Obtener clientes por tipo
    @GetMapping("/tipo/{tipoCliente}")
    public ResponseEntity<Map<String, Object>> obtenerClientesPorTipo(@PathVariable String tipoCliente) {
        List<ClienteDTO> clientes = clienteService.obtenerClientesPorTipo(tipoCliente);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", clientes
        ));
    }
    
    // Obtener clientes por distrito
    @GetMapping("/distrito/{distrito}")
    public ResponseEntity<Map<String, Object>> obtenerClientesPorDistrito(@PathVariable String distrito) {
        List<ClienteDTO> clientes = clienteService.obtenerClientesPorDistrito(distrito);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", clientes
        ));
    }
    
    // Obtener solo clientes activos (lista simple)
    @GetMapping("/activos")
    public ResponseEntity<Map<String, Object>> obtenerClientesActivos() {
        List<ClienteDTO> clientes = clienteService.obtenerClientesActivos();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", clientes
        ));
    }
    
    // Actualizar cliente
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCliente(@PathVariable UUID id, 
                                              @Valid @RequestBody ClienteUpdateDTO updateDTO) {
        try {
            ClienteDTO cliente = clienteService.actualizarCliente(id, updateDTO);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Cliente actualizado exitosamente",
                    "data", cliente
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    // Cambiar estado del cliente (activar/desactivar)
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstadoCliente(@PathVariable UUID id, 
                                                 @RequestBody Map<String, Boolean> request) {
        try {
            Boolean activo = request.get("activo");
            if (activo == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "El campo 'activo' es requerido"
                ));
            }
            
            ClienteDTO cliente = clienteService.cambiarEstadoCliente(id, activo);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", activo ? "Cliente activado exitosamente" : "Cliente desactivado exitosamente",
                    "data", cliente
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    // Eliminar cliente (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarCliente(@PathVariable UUID id) {
        try {
            clienteService.eliminarCliente(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Cliente desactivado exitosamente"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    // Eliminar cliente permanentemente
    @DeleteMapping("/{id}/permanente")
    public ResponseEntity<Map<String, Object>> eliminarClientePermanente(@PathVariable UUID id) {
        try {
            clienteService.eliminarClientePermanente(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Cliente eliminado permanentemente"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    // Estadísticas por tipo
    @GetMapping("/estadisticas/tipo")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasPorTipo() {
        List<Object[]> estadisticas = clienteService.obtenerEstadisticasPorTipo();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", estadisticas
        ));
    }
    
    // Estadísticas por distrito
    @GetMapping("/estadisticas/distrito")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasPorDistrito() {
        List<Object[]> estadisticas = clienteService.obtenerEstadisticasPorDistrito();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", estadisticas
        ));
    }
    
    // Clientes con email
    @GetMapping("/con-email")
    public ResponseEntity<Map<String, Object>> obtenerClientesConEmail() {
        List<ClienteDTO> clientes = clienteService.obtenerClientesConEmail();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", clientes
        ));
    }
}