package org.example.backend.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "detalles_guia_remision_cliente")
public class DetalleGuiaRemisionCliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guia_remision_cliente_id", nullable = false)
    private GuiaRemisionCliente guiaRemisionCliente;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @Column(name = "descripcion", nullable = false, length = 300)
    private String descripcion;
    
    @Column(name = "unidad_medida", nullable = false, length = 20)
    private String unidadMedida;
    
    @Column(name = "cantidad", nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidad;
    
    @Column(name = "peso_unitario", precision = 8, scale = 3)
    private BigDecimal pesoUnitario;
    
    @Column(name = "peso_total", precision = 10, scale = 3)
    private BigDecimal pesoTotal;
    
    @Column(name = "valor_unitario", precision = 10, scale = 2)
    private BigDecimal valorUnitario;
    
    @Column(name = "valor_total", precision = 12, scale = 2)
    private BigDecimal valorTotal;
    
    @Column(name = "lote", length = 50)
    private String lote;
    
    @Column(name = "observaciones", length = 300)
    private String observaciones;
    
    // Constructores
    public DetalleGuiaRemisionCliente() {}
    
    public DetalleGuiaRemisionCliente(GuiaRemisionCliente guiaRemisionCliente, Producto producto,
                                     String descripcion, String unidadMedida, BigDecimal cantidad,
                                     BigDecimal pesoUnitario, BigDecimal valorUnitario,
                                     String lote, String observaciones) {
        this.guiaRemisionCliente = guiaRemisionCliente;
        this.producto = producto;
        this.descripcion = descripcion;
        this.unidadMedida = unidadMedida;
        this.cantidad = cantidad;
        this.pesoUnitario = pesoUnitario;
        this.valorUnitario = valorUnitario;
        this.lote = lote;
        this.observaciones = observaciones;
        
        // Calcular totales
        if (pesoUnitario != null && cantidad != null) {
            this.pesoTotal = pesoUnitario.multiply(cantidad);
        }
        if (valorUnitario != null && cantidad != null) {
            this.valorTotal = valorUnitario.multiply(cantidad);
        }
    }
    
    // Getters y Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public GuiaRemisionCliente getGuiaRemisionCliente() {
        return guiaRemisionCliente;
    }
    
    public void setGuiaRemisionCliente(GuiaRemisionCliente guiaRemisionCliente) {
        this.guiaRemisionCliente = guiaRemisionCliente;
    }
    
    public Producto getProducto() {
        return producto;
    }
    
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getUnidadMedida() {
        return unidadMedida;
    }
    
    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }
    
    public BigDecimal getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }
    
    public BigDecimal getPesoUnitario() {
        return pesoUnitario;
    }
    
    public void setPesoUnitario(BigDecimal pesoUnitario) {
        this.pesoUnitario = pesoUnitario;
    }
    
    public BigDecimal getPesoTotal() {
        return pesoTotal;
    }
    
    public void setPesoTotal(BigDecimal pesoTotal) {
        this.pesoTotal = pesoTotal;
    }
    
    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }
    
    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }
    
    public BigDecimal getValorTotal() {
        return valorTotal;
    }
    
    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
    
    public String getLote() {
        return lote;
    }
    
    public void setLote(String lote) {
        this.lote = lote;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}