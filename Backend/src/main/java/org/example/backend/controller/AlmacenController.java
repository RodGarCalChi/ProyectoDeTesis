package org.example.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.backend.dto.AlmacenRequest;
import org.example.backend.entity.Almacen;
import org.example.backend.entity.Cliente;
import org.example.backend.entity.OperadorLogistico;
import org.example.backend.repository.AlmacenRepository;
import org.example.backend.repository.ClienteRepository;
import org.example.backend.repository.OperadorLogisticoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/almacenes")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "false")
@Tag(name = "2. Almacenes", description = "Gestión de almacenes asignados a clientes y operadores logísticos")
public class AlmacenController {

    @Autowired
    private AlmacenRepository almacenRepository;

    @Autowired
    private OperadorLogisticoRepository operadorLogisticoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> listarAlmacenes() {
        try {
            List<Almacen> almacenes = almacenRepository.findAll();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", almacenes));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error al listar almacenes: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable UUID id) {
        return almacenRepository.findById(id)
                .map(almacen -> ResponseEntity.ok(Map.of(
                        "success", true,
                        "data", almacen)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear almacén", 
               description = "Crea un nuevo almacén asociado a un operador logístico y cliente. " +
                           "Requiere IDs válidos de operador logístico y cliente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Almacén creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o IDs no encontrados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(
            @Parameter(description = "Datos del almacén incluyendo IDs de operador y cliente", required = true)
            @Valid @RequestBody AlmacenRequest request) {
        try {
            // Buscar operador logístico
            OperadorLogistico operador = operadorLogisticoRepository.findById(request.getOperadorLogisticoId())
                    .orElseThrow(() -> new RuntimeException("Operador logístico no encontrado"));

            // Buscar cliente
            Cliente cliente = clienteRepository.findById(request.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

            // Crear almacén
            Almacen almacen = new Almacen();
            almacen.setNombre(request.getNombre());
            almacen.setDireccion(request.getDireccion());
            almacen.setOperadorLogistico(operador);
            almacen.setCliente(cliente);
            almacen.setTieneAreaControlados(request.getTieneAreaControlados());

            Almacen nuevoAlmacen = almacenRepository.save(almacen);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "Almacén creado exitosamente",
                    "data", nuevoAlmacen));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error al crear almacén: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable UUID id,
            @Valid @RequestBody AlmacenRequest request) {
        try {
            return almacenRepository.findById(id)
                    .map(almacenExistente -> {
                        almacenExistente.setNombre(request.getNombre());
                        almacenExistente.setDireccion(request.getDireccion());
                        almacenExistente.setTieneAreaControlados(request.getTieneAreaControlados());

                        // Actualizar operador logístico si cambió
                        if (request.getOperadorLogisticoId() != null) {
                            OperadorLogistico operador = operadorLogisticoRepository
                                    .findById(request.getOperadorLogisticoId())
                                    .orElseThrow(() -> new RuntimeException("Operador logístico no encontrado"));
                            almacenExistente.setOperadorLogistico(operador);
                        }

                        // Actualizar cliente si cambió
                        if (request.getClienteId() != null) {
                            Cliente cliente = clienteRepository.findById(request.getClienteId())
                                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
                            almacenExistente.setCliente(cliente);
                        }

                        Almacen actualizado = almacenRepository.save(almacenExistente);
                        return ResponseEntity.ok(Map.of(
                                "success", true,
                                "message", "Almacén actualizado exitosamente",
                                "data", actualizado));
                    })
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                            "success", false,
                            "message", "Almacén no encontrado")));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error al actualizar almacén: " + e.getMessage()));
        }
    }
}
