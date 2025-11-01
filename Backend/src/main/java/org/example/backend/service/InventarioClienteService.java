package org.example.backend.service;

import org.example.backend.dto.InventarioClienteDTO;
import org.example.backend.entity.*;
import org.example.backend.enumeraciones.EstadoInventario;
import org.example.backend.enumeraciones.EstadoRecepcion;
import org.example.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventarioClienteService {

    @Autowired
    private InventarioClienteRepository inventarioRepository;

    @Autowired
    private RecepcionMercaderiaRepository recepcionRepository;

    @Autowired
    private LoteRepository loteRepository;

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Autowired
    private HistorialUbicacionRepository historialUbicacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * FASE 2: Aprobar acta y registrar productos en inventario
     */
    public List<InventarioClienteDTO> aprobarActaYRegistrar(UUID recepcionId, String usuarioNombre,
            String observaciones) {
        // Buscar la recepción
        RecepcionMercaderia recepcion = recepcionRepository.findById(recepcionId)
                .orElseThrow(() -> new RuntimeException("Recepción no encontrada"));

        // Validar estado
        if (recepcion.getEstado() != EstadoRecepcion.EN_CUARENTENA) {
            throw new RuntimeException("La recepción debe estar en estado EN_CUARENTENA");
        }

        // Buscar usuario por email (simplificado - en producción buscar por ID)
        Usuario usuario = usuarioRepository.findByEmail(usuarioNombre)
                .orElse(null);

        // Obtener detalles conformes
        List<DetalleRecepcion> detallesConformes = recepcion.getDetalles().stream()
                .filter(DetalleRecepcion::getConforme)
                .collect(Collectors.toList());

        if (detallesConformes.isEmpty()) {
            throw new RuntimeException("No hay productos conformes para registrar");
        }

        // Registrar cada producto conforme en el inventario
        List<InventarioCliente> inventariosCreados = detallesConformes.stream()
                .map(detalle -> {
                    // Crear o buscar lote
                    Lote lote = loteRepository.findByNumero(detalle.getLote().getNumero())
                            .orElseGet(() -> {
                                Lote nuevoLote = new Lote();
                                nuevoLote.setNumero(detalle.getLote().getNumero());
                                nuevoLote.setProducto(detalle.getProducto());
                                nuevoLote.setFechaVencimiento(detalle.getFechaVencimiento());
                                return loteRepository.save(nuevoLote);
                            });

                    // Crear registro en inventario
                    InventarioCliente inventario = new InventarioCliente();
                    inventario.setCliente(recepcion.getCliente());
                    inventario.setProducto(detalle.getProducto());
                    inventario.setLote(lote);
                    inventario.setRecepcion(recepcion);
                    inventario.setDetalleRecepcion(detalle);
                    inventario.setCantidadDisponible(detalle.getCantidadAceptada());
                    inventario.setCantidadReservada(0);
                    inventario.setCantidadDespachada(0);
                    inventario.setFechaIngreso(LocalDateTime.now());
                    inventario.setFechaVencimiento(detalle.getFechaVencimiento());
                    inventario.setEstado(EstadoInventario.PENDIENTE_UBICACION);
                    inventario.setTemperaturaAlmacenamiento(detalle.getTemperaturaRecepcion());
                    inventario.setObservaciones(detalle.getObservaciones());
                    inventario.setUsuarioRegistro(usuario);

                    return inventarioRepository.save(inventario);
                })
                .collect(Collectors.toList());

        // Actualizar estado de la recepción
        recepcion.setEstado(EstadoRecepcion.APROBADO);
        recepcion.setAprobadoPorCalidad(true);
        if (observaciones != null) {
            String obs = recepcion.getObservaciones() != null ? recepcion.getObservaciones() : "";
            recepcion.setObservaciones(obs + " | APROBADO: " + observaciones);
        }
        recepcionRepository.save(recepcion);

        // Convertir a DTO
        return inventariosCreados.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * FASE 3: Asignar ubicación física
     */
    public InventarioClienteDTO asignarUbicacion(UUID inventarioId, UUID ubicacionId,
            String codigoBarras, String usuarioNombre,
            String observaciones) {
        // Buscar inventario
        InventarioCliente inventario = inventarioRepository.findById(inventarioId)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));

        // Validar estado
        if (inventario.getEstado() != EstadoInventario.PENDIENTE_UBICACION) {
            throw new RuntimeException("El inventario debe estar en estado PENDIENTE_UBICACION");
        }

        // Buscar ubicación
        Ubicacion ubicacion = ubicacionRepository.findById(ubicacionId)
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));

        // Buscar usuario por email
        Usuario usuario = usuarioRepository.findByEmail(usuarioNombre)
                .orElse(null);

        // Guardar ubicación anterior para historial
        Ubicacion ubicacionAnterior = inventario.getUbicacion();

        // Actualizar inventario
        inventario.setUbicacion(ubicacion);
        inventario.setCodigoBarras(codigoBarras);
        inventario.setEstado(EstadoInventario.ALMACENADO);
        inventario.setUsuarioUbicacion(usuario);
        if (observaciones != null) {
            String obs = inventario.getObservaciones() != null ? inventario.getObservaciones() : "";
            inventario.setObservaciones(obs + " | UBICACIÓN: " + observaciones);
        }

        InventarioCliente inventarioActualizado = inventarioRepository.save(inventario);

        // Registrar en historial
        HistorialUbicacion historial = new HistorialUbicacion();
        historial.setInventarioCliente(inventario);
        historial.setUbicacionAnterior(ubicacionAnterior);
        historial.setUbicacionNueva(ubicacion);
        historial.setMotivo(observaciones != null ? observaciones : "Asignación inicial");
        historial.setUsuario(usuario);
        historialUbicacionRepository.save(historial);

        return convertToDTO(inventarioActualizado);
    }

    /**
     * Obtener productos pendientes de ubicación
     */
    @Transactional(readOnly = true)
    public List<InventarioClienteDTO> obtenerPendientesUbicacion() {
        return inventarioRepository.findPendientesUbicacion().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener inventario por cliente
     */
    @Transactional(readOnly = true)
    public List<InventarioClienteDTO> obtenerPorCliente(UUID clienteId) {
        return inventarioRepository.findByClienteId(clienteId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener por ID
     */
    @Transactional(readOnly = true)
    public InventarioClienteDTO obtenerPorId(UUID id) {
        return inventarioRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));
    }

    /**
     * Convertir entidad a DTO
     */
    private InventarioClienteDTO convertToDTO(InventarioCliente inventario) {
        InventarioClienteDTO dto = new InventarioClienteDTO();
        dto.setId(inventario.getId());

        if (inventario.getCliente() != null) {
            dto.setClienteId(inventario.getCliente().getId());
            dto.setClienteNombre(inventario.getCliente().getRazonSocial());
        }

        if (inventario.getProducto() != null) {
            dto.setProductoId(inventario.getProducto().getId());
            dto.setProductoNombre(inventario.getProducto().getNombre());
            dto.setProductoSku(inventario.getProducto().getCodigoSKU());
        }

        if (inventario.getLote() != null) {
            dto.setLoteId(inventario.getLote().getId());
            dto.setLoteNumero(inventario.getLote().getNumero());
        }

        if (inventario.getRecepcion() != null) {
            dto.setRecepcionId(inventario.getRecepcion().getId());
            dto.setNumeroOrdenCompra(inventario.getRecepcion().getNumeroOrdenCompra());
        }

        if (inventario.getDetalleRecepcion() != null) {
            dto.setDetalleRecepcionId(inventario.getDetalleRecepcion().getId());
        }

        dto.setCantidadDisponible(inventario.getCantidadDisponible());
        dto.setCantidadReservada(inventario.getCantidadReservada());
        dto.setCantidadDespachada(inventario.getCantidadDespachada());

        if (inventario.getUbicacion() != null) {
            dto.setUbicacionId(inventario.getUbicacion().getId());
            dto.setUbicacionCodigo(inventario.getUbicacion().getCodigo());
        }

        dto.setCodigoBarras(inventario.getCodigoBarras());
        dto.setFechaIngreso(inventario.getFechaIngreso());
        dto.setFechaVencimiento(inventario.getFechaVencimiento());
        dto.setEstado(inventario.getEstado());
        dto.setTemperaturaAlmacenamiento(inventario.getTemperaturaAlmacenamiento());
        dto.setObservaciones(inventario.getObservaciones());

        if (inventario.getUsuarioRegistro() != null) {
            dto.setUsuarioRegistroId(inventario.getUsuarioRegistro().getId());
            dto.setUsuarioRegistroNombre(inventario.getUsuarioRegistro().getNombres() + " " +
                    inventario.getUsuarioRegistro().getApellidos());
        }

        if (inventario.getUsuarioUbicacion() != null) {
            dto.setUsuarioUbicacionId(inventario.getUsuarioUbicacion().getId());
            dto.setUsuarioUbicacionNombre(inventario.getUsuarioUbicacion().getNombres() + " " +
                    inventario.getUsuarioUbicacion().getApellidos());
        }

        dto.setFechaCreacion(inventario.getFechaCreacion());
        dto.setFechaActualizacion(inventario.getFechaActualizacion());

        return dto;
    }
}
