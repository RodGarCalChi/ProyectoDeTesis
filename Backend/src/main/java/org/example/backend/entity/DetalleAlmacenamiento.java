package org.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "detalle_almacenamiento")
public class DetalleAlmacenamiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registro_almacenamiento_id")
    private RegistroAlmacenamiento registroAlmacenamiento;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    private Producto producto;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id")
    private Lote lote;
    
    @NotNull
    @Column(name = "cantidad")
    private Integer cantidad;
    
    @Column(name = "peso_unitario")
    private BigDecimal pesoUnitario;
    
    @Column(name = "volumen_unitario")
    private BigDecimal volumenUnitario;
    
    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;
    
    @Column(name = "ubicacion_especifica")
    private String ubicacionEspecifica;
    
    @Column(name = "temperatura_requerida")
    private BigDecimal temperaturaRequerida;
    
    @Column(name = "observaciones_producto", columnDefinition = "TEXT")
    private String observacionesProducto;
    
    @Column(name = "estado_fisico")
    private String estadoFisico;
    
    @Column(name = "requiere_atencion_especial")
    private Boolean requiereAtencionEspecial = false;
    
    @Column(name = "motivo_atencion_especial")
    private String motivoAtencionEspecial;
    
    // Constructors
    public DetalleAlmacenamiento() {}
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public RegistroAlmacenamiento getRegistroAlmacenamiento() {
        return registroAlmacenamiento;
    }
    
    public void setRegistroAlmacenamiento(RegistroAlmacenamiento registroAlmacenamiento) {
        this.registroAlmacenamiento = registroAlmacenamiento;
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
    
    public Integer getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
    
    public BigDecimal getPesoUnitario() {
        return pesoUnitario;
    }
    
    public void setPesoUnitario(BigDecimal pesoUnitario) {
        this.pesoUnitario = pesoUnitario;
    }
    
    public BigDecimal getVolumenUnitario() {
        return volumenUnitario;
    }
    
    public void setVolumenUnitario(BigDecimal volumenUnitario) {
        this.volumenUnitario = volumenUnitario;
    }
    
    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }
    
    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    
    public String getUbicacionEspecifica() {
        return ubicacionEspecifica;
    }
    
    public void setUbicacionEspecifica(String ubicacionEspecifica) {
        this.ubicacionEspecifica = ubicacionEspecifica;
    }
    
    public BigDecimal getTemperaturaRequerida() {
        return temperaturaRequerida;
    }
    
    public void setTemperaturaRequerida(BigDecimal temperaturaRequerida) {
        this.temperaturaRequerida = temperaturaRequerida;
    }
    
    public String getObservacionesProducto() {
        return observacionesProducto;
    }
    
    public void setObservacionesProducto(String observacionesProducto) {
        this.observacionesProducto = observacionesProducto;
    }
    
    public String getEstadoFisico() {
        return estadoFisico;
    }
    
    public void setEstadoFisico(String estadoFisico) {
        this.estadoFisico = estadoFisico;
    }
    
    public Boolean getRequiereAtencionEspecial() {
        return requiereAtencionEspecial;
    }
    
    public void setRequiereAtencionEspecial(Boolean requiereAtencionEspecial) {
        this.requiereAtencionEspecial = requiereAtencionEspecial;
    }
    
    public String getMotivoAtencionEspecial() {
        return motivoAtencionEspecial;
    }
    
    public void setMotivoAtencionEspecial(String motivoAtencionEspecial) {
        this.motivoAtencionEspecial = motivoAtencionEspecial;
    }
}