package org.example.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.backend.enumeraciones.TamanoCaja;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "cajas")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Caja {
    
    @Id
    @UuidGenerator
    private UUID id;
    
    @NotBlank(message = "El código es obligatorio")
    @Size(max = 50, message = "El código no puede exceder 50 caracteres")
    @Column(name = "codigo", nullable = false, length = 50, unique = true)
    private String codigo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "palet_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Palet palet;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Cliente cliente;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Producto producto;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad; // Ej: 10 vacunas
    
    @NotBlank(message = "El lote del producto es obligatorio")
    @Size(max = 50, message = "El lote no puede exceder 50 caracteres")
    @Column(name = "lote_producto", nullable = false, length = 50)
    private String loteProducto;
    
    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;
    
    @NotNull(message = "El tamaño de caja es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "tamano", nullable = false, length = 20)
    private TamanoCaja tamano;
    
    @Column(name = "peso_kg")
    private Float pesoKg;
    
    @Column(name = "largo_cm")
    private Float largoCm;
    
    @Column(name = "ancho_cm")
    private Float anchoCm;
    
    @Column(name = "alto_cm")
    private Float altoCm;
    
    @Column(name = "temperatura_requerida_min")
    private Float temperaturaRequeridaMin;
    
    @Column(name = "temperatura_requerida_max")
    private Float temperaturaRequeridaMax;
    
    @Column(name = "requiere_cadena_frio", nullable = false)
    private Boolean requiereCadenaFrio = false;
    
    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    @Column(name = "observaciones", length = 500)
    private String observaciones;
    
    @Column(name = "sellada", nullable = false)
    private Boolean sellada = true;
    
    @Column(name = "fecha_sellado")
    private LocalDateTime fechaSellado;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public Caja() {}
    
    public Caja(String codigo, Palet palet, Cliente cliente, Producto producto, Integer cantidad) {
        this.codigo = codigo;
        this.palet = palet;
        this.cliente = cliente;
        this.producto = producto;
        this.cantidad = cantidad;
    }
    
    // Getters y Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    
    public Palet getPalet() { return palet; }
    public void setPalet(Palet palet) { this.palet = palet; }
    
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    
    public String getLoteProducto() { return loteProducto; }
    public void setLoteProducto(String loteProducto) { this.loteProducto = loteProducto; }
    
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
    
    public TamanoCaja getTamano() { return tamano; }
    public void setTamano(TamanoCaja tamano) { this.tamano = tamano; }
    
    public Float getPesoKg() { return pesoKg; }
    public void setPesoKg(Float pesoKg) { this.pesoKg = pesoKg; }
    
    public Float getLargoCm() { return largoCm; }
    public void setLargoCm(Float largoCm) { this.largoCm = largoCm; }
    
    public Float getAnchoCm() { return anchoCm; }
    public void setAnchoCm(Float anchoCm) { this.anchoCm = anchoCm; }
    
    public Float getAltoCm() { return altoCm; }
    public void setAltoCm(Float altoCm) { this.altoCm = altoCm; }
    
    public Float getTemperaturaRequeridaMin() { return temperaturaRequeridaMin; }
    public void setTemperaturaRequeridaMin(Float temperaturaRequeridaMin) { this.temperaturaRequeridaMin = temperaturaRequeridaMin; }
    
    public Float getTemperaturaRequeridaMax() { return temperaturaRequeridaMax; }
    public void setTemperaturaRequeridaMax(Float temperaturaRequeridaMax) { this.temperaturaRequeridaMax = temperaturaRequeridaMax; }
    
    public Boolean getRequiereCadenaFrio() { return requiereCadenaFrio; }
    public void setRequiereCadenaFrio(Boolean requiereCadenaFrio) { this.requiereCadenaFrio = requiereCadenaFrio; }
    
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    
    public Boolean getSellada() { return sellada; }
    public void setSellada(Boolean sellada) { this.sellada = sellada; }
    
    public LocalDateTime getFechaSellado() { return fechaSellado; }
    public void setFechaSellado(LocalDateTime fechaSellado) { this.fechaSellado = fechaSellado; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Caja caja = (Caja) o;
        return Objects.equals(id, caja.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
