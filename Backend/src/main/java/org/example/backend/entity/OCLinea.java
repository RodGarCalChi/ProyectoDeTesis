package org.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.example.backend.enumeraciones.CondicionAlmacen;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "oc_lineas")
public class OCLinea {
    
    @Id
    @UuidGenerator
    private UUID id;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
    
    @NotNull(message = "El precio unitario es obligatorio")
    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;
    
    @NotNull(message = "La condición de almacén es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(name = "condicion_almacen", nullable = false, length = 20)
    private CondicionAlmacen condicionAlmacen;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oc_id", nullable = false)
    private OrdenCompra ordenCompra;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public OCLinea() {}
    
    public OCLinea(Integer cantidad, BigDecimal precioUnitario, 
                  CondicionAlmacen condicionAlmacen, OrdenCompra ordenCompra, Producto producto) {
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.condicionAlmacen = condicionAlmacen;
        this.ordenCompra = ordenCompra;
        this.producto = producto;
    }
    
    // Getters y Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public Integer getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
    
    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }
    
    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
    
    public CondicionAlmacen getCondicionAlmacen() {
        return condicionAlmacen;
    }
    
    public void setCondicionAlmacen(CondicionAlmacen condicionAlmacen) {
        this.condicionAlmacen = condicionAlmacen;
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
    
    public Producto getProducto() {
        return producto;
    }
    
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    public UUID getProductoId() {
        return producto != null ? producto.getId() : null;
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
    public BigDecimal getSubtotal() {
        if (cantidad != null && precioUnitario != null) {
            return precioUnitario.multiply(new BigDecimal(cantidad));
        }
        return BigDecimal.ZERO;
    }
    
    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OCLinea ocLinea = (OCLinea) o;
        return Objects.equals(id, ocLinea.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    // toString
    @Override
    public String toString() {
        return "OCLinea{" +
                "id=" + id +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", condicionAlmacen=" + condicionAlmacen +
                ", ordenCompraId=" + getOrdenCompraId() +
                ", productoId=" + getProductoId() +
                '}';
    }
}