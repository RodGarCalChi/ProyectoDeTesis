package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.dto.ProductoCreateDTO;
import org.example.backend.dto.ProductoDTO;
import org.example.backend.dto.ProductoUpdateDTO;
import org.example.backend.enumeraciones.TipoProducto;
import org.example.backend.service.ProductoService;
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
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // Crear producto
    @PostMapping
    public ResponseEntity<?> crearProducto(@Valid @RequestBody ProductoCreateDTO createDTO) {
        try {
            ProductoDTO producto = productoService.crearProducto(createDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "Producto creado exitosamente",
                    "data", producto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    // Obtener todos los productos
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerProductos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductoDTO> productos = productoService.obtenerProductosPaginados(pageable);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", productos.getContent(),
                "totalElements", productos.getTotalElements(),
                "totalPages", productos.getTotalPages(),
                "currentPage", productos.getNumber(),
                "size", productos.getSize()));
    }

    // Obtener producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerProductoPorId(@PathVariable UUID id) {
        Optional<ProductoDTO> producto = productoService.obtenerProductoPorId(id);

        if (producto.isPresent()) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", producto.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener producto por código SKU
    @GetMapping("/sku/{codigoSKU}")
    public ResponseEntity<Map<String, Object>> obtenerProductoPorSKU(@PathVariable String codigoSKU) {
        Optional<ProductoDTO> producto = productoService.obtenerProductoPorSKU(codigoSKU);

        if (producto.isPresent()) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", producto.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Buscar productos con filtros
    @GetMapping("/buscar")
    public ResponseEntity<Map<String, Object>> buscarProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String codigoSKU,
            @RequestParam(required = false) TipoProducto tipo,
            @RequestParam(required = false) Boolean requiereCadenaFrio,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductoDTO> productos = productoService.buscarProductosConFiltros(
                nombre, codigoSKU, tipo, requiereCadenaFrio, pageable);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", productos.getContent(),
                "totalElements", productos.getTotalElements(),
                "totalPages", productos.getTotalPages(),
                "currentPage", productos.getNumber(),
                "size", productos.getSize()));
    }

    // Obtener productos por tipo
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<Map<String, Object>> obtenerProductosPorTipo(@PathVariable TipoProducto tipo) {
        List<ProductoDTO> productos = productoService.obtenerProductosPorTipo(tipo);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", productos));
    }

    // Obtener productos que requieren cadena de frío
    @GetMapping("/cadena-frio")
    public ResponseEntity<Map<String, Object>> obtenerProductosCadenaFrio(
            @RequestParam(defaultValue = "true") Boolean requiereCadenaFrio) {
        List<ProductoDTO> productos = productoService.obtenerProductosCadenaFrio(requiereCadenaFrio);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", productos));
    }

    // Actualizar producto
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable UUID id,
            @Valid @RequestBody ProductoUpdateDTO updateDTO) {
        try {
            ProductoDTO producto = productoService.actualizarProducto(id, updateDTO);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Producto actualizado exitosamente",
                    "data", producto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    // Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarProducto(@PathVariable UUID id) {
        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Producto eliminado exitosamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    // Estadísticas por tipo
    @GetMapping("/estadisticas/tipo")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasPorTipo() {
        List<Object[]> estadisticas = productoService.obtenerEstadisticasPorTipo();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", estadisticas));
    }

    // Productos próximos a vencer
    @GetMapping("/proximos-vencer")
    public ResponseEntity<Map<String, Object>> obtenerProductosProximosAVencer(
            @RequestParam(defaultValue = "6") Integer mesesLimite) {
        List<ProductoDTO> productos = productoService.obtenerProductosProximosAVencer(mesesLimite);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", productos));
    }

    // Obtener tipos de producto disponibles
    @GetMapping("/tipos")
    public ResponseEntity<Map<String, Object>> obtenerTiposProducto() {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", TipoProducto.values()));
    }

    // Endpoint de prueba para verificar conectividad
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testEndpoint() {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "ProductoController funcionando correctamente",
                "timestamp", java.time.LocalDateTime.now()));
    }

    
    // Endpoint de diagnóstico simple
    @GetMapping("/diagnostico")
    public ResponseEntity<Map<String, Object>> diagnostico() {
        try {
            boolean servicioOk = productoService != null;
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "servicioInyectado", servicioOk,
                    "message", "Diagnóstico completado"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "error", e.getMessage()));
        }
    }
}