package org.example.backend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.backend.dto.CajaRequest;
import org.example.backend.entity.Caja;
import org.example.backend.entity.Cliente;
import org.example.backend.entity.Palet;
import org.example.backend.entity.Producto;
import org.example.backend.repository.CajaRepository;
import org.example.backend.repository.ClienteRepository;
import org.example.backend.repository.PaletRepository;
import org.example.backend.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/cajas")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "false")
@Tag(name = "7. Cajas", description = "Gestión de cajas dentro de palets con control de vencimiento")
public class CajaController {

    @Autowired
    private CajaRepository cajaRepository;
    
    @Autowired
    private PaletRepository paletRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> listarCajas() {
        try {
            List<Caja> cajas = cajaRepository.findAll();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", cajas));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error al listar cajas: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable UUID id) {
        return cajaRepository.findById(id)
                .map(caja -> ResponseEntity.ok(Map.of(
                        "success", true,
                        "data", caja)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Caja no encontrada")));
    }

    @GetMapping("/palet/{paletId}")
    public ResponseEntity<Map<String, Object>> listarPorPalet(@PathVariable UUID paletId) {
        try {
            List<Caja> cajas = cajaRepository.findByPaletId(paletId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", cajas));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error al listar cajas por palet: " + e.getMessage()));
        }
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<Map<String, Object>> listarPorCliente(@PathVariable UUID clienteId) {
        try {
            List<Caja> cajas = cajaRepository.findByClienteId(clienteId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", cajas));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error al listar cajas por cliente: " + e.getMessage()));
        }
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<Map<String, Object>> listarPorProducto(@PathVariable UUID productoId) {
        try {
            List<Caja> cajas = cajaRepository.findByProductoId(productoId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", cajas));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error al listar cajas por producto: " + e.getMessage()));
        }
    }

    @GetMapping("/proximas-vencer")
    public ResponseEntity<Map<String, Object>> listarProximasAVencer(@RequestParam(defaultValue = "30") int dias) {
        try {
            LocalDate fechaLimite = LocalDate.now().plusDays(dias);
            List<Caja> cajas = cajaRepository.findProximasAVencer(fechaLimite);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", cajas,
                    "diasConsiderados", dias));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error al listar cajas próximas a vencer: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody CajaRequest request) {
        try {
            // Buscar palet
            Palet palet = paletRepository.findById(request.getPaletId())
                    .orElseThrow(() -> new RuntimeException("Palet no encontrado"));
            
            // Buscar cliente
            Cliente cliente = clienteRepository.findById(request.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            
            // Buscar producto
            Producto producto = productoRepository.findById(request.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
            // Crear caja
            Caja caja = new Caja();
            caja.setCodigo(request.getCodigo());
            caja.setPalet(palet);
            caja.setCliente(cliente);
            caja.setProducto(producto);
            caja.setCantidad(request.getCantidad());
            caja.setLoteProducto(request.getLoteProducto());
            caja.setFechaVencimiento(request.getFechaVencimiento());
            caja.setTamano(request.getTamano());
            caja.setPesoKg(request.getPesoKg());
            caja.setLargoCm(request.getLargoCm());
            caja.setAnchoCm(request.getAnchoCm());
            caja.setAltoCm(request.getAltoCm());
            caja.setTemperaturaRequeridaMin(request.getTemperaturaRequeridaMin());
            caja.setTemperaturaRequeridaMax(request.getTemperaturaRequeridaMax());
            caja.setRequiereCadenaFrio(request.getRequiereCadenaFrio());
            caja.setObservaciones(request.getObservaciones());
            caja.setSellada(request.getSellada());
            
            Caja nuevaCaja = cajaRepository.save(caja);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "Caja creada exitosamente",
                    "data", nuevaCaja));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error al crear caja: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable UUID id, @Valid @RequestBody Caja caja) {
        return cajaRepository.findById(id)
                .map(cajaExistente -> {
                    if (caja.getCodigo() != null) cajaExistente.setCodigo(caja.getCodigo());
                    if (caja.getCantidad() != null) cajaExistente.setCantidad(caja.getCantidad());
                    if (caja.getLoteProducto() != null) cajaExistente.setLoteProducto(caja.getLoteProducto());
                    if (caja.getFechaVencimiento() != null) cajaExistente.setFechaVencimiento(caja.getFechaVencimiento());
                    if (caja.getTamano() != null) cajaExistente.setTamano(caja.getTamano());
                    if (caja.getPesoKg() != null) cajaExistente.setPesoKg(caja.getPesoKg());
                    if (caja.getSellada() != null) cajaExistente.setSellada(caja.getSellada());
                    if (caja.getObservaciones() != null) cajaExistente.setObservaciones(caja.getObservaciones());
                    Caja actualizada = cajaRepository.save(cajaExistente);
                    return ResponseEntity.ok(Map.of(
                            "success", true,
                            "message", "Caja actualizada exitosamente",
                            "data", actualizada));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Caja no encontrada")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable UUID id) {
        return cajaRepository.findById(id)
                .map(caja -> {
                    cajaRepository.delete(caja);
                    Map<String, Object> response = Map.of(
                            "success", true,
                            "message", "Caja eliminada exitosamente");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Caja no encontrada")));
    }
}
