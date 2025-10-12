package org.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "detalles_recepcion")
public class DetalleRecepcion {
    
    @Id
    @UuidGenerator
    private UUID id;
    
    @NotNull(message = "La recepción es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recepcion_id", nullable = false)
    private RecepcionMercaderia recepcion;
    
    @NotNull(message = "El producto es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @NotNull(message = "El lote es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id", nullable = false)
    private Lote lote;
    
    @NotNull(message = "La cantidad esperada es obligatoria")
    @Positive(message = "La cantidad esperada debe ser positiva")
    @Column(name = "cantidad_esperada", nullable = false)
    private Integer cantidadEsperada;
    
    @NotNull(message = "La cantidad recibida es obligatoria")
    @Column(name = "cantidad_recibida", nullable = false)
    private Integer cantidadRecibida;
    
    @Column(name = "cantidad_aceptada")
    private Integer cantidadAceptada;
    
    @Column(name = "cantidad_rechazada")
    private Integer cantidadRechazada;
    
    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;
    
    @Column(name = "precio_unitario", precision = 10, scale = 2)
    private BigDecimal precioUnitario;
    
    @Column(name = "temperatura_recepcion", precision = 5, scale = 2)
    private BigDecimal temperaturaRecepcion;
    
    @Column(name = "observaciones", length = 500)
    private String observaciones;
    
    @Column(name = "conforme", nullable = false)
    private Boolean conforme = false;
    
    @Column(name = "motivo_rechazo", length = 200)
    private String motivoRechazo;
    
    // Constructores
    public DetalleRecepcion() {}
    
    public DetalleRecepcion(RecepcionMercaderia recepcion, Producto producto, Lote lote,
                           Integer cantidadEsperada, Integer cantidadRecibida) {
        this.recepcion = recepcion;
        this.producto = producto;
        this.lote = lote;
        this.cantidadEsperada = cantidadEsperada;
        this.cantidadRecibida = cantidadRecibida;
    }
    
    // Getters y Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public RecepcionMercaderia getRecepcion() {
        return recepcion;
    }
    
    public void setRecepcion(RecepcionMercaderia recepcion) {
        this.recepcion = recepcion;
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
    
    public Integer getCantidadEsperada() {
        return cantidadEsperada;
    }
    
    public void setCantidadEsperada(Integer cantidadEsperada) {
        this.cantidadEsperada = cantidadEsperada;
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
    
    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }
    
    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    
    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }
    
    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
    
    public BigDecimal getTemperaturaRecepcion() {
        return temperaturaRecepcion;
    }
    
    public void setTemperaturaRecepcion(BigDecimal temperaturaRecepcion) {
        this.temperaturaRecepcion = temperaturaRecepcion;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public Boolean getConforme() {
        return conforme;
    }
    
    public void setConforme(Boolean conforme) {
        this.conforme = conforme;
    }
    
    public String getMotivoRechazo() {
        return motivoRechazo;
    }
    
    public void setMotivoRechazo(String motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }
    
    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetalleRecepcion that = (DetalleRecepcion) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}