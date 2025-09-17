package org.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "recepcion_lineas")
public class RecepcionLinea {
    
    @Id
    @UuidGenerator
    private UUID id;
    
    @NotNull(message = "La cantidad recibida es obligatoria")
    @Column(name = "cantidad_recibida", nullable = false)
    private Integer cantidadRecibida;
    
    @NotNull(message = "La cantidad aceptada es obligatoria")
    @Column(name = "cantidad_aceptada", nullable = false)
    private Integer cantidadAceptada;
    
    @NotNull(message = "La cantidad rechazada es obligatoria")
    @Column(name = "cantidad_rechazada", nullable = false)
    private Integer cantidadRechazada;
    
    @Size(max = 200, message = "El motivo de rechazo no puede exceder 200 caracteres")
    @Column(name = "motivo_rechazo", length = 200)
    private String motivoRechazo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recepcion_id", nullable = false)
    private RecepcionNew recepcion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id", nullable = false)
    private Lote lote;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public RecepcionLinea() {}
    
    public RecepcionLinea(Integer cantidadRecibida, Integer cantidadAceptada, 
                         Integer cantidadRechazada, String motivoRechazo, 
                         RecepcionNew recepcion, Producto producto, Lote lote) {
        this.cantidadRecibida = cantidadRecibida;
        this.cantidadAceptada = cantidadAceptada;
        this.cantidadRechazada = cantidadRechazada;
        this.motivoRechazo = motivoRechazo;
        this.recepcion = recepcion;
        this.producto = producto;
        this.lote = lote;
    }
    
    // Getters y Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public Integer getCantidadRecibida() {
        return cantidadRecibida;
    }
    
    public void setCantidadRecibida(Integer cantidadRecibida) {
        this.cantidadRecibida = cantidadRecibida;
    }
    
    public Integer getCantidadAceptada() {
        return cantidadAceptada;
    }
    
    public void setCantidadAceptada(Integer cantidadAceptada) {
        this.cantidadAceptada = cantidadAceptada;
    }
    
    public Integer getCantidadRechazada() {
        return cantidadRechazada;
    }
    
    public void setCantidadRechazada(Integer cantidadRechazada) {
        this.cantidadRechazada = cantidadRechazada;
    }
    
    public String getMotivoRechazo() {
        return motivoRechazo;
    }
    
    public void setMotivoRechazo(String motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
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
    
    public Producto getProducto() {
        return producto;
    }
    
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    public UUID getProductoId() {
        return producto != null ? producto.getId() : null;
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
    
    // Métodos de utilidad
    public boolean isValid() {
        return cantidadRecibida != null && cantidadAceptada != null && cantidadRechazada != null &&
               cantidadRecibida == (cantidadAceptada + cantidadRechazada);
    }
    
    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecepcionLinea that = (RecepcionLinea) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    // toString
    @Override
    public String toString() {
        return "RecepcionLinea{" +
                "id=" + id +
                ", cantidadRecibida=" + cantidadRecibida +
                ", cantidadAceptada=" + cantidadAceptada +
                ", cantidadRechazada=" + cantidadRechazada +
                ", motivoRechazo='" + motivoRechazo + '\'' +
                ", recepcionId=" + getRecepcionId() +
                ", productoId=" + getProductoId() +
                ", loteId=" + getLoteId() +
                '}';
    }
}