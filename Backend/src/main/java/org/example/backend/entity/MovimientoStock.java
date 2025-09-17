package org.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "movimientos_stock")
public class MovimientoStock {
    
    @Id
    @UuidGenerator
    private UUID id;
    
    @NotNull(message = "La fecha es obligatoria")
    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;
    
    @NotBlank(message = "El tipo es obligatorio")
    @Size(max = 20, message = "El tipo no puede exceder 20 caracteres")
    @Column(name = "tipo", nullable = false, length = 20)
    private String tipo;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
    
    @Size(max = 50, message = "El documento de referencia no puede exceder 50 caracteres")
    @Column(name = "documento_ref", length = 50)
    private String documentoRef;
    
    @Size(max = 200, message = "La observación no puede exceder 200 caracteres")
    @Column(name = "observacion", length = 200)
    private String observacion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id", nullable = false)
    private Lote lote;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origen_ubicacion_id")
    private Ubicacion origenUbicacion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destino_ubicacion_id")
    private Ubicacion destinoUbicacion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public MovimientoStock() {}
    
    public MovimientoStock(LocalDateTime fecha, String tipo, Integer cantidad, 
                          String documentoRef, String observacion, Lote lote,
                          Ubicacion origenUbicacion, Ubicacion destinoUbicacion, 
                          Usuario usuario) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.documentoRef = documentoRef;
        this.observacion = observacion;
        this.lote = lote;
        this.origenUbicacion = origenUbicacion;
        this.destinoUbicacion = destinoUbicacion;
        this.usuario = usuario;
    }
    
    // Getters y Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public LocalDateTime getFecha() {
        return fecha;
    }
    
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public Integer getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
    
    public String getDocumentoRef() {
        return documentoRef;
    }
    
    public void setDocumentoRef(String documentoRef) {
        this.documentoRef = documentoRef;
    }
    
    public String getObservacion() {
        return observacion;
    }
    
    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
    
    public Lote getLote() {
        return lote;
    }
    
    public void setLote(Lote lote) {
        this.lote = lote;
    }
    
    public UUID getLoteId() {
        return lote != null ? lote.getId() : null;
    }
    
    public Ubicacion getOrigenUbicacion() {
        return origenUbicacion;
    }
    
    public void setOrigenUbicacion(Ubicacion origenUbicacion) {
        this.origenUbicacion = origenUbicacion;
    }
    
    public UUID getOrigenUbicacionId() {
        return origenUbicacion != null ? origenUbicacion.getId() : null;
    }
    
    public Ubicacion getDestinoUbicacion() {
        return destinoUbicacion;
    }
    
    public void setDestinoUbicacion(Ubicacion destinoUbicacion) {
        this.destinoUbicacion = destinoUbicacion;
    }
    
    public UUID getDestinoUbicacionId() {
        return destinoUbicacion != null ? destinoUbicacion.getId() : null;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public UUID getUsuarioId() {
        return usuario != null ? usuario.getId() : null;
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
    
    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovimientoStock that = (MovimientoStock) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    // toString
    @Override
    public String toString() {
        return "MovimientoStock{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", tipo='" + tipo + '\'' +
                ", cantidad=" + cantidad +
                ", documentoRef='" + documentoRef + '\'' +
                ", observacion='" + observacion + '\'' +
                ", loteId=" + getLoteId() +
                ", origenUbicacionId=" + getOrigenUbicacionId() +
                ", destinoUbicacionId=" + getDestinoUbicacionId() +
                ", usuarioId=" + getUsuarioId() +
                '}';
    }
}