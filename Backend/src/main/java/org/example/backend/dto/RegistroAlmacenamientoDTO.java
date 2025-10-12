package org.example.backend.dto;

import org.example.backend.enumeraciones.EstadoAlmacenamiento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class RegistroAlmacenamientoDTO {
    
    private UUID id;
    private String numeroGuiaRemision;
    private UUID clienteId;
    private String clienteNombre;
    private String clienteRuc;
    private LocalDateTime fechaAlmacenamiento;
    private String operadorResponsable;
    private EstadoAlmacenamiento estado;
    private String ubicacionAlmacen;
    private BigDecimal temperaturaAlmacenamiento;
    private String observaciones;
    private Boolean verificadoPorAdmin;
    private String verificadorAdmin;
    private LocalDateTime fechaVerificacion;
    private BigDecimal pesoTotal;
    private BigDecimal volumenTotal;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private List<DetalleAlmacenamientoDTO> detalles;
    
    // Constructors
    public RegistroAlmacenamientoDTO() {}
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
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
    
    public String getClienteRuc() {
        return clienteRuc;
    }
    
    public void setClienteRuc(String clienteRuc) {
        this.clienteRuc = clienteRuc;
    }
    
    public LocalDateTime getFechaAlmacenamiento() {
        return fechaAlmacenamiento;
    }
    
    public void setFechaAlmacenamiento(LocalDateTime fechaAlmacenamiento) {
        this.fechaAlmacenamiento = fechaAlmacenamiento;
    }
    
    public String getOperadorResponsable() {
        return operadorResponsable;
    }
    
    public void setOperadorResponsable(String operadorResponsable) {
        this.operadorResponsable = operadorResponsable;
    }
    
    public EstadoAlmacenamiento getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoAlmacenamiento estado) {
        this.estado = estado;
    }
    
    public String getUbicacionAlmacen() {
        return ubicacionAlmacen;
    }
    
    public void setUbicacionAlmacen(String ubicacionAlmacen) {
        this.ubicacionAlmacen = ubicacionAlmacen;
    }
    
    public BigDecimal getTemperaturaAlmacenamiento() {
        return temperaturaAlmacenamiento;
    }
    
    public void setTemperaturaAlmacenamiento(BigDecimal temperaturaAlmacenamiento) {
        this.temperaturaAlmacenamiento = temperaturaAlmacenamiento;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public Boolean getVerificadoPorAdmin() {
        return verificadoPorAdmin;
    }
    
    public void setVerificadoPorAdmin(Boolean verificadoPorAdmin) {
        this.verificadoPorAdmin = verificadoPorAdmin;
    }
    
    public String getVerificadorAdmin() {
        return verificadorAdmin;
    }
    
    public void setVerificadorAdmin(String verificadorAdmin) {
        this.verificadorAdmin = verificadorAdmin;
    }
    
    public LocalDateTime getFechaVerificacion() {
        return fechaVerificacion;
    }
    
    public void setFechaVerificacion(LocalDateTime fechaVerificacion) {
        this.fechaVerificacion = fechaVerificacion;
    }
    
    public BigDecimal getPesoTotal() {
        return pesoTotal;
    }
    
    public void setPesoTotal(BigDecimal pesoTotal) {
        this.pesoTotal = pesoTotal;
    }
    
    public BigDecimal getVolumenTotal() {
        return volumenTotal;
    }
    
    public void setVolumenTotal(BigDecimal volumenTotal) {
        this.volumenTotal = volumenTotal;
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
    
    public List<DetalleAlmacenamientoDTO> getDetalles() {
        return detalles;
    }
    
    public void setDetalles(List<DetalleAlmacenamientoDTO> detalles) {
        this.detalles = detalles;
    }
}