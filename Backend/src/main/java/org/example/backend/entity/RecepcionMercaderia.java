package org.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.backend.enumeraciones.EstadoRecepcion;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "recepciones_mercaderia")
public class RecepcionMercaderia {
    
    @Id
    @UuidGenerator
    private UUID id;
    
    @NotBlank(message = "El número de orden de compra es obligatorio")
    @Size(max = 50, message = "El número de orden no puede exceder 50 caracteres")
    @Column(name = "numero_orden_compra", nullable = false, length = 50)
    private String numeroOrdenCompra;
    
    @NotBlank(message = "El número de guía de remisión es obligatorio")
    @Size(max = 50, message = "El número de guía no puede exceder 50 caracteres")
    @Column(name = "numero_guia_remision", nullable = false, length = 50)
    private String numeroGuiaRemision;
    
    @NotNull(message = "El cliente es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @NotNull(message = "La fecha de recepción es obligatoria")
    @Column(name = "fecha_recepcion", nullable = false)
    private LocalDateTime fechaRecepcion;
    
    @NotBlank(message = "El responsable de recepción es obligatorio")
    @Size(max = 100, message = "El nombre del responsable no puede exceder 100 caracteres")
    @Column(name = "responsable_recepcion", nullable = false, length = 100)
    private String responsableRecepcion;
    
    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoRecepcion estado;
    
    @Column(name = "temperatura_llegada")
    private BigDecimal temperaturaLlegada;
    
    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    @Column(name = "observaciones", length = 500)
    private String observaciones;
    
    @Column(name = "verificacion_documentos", nullable = false)
    private Boolean verificacionDocumentos = false;
    
    @Column(name = "verificacion_fisica", nullable = false)
    private Boolean verificacionFisica = false;
    
    @Column(name = "verificacion_temperatura", nullable = false)
    private Boolean verificacionTemperatura = false;
    
    @Column(name = "aprobado_por_calidad", nullable = false)
    private Boolean aprobadoPorCalidad = false;
    
    @Size(max = 100, message = "El nombre del inspector no puede exceder 100 caracteres")
    @Column(name = "inspector_calidad", length = 100)
    private String inspectorCalidad;
    
    @Column(name = "fecha_inspeccion")
    private LocalDateTime fechaInspeccion;
    
    @OneToMany(mappedBy = "recepcion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleRecepcion> detalles = new ArrayList<>();
    
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public RecepcionMercaderia() {}
    
    public RecepcionMercaderia(String numeroOrdenCompra, String numeroGuiaRemision, 
                              Cliente cliente, LocalDateTime fechaRecepcion, 
                              String responsableRecepcion, EstadoRecepcion estado) {
        this.numeroOrdenCompra = numeroOrdenCompra;
        this.numeroGuiaRemision = numeroGuiaRemision;
        this.cliente = cliente;
        this.fechaRecepcion = fechaRecepcion;
        this.responsableRecepcion = responsableRecepcion;
        this.estado = estado;
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
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
    
    public List<DetalleRecepcion> getDetalles() {
        return detalles;
    }
    
    public void setDetalles(List<DetalleRecepcion> detalles) {
        this.detalles = detalles;
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
    
    // Métodos de utilidad
    public void addDetalle(DetalleRecepcion detalle) {
        detalles.add(detalle);
        detalle.setRecepcion(this);
    }
    
    public void removeDetalle(DetalleRecepcion detalle) {
        detalles.remove(detalle);
        detalle.setRecepcion(null);
    }
    
    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecepcionMercaderia that = (RecepcionMercaderia) o;
        return Objects.equals(id, that.id) && 
               Objects.equals(numeroOrdenCompra, that.numeroOrdenCompra);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, numeroOrdenCompra);
    }
    
    @Override
    public String toString() {
        return "RecepcionMercaderia{" +
                "id=" + id +
                ", numeroOrdenCompra='" + numeroOrdenCompra + '\'' +
                ", numeroGuiaRemision='" + numeroGuiaRemision + '\'' +
                ", fechaRecepcion=" + fechaRecepcion +
                ", responsableRecepcion='" + responsableRecepcion + '\'' +
                ", estado=" + estado +
                '}';
    }
}