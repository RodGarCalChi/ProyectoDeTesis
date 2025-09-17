package org.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "registros_temperatura")
public class RegistroTemperatura {
    
    @Id
    @UuidGenerator
    private UUID id;
    
    @NotNull(message = "El timestamp es obligatorio")
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    @NotNull(message = "El valor en grados Celsius es obligatorio")
    @Column(name = "valor_c", nullable = false)
    private Float valorC;
    
    @Column(name = "en_rango", nullable = false)
    private Boolean enRango = true;
    
    @Column(name = "alerta", nullable = false)
    private Boolean alerta = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_id", nullable = false)
    private SensorTemperatura sensor;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public RegistroTemperatura() {}
    
    public RegistroTemperatura(LocalDateTime timestamp, Float valorC, 
                              Boolean enRango, Boolean alerta, SensorTemperatura sensor) {
        this.timestamp = timestamp;
        this.valorC = valorC;
        this.enRango = enRango;
        this.alerta = alerta;
        this.sensor = sensor;
    }
    
    // Getters y Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public Float getValorC() {
        return valorC;
    }
    
    public void setValorC(Float valorC) {
        this.valorC = valorC;
    }
    
    public Boolean getEnRango() {
        return enRango;
    }
    
    public void setEnRango(Boolean enRango) {
        this.enRango = enRango;
    }
    
    public Boolean getAlerta() {
        return alerta;
    }
    
    public void setAlerta(Boolean alerta) {
        this.alerta = alerta;
    }
    
    public SensorTemperatura getSensor() {
        return sensor;
    }
    
    public void setSensor(SensorTemperatura sensor) {
        this.sensor = sensor;
    }
    
    public UUID getSensorId() {
        return sensor != null ? sensor.getId() : null;
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
        RegistroTemperatura that = (RegistroTemperatura) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    // toString
    @Override
    public String toString() {
        return "RegistroTemperatura{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", valorC=" + valorC +
                ", enRango=" + enRango +
                ", alerta=" + alerta +
                ", sensorId=" + getSensorId() +
                '}';
    }
}