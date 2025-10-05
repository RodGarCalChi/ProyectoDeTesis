package org.example.backend.controller;

import org.example.backend.dto.MovimientoEntradaDTO;
import org.example.backend.dto.MovimientoSalidaDTO;
import org.example.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/movimientos")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:9002"})
public class MovimientosController {

    @Autowired
    private AuthService authService;
    
    @GetMapping("/validar-acceso")
    public ResponseEntity<Map<String, Object>> validarAcceso(@RequestHeader("Authorization") String authHeader) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            AuthService.ValidationResult validacion = authService.validarAccesoMovimientos(authHeader);
            
            if (!validacion.isValido()) {
                response.put("success", false);
                response.put("message", validacion.getMensaje());
                
                if (validacion.getRol() != null) {
                    response.put("rolActual", validacion.getRol());
                    response.put("rolRequerido", "Recepcion");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }
            }
            
            response.put("success", true);
            response.put("message", validacion.getMensaje());
            response.put("rol", validacion.getRol());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @PostMapping("/entrada")
    public ResponseEntity<Map<String, Object>> registrarEntrada(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody MovimientoEntradaDTO movimientoDTO) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validar autorización
            ResponseEntity<Map<String, Object>> validacionResponse = validarAcceso(authHeader);
            if (validacionResponse.getStatusCode() != HttpStatus.OK) {
                return validacionResponse;
            }
            
            // Validar datos del movimiento
            if (movimientoDTO.getReferencia() == null || movimientoDTO.getReferencia().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "El número de referencia es obligatorio");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (movimientoDTO.getProveedor() == null || movimientoDTO.getProveedor().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "El proveedor es obligatorio");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (movimientoDTO.getProducto() == null || movimientoDTO.getProducto().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "El producto es obligatorio");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (movimientoDTO.getCantidad() == null || movimientoDTO.getCantidad() <= 0) {
                response.put("success", false);
                response.put("message", "La cantidad debe ser mayor a 0");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (movimientoDTO.getUbicacion() == null || movimientoDTO.getUbicacion().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "La ubicación es obligatoria");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (movimientoDTO.getRecibidoPor() == null || movimientoDTO.getRecibidoPor().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "El campo 'Recibido por' es obligatorio");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Simular guardado en base de datos
            Long movimientoId = System.currentTimeMillis(); // ID simulado
            
            response.put("success", true);
            response.put("message", "Entrada de mercadería registrada exitosamente");
            response.put("movimientoId", movimientoId);
            response.put("referencia", movimientoDTO.getReferencia());
            response.put("fechaRegistro", java.time.LocalDateTime.now().toString());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error interno del servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @PostMapping("/salida")
    public ResponseEntity<Map<String, Object>> registrarSalida(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody MovimientoSalidaDTO movimientoDTO) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validar autorización
            ResponseEntity<Map<String, Object>> validacionResponse = validarAcceso(authHeader);
            if (validacionResponse.getStatusCode() != HttpStatus.OK) {
                return validacionResponse;
            }
            
            // Validaciones similares para salida
            if (movimientoDTO.getReferencia() == null || movimientoDTO.getReferencia().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "El número de referencia es obligatorio");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Simular guardado en base de datos
            Long movimientoId = System.currentTimeMillis();
            
            response.put("success", true);
            response.put("message", "Salida de mercadería registrada exitosamente");
            response.put("movimientoId", movimientoId);
            response.put("referencia", movimientoDTO.getReferencia());
            response.put("fechaRegistro", java.time.LocalDateTime.now().toString());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error interno del servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @GetMapping("/proveedores")
    public ResponseEntity<Map<String, Object>> obtenerProveedores(@RequestHeader("Authorization") String authHeader) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validar autorización
            ResponseEntity<Map<String, Object>> validacionResponse = validarAcceso(authHeader);
            if (validacionResponse.getStatusCode() != HttpStatus.OK) {
                return validacionResponse;
            }
            
            // Datos simulados de proveedores
            Map<String, String> proveedores = new HashMap<>();
            proveedores.put("proveedor1", "Laboratorio Farmacéutico ABC");
            proveedores.put("proveedor2", "Distribuidora Médica XYZ");
            proveedores.put("proveedor3", "Suministros Hospitalarios DEF");
            
            response.put("success", true);
            response.put("proveedores", proveedores);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @GetMapping("/productos")
    public ResponseEntity<Map<String, Object>> obtenerProductos(@RequestHeader("Authorization") String authHeader) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validar autorización
            ResponseEntity<Map<String, Object>> validacionResponse = validarAcceso(authHeader);
            if (validacionResponse.getStatusCode() != HttpStatus.OK) {
                return validacionResponse;
            }
            
            // Datos simulados de productos
            Map<String, String> productos = new HashMap<>();
            productos.put("producto1", "Paracetamol 500mg");
            productos.put("producto2", "Ibuprofeno 400mg");
            productos.put("producto3", "Amoxicilina 250mg");
            productos.put("producto4", "Aspirina 100mg");
            productos.put("producto5", "Omeprazol 20mg");
            
            response.put("success", true);
            response.put("productos", productos);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @GetMapping("/ubicaciones")
    public ResponseEntity<Map<String, Object>> obtenerUbicaciones(@RequestHeader("Authorization") String authHeader) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validar autorización
            ResponseEntity<Map<String, Object>> validacionResponse = validarAcceso(authHeader);
            if (validacionResponse.getStatusCode() != HttpStatus.OK) {
                return validacionResponse;
            }
            
            // Datos simulados de ubicaciones
            Map<String, String> ubicaciones = new HashMap<>();
            ubicaciones.put("almacen1", "Almacén Principal - Estante A1");
            ubicaciones.put("almacen2", "Almacén Secundario - Estante B2");
            ubicaciones.put("almacen3", "Área de Cuarentena - Estante C3");
            ubicaciones.put("almacen4", "Zona de Refrigerados - Estante R1");
            
            response.put("success", true);
            response.put("ubicaciones", ubicaciones);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}