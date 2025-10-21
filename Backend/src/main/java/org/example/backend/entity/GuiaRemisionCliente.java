package org.example.backend.entity;

import jakarta.persistence.*;
import org.example.backend.enumeraciones.EstadoDocumento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "guias_remision_cliente")
public class GuiaRemisionCliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @Column(name = "numero_guia", unique = true, nullable = false, length = 50)
    private String numeroGuia;
    
    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision;
    
    @Column(name = "fecha_traslado")
    private LocalDateTime fechaTraslado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @Column(name = "destinatario", nullable = false, length = 200)
    private String destinatario;
    
    @Column(name = "direccion_origen", nullable = false, length = 300)
    private String direccionOrigen;
    
    @Column(name = "direccion_destino", nullable = false, length = 300)
    private String direccionDestino;
    
    @Column(name = "motivo_traslado", nullable = false, length = 200)
    private String motivoTraslado;
    
    @Column(name = "transportista", length = 200)
    private String transportista;
    
    @Column(name = "placa_vehiculo", length = 20)
    private String placaVehiculo;
    
    @Column(name = "licencia_conductor", length = 20)
    private String licenciaConductor;
    
    @Column(name = "peso_total", precision = 10, scale = 2)
    private BigDecimal pesoTotal;
    
    @Column(name = "valor_total", precision = 12, scale = 2)
    private BigDecimal valorTotal;
    
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
    
    @OneToMany(mappedBy = "guiaRemisionCliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleGuiaRemisionCliente> detalles;
    
    // Constructores
    public GuiaRemisionCliente() {}
    
    public GuiaRemisionCliente(String numeroGuia, LocalDateTime fechaEmision, 
                              LocalDateTime fechaTraslado, Cliente cliente, String destinatario,
                              String direccionOrigen, String direccionDestino, String motivoTraslado,
                              String transportista, String placaVehiculo, String licenciaConductor,
                              String observaciones, String creadoPor) {
        this.numeroGuia = numeroGuia;
        this.fechaEmision = fechaEmision;
        this.fechaTraslado = fechaTraslado;
        this.cliente = cliente;
        this.destinatario = destinatario;
        this.direccionOrigen = direccionOrigen;
        this.direccionDestino = direccionDestino;
        this.motivoTraslado = motivoTraslado;
        this.transportista = transportista;
        this.placaVehiculo = placaVehiculo;
        this.licenciaConductor = licenciaConductor;
        this.observaciones = observaciones;
        this.creadoPor = creadoPor;
        this.estado = EstadoDocumento.BORRADOR;
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
    
    public String getNumeroGuia() {
        return numeroGuia;
    }
    
    public void setNumeroGuia(String numeroGuia) {
        this.numeroGuia = numeroGuia;
    }
    
    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }
    
    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }
    
    public LocalDateTime getFechaTraslado() {
        return fechaTraslado;
    }
    
    public void setFechaTraslado(LocalDateTime fechaTraslado) {
        this.fechaTraslado = fechaTraslado;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public String getDestinatario() {
        return destinatario;
    }
    
    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }
    
    public String getDireccionOrigen() {
        return direccionOrigen;
    }
    
    public void setDireccionOrigen(String direccionOrigen) {
        this.direccionOrigen = direccionOrigen;
    }
    
    public String getDireccionDestino() {
        return direccionDestino;
    }
    
    public void setDireccionDestino(String direccionDestino) {
        this.direccionDestino = direccionDestino;
    }
    
    public String getMotivoTraslado() {
        return motivoTraslado;
    }
    
    public void setMotivoTraslado(String motivoTraslado) {
        this.motivoTraslado = motivoTraslado;
    }
    
    public String getTransportista() {
        return transportista;
    }
    
    public void setTransportista(String transportista) {
        this.transportista = transportista;
    }
    
    public String getPlacaVehiculo() {
        return placaVehiculo;
    }
    
    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
    }
    
    public String getLicenciaConductor() {
        return licenciaConductor;
    }
    
    public void setLicenciaConductor(String licenciaConductor) {
        this.licenciaConductor = licenciaConductor;
    }
    
    public BigDecimal getPesoTotal() {
        return pesoTotal;
    }
    
    public void setPesoTotal(BigDecimal pesoTotal) {
        this.pesoTotal = pesoTotal;
    }
    
    public BigDecimal getValorTotal() {
        return valorTotal;
    }
    
    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
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
    
    public List<DetalleGuiaRemisionCliente> getDetalles() {
        return detalles;
    }
    
    public void setDetalles(List<DetalleGuiaRemisionCliente> detalles) {
        this.detalles = detalles;
    }
}