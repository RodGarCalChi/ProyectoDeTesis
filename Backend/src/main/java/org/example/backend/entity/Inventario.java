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
@Table(name = "inventarios")
public class Inventario {
    
    @Id
    @UuidGenerator
    private UUID id;
    
    @Column(name = "cantidad_disponible", nullable = false)
    private Integer cantidadDisponible = 0;
    
    @Column(name = "cantidad_reservada", nullable = false)
    private Integer cantidadReservada = 0;
    
    @Column(name = "cantidad_danada", nullable = false)
    private Integer cantidadDanada = 0;
    
    @Size(max = 10, message = "La política de rotación no puede exceder 10 caracteres")
    @Column(name = "politica_rotacion", length = 10)
    private String politicaRotacion;
    
    @Column(name = "ultimo_conteo")
    private LocalDateTime ultimoConteo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id", nullable = false)
    private Lote lote;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ubicacion_id", nullable = false)
    private Ubicacion ubicacion;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public Inventario() {}
    
    public Inventario(Integer cantidadDisponible, Integer cantidadReservada, 
                     Integer cantidadDanada, String politicaRotacion, 
                     LocalDateTime ultimoConteo, Lote lote, Ubicacion ubicacion) {
        this.cantidadDisponible = cantidadDisponible;
        this.cantidadReservada = cantidadReservada;
        this.cantidadDanada = cantidadDanada;
        this.politicaRotacion = politicaRotacion;
        this.ultimoConteo = ultimoConteo;
        this.lote = lote;
        this.ubicacion = ubicacion;
    }
    
    // Getters y Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public Integer getCantidadDisponible() {
        return cantidadDisponible;
    }
    
    public void setCantidadDisponible(Integer cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }
    
    public Integer getCantidadReservada() {
        return cantidadReservada;
    }
    
    public void setCantidadReservada(Integer cantidadReservada) {
        this.cantidadReservada = cantidadReservada;
    }
    
    public Integer getCantidadDanada() {
        return cantidadDanada;
    }
    
    public void setCantidadDanada(Integer cantidadDanada) {
        this.cantidadDanada = cantidadDanada;
    }
    
    public String getPoliticaRotacion() {
        return politicaRotacion;
    }
    
    public void setPoliticaRotacion(String politicaRotacion) {
        this.politicaRotacion = politicaRotacion;
    }
    
    public LocalDateTime getUltimoConteo() {
        return ultimoConteo;
    }
    
    public void setUltimoConteo(LocalDateTime ultimoConteo) {
        this.ultimoConteo = ultimoConteo;
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
    
    public Ubicacion getUbicacion() {
        return ubicacion;
    }
    
    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }
    
    public UUID getUbicacionId() {
        return ubicacion != null ? ubicacion.getId() : null;
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
    public Integer getCantidadTotal() {
        return cantidadDisponible + cantidadReservada + cantidadDanada;
    }
    
    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inventario inventario = (Inventario) o;
        return Objects.equals(id, inventario.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    // toString
    @Override
    public String toString() {
        return "Inventario{" +
                "id=" + id +
                ", cantidadDisponible=" + cantidadDisponible +
                ", cantidadReservada=" + cantidadReservada +
                ", cantidadDanada=" + cantidadDanada +
                ", politicaRotacion='" + politicaRotacion + '\'' +
                ", ultimoConteo=" + ultimoConteo +
                ", loteId=" + getLoteId() +
                ", ubicacionId=" + getUbicacionId() +
                '}';
    }
}