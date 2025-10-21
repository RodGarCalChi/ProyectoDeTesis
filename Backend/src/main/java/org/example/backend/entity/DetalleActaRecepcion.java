package org.example.backend.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "detalles_acta_recepcion")
public class DetalleActaRecepcion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acta_recepcion_id", nullable = false)
    private ActaRecepcion actaRecepcion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @Column(name = "lote", length = 50)
    private String lote;
    
    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;
    
    @Column(name = "cantidad_declarada", nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidadDeclarada;
    
    @Column(name = "cantidad_recibida", nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidadRecibida;
    
    @Column(name = "cantidad_conforme", precision = 10, scale = 2)
    private BigDecimal cantidadConforme;
    
    @Column(name = "cantidad_no_conforme", precision = 10, scale = 2)
    private BigDecimal cantidadNoConforme;
    
    @Column(name = "precio_unitario", precision = 10, scale = 2)
    private BigDecimal precioUnitario;
    
    @Column(name = "observaciones", length = 500)
    private String observaciones;
    
    @Column(name = "conforme", nullable = false)
    private Boolean conforme;
    
    @Column(name = "motivo_no_conformidad", length = 300)
    private String motivoNoConformidad;
    
    // Constructores
    public DetalleActaRecepcion() {}
    
    public DetalleActaRecepcion(ActaRecepcion actaRecepcion, Producto producto, String lote,
                               LocalDate fechaVencimiento, BigDecimal cantidadDeclarada,
                               BigDecimal cantidadRecibida, BigDecimal precioUnitario,
                               String observaciones, Boolean conforme) {
        this.actaRecepcion = actaRecepcion;
        this.producto = producto;
        this.lote = lote;
        this.fechaVencimiento = fechaVencimiento;
        this.cantidadDeclarada = cantidadDeclarada;
        this.cantidadRecibida = cantidadRecibida;
        this.precioUnitario = precioUnitario;
        this.observaciones = observaciones;
        this.conforme = conforme;
        
        // Calcular cantidades conformes y no conformes
        if (conforme) {
            this.cantidadConforme = cantidadRecibida;
            this.cantidadNoConforme = BigDecimal.ZERO;
        } else {
            this.cantidadConforme = BigDecimal.ZERO;
            this.cantidadNoConforme = cantidadRecibida;
        }
    }
    
    // Getters y Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public ActaRecepcion getActaRecepcion() {
        return actaRecepcion;
    }
    
    public void setActaRecepcion(ActaRecepcion actaRecepcion) {
        this.actaRecepcion = actaRecepcion;
    }
    
    public Producto getProducto() {
        return producto;
    }
    
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    public String getLote() {
        return lote;
    }
    
    public void setLote(String lote) {
        this.lote = lote;
    }
    
    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }
    
    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    
    public BigDecimal getCantidadDeclarada() {
        return cantidadDeclarada;
    }
    
    public void setCantidadDeclarada(BigDecimal cantidadDeclarada) {
        this.cantidadDeclarada = cantidadDeclarada;
    }
    
    public BigDecimal getCantidadRecibida() {
        return cantidadRecibida;
    }
    
    public void setCantidadRecibida(BigDecimal cantidadRecibida) {
        this.cantidadRecibida = cantidadRecibida;
    }
    
    public BigDecimal getCantidadConforme() {
        return cantidadConforme;
    }
    
    public void setCantidadConforme(BigDecimal cantidadConforme) {
        this.cantidadConforme = cantidadConforme;
    }
    
    public BigDecimal getCantidadNoConforme() {
        return cantidadNoConforme;
    }
    
    public void setCantidadNoConforme(BigDecimal cantidadNoConforme) {
        this.cantidadNoConforme = cantidadNoConforme;
    }
    
    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }
    
    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
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
    
    public String getMotivoNoConformidad() {
        return motivoNoConformidad;
    }
    
    public void setMotivoNoConformidad(String motivoNoConformidad) {
        this.motivoNoConformidad = motivoNoConformidad;
    }
}