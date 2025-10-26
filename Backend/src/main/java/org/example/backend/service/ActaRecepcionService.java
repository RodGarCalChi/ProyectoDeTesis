package org.example.backend.service;

import org.example.backend.dto.*;
import org.example.backend.entity.ActaRecepcion;
import org.example.backend.entity.Cliente;
import org.example.backend.entity.DetalleActaRecepcion;
import org.example.backend.entity.Producto;
import org.example.backend.enumeraciones.EstadoDocumento;
import org.example.backend.repository.ActaRecepcionRepository;
import org.example.backend.repository.ClienteRepository;
import org.example.backend.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ActaRecepcionService {
    
    @Autowired
    private ActaRecepcionRepository actaRecepcionRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    // ========== FLUJO EN DOS PASOS ==========
    
    // PASO 1: Crear acta de recepción sin productos
    public ActaRecepcionDTO crearActaSinProductos(ActaRecepcionCreateDTO createDTO) {
        // Validar que no exista el número de acta
        if (actaRecepcionRepository.existsByNumeroActa(createDTO.getNumeroActa())) {
            throw new RuntimeException("Ya existe un acta con el número: " + createDTO.getNumeroActa());
        }
        
        // Buscar cliente
        Cliente cliente = clienteRepository.findById(createDTO.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + createDTO.getClienteId()));
        
        // Crear entidad sin productos
        ActaRecepcion acta = new ActaRecepcion(
                createDTO.getNumeroActa(),
                createDTO.getFechaRecepcion(),
                cliente,
                createDTO.getResponsableRecepcion(),
                createDTO.getLugarRecepcion(),
                createDTO.getTemperaturaLlegada(),
                createDTO.getCondicionesTransporte(),
                createDTO.getObservaciones(),
                createDTO.getEstado() != null ? createDTO.getEstado() : EstadoDocumento.BORRADOR,
                createDTO.getCreadoPor()
        );
        
        // Guardar acta
        ActaRecepcion actaGuardada = actaRecepcionRepository.save(acta);
        
        return convertirADTO(actaGuardada);
    }
    
    // PASO 2: Agregar producto al acta existente
    public ActaRecepcionDTO agregarProductoAlActa(UUID actaId, AgregarDetalleActaDTO detalleDTO) {
        // Buscar acta
        ActaRecepcion acta = actaRecepcionRepository.findById(actaId)
                .orElseThrow(() -> new RuntimeException("Acta no encontrada con ID: " + actaId));
        
        // Validar que el acta esté en estado BORRADOR
        if (acta.getEstado() != EstadoDocumento.BORRADOR) {
            throw new RuntimeException("Solo se pueden agregar productos a actas en estado BORRADOR");
        }
        
        // Buscar producto
        Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + detalleDTO.getProductoId()));
        
        // Crear detalle (convertir Integer a BigDecimal)
        DetalleActaRecepcion detalle = new DetalleActaRecepcion(
                acta,
                producto,
                detalleDTO.getLote(),
                detalleDTO.getFechaVencimiento(),
                new java.math.BigDecimal(detalleDTO.getCantidadDeclarada()),
                new java.math.BigDecimal(detalleDTO.getCantidadRecibida()),
                detalleDTO.getPrecioUnitario(),
                detalleDTO.getObservaciones(),
                detalleDTO.getConforme()
        );
        
        // Agregar detalle al acta
        if (acta.getDetalles() == null) {
            acta.setDetalles(new java.util.ArrayList<>());
        }
        acta.getDetalles().add(detalle);
        
        // Guardar
        ActaRecepcion actaActualizada = actaRecepcionRepository.save(acta);
        
        return convertirADTO(actaActualizada);
    }
    
    // Actualizar solo datos generales del acta (sin tocar productos)
    public ActaRecepcionDTO actualizarDatosGenerales(UUID actaId, ActaRecepcionUpdateDTO updateDTO) {
        ActaRecepcion acta = actaRecepcionRepository.findById(actaId)
                .orElseThrow(() -> new RuntimeException("Acta no encontrada con ID: " + actaId));
        
        // Validar que el acta esté en estado BORRADOR
        if (acta.getEstado() != EstadoDocumento.BORRADOR) {
            throw new RuntimeException("Solo se pueden modificar actas en estado BORRADOR");
        }
        
        // Validar que no se cambie a un número que ya existe
        if (!acta.getNumeroActa().equals(updateDTO.getNumeroActa()) &&
            actaRecepcionRepository.existsByNumeroActa(updateDTO.getNumeroActa())) {
            throw new RuntimeException("Ya existe un acta con el número: " + updateDTO.getNumeroActa());
        }
        
        // Actualizar campos generales (sin tocar detalles)
        acta.setNumeroActa(updateDTO.getNumeroActa());
        acta.setFechaRecepcion(updateDTO.getFechaRecepcion());
        acta.setResponsableRecepcion(updateDTO.getResponsableRecepcion());
        acta.setLugarRecepcion(updateDTO.getLugarRecepcion());
        acta.setTemperaturaLlegada(updateDTO.getTemperaturaLlegada());
        acta.setCondicionesTransporte(updateDTO.getCondicionesTransporte());
        acta.setObservaciones(updateDTO.getObservaciones());
        
        if (updateDTO.getEstado() != null) {
            acta.setEstado(updateDTO.getEstado());
        }
        
        if (updateDTO.getActualizadoPor() != null) {
            acta.setActualizadoPor(updateDTO.getActualizadoPor());
        }
        
        // Actualizar cliente si cambió
        if (!acta.getCliente().getId().equals(updateDTO.getClienteId())) {
            Cliente cliente = clienteRepository.findById(updateDTO.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + updateDTO.getClienteId()));
            acta.setCliente(cliente);
        }
        
        acta = actaRecepcionRepository.save(acta);
        return convertirADTO(acta);
    }
    
    // Eliminar un producto del acta
    public ActaRecepcionDTO eliminarProductoDelActa(UUID actaId, UUID detalleId) {
        ActaRecepcion acta = actaRecepcionRepository.findById(actaId)
                .orElseThrow(() -> new RuntimeException("Acta no encontrada con ID: " + actaId));
        
        // Validar que el acta esté en estado BORRADOR
        if (acta.getEstado() != EstadoDocumento.BORRADOR) {
            throw new RuntimeException("Solo se pueden eliminar productos de actas en estado BORRADOR");
        }
        
        // Buscar y eliminar el detalle
        boolean removed = acta.getDetalles().removeIf(detalle -> detalle.getId().equals(detalleId));
        
        if (!removed) {
            throw new RuntimeException("Detalle no encontrado con ID: " + detalleId);
        }
        
        // Guardar
        ActaRecepcion actaActualizada = actaRecepcionRepository.save(acta);
        
        return convertirADTO(actaActualizada);
    }
    
    // ========== MÉTODOS ORIGINALES (Compatibilidad) ==========
    
    // Crear nueva acta de recepción (con productos - método original)
    public ActaRecepcionDTO crearActaRecepcion(ActaRecepcionDTO actaDTO) {
        // Validar que no exista el número de acta
        if (actaRecepcionRepository.existsByNumeroActa(actaDTO.getNumeroActa())) {
            throw new RuntimeException("Ya existe un acta con el número: " + actaDTO.getNumeroActa());
        }
        
        // Buscar cliente
        Cliente cliente = clienteRepository.findById(actaDTO.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + actaDTO.getClienteId()));
        
        // Crear entidad
        ActaRecepcion acta = new ActaRecepcion(
                actaDTO.getNumeroActa(),
                actaDTO.getFechaRecepcion(),
                cliente,
                actaDTO.getResponsableRecepcion(),
                actaDTO.getLugarRecepcion(),
                actaDTO.getTemperaturaLlegada(),
                actaDTO.getCondicionesTransporte(),
                actaDTO.getObservaciones(),
                actaDTO.getEstado() != null ? actaDTO.getEstado() : EstadoDocumento.BORRADOR,
                actaDTO.getCreadoPor()
        );
        
        // Guardar acta
        ActaRecepcion actaGuardada = actaRecepcionRepository.save(acta);
        
        // Procesar detalles si existen
        if (actaDTO.getDetalles() != null && !actaDTO.getDetalles().isEmpty()) {
            List<DetalleActaRecepcion> detalles = actaDTO.getDetalles().stream()
                    .map(detalleDTO -> crearDetalleActaRecepcion(actaGuardada, detalleDTO))
                    .collect(Collectors.toList());
            actaGuardada.setDetalles(detalles);
        }
        
        return convertirADTO(actaGuardada);
    }
    
    // Obtener acta por ID
    @Transactional(readOnly = true)
    public Optional<ActaRecepcionDTO> obtenerActaPorId(UUID id) {
        return actaRecepcionRepository.findById(id)
                .map(this::convertirADTO);
    }
    
    // Obtener acta por número
    @Transactional(readOnly = true)
    public Optional<ActaRecepcionDTO> obtenerActaPorNumero(String numeroActa) {
        return actaRecepcionRepository.findByNumeroActa(numeroActa)
                .map(this::convertirADTO);
    }
    
    // Obtener todas las actas con paginación
    @Transactional(readOnly = true)
    public Page<ActaRecepcionDTO> obtenerActasPaginadas(Pageable pageable) {
        return actaRecepcionRepository.findAll(pageable)
                .map(this::convertirADTO);
    }
    
    // Buscar actas con filtros
    @Transactional(readOnly = true)
    public Page<ActaRecepcionDTO> buscarActasConFiltros(String numeroActa, UUID clienteId,
                                                       EstadoDocumento estado, String responsable,
                                                       LocalDateTime fechaInicio, LocalDateTime fechaFin,
                                                       Pageable pageable) {
        return actaRecepcionRepository.findWithFilters(numeroActa, clienteId, estado, responsable,
                fechaInicio, fechaFin, pageable)
                .map(this::convertirADTO);
    }
    
    // Obtener actas por cliente
    @Transactional(readOnly = true)
    public List<ActaRecepcionDTO> obtenerActasPorCliente(UUID clienteId) {
        return actaRecepcionRepository.findByClienteId(clienteId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    // Obtener actas por estado
    @Transactional(readOnly = true)
    public List<ActaRecepcionDTO> obtenerActasPorEstado(EstadoDocumento estado) {
        return actaRecepcionRepository.findByEstado(estado).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    // Actualizar acta de recepción
    public ActaRecepcionDTO actualizarActaRecepcion(UUID id, ActaRecepcionDTO actaDTO) {
        ActaRecepcion acta = actaRecepcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Acta no encontrada con ID: " + id));
        
        // Validar que no se cambie a un número que ya existe
        if (!acta.getNumeroActa().equals(actaDTO.getNumeroActa()) &&
            actaRecepcionRepository.existsByNumeroActa(actaDTO.getNumeroActa())) {
            throw new RuntimeException("Ya existe un acta con el número: " + actaDTO.getNumeroActa());
        }
        
        // Actualizar campos
        acta.setNumeroActa(actaDTO.getNumeroActa());
        acta.setFechaRecepcion(actaDTO.getFechaRecepcion());
        acta.setResponsableRecepcion(actaDTO.getResponsableRecepcion());
        acta.setLugarRecepcion(actaDTO.getLugarRecepcion());
        acta.setTemperaturaLlegada(actaDTO.getTemperaturaLlegada());
        acta.setCondicionesTransporte(actaDTO.getCondicionesTransporte());
        acta.setObservaciones(actaDTO.getObservaciones());
        acta.setEstado(actaDTO.getEstado());
        acta.setActualizadoPor(actaDTO.getActualizadoPor());
        
        // Actualizar cliente si cambió
        if (!acta.getCliente().getId().equals(actaDTO.getClienteId())) {
            Cliente cliente = clienteRepository.findById(actaDTO.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + actaDTO.getClienteId()));
            acta.setCliente(cliente);
        }
        
        acta = actaRecepcionRepository.save(acta);
        return convertirADTO(acta);
    }
    
    // Cambiar estado del acta
    public ActaRecepcionDTO cambiarEstadoActa(UUID id, EstadoDocumento nuevoEstado, String actualizadoPor) {
        ActaRecepcion acta = actaRecepcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Acta no encontrada con ID: " + id));
        
        acta.setEstado(nuevoEstado);
        acta.setActualizadoPor(actualizadoPor);
        
        acta = actaRecepcionRepository.save(acta);
        return convertirADTO(acta);
    }
    
    // Aprobar acta
    public ActaRecepcionDTO aprobarActa(UUID id, String aprobadoPor) {
        return cambiarEstadoActa(id, EstadoDocumento.APROBADO, aprobadoPor);
    }
    
    // Rechazar acta
    public ActaRecepcionDTO rechazarActa(UUID id, String rechazadoPor) {
        return cambiarEstadoActa(id, EstadoDocumento.RECHAZADO, rechazadoPor);
    }
    
    // Obtener estadísticas por estado
    @Transactional(readOnly = true)
    public List<Object[]> obtenerEstadisticasPorEstado() {
        return actaRecepcionRepository.countByEstado();
    }
    
    // Obtener estadísticas por cliente
    @Transactional(readOnly = true)
    public List<Object[]> obtenerEstadisticasPorCliente() {
        return actaRecepcionRepository.countByCliente();
    }
    
    // Obtener actas pendientes de aprobación
    @Transactional(readOnly = true)
    public List<ActaRecepcionDTO> obtenerActasPendientesAprobacion() {
        return actaRecepcionRepository.findPendingApproval().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    // Métodos auxiliares
    private DetalleActaRecepcion crearDetalleActaRecepcion(ActaRecepcion acta, DetalleActaRecepcionDTO detalleDTO) {
        Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + detalleDTO.getProductoId()));
        
        return new DetalleActaRecepcion(
                acta,
                producto,
                detalleDTO.getLote(),
                detalleDTO.getFechaVencimiento(),
                detalleDTO.getCantidadDeclarada(),
                detalleDTO.getCantidadRecibida(),
                detalleDTO.getPrecioUnitario(),
                detalleDTO.getObservaciones(),
                detalleDTO.getConforme()
        );
    }
    
    private ActaRecepcionDTO convertirADTO(ActaRecepcion acta) {
        ActaRecepcionDTO dto = new ActaRecepcionDTO(
                acta.getId(),
                acta.getNumeroActa(),
                acta.getFechaRecepcion(),
                acta.getCliente().getId(),
                acta.getCliente().getRazonSocial(),
                acta.getResponsableRecepcion(),
                acta.getLugarRecepcion(),
                acta.getTemperaturaLlegada(),
                acta.getCondicionesTransporte(),
                acta.getObservaciones(),
                acta.getEstado(),
                acta.getFechaCreacion(),
                acta.getFechaActualizacion(),
                acta.getCreadoPor(),
                acta.getActualizadoPor()
        );
        
        // Convertir detalles si existen
        if (acta.getDetalles() != null && !acta.getDetalles().isEmpty()) {
            List<DetalleActaRecepcionDTO> detallesDTO = acta.getDetalles().stream()
                    .map(this::convertirDetalleADTO)
                    .collect(Collectors.toList());
            dto.setDetalles(detallesDTO);
        }
        
        return dto;
    }
    
    private DetalleActaRecepcionDTO convertirDetalleADTO(DetalleActaRecepcion detalle) {
        return new DetalleActaRecepcionDTO(
                detalle.getId(),
                detalle.getProducto().getId(),
                detalle.getProducto().getNombre(),
                detalle.getProducto().getCodigoSKU(),
                detalle.getLote(),
                detalle.getFechaVencimiento(),
                detalle.getCantidadDeclarada(),
                detalle.getCantidadRecibida(),
                detalle.getCantidadConforme(),
                detalle.getCantidadNoConforme(),
                detalle.getPrecioUnitario(),
                detalle.getObservaciones(),
                detalle.getConforme(),
                detalle.getMotivoNoConformidad()
        );
    }
}