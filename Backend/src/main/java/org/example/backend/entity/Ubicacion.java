package org.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "ubicaciones")
public class Ubicacion {
    
    @Id
    @UuidGenerator
    private UUID id;
    
    @NotBlank(message = "El código es obligatorio")
    @Size(max = 30, message = "El código no puede exceder 30 caracteres")
    @Column(name = "codigo", nullable = false, length = 30)
    private String codigo;
    
    @Column(name = "capacidad")
    private Integer capacidad;
    
    @Column(name = "temp_objetivo_min")
    private Float tempObjetivoMin;
    
    @Column(name = "temp_objetivo_max")
    private Float tempObjetivoMax;
    
    @Column(name = "disponible", nullable = false)
    private Boolean disponible = true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zona_id", nullable = false)
    private Zona zona;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public Ubicacion() {}
    
    public Ubicacion(String codigo, Integer capacidad, Float tempObjetivoMin, 
                    Float tempObjetivoMax, Boolean disponible, Zona zona) {
        this.codigo = codigo;
        this.capacidad = capacidad;
        this.tempObjetivoMin = tempObjetivoMin;
        this.tempObjetivoMax = tempObjetivoMax;
        this.disponible = disponible;
        this.zona = zona;
    }
    
    // Getters y Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public Integer getCapacidad() {
        return capacidad;
    }
    
    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }
    
    public Float getTempObjetivoMin() {
        return tempObjetivoMin;
    }
    
    public void setTempObjetivoMin(Float tempObjetivoMin) {
        this.tempObjetivoMin = tempObjetivoMin;
    }
    
    public Float getTempObjetivoMax() {
        return tempObjetivoMax;
    }
    
    public void setTempObjetivoMax(Float tempObjetivoMax) {
        this.tempObjetivoMax = tempObjetivoMax;
    }
    
    public Boolean getDisponible() {
        return disponible;
    }
    
    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
    
    public Zona getZona() {
        return zona;
    }
    
    public void setZona(Zona zona) {
        this.zona = zona;
    }
    
    public UUID getZonaId() {
        return zona != null ? zona.getId() : null;
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
        Ubicacion ubicacion = (Ubicacion) o;
        return Objects.equals(id, ubicacion.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    // toString
    @Override
    public String toString() {
        return "Ubicacion{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", capacidad=" + capacidad +
                ", tempObjetivoMin=" + tempObjetivoMin +
                ", tempObjetivoMax=" + tempObjetivoMax +
                ", disponible=" + disponible +
                ", zonaId=" + getZonaId() +
                '}';
    }
}