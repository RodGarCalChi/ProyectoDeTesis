package org.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.backend.enumeraciones.EstadoRecepcion;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class RecepcionMercaderiaDTO {
    
    private UUID id;
    
    @NotBlank(message = "El número de orden de compra es obligatorio")
    @Size(max = 50, message = "El número de orden no puede exceder 50 caracteres")
    private String numeroOrdenCompra;
    
    @NotBlank(message = "El número de guía de remisión es obligatorio")
    @Size(max = 50, message = "El número de guía no puede exceder 50 caracteres")
    private String numeroGuiaRemision;
    
    @NotNull(message = "El ID del cliente es obligatorio")
    private UUID clienteId;
    
    private String clienteNombre;
    
    @NotNull(message = "La fecha de recepción es obligatoria")
    private LocalDateTime fechaRecepcion;
    
    @NotBlank(message = "El responsable de recepción es obligatorio")
    @Size(max = 100, message = "El nombre del responsable no puede exceder 100 caracteres")
    private String responsableRecepcion;
    
    @NotNull(message = "El estado es obligatorio")
    private EstadoRecepcion estado;
    
    private BigDecimal temperaturaLlegada;
    
    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    private String observaciones;
    
    private Boolean verificacionDocumentos;
    private Boolean verificacionFisica;
    private Boolean verificacionTemperatura;
    private Boolean aprobadoPorCalidad;
    
    @Size(max = 100, message = "El nombre del inspector no puede exceder 100 caracteres")
    private String inspectorCalidad;
    
    private LocalDateTime fechaInspeccion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    private List<DetalleRecepcionDTO> detalles;
    
    // Constructores
    public RecepcionMercaderiaDTO() {}
    
    public RecepcionMercaderiaDTO(UUID id, String numeroOrdenCompra, String numeroGuiaRemision,
                                 UUID clienteId, String clienteNombre, LocalDateTime fechaRecepcion,
                                 String responsableRecepcion, EstadoRecepcion estado,
                                 BigDecimal temperaturaLlegada, String observaciones,
                                 Boolean verificacionDocumentos, Boolean verificacionFisica,
                                 Boolean verificacionTemperatura, Boolean aprobadoPorCalidad,
                                 String inspectorCalidad, LocalDateTime fechaInspeccion,
                                 LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.id = id;
        this.numeroOrdenCompra = numeroOrdenCompra;
        this.numeroGuiaRemision = numeroGuiaRemision;
        this.clienteId = clienteId;
        this.clienteNombre = clienteNombre;
        this.fechaRecepcion = fechaRecepcion;
        this.responsableRecepcion = responsableRecepcion;
        this.estado = estado;
        this.temperaturaLlegada = temperaturaLlegada;
        this.observaciones = observaciones;
        this.verificacionDocumentos = verificacionDocumentos;
        this.verificacionFisica = verificacionFisica;
        this.verificacionTemperatura = verificacionTemperatura;
        this.aprobadoPorCalidad = aprobadoPorCalidad;
        this.inspectorCalidad = inspectorCalidad;
        this.fechaInspeccion = fechaInspeccion;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }
    
    // Getters y Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getNumeroOrdenCompra() {
        return numeroOrdenCompra;
    }
    
    public void setNumeroOrdenCompra(String numeroOrdenCompra) {
        this.numeroOrdenCompra = numeroOrdenCompra;
    }
    
    public String getNumeroGuiaRemision() {
        return numeroGuiaRemision;
    }
    
    public void setNumeroGuiaRemision(String numeroGuiaRemision) {
        this.numeroGuiaRemision = numeroGuiaRemision;
    }
    
    public UUID getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(UUID clienteId) {
        this.clienteId = clienteId;
    }
    
    public String getClienteNombre() {
        return clienteNombre;
    }
    
    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }
    
    public LocalDateTime getFechaRecepcion() {
        return fechaRecepcion;
    }
    
    public void setFechaRecepcion(LocalDateTime fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }
    
    public String getResponsableRecepcion() {
        return responsableRecepcion;
    }
    
    public void setResponsableRecepcion(String responsableRecepcion) {
        this.responsableRecepcion = responsableRecepcion;
    }
    
    public EstadoRecepcion getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoRecepcion estado) {
        this.estado = estado;
    }
    
    public BigDecimal getTemperaturaLlegada() {
        return temperaturaLlegada;
    }
    
    public void setTemperaturaLlegada(BigDecimal temperaturaLlegada) {
        this.temperaturaLlegada = temperaturaLlegada;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public Boolean getVerificacionDocumentos() {
        return verificacionDocumentos;
    }
    
    public void setVerificacionDocumentos(Boolean verificacionDocumentos) {
        this.verificacionDocumentos = verificacionDocumentos;
    }
    
    public Boolean getVerificacionFisica() {
        return verificacionFisica;
    }
    
    public void setVerificacionFisica(Boolean verificacionFisica) {
        this.verificacionFisica = verificacionFisica;
    }
    
    public Boolean getVerificacionTemperatura() {
        return verificacionTemperatura;
    }
    
    public void setVerificacionTemperatura(Boolean verificacionTemperatura) {
        this.verificacionTemperatura = verificacionTemperatura;
    }
    
    public Boolean getAprobadoPorCalidad() {
        return aprobadoPorCalidad;
    }
    
    public void setAprobadoPorCalidad(Boolean aprobadoPorCalidad) {
        this.aprobadoPorCalidad = aprobadoPorCalidad;
    }
    
    public String getInspectorCalidad() {
        return inspectorCalidad;
    }
    
    public void setInspectorCalidad(String inspectorCalidad) {
        this.inspectorCalidad = inspectorCalidad;
    }
    
    public LocalDateTime getFechaInspeccion() {
        return fechaInspeccion;
    }
    
    public void setFechaInspeccion(LocalDateTime fechaInspeccion) {
        this.fechaInspeccion = fechaInspeccion;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
    
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
    
    public List<DetalleRecepcionDTO> getDetalles() {
        return detalles;
    }
    
    public void setDetalles(List<DetalleRecepcionDTO> detalles) {
        this.detalles = detalles;
    }
}