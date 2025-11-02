package org.example.backend.service;

import org.example.backend.dto.RecepcionMercaderiaDTO;
import org.example.backend.dto.DetalleRecepcionDTO;
import org.example.backend.entity.RecepcionMercaderia;
import org.example.backend.entity.DetalleRecepcion;
import org.example.backend.entity.Producto;
import org.example.backend.entity.Lote;
import org.example.backend.entity.Cliente;
import org.example.backend.enumeraciones.EstadoRecepcion;
import org.example.backend.repository.RecepcionMercaderiaRepository;
import org.example.backend.repository.ProductoRepository;
import org.example.backend.repository.LoteRepository;
import org.example.backend.repository.ClienteRepository;
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
public class RecepcionMercaderiaService {

    @Autowired
    private RecepcionMercaderiaRepository recepcionRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private LoteRepository loteRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // Crear nueva recepción
    public RecepcionMercaderiaDTO crearRecepcion(RecepcionMercaderiaDTO recepcionDTO) {
        // Verificar que no exista la orden de compra
        if (recepcionRepository.existsByNumeroOrdenCompra(recepcionDTO.getNumeroOrdenCompra())) {
            throw new RuntimeException("Ya existe una recepción con el número de orden: " +
                    recepcionDTO.getNumeroOrdenCompra());
        }

        // Obtener cliente por ID
        Cliente cliente = clienteRepository.findById(recepcionDTO.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Crear entidad
        RecepcionMercaderia recepcion = new RecepcionMercaderia();
        recepcion.setNumeroOrdenCompra(recepcionDTO.getNumeroOrdenCompra());
        recepcion.setNumeroGuiaRemision(recepcionDTO.getNumeroGuiaRemision());
        recepcion.setCliente(cliente);
        recepcion.setFechaRecepcion(recepcionDTO.getFechaRecepcion());
        recepcion.setResponsableRecepcion(recepcionDTO.getResponsableRecepcion());
        recepcion.setEstado(EstadoRecepcion.PENDIENTE);
        recepcion.setTemperaturaLlegada(recepcionDTO.getTemperaturaLlegada());
        recepcion.setObservaciones(recepcionDTO.getObservaciones());

        // Procesar detalles si existen
        if (recepcionDTO.getDetalles() != null) {
            for (DetalleRecepcionDTO detalleDTO : recepcionDTO.getDetalles()) {
                DetalleRecepcion detalle = crearDetalleFromDTO(detalleDTO);
                recepcion.addDetalle(detalle);
            }
        }

        RecepcionMercaderia savedRecepcion = recepcionRepository.save(recepcion);
        return convertToDTO(savedRecepcion);
    }

    // Obtener todas las recepciones
    @Transactional(readOnly = true)
    public Page<RecepcionMercaderiaDTO> obtenerRecepcionesPaginadas(Pageable pageable) {
        return recepcionRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    // Obtener recepción por ID
    @Transactional(readOnly = true)
    public Optional<RecepcionMercaderiaDTO> obtenerRecepcionPorId(UUID id) {
        return recepcionRepository.findById(id)
                .map(this::convertToDTO);
    }

    // Buscar recepciones con filtros
    @Transactional(readOnly = true)
    public Page<RecepcionMercaderiaDTO> buscarRecepcionesConFiltros(
            String numeroOrden, String numeroGuia, EstadoRecepcion estado,
            UUID clienteId, String responsable, Pageable pageable) {
        return recepcionRepository.findRecepcionesWithFilters(
                numeroOrden, numeroGuia, estado, clienteId, responsable, pageable)
                .map(this::convertToDTO);
    }

    // Obtener recepciones por estado
    @Transactional(readOnly = true)
    public List<RecepcionMercaderiaDTO> obtenerRecepcionesPorEstado(EstadoRecepcion estado) {
        return recepcionRepository.findByEstado(estado).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Iniciar verificación de recepción
    public RecepcionMercaderiaDTO iniciarVerificacion(UUID recepcionId, String inspector) {
        RecepcionMercaderia recepcion = recepcionRepository.findById(recepcionId)
                .orElseThrow(() -> new RuntimeException("Recepción no encontrada"));

        if (recepcion.getEstado() != EstadoRecepcion.PENDIENTE) {
            throw new RuntimeException("La recepción no está en estado pendiente");
        }

        recepcion.setEstado(EstadoRecepcion.EN_VERIFICACION);
        recepcion.setInspectorCalidad(inspector);
        recepcion.setFechaInspeccion(LocalDateTime.now());

        RecepcionMercaderia updatedRecepcion = recepcionRepository.save(recepcion);
        return convertToDTO(updatedRecepcion);
    }

    // Completar verificación documental
    public RecepcionMercaderiaDTO completarVerificacionDocumental(UUID recepcionId, boolean aprobado) {
        RecepcionMercaderia recepcion = recepcionRepository.findById(recepcionId)
                .orElseThrow(() -> new RuntimeException("Recepción no encontrada"));

        recepcion.setVerificacionDocumentos(aprobado);

        if (!aprobado) {
            recepcion.setEstado(EstadoRecepcion.RECHAZADO);
        }

        RecepcionMercaderia updatedRecepcion = recepcionRepository.save(recepcion);
        return convertToDTO(updatedRecepcion);
    }

    // Completar verificación física
    public RecepcionMercaderiaDTO completarVerificacionFisica(UUID recepcionId, boolean aprobado) {
        RecepcionMercaderia recepcion = recepcionRepository.findById(recepcionId)
                .orElseThrow(() -> new RuntimeException("Recepción no encontrada"));

        recepcion.setVerificacionFisica(aprobado);

        if (!aprobado) {
            recepcion.setEstado(EstadoRecepcion.RECHAZADO);
        }

        RecepcionMercaderia updatedRecepcion = recepcionRepository.save(recepcion);
        return convertToDTO(updatedRecepcion);
    }

    // Completar verificación de temperatura
    public RecepcionMercaderiaDTO completarVerificacionTemperatura(UUID recepcionId, boolean aprobado) {
        RecepcionMercaderia recepcion = recepcionRepository.findById(recepcionId)
                .orElseThrow(() -> new RuntimeException("Recepción no encontrada"));

        recepcion.setVerificacionTemperatura(aprobado);

        if (!aprobado) {
            recepcion.setEstado(EstadoRecepcion.RECHAZADO);
        }

        RecepcionMercaderia updatedRecepcion = recepcionRepository.save(recepcion);
        return convertToDTO(updatedRecepcion);
    }

    // Enviar a cuarentena
    public RecepcionMercaderiaDTO enviarACuarentena(UUID recepcionId) {
        RecepcionMercaderia recepcion = recepcionRepository.findById(recepcionId)
                .orElseThrow(() -> new RuntimeException("Recepción no encontrada"));

        recepcion.setEstado(EstadoRecepcion.EN_CUARENTENA);

        RecepcionMercaderia updatedRecepcion = recepcionRepository.save(recepcion);
        return convertToDTO(updatedRecepcion);
    }

    // Aprobar por control de calidad
    public RecepcionMercaderiaDTO aprobarPorCalidad(UUID recepcionId, String inspector) {
        RecepcionMercaderia recepcion = recepcionRepository.findById(recepcionId)
                .orElseThrow(() -> new RuntimeException("Recepción no encontrada"));

        recepcion.setAprobadoPorCalidad(true);
        recepcion.setInspectorCalidad(inspector);
        recepcion.setFechaInspeccion(LocalDateTime.now());
        recepcion.setEstado(EstadoRecepcion.APROBADO);

        RecepcionMercaderia updatedRecepcion = recepcionRepository.save(recepcion);
        return convertToDTO(updatedRecepcion);
    }

    // Rechazar por control de calidad
    public RecepcionMercaderiaDTO rechazarPorCalidad(UUID recepcionId, String inspector, String motivo) {
        RecepcionMercaderia recepcion = recepcionRepository.findById(recepcionId)
                .orElseThrow(() -> new RuntimeException("Recepción no encontrada"));

        recepcion.setAprobadoPorCalidad(false);
        recepcion.setInspectorCalidad(inspector);
        recepcion.setFechaInspeccion(LocalDateTime.now());
        recepcion.setEstado(EstadoRecepcion.RECHAZADO);
        recepcion.setObservaciones(recepcion.getObservaciones() + " | RECHAZADO: " + motivo);

        RecepcionMercaderia updatedRecepcion = recepcionRepository.save(recepcion);
        return convertToDTO(updatedRecepcion);
    }

    // Almacenar productos aprobados
    public RecepcionMercaderiaDTO almacenarProductos(UUID recepcionId) {
        RecepcionMercaderia recepcion = recepcionRepository.findById(recepcionId)
                .orElseThrow(() -> new RuntimeException("Recepción no encontrada"));

        if (recepcion.getEstado() != EstadoRecepcion.APROBADO) {
            throw new RuntimeException("La recepción debe estar aprobada para almacenar");
        }

        recepcion.setEstado(EstadoRecepcion.ALMACENADO);

        // Aquí se actualizaría el inventario con los productos recibidos

        RecepcionMercaderia updatedRecepcion = recepcionRepository.save(recepcion);
        return convertToDTO(updatedRecepcion);
    }

    // Obtener estadísticas
    @Transactional(readOnly = true)
    public List<Object[]> obtenerEstadisticasPorEstado() {
        return recepcionRepository.countRecepcionesByEstado();
    }

    // Obtener recepciones pendientes
    @Transactional(readOnly = true)
    public List<RecepcionMercaderiaDTO> obtenerRecepcionesPendientes() {
        return recepcionRepository.findRecepcionesPendientesVerificacion().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener recepciones en cuarentena
    @Transactional(readOnly = true)
    public List<RecepcionMercaderiaDTO> obtenerRecepcionesEnCuarentena() {
        return recepcionRepository.findRecepcionesEnCuarentena().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Métodos auxiliares
    private DetalleRecepcion crearDetalleFromDTO(DetalleRecepcionDTO dto) {
        DetalleRecepcion detalle = new DetalleRecepcion();

        // Obtener producto y lote por ID
        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        Lote lote = loteRepository.findById(dto.getLoteId())
                .orElseThrow(() -> new RuntimeException("Lote no encontrado"));

        detalle.setProducto(producto);
        detalle.setLote(lote);
        detalle.setCantidadEsperada(dto.getCantidadEsperada());
        detalle.setCantidadRecibida(dto.getCantidadRecibida());
        detalle.setCantidadAceptada(dto.getCantidadAceptada());
        detalle.setCantidadRechazada(dto.getCantidadRechazada());
        detalle.setFechaVencimiento(dto.getFechaVencimiento());
        detalle.setPrecioUnitario(dto.getPrecioUnitario());
        detalle.setTemperaturaRecepcion(dto.getTemperaturaRecepcion());
        detalle.setObservaciones(dto.getObservaciones());
        detalle.setConforme(dto.getConforme());
        detalle.setMotivoRechazo(dto.getMotivoRechazo());

        return detalle;
    }

    private RecepcionMercaderiaDTO convertToDTO(RecepcionMercaderia recepcion) {
        RecepcionMercaderiaDTO dto = new RecepcionMercaderiaDTO();
        dto.setId(recepcion.getId());
        dto.setNumeroOrdenCompra(recepcion.getNumeroOrdenCompra());
        dto.setNumeroGuiaRemision(recepcion.getNumeroGuiaRemision());

        if (recepcion.getCliente() != null) {
            dto.setClienteId(recepcion.getCliente().getId());
            dto.setClienteNombre(recepcion.getCliente().getRazonSocial());
        }

        dto.setFechaRecepcion(recepcion.getFechaRecepcion());
        dto.setResponsableRecepcion(recepcion.getResponsableRecepcion());
        dto.setEstado(recepcion.getEstado());
        dto.setTemperaturaLlegada(recepcion.getTemperaturaLlegada());
        dto.setObservaciones(recepcion.getObservaciones());
        dto.setVerificacionDocumentos(recepcion.getVerificacionDocumentos());
        dto.setVerificacionFisica(recepcion.getVerificacionFisica());
        dto.setVerificacionTemperatura(recepcion.getVerificacionTemperatura());
        dto.setAprobadoPorCalidad(recepcion.getAprobadoPorCalidad());
        dto.setInspectorCalidad(recepcion.getInspectorCalidad());
        dto.setFechaInspeccion(recepcion.getFechaInspeccion());
        dto.setFechaCreacion(recepcion.getFechaCreacion());
        dto.setFechaActualizacion(recepcion.getFechaActualizacion());

        // Convertir detalles
        if (recepcion.getDetalles() != null) {
            List<DetalleRecepcionDTO> detallesDTO = recepcion.getDetalles().stream()
                    .map(this::convertDetalleToDTO)
                    .collect(Collectors.toList());
            dto.setDetalles(detallesDTO);
        }

        return dto;
    }

    private DetalleRecepcionDTO convertDetalleToDTO(DetalleRecepcion detalle) {
        DetalleRecepcionDTO dto = new DetalleRecepcionDTO();
        dto.setId(detalle.getId());

        if (detalle.getProducto() != null) {
            dto.setProductoId(detalle.getProducto().getId());
            dto.setProductoNombre(detalle.getProducto().getNombre());
            dto.setProductoSku(detalle.getProducto().getCodigoSKU());
        }

        if (detalle.getLote() != null) {
            dto.setLoteId(detalle.getLote().getId());
            dto.setLoteNumero(detalle.getLote().getNumero());
        }

        dto.setCantidadEsperada(detalle.getCantidadEsperada());
        dto.setCantidadRecibida(detalle.getCantidadRecibida());
        dto.setCantidadAceptada(detalle.getCantidadAceptada());
        dto.setCantidadRechazada(detalle.getCantidadRechazada());
        dto.setFechaVencimiento(detalle.getFechaVencimiento());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setTemperaturaRecepcion(detalle.getTemperaturaRecepcion());
        dto.setObservaciones(detalle.getObservaciones());
        dto.setConforme(detalle.getConforme());
        dto.setMotivoRechazo(detalle.getMotivoRechazo());

        return dto;
    }

    // Cambiar estado de la recepción directamente
    public RecepcionMercaderiaDTO cambiarEstado(UUID recepcionId, EstadoRecepcion nuevoEstado, String actualizadoPor) {
        RecepcionMercaderia recepcion = recepcionRepository.findById(recepcionId)
                .orElseThrow(() -> new RuntimeException("Recepción no encontrada"));

        // Validar transición de estado si es necesario
        EstadoRecepcion estadoAnterior = recepcion.getEstado();
        
        // Actualizar estado
        recepcion.setEstado(nuevoEstado);
        
        // Agregar observación sobre el cambio de estado
        String observacionCambio = String.format(" | Estado cambiado de %s a %s por %s el %s", 
            estadoAnterior, nuevoEstado, 
            actualizadoPor != null ? actualizadoPor : "Sistema",
            LocalDateTime.now());
        
        String observacionesActuales = recepcion.getObservaciones();
        if (observacionesActuales == null || observacionesActuales.isEmpty()) {
            recepcion.setObservaciones(observacionCambio);
        } else {
            recepcion.setObservaciones(observacionesActuales + observacionCambio);
        }

        RecepcionMercaderia updatedRecepcion = recepcionRepository.save(recepcion);
        return convertToDTO(updatedRecepcion);
    }
}