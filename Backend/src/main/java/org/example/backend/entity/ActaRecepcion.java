package org.example.backend.entity;

import jakarta.persistence.*;
import org.example.backend.enumeraciones.EstadoDocumento;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "actas_recepcion")
public class ActaRecepcion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @Column(name = "numero_acta", unique = true, nullable = false, length = 50)
    private String numeroActa;
    
    @Column(name = "fecha_recepcion", nullable = false)
    private LocalDateTime fechaRecepcion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @Column(name = "responsable_recepcion", nullable = false, length = 100)
    private String responsableRecepcion;
    
    @Column(name = "lugar_recepcion", length = 200)
    private String lugarRecepcion;
    
    @Column(name = "temperatura_llegada", precision = 5, scale = 2)
    private BigDecimal temperaturaLlegada;
    
    @Column(name = "condiciones_transporte", length = 500)
    private String condicionesTransporte;
    
    @Column(name = "observaciones", length = 1000)
    private String observaciones;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoDocumento estado;
    
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @Column(name = "creado_por", length = 100)
    private String creadoPor;
    
    @Column(name = "actualizado_por", length = 100)
    private String actualizadoPor;
    
    @OneToMany(mappedBy = "actaRecepcion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleActaRecepcion> detalles;
    
    // Constructores
    public ActaRecepcion() {}
    
    public ActaRecepcion(String numeroActa, LocalDateTime fechaRecepcion, Cliente cliente,
                        String responsableRecepcion, String lugarRecepcion, 
                        BigDecimal temperaturaLlegada, String condicionesTransporte,
                        String observaciones, EstadoDocumento estado, String creadoPor) {
        this.numeroActa = numeroActa;
        this.fechaRecepcion = fechaRecepcion;
        this.cliente = cliente;
        this.responsableRecepcion = responsableRecepcion;
        this.lugarRecepcion = lugarRecepcion;
        this.temperaturaLlegada = temperaturaLlegada;
        this.condicionesTransporte = condicionesTransporte;
        this.observaciones = observaciones;
        this.estado = estado;
        this.creadoPor = creadoPor;
        this.fechaCreacion = LocalDateTime.now();
    }
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (estado == null) {
            estado = EstadoDocumento.BORRADOR;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
    
    // Getters y Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getNumeroActa() {
        return numeroActa;
    }
    
    public void setNumeroActa(String numeroActa) {
        this.numeroActa = numeroActa;
    }
    
    public LocalDateTime getFechaRecepcion() {
        return fechaRecepcion;
    }
    
    public void setFechaRecepcion(LocalDateTime fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public String getResponsableRecepcion() {
        return responsableRecepcion;
    }
    
    public void setResponsableRecepcion(String responsableRecepcion) {
        this.responsableRecepcion = responsableRecepcion;
    }
    
    public String getLugarRecepcion() {
        return lugarRecepcion;
    }
    
    public void setLugarRecepcion(String lugarRecepcion) {
        this.lugarRecepcion = lugarRecepcion;
    }
    
    public BigDecimal getTemperaturaLlegada() {
        return temperaturaLlegada;
    }
    
    public void setTemperaturaLlegada(BigDecimal temperaturaLlegada) {
        this.temperaturaLlegada = temperaturaLlegada;
    }
    
    public String getCondicionesTransporte() {
        return condicionesTransporte;
    }
    
    public void setCondicionesTransporte(String condicionesTransporte) {
        this.condicionesTransporte = condicionesTransporte;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public EstadoDocumento getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoDocumento estado) {
        this.estado = estado;
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
    
    public String getCreadoPor() {
        return creadoPor;
    }
    
    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }
    
    public String getActualizadoPor() {
        return actualizadoPor;
    }
    
    public void setActualizadoPor(String actualizadoPor) {
        this.actualizadoPor = actualizadoPor;
    }
    
    public List<DetalleActaRecepcion> getDetalles() {
        return detalles;
    }
    
    public void setDetalles(List<DetalleActaRecepcion> detalles) {
        this.detalles = detalles;
    }
}