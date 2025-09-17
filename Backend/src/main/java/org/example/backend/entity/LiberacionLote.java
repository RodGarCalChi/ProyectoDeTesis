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
@Table(name = "liberaciones_lote")
public class LiberacionLote {
    
    @Id
    @UuidGenerator
    private UUID id;
    
    @NotNull(message = "La fecha es obligatoria")
    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;
    
    @NotBlank(message = "La condición es obligatoria")
    @Size(max = 20, message = "La condición no puede exceder 20 caracteres")
    @Column(name = "condicion", nullable = false, length = 20)
    private String condicion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id", nullable = false)
    private Lote lote;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_calidad_id", nullable = false)
    private Usuario tecnicoCalidad;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public LiberacionLote() {}
    
    public LiberacionLote(LocalDateTime fecha, String condicion, Lote lote, Usuario tecnicoCalidad) {
        this.fecha = fecha;
        this.condicion = condicion;
        this.lote = lote;
        this.tecnicoCalidad = tecnicoCalidad;
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
    
    public String getCondicion() {
        return condicion;
    }
    
    public void setCondicion(String condicion) {
        this.condicion = condicion;
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
    
    public Usuario getTecnicoCalidad() {
        return tecnicoCalidad;
    }
    
    public void setTecnicoCalidad(Usuario tecnicoCalidad) {
        this.tecnicoCalidad = tecnicoCalidad;
    }
    
    public UUID getTecnicoCalidadId() {
        return tecnicoCalidad != null ? tecnicoCalidad.getId() : null;
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
        LiberacionLote that = (LiberacionLote) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    // toString
    @Override
    public String toString() {
        return "LiberacionLote{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", condicion='" + condicion + '\'' +
                ", loteId=" + getLoteId() +
                ", tecnicoCalidadId=" + getTecnicoCalidadId() +
                '}';
    }
}