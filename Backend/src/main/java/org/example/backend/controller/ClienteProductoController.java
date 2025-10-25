package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.dto.*;
import org.example.backend.service.ClienteProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/cliente-productos")
@CrossOrigin(origins = "*")
public class ClienteProductoController {
    
    @Autowired
    private ClienteProductoService clienteProductoService;
    
    // Asignar producto a cliente
    @PostMapping
    public ResponseEntity<?> asignarProductoACliente(@Valid @RequestBody ClienteProductoDTO dto) {
        try {
            ClienteProductoDTO resultado = clienteProductoService.asignarProductoACliente(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "Producto asignado al cliente exitosamente",
                    "data", resultado
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    // Obtener productos de un cliente (solo productos)
    @GetMapping("/cliente/{clienteId}/productos")
    public ResponseEntity<Map<String, Object>> obtenerProductosDeCliente(@PathVariable UUID clienteId) {
        try {
            List<ProductoDTO> productos = clienteProductoService.obtenerProductosDeCliente(clienteId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", productos,
                    "total", productos.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    // Obtener relaciones completas de un cliente
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<Map<String, Object>> obtenerRelacionesDeCliente(@PathVariable UUID clienteId) {
        try {
            List<ClienteProductoDTO> relaciones = clienteProductoService.obtenerRelacionesDeCliente(clienteId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", relaciones,
                    "total", relaciones.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    // Obtener relaciones de un producto
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<Map<String, Object>> obtenerRelacionesDeProducto(@PathVariable UUID productoId) {
        try {
            List<ClienteProductoDTO> relaciones = clienteProductoService.obtenerRelacionesDeProducto(productoId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", relaciones,
                    "total", relaciones.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    // Desactivar relación
    @DeleteMapping("/cliente/{clienteId}/producto/{productoId}")
    public ResponseEntity<Map<String, Object>> desactivarRelacion(
            @PathVariable UUID clienteId,
            @PathVariable UUID productoId) {
        try {
            clienteProductoService.desactivarRelacion(clienteId, productoId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Relación desactivada exitosamente"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    // Activar relación
    @PatchMapping("/cliente/{clienteId}/producto/{productoId}/activar")
    public ResponseEntity<Map<String, Object>> activarRelacion(
            @PathVariable UUID clienteId,
            @PathVariable UUID productoId) {
        try {
            clienteProductoService.activarRelacion(clienteId, productoId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Relación activada exitosamente"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    // Contar productos de un cliente
    @GetMapping("/cliente/{clienteId}/count")
    public ResponseEntity<Map<String, Object>> contarProductosDeCliente(@PathVariable UUID clienteId) {
        try {
            Long count = clienteProductoService.contarProductosDeCliente(clienteId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "count", count
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    // ========== NUEVOS ENDPOINTS AVANZADOS ==========
    
    // Asignar múltiples productos a un cliente existente
    @PostMapping("/asignar-varios")
    public ResponseEntity<?> asignarVariosProductos(@Valid @RequestBody AsignarProductosDTO dto) {
        try {
            ResultadoAsignacionDTO resultado = clienteProductoService.asignarVariosProductos(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", resultado.getMensaje(),
                    "data", resultado
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    // Crear cliente con productos (existentes y/o nuevos)
    @PostMapping("/crear-cliente-con-productos")
    public ResponseEntity<?> crearClienteConProductos(@Valid @RequestBody ClienteConProductosDTO dto) {
        try {
            ResultadoAsignacionDTO resultado = clienteProductoService.crearClienteConProductos(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", resultado.getMensaje(),
                    "data", resultado
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    // Crear/asociar producto con cliente (maneja todos los casos)
    @PostMapping("/crear-o-asociar")
    public ResponseEntity<?> crearOAsociarProductoConCliente(@Valid @RequestBody ProductoConClienteDTO dto) {
        try {
            ResultadoAsignacionDTO resultado = clienteProductoService.crearOAsociarProductoConCliente(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", resultado.getMensaje(),
                    "data", resultado
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}
