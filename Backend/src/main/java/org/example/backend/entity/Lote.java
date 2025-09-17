package org.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.backend.enumeraciones.EstadoLote;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "lotes")
public class Lote {
    
    @Id
    @UuidGenerator
    private UUID id;
    
    @NotBlank(message = "El código de lote es obligatorio")
    @Size(max = 30, message = "El código de lote no puede exceder 30 caracteres")
    @Column(name = "codigo_lote", nullable = false, length = 30)
    private String codigoLote;
    
    @NotNull(message = "La fecha de fabricación es obligatoria")
    @Column(name = "fecha_fabricacion", nullable = false)
    private LocalDate fechaFabricacion;
    
    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;
    
    @NotNull(message = "El estado del lote es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoLote estado;
    
    @Size(max = 200, message = "El motivo de retención no puede exceder 200 caracteres")
    @Column(name = "motivo_retencion", length = 200)
    private String motivoRetencion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public Lote() {}
    
    public Lote(String codigoLote, LocalDate fechaFabricacion, LocalDate fechaVencimiento, 
               EstadoLote estado, String motivoRetencion, Producto producto) {
        this.codigoLote = codigoLote;
        this.fechaFabricacion = fechaFabricacion;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
        this.motivoRetencion = motivoRetencion;
        this.producto = producto;
    }
    
    // Getters y Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getCodigoLote() {
        return codigoLote;
    }
    
    public void setCodigoLote(String codigoLote) {
        this.codigoLote = codigoLote;
    }
    
    public LocalDate getFechaFabricacion() {
        return fechaFabricacion;
    }
    
    public void setFechaFabricacion(LocalDate fechaFabricacion) {
        this.fechaFabricacion = fechaFabricacion;
    }
    
    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }
    
    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    
    public EstadoLote getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoLote estado) {
        this.estado = estado;
    }
    
    public String getMotivoRetencion() {
        return motivoRetencion;
    }
    
    public void setMotivoRetencion(String motivoRetencion) {
        this.motivoRetencion = motivoRetencion;
    }
    
    public Producto getProducto() {
        return producto;
    }
    
    public void setProducto(Producto producto) {
        this.producto = producto;
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
        Lote lote = (Lote) o;
        return Objects.equals(id, lote.id) && Objects.equals(codigoLote, lote.codigoLote);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, codigoLote);
    }
    
    // toString
    @Override
    public String toString() {
        return "Lote{" +
                "id=" + id +
                ", codigoLote='" + codigoLote + '\'' +
                ", fechaFabricacion=" + fechaFabricacion +
                ", fechaVencimiento=" + fechaVencimiento +
                ", estado=" + estado +
                ", motivoRetencion='" + motivoRetencion + '\'' +
                ", producto=" + (producto != null ? producto.getCodigoSKU() : "null") +
                '}';
    }
}