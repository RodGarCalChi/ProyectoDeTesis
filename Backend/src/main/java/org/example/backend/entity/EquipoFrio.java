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
@Table(name = "equipos_frio")
public class EquipoFrio {
    
    @Id
    @UuidGenerator
    private UUID id;
    
    @NotBlank(message = "El tipo es obligatorio")
    @Size(max = 30, message = "El tipo no puede exceder 30 caracteres")
    @Column(name = "tipo", nullable = false, length = 30)
    private String tipo;
    
    @NotBlank(message = "El código es obligatorio")
    @Size(max = 30, message = "El código no puede exceder 30 caracteres")
    @Column(name = "codigo", nullable = false, length = 30, unique = true)
    private String codigo;
    
    @NotBlank(message = "La ubicación física es obligatoria")
    @Size(max = 100, message = "La ubicación física no puede exceder 100 caracteres")
    @Column(name = "ubicacion_fisica", nullable = false, length = 100)
    private String ubicacionFisica;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public EquipoFrio() {}
    
    public EquipoFrio(String tipo, String codigo, String ubicacionFisica) {
        this.tipo = tipo;
        this.codigo = codigo;
        this.ubicacionFisica = ubicacionFisica;
    }
    
    // Getters y Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public String getUbicacionFisica() {
        return ubicacionFisica;
    }
    
    public void setUbicacionFisica(String ubicacionFisica) {
        this.ubicacionFisica = ubicacionFisica;
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
        EquipoFrio that = (EquipoFrio) o;
        return Objects.equals(id, that.id) && Objects.equals(codigo, that.codigo);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, codigo);
    }
    
    // toString
    @Override
    public String toString() {
        return "EquipoFrio{" +
                "id=" + id +
                ", tipo='" + tipo + '\'' +
                ", codigo='" + codigo + '\'' +
                ", ubicacionFisica='" + ubicacionFisica + '\'' +
                '}';
    }
}