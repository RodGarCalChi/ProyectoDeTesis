package org.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.backend.enumeraciones.EstadoPedido;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "pedidos")
public class Pedido {
    
    @Id
    @UuidGenerator
    private UUID id;
    
    @NotNull(message = "La fecha de creación es obligatoria")
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    
    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoPedido estado;
    
    @NotBlank(message = "La prioridad es obligatoria")
    @Size(max = 20, message = "La prioridad no puede exceder 20 caracteres")
    @Column(name = "prioridad", nullable = false, length = 20)
    private String prioridad;
    
    @Size(max = 20, message = "La temperatura requerida no puede exceder 20 caracteres")
    @Column(name = "temperatura_requerida", length = 20)
    private String temperaturaRequerida;
    
    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @CreationTimestamp
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public Pedido() {}
    
    public Pedido(LocalDateTime fechaCreacion, EstadoPedido estado, String prioridad, 
                 String temperaturaRequerida, BigDecimal total, Cliente cliente) {
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.prioridad = prioridad;
        this.temperaturaRequerida = temperaturaRequerida;
        this.total = total;
        this.cliente = cliente;
    }
    
    // Getters y Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public EstadoPedido getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }
    
    public String getPrioridad() {
        return prioridad;
    }
    
    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }
    
    public String getTemperaturaRequerida() {
        return temperaturaRequerida;
    }
    
    public void setTemperaturaRequerida(String temperaturaRequerida) {
        this.temperaturaRequerida = temperaturaRequerida;
    }
    
    public BigDecimal getTotal() {
        return total;
    }
    
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public UUID getClienteId() {
        return cliente != null ? cliente.getId() : null;
    }
    
    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
    
    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
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
        Pedido pedido = (Pedido) o;
        return Objects.equals(id, pedido.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    // toString
    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", fechaCreacion=" + fechaCreacion +
                ", estado=" + estado +
                ", prioridad='" + prioridad + '\'' +
                ", temperaturaRequerida='" + temperaturaRequerida + '\'' +
                ", total=" + total +
                ", clienteId=" + getClienteId() +
                '}';
    }
}