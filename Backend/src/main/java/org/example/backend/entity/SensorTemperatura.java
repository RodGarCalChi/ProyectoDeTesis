package org.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "sensores_temperatura")
public class SensorTemperatura {
    
    @Id
    @UuidGenerator
    private UUID id;
    
    @NotBlank(message = "El número de serie es obligatorio")
    @Size(max = 30, message = "El número de serie no puede exceder 30 caracteres")
    @Column(name = "serie", nullable = false, length = 30, unique = true)
    private String serie;
    
    @NotNull(message = "La fecha de calibración es obligatoria")
    @Column(name = "calibrado_hasta", nullable = false)
    private LocalDate calibradoHasta;
    
    @NotNull(message = "La frecuencia en segundos es obligatoria")
    @Column(name = "frecuencia_seg", nullable = false)
    private Integer frecuenciaSeg;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_frio_id", nullable = false)
    private EquipoFrio equipoFrio;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public SensorTemperatura() {}
    
    public SensorTemperatura(String serie, LocalDate calibradoHasta, 
                            Integer frecuenciaSeg, EquipoFrio equipoFrio) {
        this.serie = serie;
        this.calibradoHasta = calibradoHasta;
        this.frecuenciaSeg = frecuenciaSeg;
        this.equipoFrio = equipoFrio;
    }
    
    // Getters y Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getSerie() {
        return serie;
    }
    
    public void setSerie(String serie) {
        this.serie = serie;
    }
    
    public LocalDate getCalibradoHasta() {
        return calibradoHasta;
    }
    
    public void setCalibradoHasta(LocalDate calibradoHasta) {
        this.calibradoHasta = calibradoHasta;
    }
    
    public Integer getFrecuenciaSeg() {
        return frecuenciaSeg;
    }
    
    public void setFrecuenciaSeg(Integer frecuenciaSeg) {
        this.frecuenciaSeg = frecuenciaSeg;
    }
    
    public EquipoFrio getEquipoFrio() {
        return equipoFrio;
    }
    
    public void setEquipoFrio(EquipoFrio equipoFrio) {
        this.equipoFrio = equipoFrio;
    }
    
    public UUID getEquipoFrioId() {
        return equipoFrio != null ? equipoFrio.getId() : null;
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
        SensorTemperatura that = (SensorTemperatura) o;
        return Objects.equals(id, that.id) && Objects.equals(serie, that.serie);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, serie);
    }
    
    // toString
    @Override
    public String toString() {
        return "SensorTemperatura{" +
                "id=" + id +
                ", serie='" + serie + '\'' +
                ", calibradoHasta=" + calibradoHasta +
                ", frecuenciaSeg=" + frecuenciaSeg +
                ", equipoFrioId=" + getEquipoFrioId() +
                '}';
    }
}