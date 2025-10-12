package org.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.example.backend.enumeraciones.EstadoAlmacenamiento;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "registro_almacenamiento")
public class RegistroAlmacenamiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @NotNull
    @Column(name = "numero_guia_remision", unique = true)
    private String numeroGuiaRemision;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    
    @NotNull
    @Column(name = "fecha_almacenamiento")
    private LocalDateTime fechaAlmacenamiento;
    
    @NotNull
    @Column(name = "operador_responsable")
    private String operadorResponsable;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoAlmacenamiento estado;
    
    @Column(name = "ubicacion_almacen")
    private String ubicacionAlmacen;
    
    @Column(name = "temperatura_almacenamiento")
    private BigDecimal temperaturaAlmacenamiento;
    
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
    
    @Column(name = "verificado_por_admin")
    private Boolean verificadoPorAdmin = false;
    
    @Column(name = "verificador_admin")
    private String verificadorAdmin;
    
    @Column(name = "fecha_verificacion")
    private LocalDateTime fechaVerificacion;
    
    @Column(name = "peso_total")
    private BigDecimal pesoTotal;
    
    @Column(name = "volumen_total")
    private BigDecimal volumenTotal;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @OneToMany(mappedBy = "registroAlmacenamiento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleAlmacenamiento> detalles = new ArrayList<>();
    
    // Constructors
    public RegistroAlmacenamiento() {}
    
    // Helper methods
    public void addDetalle(DetalleAlmacenamiento detalle) {
        detalles.add(detalle);
        detalle.setRegistroAlmacenamiento(this);
    }
    
    public void removeDetalle(DetalleAlmacenamiento detalle) {
        detalles.remove(detalle);
        detalle.setRegistroAlmacenamiento(null);
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getNumeroGuiaRemision() {
        return numeroGuiaRemision;
    }
    
    public void setNumeroGuiaRemision(String numeroGuiaRemision) {
        this.numeroGuiaRemision = numeroGuiaRemision;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public LocalDateTime getFechaAlmacenamiento() {
        return fechaAlmacenamiento;
    }
    
    public void setFechaAlmacenamiento(LocalDateTime fechaAlmacenamiento) {
        this.fechaAlmacenamiento = fechaAlmacenamiento;
    }
    
    public String getOperadorResponsable() {
        return operadorResponsable;
    }
    
    public void setOperadorResponsable(String operadorResponsable) {
        this.operadorResponsable = operadorResponsable;
    }
    
    public EstadoAlmacenamiento getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoAlmacenamiento estado) {
        this.estado = estado;
    }
    
    public String getUbicacionAlmacen() {
        return ubicacionAlmacen;
    }
    
    public void setUbicacionAlmacen(String ubicacionAlmacen) {
        this.ubicacionAlmacen = ubicacionAlmacen;
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
    
    public Boolean getVerificadoPorAdmin() {
        return verificadoPorAdmin;
    }
    
    public void setVerificadoPorAdmin(Boolean verificadoPorAdmin) {
        this.verificadoPorAdmin = verificadoPorAdmin;
    }
    
    public String getVerificadorAdmin() {
        return verificadorAdmin;
    }
    
    public void setVerificadorAdmin(String verificadorAdmin) {
        this.verificadorAdmin = verificadorAdmin;
    }
    
    public LocalDateTime getFechaVerificacion() {
        return fechaVerificacion;
    }
    
    public void setFechaVerificacion(LocalDateTime fechaVerificacion) {
        this.fechaVerificacion = fechaVerificacion;
    }
    
    public BigDecimal getPesoTotal() {
        return pesoTotal;
    }
    
    public void setPesoTotal(BigDecimal pesoTotal) {
        this.pesoTotal = pesoTotal;
    }
    
    public BigDecimal getVolumenTotal() {
        return volumenTotal;
    }
    
    public void setVolumenTotal(BigDecimal volumenTotal) {
        this.volumenTotal = volumenTotal;
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
    
    public List<DetalleAlmacenamiento> getDetalles() {
        return detalles;
    }
    
    public void setDetalles(List<DetalleAlmacenamiento> detalles) {
        this.detalles = detalles;
    }
}