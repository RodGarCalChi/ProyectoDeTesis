package org.example.backend.controller;

import org.example.backend.entity.Cliente;
import org.example.backend.entity.Producto;
import org.example.backend.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:9002"})
public class ClienteController {
    
    @Autowired
    private ClienteService clienteService;
    
    /**
     * Asignar múltiples productos a un cliente
     * POST /api/clientes/{clienteId}/productos
     */
    @PostMapping("/{clienteId}/productos")
    public ResponseEntity<Map<String, Object>> asignarProductos(
            @PathVariable UUID clienteId,
            @RequestBody List<UUID> productosIds) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Cliente cliente = clienteService.asignarProductos(clienteId, productosIds);
            
            response.put("success", true);
            response.put("message", "Productos asignados exitosamente");
            response.put("cliente", Map.of(
                "id", cliente.getId(),
                "nombre", cliente.getNombre(),
                "ruc", cliente.getRuc()
            ));
            response.put("total_productos", cliente.getProductos().size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Asignar un solo producto a un cliente
     * POST /api/clientes/{clienteId}/productos/{productoId}
     */
    @PostMapping("/{clienteId}/productos/{productoId}")
    public ResponseEntity<Map<String, Object>> asignarProducto(
            @PathVariable UUID clienteId,
            @PathVariable UUID productoId) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Cliente cliente = clienteService.asignarProducto(clienteId, productoId);
            
            response.put("success", true);
            response.put("message", "Producto asignado exitosamente");
            response.put("total_productos", cliente.getProductos().size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Obtener todos los productos de un cliente
     * GET /api/clientes/{clienteId}/productos
     */
    @GetMapping("/{clienteId}/productos")
    public ResponseEntity<Map<String, Object>> obtenerProductos(@PathVariable UUID clienteId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Set<Producto> productos = clienteService.obtenerProductosDeCliente(clienteId);
            
            List<Map<String, Object>> productosDTO = new ArrayList<>();
            for (Producto producto : productos) {
                productosDTO.add(Map.of(
                    "id", producto.getId(),
                    "codigoSKU", producto.getCodigoSKU(),
                    "nombre", producto.getNombre(),
                    "tipo", producto.getTipo().name(),
                    "requiereCadenaFrio", producto.getRequiereCadenaFrio()
                ));
            }
            
            response.put("success", true);
            response.put("total", productos.size());
            response.put("productos", productosDTO);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Remover un producto de un cliente
     * DELETE /api/clientes/{clienteId}/productos/{productoId}
     */
    @DeleteMapping("/{clienteId}/productos/{productoId}")
    public ResponseEntity<Map<String, Object>> removerProducto(
            @PathVariable UUID clienteId,
            @PathVariable UUID productoId) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            clienteService.removerProducto(clienteId, productoId);
            
            response.put("success", true);
            response.put("message", "Producto removido exitosamente");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Remover todos los productos de un cliente
     * DELETE /api/clientes/{clienteId}/productos
     */
    @DeleteMapping("/{clienteId}/productos")
    public ResponseEntity<Map<String, Object>> removerTodosLosProductos(@PathVariable UUID clienteId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            clienteService.removerTodosLosProductos(clienteId);
            
            response.put("success", true);
            response.put("message", "Todos los productos han sido removidos del cliente");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Obtener todos los clientes que tienen un producto específico
     * GET /api/clientes/por-producto/{productoId}
     */
    @GetMapping("/por-producto/{productoId}")
    public ResponseEntity<Map<String, Object>> obtenerClientesPorProducto(@PathVariable UUID productoId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Set<Cliente> clientes = clienteService.obtenerClientesDeProducto(productoId);
            
            List<Map<String, Object>> clientesDTO = new ArrayList<>();
            for (Cliente cliente : clientes) {
                clientesDTO.add(Map.of(
                    "id", cliente.getId(),
                    "nombre", cliente.getNombre(),
                    "ruc", cliente.getRuc(),
                    "email", cliente.getEmail() != null ? cliente.getEmail() : ""
                ));
            }
            
            response.put("success", true);
            response.put("total", clientes.size());
            response.put("clientes", clientesDTO);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Verificar si un cliente tiene un producto específico
     * GET /api/clientes/{clienteId}/tiene-producto/{productoId}
     */
    @GetMapping("/{clienteId}/tiene-producto/{productoId}")
    public ResponseEntity<Map<String, Object>> verificarProducto(
            @PathVariable UUID clienteId,
            @PathVariable UUID productoId) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean tieneProducto = clienteService.clienteTieneProducto(clienteId, productoId);
            
            response.put("success", true);
            response.put("tieneProducto", tieneProducto);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}