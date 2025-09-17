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
@Table(name = "inspecciones_calidad")
public class InspeccionCalidad {
    
    @Id
    @UuidGenerator
    private UUID id;
    
    @NotNull(message = "La fecha es obligatoria")
    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;
    
    @NotBlank(message = "El resultado es obligatorio")
    @Size(max = 20, message = "El resultado no puede exceder 20 caracteres")
    @Column(name = "resultado", nullable = false, length = 20)
    private String resultado;
    
    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    @Column(name = "observaciones", length = 500)
    private String observaciones;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recepcion_id", nullable = false)
    private RecepcionNew recepcion;
    
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
    public InspeccionCalidad() {}
    
    public InspeccionCalidad(LocalDateTime fecha, String resultado, String observaciones, 
                            RecepcionNew recepcion, Usuario tecnicoCalidad) {
        this.fecha = fecha;
        this.resultado = resultado;
        this.observaciones = observaciones;
        this.recepcion = recepcion;
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
    
    public String getResultado() {
        return resultado;
    }
    
    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public RecepcionNew getRecepcion() {
        return recepcion;
    }
    
    public void setRecepcion(RecepcionNew recepcion) {
        this.recepcion = recepcion;
    }
    
    public UUID getRecepcionId() {
        return recepcion != null ? recepcion.getId() : null;
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
        InspeccionCalidad that = (InspeccionCalidad) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    // toString
    @Override
    public String toString() {
        return "InspeccionCalidad{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", resultado='" + resultado + '\'' +
                ", observaciones='" + observaciones + '\'' +
                ", recepcionId=" + getRecepcionId() +
                ", tecnicoCalidadId=" + getTecnicoCalidadId() +
                '}';
    }
}