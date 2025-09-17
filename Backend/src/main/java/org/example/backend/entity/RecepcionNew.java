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
@Table(name = "recepciones")
public class RecepcionNew {
    
    @Id
    @UuidGenerator
    private UUID id;
    
    @NotNull(message = "La fecha de llegada es obligatoria")
    @Column(name = "fecha_llegada", nullable = false)
    private LocalDateTime fechaLlegada;
    
    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 20, message = "El estado no puede exceder 20 caracteres")
    @Column(name = "estado", nullable = false, length = 20)
    private String estado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oc_id", nullable = false)
    private OrdenCompra ordenCompra;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recepcionista_id", nullable = false)
    private Usuario recepcionista;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public RecepcionNew() {}
    
    public RecepcionNew(LocalDateTime fechaLlegada, String estado, 
                      OrdenCompra ordenCompra, Usuario recepcionista) {
        this.fechaLlegada = fechaLlegada;
        this.estado = estado;
        this.ordenCompra = ordenCompra;
        this.recepcionista = recepcionista;
    }
    
    // Getters y Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public LocalDateTime getFechaLlegada() {
        return fechaLlegada;
    }
    
    public void setFechaLlegada(LocalDateTime fechaLlegada) {
        this.fechaLlegada = fechaLlegada;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public OrdenCompra getOrdenCompra() {
        return ordenCompra;
    }
    
    public void setOrdenCompra(OrdenCompra ordenCompra) {
        this.ordenCompra = ordenCompra;
    }
    
    public UUID getOrdenCompraId() {
        return ordenCompra != null ? ordenCompra.getId() : null;
    }
    
    public Usuario getRecepcionista() {
        return recepcionista;
    }
    
    public void setRecepcionista(Usuario recepcionista) {
        this.recepcionista = recepcionista;
    }
    
    public UUID getRecepcionistaId() {
        return recepcionista != null ? recepcionista.getId() : null;
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
        RecepcionNew that = (RecepcionNew) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    // toString
    @Override
    public String toString() {
        return "Recepcion{" +
                "id=" + id +
                ", fechaLlegada=" + fechaLlegada +
                ", estado='" + estado + '\'' +
                ", ordenCompraId=" + getOrdenCompraId() +
                ", recepcionistaId=" + getRecepcionistaId() +
                '}';
    }
}