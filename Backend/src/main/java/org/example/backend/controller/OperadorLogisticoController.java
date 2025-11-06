package org.example.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.backend.entity.OperadorLogistico;
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
@RequestMapping("/api/operadores-logisticos")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "false")
@Tag(name = "1. Operadores Logísticos", description = "Gestión de operadores logísticos que administran los almacenes")
public class OperadorLogisticoController {

    @Autowired
    private OperadorLogisticoRepository operadorLogisticoRepository;

    @Operation(summary = "Listar operadores logísticos", description = "Obtiene la lista completa de operadores logísticos registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarOperadores() {
        try {
            List<OperadorLogistico> operadores = operadorLogisticoRepository.findAll();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", operadores));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error al listar operadores logísticos: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable UUID id) {
        return operadorLogisticoRepository.findById(id)
                .map(operador -> ResponseEntity.ok(Map.of(
                        "success", true,
                        "data", operador)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Operador logístico no encontrado")));
    }

    @Operation(summary = "Crear operador logístico", description = "Registra un nuevo operador logístico en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Operador creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(
            @Parameter(description = "Datos del operador logístico a crear", required = true)
            @Valid @RequestBody OperadorLogistico operador) {
        try {
            OperadorLogistico nuevoOperador = operadorLogisticoRepository.save(operador);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "Operador logístico creado exitosamente",
                    "data", nuevoOperador));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error al crear operador logístico: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable UUID id,
            @Valid @RequestBody OperadorLogistico operador) {
        return operadorLogisticoRepository.findById(id)
                .map(operadorExistente -> {
                    operadorExistente.setNombre(operador.getNombre());
                    operadorExistente.setRuc(operador.getRuc());
                    operadorExistente.setDireccion(operador.getDireccion());
                    operadorExistente.setTelefono(operador.getTelefono());
                    operadorExistente.setEmail(operador.getEmail());
                    operadorExistente.setActivo(operador.getActivo());
                    OperadorLogistico actualizado = operadorLogisticoRepository.save(operadorExistente);
                    return ResponseEntity.ok(Map.of(
                            "success", true,
                            "message", "Operador logístico actualizado exitosamente",
                            "data", actualizado));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Operador logístico no encontrado")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable UUID id) {
        return operadorLogisticoRepository.findById(id)
                .map(operador -> {
                    operadorLogisticoRepository.delete(operador);
                    Map<String, Object> response = Map.of(
                            "success", true,
                            "message", "Operador logístico eliminado exitosamente");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Operador logístico no encontrado")));
    }
}
