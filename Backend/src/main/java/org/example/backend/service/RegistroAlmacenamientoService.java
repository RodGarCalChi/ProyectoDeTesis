package org.example.backend.service;

import org.example.backend.dto.RegistroAlmacenamientoDTO;
import org.example.backend.dto.DetalleAlmacenamientoDTO;
import org.example.backend.entity.RegistroAlmacenamiento;
import org.example.backend.entity.DetalleAlmacenamiento;
import org.example.backend.entity.Cliente;
import org.example.backend.entity.Producto;
import org.example.backend.entity.Lote;
import org.example.backend.enumeraciones.EstadoAlmacenamiento;
import org.example.backend.repository.RegistroAlmacenamientoRepository;
import org.example.backend.repository.ClienteRepository;
import org.example.backend.repository.ProductoRepository;
import org.example.backend.repository.LoteRepository;
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
public class RegistroAlmacenamientoService {
    
    @Autowired
    private RegistroAlmacenamientoRepository registroRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private LoteRepository loteRepository;
    
    // Crear nuevo registro de almacenamiento
    public RegistroAlmacenamientoDTO crearRegistro(RegistroAlmacenamientoDTO registroDTO) {
        // Verificar que no exista la guía de remisión
        if (registroRepository.existsByNumeroGuiaRemision(registroDTO.getNumeroGuiaRemision())) {
            throw new RuntimeException("Ya existe un registro con la guía de remisión: " + 
                                     registroDTO.getNumeroGuiaRemision());
        }
        
        // Obtener cliente por ID
        Cliente cliente = clienteRepository.findById(registroDTO.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        // Crear entidad
        RegistroAlmacenamiento registro = new RegistroAlmacenamiento();
        registro.setNumeroGuiaRemision(registroDTO.getNumeroGuiaRemision());
        registro.setCliente(cliente);
        registro.setFechaAlmacenamiento(registroDTO.getFechaAlmacenamiento());
        registro.setOperadorResponsable(registroDTO.getOperadorResponsable());
        registro.setEstado(EstadoAlmacenamiento.PENDIENTE);
        registro.setUbicacionAlmacen(registroDTO.getUbicacionAlmacen());
        registro.setTemperaturaAlmacenamiento(registroDTO.getTemperaturaAlmacenamiento());
        registro.setObservaciones(registroDTO.getObservaciones());
        registro.setPesoTotal(registroDTO.getPesoTotal());
        registro.setVolumenTotal(registroDTO.getVolumenTotal());
        
        // Procesar detalles si existen
        if (registroDTO.getDetalles() != null) {
            for (DetalleAlmacenamientoDTO detalleDTO : registroDTO.getDetalles()) {
                DetalleAlmacenamiento detalle = crearDetalleFromDTO(detalleDTO);
                registro.addDetalle(detalle);
            }
        }
        
        RegistroAlmacenamiento savedRegistro = registroRepository.save(registro);
        return convertToDTO(savedRegistro);
    }
    
    // Obtener todos los registros
    @Transactional(readOnly = true)
    public Page<RegistroAlmacenamientoDTO> obtenerRegistrosPaginados(Pageable pageable) {
        return registroRepository.findAll(pageable)
                .map(this::convertToDTO);
    }
    
    // Obtener registro por ID
    @Transactional(readOnly = true)
    public Optional<RegistroAlmacenamientoDTO> obtenerRegistroPorId(UUID id) {
        return registroRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    // Buscar registros con filtros
    @Transactional(readOnly = true)
    public Page<RegistroAlmacenamientoDTO> buscarRegistrosConFiltros(
            String numeroGuia, EstadoAlmacenamiento estado, UUID clienteId,
            String operador, String ubicacion, Pageable pageable) {
        return registroRepository.findRegistrosWithFilters(
                numeroGuia, estado, clienteId, operador, ubicacion, pageable)
                .map(this::convertToDTO);
    }
    
    // Obtener registros por estado
    @Transactional(readOnly = true)
    public List<RegistroAlmacenamientoDTO> obtenerRegistrosPorEstado(EstadoAlmacenamiento estado) {
        return registroRepository.findByEstado(estado).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Iniciar proceso de almacenamiento
    public RegistroAlmacenamientoDTO iniciarAlmacenamiento(UUID registroId) {
        RegistroAlmacenamiento registro = registroRepository.findById(registroId)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));
        
        if (registro.getEstado() != EstadoAlmacenamiento.PENDIENTE) {
            throw new RuntimeException("El registro no está en estado pendiente");
        }
        
        registro.setEstado(EstadoAlmacenamiento.EN_PROCESO);
        
        RegistroAlmacenamiento updatedRegistro = registroRepository.save(registro);
        return convertToDTO(updatedRegistro);
    }
    
    // Completar almacenamiento
    public RegistroAlmacenamientoDTO completarAlmacenamiento(UUID registroId) {
        RegistroAlmacenamiento registro = registroRepository.findById(registroId)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));
        
        if (registro.getEstado() != EstadoAlmacenamiento.EN_PROCESO) {
            throw new RuntimeException("El registro no está en proceso");
        }
        
        registro.setEstado(EstadoAlmacenamiento.ALMACENADO);
        
        RegistroAlmacenamiento updatedRegistro = registroRepository.save(registro);
        return convertToDTO(updatedRegistro);
    }
    
    // Enviar a verificación administrativa
    public RegistroAlmacenamientoDTO enviarAVerificacion(UUID registroId) {
        RegistroAlmacenamiento registro = registroRepository.findById(registroId)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));
        
        if (registro.getEstado() != EstadoAlmacenamiento.ALMACENADO) {
            throw new RuntimeException("El registro debe estar almacenado para enviar a verificación");
        }
        
        registro.setEstado(EstadoAlmacenamiento.PENDIENTE_VERIFICACION);
        
        RegistroAlmacenamiento updatedRegistro = registroRepository.save(registro);
        return convertToDTO(updatedRegistro);
    }
    
    // Verificar por área administrativa
    public RegistroAlmacenamientoDTO verificarPorAdmin(UUID registroId, String verificador) {
        RegistroAlmacenamiento registro = registroRepository.findById(registroId)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));
        
        if (registro.getEstado() != EstadoAlmacenamiento.PENDIENTE_VERIFICACION) {
            throw new RuntimeException("El registro no está pendiente de verificación");
        }
        
        registro.setVerificadoPorAdmin(true);
        registro.setVerificadorAdmin(verificador);
        registro.setFechaVerificacion(LocalDateTime.now());
        registro.setEstado(EstadoAlmacenamiento.VERIFICADO);
        
        RegistroAlmacenamiento updatedRegistro = registroRepository.save(registro);
        return convertToDTO(updatedRegistro);
    }
    
    // Rechazar registro
    public RegistroAlmacenamientoDTO rechazarRegistro(UUID registroId, String motivo, String verificador) {
        RegistroAlmacenamiento registro = registroRepository.findById(registroId)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));
        
        registro.setVerificadoPorAdmin(false);
        registro.setVerificadorAdmin(verificador);
        registro.setFechaVerificacion(LocalDateTime.now());
        registro.setEstado(EstadoAlmacenamiento.RECHAZADO);
        registro.setObservaciones(registro.getObservaciones() + " | RECHAZADO: " + motivo);
        
        RegistroAlmacenamiento updatedRegistro = registroRepository.save(registro);
        return convertToDTO(updatedRegistro);
    }
    
    // Reubicar mercadería
    public RegistroAlmacenamientoDTO reubicarMercaderia(UUID registroId, String nuevaUbicacion, String operador) {
        RegistroAlmacenamiento registro = registroRepository.findById(registroId)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));
        
        String ubicacionAnterior = registro.getUbicacionAlmacen();
        registro.setUbicacionAlmacen(nuevaUbicacion);
        registro.setEstado(EstadoAlmacenamiento.REUBICADO);
        registro.setObservaciones(registro.getObservaciones() + 
                " | REUBICADO de " + ubicacionAnterior + " a " + nuevaUbicacion + " por " + operador);
        
        RegistroAlmacenamiento updatedRegistro = registroRepository.save(registro);
        return convertToDTO(updatedRegistro);
    }
    
    // Obtener registros pendientes de verificación
    @Transactional(readOnly = true)
    public List<RegistroAlmacenamientoDTO> obtenerRegistrosPendientesVerificacion() {
        return registroRepository.findRegistrosPendientesVerificacion().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Obtener registros con atención especial
    @Transactional(readOnly = true)
    public List<RegistroAlmacenamientoDTO> obtenerRegistrosConAtencionEspecial() {
        return registroRepository.findRegistrosConAtencionEspecial().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Obtener registros del día
    @Transactional(readOnly = true)
    public List<RegistroAlmacenamientoDTO> obtenerRegistrosDelDia() {
        return registroRepository.findRegistrosDelDia().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Obtener estadísticas
    @Transactional(readOnly = true)
    public List<Object[]> obtenerEstadisticasPorEstado() {
        return registroRepository.countRegistrosByEstado();
    }
    
    @Transactional(readOnly = true)
    public List<Object[]> obtenerEstadisticasPorCliente() {
        return registroRepository.countRegistrosByCliente();
    }
    
    // Métodos auxiliares
    private DetalleAlmacenamiento crearDetalleFromDTO(DetalleAlmacenamientoDTO dto) {
        DetalleAlmacenamiento detalle = new DetalleAlmacenamiento();
        
        // Obtener producto
        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        detalle.setProducto(producto);
        
        // Obtener lote si existe
        if (dto.getLoteId() != null) {
            Lote lote = loteRepository.findById(dto.getLoteId())
                    .orElseThrow(() -> new RuntimeException("Lote no encontrado"));
            detalle.setLote(lote);
        }
        
        detalle.setCantidad(dto.getCantidad());
        detalle.setPesoUnitario(dto.getPesoUnitario());
        detalle.setVolumenUnitario(dto.getVolumenUnitario());
        detalle.setFechaVencimiento(dto.getFechaVencimiento());
        detalle.setUbicacionEspecifica(dto.getUbicacionEspecifica());
        detalle.setTemperaturaRequerida(dto.getTemperaturaRequerida());
        detalle.setObservacionesProducto(dto.getObservacionesProducto());
        detalle.setEstadoFisico(dto.getEstadoFisico());
        detalle.setRequiereAtencionEspecial(dto.getRequiereAtencionEspecial());
        detalle.setMotivoAtencionEspecial(dto.getMotivoAtencionEspecial());
        
        return detalle;
    }
    
    private RegistroAlmacenamientoDTO convertToDTO(RegistroAlmacenamiento registro) {
        RegistroAlmacenamientoDTO dto = new RegistroAlmacenamientoDTO();
        dto.setId(registro.getId());
        dto.setNumeroGuiaRemision(registro.getNumeroGuiaRemision());
        dto.setClienteId(registro.getCliente().getId());
        dto.setClienteNombre(registro.getCliente().getRazonSocial());
        dto.setClienteRuc(registro.getCliente().getRucDni());
        dto.setFechaAlmacenamiento(registro.getFechaAlmacenamiento());
        dto.setOperadorResponsable(registro.getOperadorResponsable());
        dto.setEstado(registro.getEstado());
        dto.setUbicacionAlmacen(registro.getUbicacionAlmacen());
        dto.setTemperaturaAlmacenamiento(registro.getTemperaturaAlmacenamiento());
        dto.setObservaciones(registro.getObservaciones());
        dto.setVerificadoPorAdmin(registro.getVerificadoPorAdmin());
        dto.setVerificadorAdmin(registro.getVerificadorAdmin());
        dto.setFechaVerificacion(registro.getFechaVerificacion());
        dto.setPesoTotal(registro.getPesoTotal());
        dto.setVolumenTotal(registro.getVolumenTotal());
        dto.setFechaCreacion(registro.getFechaCreacion());
        dto.setFechaActualizacion(registro.getFechaActualizacion());
        
        // Convertir detalles
        if (registro.getDetalles() != null && !registro.getDetalles().isEmpty()) {
            List<DetalleAlmacenamientoDTO> detallesDTO = registro.getDetalles().stream()
                    .map(this::convertDetalleToDTO)
                    .collect(Collectors.toList());
            dto.setDetalles(detallesDTO);
        }
        
        return dto;
    }
    
    private DetalleAlmacenamientoDTO convertDetalleToDTO(DetalleAlmacenamiento detalle) {
        DetalleAlmacenamientoDTO dto = new DetalleAlmacenamientoDTO();
        dto.setId(detalle.getId());
        dto.setProductoId(detalle.getProducto().getId());
        dto.setProductoNombre(detalle.getProducto().getNombre());
        dto.setProductoSku(detalle.getProducto().getCodigoSKU());
        dto.setProductoTipo(detalle.getProducto().getTipo().toString());
        
        if (detalle.getLote() != null) {
            dto.setLoteId(detalle.getLote().getId());
            dto.setLoteNumero(detalle.getLote().getNumero());
        }
        
        dto.setCantidad(detalle.getCantidad());
        dto.setPesoUnitario(detalle.getPesoUnitario());
        dto.setVolumenUnitario(detalle.getVolumenUnitario());
        dto.setFechaVencimiento(detalle.getFechaVencimiento());
        dto.setUbicacionEspecifica(detalle.getUbicacionEspecifica());
        dto.setTemperaturaRequerida(detalle.getTemperaturaRequerida());
        dto.setObservacionesProducto(detalle.getObservacionesProducto());
        dto.setEstadoFisico(detalle.getEstadoFisico());
        dto.setRequiereAtencionEspecial(detalle.getRequiereAtencionEspecial());
        dto.setMotivoAtencionEspecial(detalle.getMotivoAtencionEspecial());
        
        return dto;
    }
}