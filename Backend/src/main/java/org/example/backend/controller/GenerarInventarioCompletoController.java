package org.example.backend.controller;

import java.util.Map;

import org.example.backend.dto.GenerarInventarioCompletoDTO;
import org.example.backend.service.GenerarInventarioCompletoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "*")
public class GenerarInventarioCompletoController {
    
    @Autowired
    private GenerarInventarioCompletoService generarInventarioService;
    
    /**
     * Genera el inventario completo jerárquico para uno o más clientes
     * 
     * POST /api/inventario/generar-completo
     * 
     * Crea en orden jerárquico:
     * 1. OperadorLogistico
     * 2. Almacen (para cada cliente)
     * 3. Zonas (para cada almacén)
     * 4. Ubicaciones (para cada zona)
     * 5. Productos
     * 6. Lotes
     * 7. Palets
     * 8. InventarioCliente (para cada cliente)
     */
    @PostMapping("/generar-completo")
    public ResponseEntity<?> generarInventarioCompleto(
            @Valid @RequestBody GenerarInventarioCompletoDTO request) {
        try {
            // Validaciones básicas
            if (request.getClienteIds() == null || request.getClienteIds().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "La lista de clientes no puede estar vacía"
                ));
            }
            
            if (request.getZonas() == null || request.getZonas().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "La lista de zonas no puede estar vacía"
                ));
            }
            
            if (request.getProductosInventario() == null || request.getProductosInventario().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "La lista de productos con inventario no puede estar vacía"
                ));
            }
            
            Map<String, Object> resultado = generarInventarioService.generarInventarioCompleto(request);
            
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





