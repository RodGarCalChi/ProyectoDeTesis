package org.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "historial_ubicaciones")
public class HistorialUbicacion {
    
    @Id
    @UuidGenerator
    private UUID id;
    
    @NotNull(message = "El inventario cliente es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventario_cliente_id", nullable = false)
    private InventarioCliente inventarioCliente;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ubicacion_anterior_id")
    private Ubicacion ubicacionAnterior;
    
    @NotNull(message = "La ubicación nueva es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ubicacion_nueva_id", nullable = false)
    private Ubicacion ubicacionNueva;
    
    @Column(name = "motivo", length = 200)
    private String motivo;
    
    @NotNull(message = "El usuario es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @NotNull(message = "La fecha de movimiento es obligatoria")
    @Column(name = "fecha_movimiento", nullable = false)
    private LocalDateTime fechaMovimiento;
    
    // Constructores
    public HistorialUbicacion() {
        this.fechaMovimiento = LocalDateTime.now();
    }
    
    public HistorialUbicacion(InventarioCliente inventarioCliente, Ubicacion ubicacionAnterior,
                             Ubicacion ubicacionNueva, String motivo, Usuario usuario) {
        this.inventarioCliente = inventarioCliente;
        this.ubicacionAnterior = ubicacionAnterior;
        this.ubicacionNueva = ubicacionNueva;
        this.motivo = motivo;
        this.usuario = usuario;
        this.fechaMovimiento = LocalDateTime.now();
    }
    
    // Getters y Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public InventarioCliente getInventarioCliente() {
        return inventarioCliente;
    }
    
    public void setInventarioCliente(InventarioCliente inventarioCliente) {
        this.inventarioCliente = inventarioCliente;
    }
    
    public Ubicacion getUbicacionAnterior() {
        return ubicacionAnterior;
    }
    
    public void setUbicacionAnterior(Ubicacion ubicacionAnterior) {
        this.ubicacionAnterior = ubicacionAnterior;
    }
    
    public Ubicacion getUbicacionNueva() {
        return ubicacionNueva;
    }
    
    public void setUbicacionNueva(Ubicacion ubicacionNueva) {
        this.ubicacionNueva = ubicacionNueva;
    }
    
    public String getMotivo() {
        return motivo;
    }
    
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public LocalDateTime getFechaMovimiento() {
        return fechaMovimiento;
    }
    
    public void setFechaMovimiento(LocalDateTime fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }
    
    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistorialUbicacion that = (HistorialUbicacion) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
