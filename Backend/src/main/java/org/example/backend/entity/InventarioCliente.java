package org.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.example.backend.enumeraciones.EstadoInventario;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "inventario_cliente")
public class InventarioCliente {
    
    @Id
    @UuidGenerator
    private UUID id;
    
    @NotNull(message = "El cliente es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @NotNull(message = "El producto es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @NotNull(message = "El lote es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id", nullable = false)
    private Lote lote;
    
    @NotNull(message = "La recepción es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recepcion_id", nullable = false)
    private RecepcionMercaderia recepcion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detalle_recepcion_id")
    private DetalleRecepcion detalleRecepcion;
    
    @NotNull(message = "La cantidad disponible es obligatoria")
    @PositiveOrZero(message = "La cantidad disponible debe ser mayor o igual a cero")
    @Column(name = "cantidad_disponible", nullable = false)
    private Integer cantidadDisponible;
    
    @PositiveOrZero(message = "La cantidad reservada debe ser mayor o igual a cero")
    @Column(name = "cantidad_reservada", nullable = false)
    private Integer cantidadReservada = 0;
    
    @PositiveOrZero(message = "La cantidad despachada debe ser mayor o igual a cero")
    @Column(name = "cantidad_despachada", nullable = false)
    private Integer cantidadDespachada = 0;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;
    
    @Column(name = "codigo_barras", length = 100)
    private String codigoBarras;
    
    @NotNull(message = "La fecha de ingreso es obligatoria")
    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDateTime fechaIngreso;
    
    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;
    
    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 30)
    private EstadoInventario estado;
    
    @Column(name = "temperatura_almacenamiento", precision = 5, scale = 2)
    private BigDecimal temperaturaAlmacenamiento;
    
    @Column(name = "observaciones", length = 500)
    private String observaciones;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_registro_id")
    private Usuario usuarioRegistro;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_ubicacion_id")
    private Usuario usuarioUbicacion;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public InventarioCliente() {}
    
    public InventarioCliente(Cliente cliente, Producto producto, Lote lote, 
                            RecepcionMercaderia recepcion, Integer cantidadDisponible,
                            LocalDate fechaVencimiento, EstadoInventario estado) {
        this.cliente = cliente;
        this.producto = producto;
        this.lote = lote;
        this.recepcion = recepcion;
        this.cantidadDisponible = cantidadDisponible;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
        this.fechaIngreso = LocalDateTime.now();
    }
    
    // Getters y Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public Producto getProducto() {
        return producto;
    }
    
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    public Lote getLote() {
        return lote;
    }
    
    public void setLote(Lote lote) {
        this.lote = lote;
    }
    
    public RecepcionMercaderia getRecepcion() {
        return recepcion;
    }
    
    public void setRecepcion(RecepcionMercaderia recepcion) {
        this.recepcion = recepcion;
    }
    
    public DetalleRecepcion getDetalleRecepcion() {
        return detalleRecepcion;
    }
    
    public void setDetalleRecepcion(DetalleRecepcion detalleRecepcion) {
        this.detalleRecepcion = detalleRecepcion;
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
    
    public Integer getCantidadDespachada() {
        return cantidadDespachada;
    }
    
    public void setCantidadDespachada(Integer cantidadDespachada) {
        this.cantidadDespachada = cantidadDespachada;
    }
    
    public Ubicacion getUbicacion() {
        return ubicacion;
    }
    
    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }
    
    public String getCodigoBarras() {
        return codigoBarras;
    }
    
    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }
    
    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }
    
    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
    
    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }
    
    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    
    public EstadoInventario getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoInventario estado) {
        this.estado = estado;
    }
    
    public BigDecimal getTemperaturaAlmacenamiento() {
        return temperaturaAlmacenamiento;
    }
    
    public void setTemperaturaAlmacenamiento(BigDecimal temperaturaAlmacenamiento) {
        this.temperaturaAlmacenamiento = temperaturaAlmacenamiento;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public Usuario getUsuarioRegistro() {
        return usuarioRegistro;
    }
    
    public void setUsuarioRegistro(Usuario usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }
    
    public Usuario getUsuarioUbicacion() {
        return usuarioUbicacion;
    }
    
    public void setUsuarioUbicacion(Usuario usuarioUbicacion) {
        this.usuarioUbicacion = usuarioUbicacion;
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
        InventarioCliente that = (InventarioCliente) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "InventarioCliente{" +
                "id=" + id +
                ", cantidadDisponible=" + cantidadDisponible +
                ", estado=" + estado +
                ", fechaVencimiento=" + fechaVencimiento +
                '}';
    }
}
