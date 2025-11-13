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

    // ========== ENDPOINT DE ASIGNACIÓN MASIVA ==========
    // Asignación masiva de múltiples productos a múltiples clientes
    @PostMapping("/asignar-masivo")
    public ResponseEntity<?> asignarMasivo(@RequestBody List<ClienteProductoDTO> asignaciones) {
        try {
            // Validación básica
            if (asignaciones == null || asignaciones.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "La lista de asignaciones no puede estar vacía"
                ));
            }

            List<ClienteProductoDTO> resultados = clienteProductoService.asignarMasivo(asignaciones);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "Se procesaron " + resultados.size() + " asignaciones exitosamente",
                    "data", resultados,
                    "total", resultados.size()
            ));
        } catch (RuntimeException e) {
            e.printStackTrace(); // Esto imprimirá el error en los logs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error: " + e.getMessage(),
                    "error", e.getClass().getSimpleName()
            ));
        } catch (Exception e) {
            e.printStackTrace(); // Esto imprimirá el error en los logs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error inesperado: " + e.getMessage(),
                    "error", e.getClass().getSimpleName()
            ));
        }
    }

    // Endpoint de prueba simple
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "El endpoint funciona correctamente",
                "timestamp", System.currentTimeMillis()
        ));
    }

    // Listar todas las relaciones cliente-producto
    @GetMapping("/listar")
    public ResponseEntity<Map<String, Object>> listarTodasLasRelaciones() {
        try {
            List<ClienteProductoDTO> relaciones = clienteProductoService.listarTodasLasRelaciones();
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

    // Listar solo relaciones activas
    @GetMapping("/listar/activas")
    public ResponseEntity<Map<String, Object>> listarRelacionesActivas() {
        try {
            List<ClienteProductoDTO> relaciones = clienteProductoService.listarRelacionesActivas();
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
    
    /**
     * Genera productos y los asocia automáticamente a los clientes especificados
     * 
     * POST /api/cliente-productos/generar-productos-con-clientes
     * 
     * Body:
     * {
     *   "productos": [
     *     {
     *       "codigoSKU": "PROD001",
     *       "nombre": "Paracetamol 500mg",
     *       "tipo": "Medicamento",
     *       "condicionAlmacen": "Ambiente_15_25",
     *       "requiereCadenaFrio": false,
     *       "registroSanitario": "RS123456",
     *       "unidadMedida": "Tabletas",
     *       "vidaUtilMeses": 24,
     *       "tempMin": 15.0,
     *       "tempMax": 25.0
     *     }
     *   ],
     *   "clienteIds": ["uuid-cliente-1", "uuid-cliente-2"],
     *   "observaciones": "Asignación automática"
     * }
     */
    @PostMapping("/generar-productos-con-clientes")
    public ResponseEntity<?> generarProductosYAsociarClientes(
            @Valid @RequestBody GenerarProductosConClientesDTO request) {
        try {
            // Validaciones básicas
            if (request.getProductos() == null || request.getProductos().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "La lista de productos no puede estar vacía"
                ));
            }
            
            if (request.getClienteIds() == null || request.getClienteIds().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "La lista de clientes no puede estar vacía"
                ));
            }
            
            Map<String, Object> resultado = clienteProductoService.generarProductosYAsociarClientes(request);
            
            if ((Boolean) resultado.get("exitoso")) {
                return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                        "success", true,
                        "message", resultado.get("mensaje"),
                        "data", resultado
                ));
            } else {
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(Map.of(
                        "success", true,
                        "message", resultado.get("mensaje"),
                        "data", resultado
                ));
            }
            
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error inesperado: " + e.getMessage()
            ));
        }
    }
}
