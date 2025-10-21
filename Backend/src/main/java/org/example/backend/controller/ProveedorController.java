package org.example.backend.controller;

import org.example.backend.entity.Proveedor;
import org.example.backend.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "*")
public class ProveedorController {
    
    @Autowired
    private ProveedorRepository proveedorRepository;
    
    // Obtener proveedores activos
    @GetMapping("/activos")
    public ResponseEntity<Map<String, Object>> obtenerProveedoresActivos() {
        try {
            System.out.println("🔍 Buscando proveedores activos...");
            List<Proveedor> proveedores = proveedorRepository.findByHabilitadoTrue();
            System.out.println("📊 Proveedores encontrados: " + proveedores.size());
            
            for (Proveedor p : proveedores) {
                System.out.println("  - " + p.getRazonSocial() + " (ID: " + p.getId() + ")");
            }
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", proveedores
            ));
        } catch (Exception e) {
            System.err.println("❌ Error al obtener proveedores: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Error al obtener proveedores: " + e.getMessage()
            ));
        }
    }
    
    // Obtener todos los proveedores
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodosLosProveedores() {
        List<Proveedor> proveedores = proveedorRepository.findAll();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", proveedores
        ));
    }
    
    // Endpoint de prueba simple
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testEndpoint() {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "ProveedorController funcionando correctamente",
                "timestamp", java.time.LocalDateTime.now()
        ));
    }
    
    // Crear proveedor de prueba
    @PostMapping("/crear-prueba")
    public ResponseEntity<Map<String, Object>> crearProveedorPrueba() {
        try {
            System.out.println("🔄 Intentando crear proveedor de prueba...");
            
            // Verificar si ya existe
            boolean existe = proveedorRepository.existsByRuc("20123456789");
            System.out.println("📋 Proveedor existe: " + existe);
            
            if (existe) {
                System.out.println("✅ Proveedor ya existe, retornando...");
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Proveedor de prueba ya existe"
                ));
            }
            
            System.out.println("📝 Creando nuevo proveedor...");
            Proveedor proveedor = new Proveedor(
                    "Distribuidora Farmacéutica S.A.C.",
                    "20123456789",
                    "Juan Pérez",
                    "01-234-5678",
                    "contacto@distribuidora.com",
                    "Av. Principal 123, Lima",
                    true
            );
            
            proveedor = proveedorRepository.save(proveedor);
            System.out.println("✅ Proveedor creado con ID: " + proveedor.getId());
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Proveedor de prueba creado exitosamente",
                    "data", proveedor
            ));
        } catch (Exception e) {
            System.err.println("❌ Error al crear proveedor: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Error al crear proveedor: " + e.getMessage()
            ));
        }
    }
}