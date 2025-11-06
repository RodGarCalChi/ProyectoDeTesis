package org.example.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "palets")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Palet {
    
    @Id
    @UuidGenerator
    private UUID id;
    
    @NotBlank(message = "El código es obligatorio")
    @Size(max = 50, message = "El código no puede exceder 50 caracteres")
    @Column(name = "codigo", nullable = false, length = 50, unique = true)
    private String codigo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Lote lote;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ubicacion_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "zona"})
    private Ubicacion ubicacion;
    
    @Column(name = "capacidad_maxima")
    private Integer capacidadMaxima; // Número máximo de cajas
    
    @Column(name = "cajas_actuales")
    private Integer cajasActuales = 0;
    
    @Column(name = "peso_maximo_kg")
    private Float pesoMaximoKg;
    
    @Column(name = "peso_actual_kg")
    private Float pesoActualKg = 0.0f;
    
    @Column(name = "disponible", nullable = false)
    private Boolean disponible = true;
    
    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    @Column(name = "observaciones", length = 500)
    private String observaciones;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public Palet() {}
    
    public Palet(String codigo, Lote lote) {
        this.codigo = codigo;
        this.lote = lote;
    }
    
    // Getters y Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    
    public Lote getLote() { return lote; }
    public void setLote(Lote lote) { this.lote = lote; }
    
    public Ubicacion getUbicacion() { return ubicacion; }
    public void setUbicacion(Ubicacion ubicacion) { this.ubicacion = ubicacion; }
    
    public Integer getCapacidadMaxima() { return capacidadMaxima; }
    public void setCapacidadMaxima(Integer capacidadMaxima) { this.capacidadMaxima = capacidadMaxima; }
    
    public Integer getCajasActuales() { return cajasActuales; }
    public void setCajasActuales(Integer cajasActuales) { this.cajasActuales = cajasActuales; }
    
    public Float getPesoMaximoKg() { return pesoMaximoKg; }
    public void setPesoMaximoKg(Float pesoMaximoKg) { this.pesoMaximoKg = pesoMaximoKg; }
    
    public Float getPesoActualKg() { return pesoActualKg; }
    public void setPesoActualKg(Float pesoActualKg) { this.pesoActualKg = pesoActualKg; }
    
    public Boolean getDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }
    
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Palet palet = (Palet) o;
        return Objects.equals(id, palet.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
