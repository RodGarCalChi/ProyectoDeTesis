package org.example.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.example.backend.enumeraciones.EstadoInventario;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class InventarioClienteDTO {
    
    private UUID id;
    
    @NotNull(message = "El ID del cliente es obligatorio")
    private UUID clienteId;
    private String clienteNombre;
    
    @NotNull(message = "El ID del producto es obligatorio")
    private UUID productoId;
    private String productoNombre;
    private String productoSku;
    
    @NotNull(message = "El ID del lote es obligatorio")
    private UUID loteId;
    private String loteNumero;
    
    @NotNull(message = "El ID de la recepción es obligatorio")
    private UUID recepcionId;
    private String numeroOrdenCompra;
    
    private UUID detalleRecepcionId;
    
    @NotNull(message = "La cantidad disponible es obligatoria")
    @PositiveOrZero(message = "La cantidad disponible debe ser mayor o igual a cero")
    private Integer cantidadDisponible;
    
    private Integer cantidadReservada;
    private Integer cantidadDespachada;
    
    private UUID ubicacionId;
    private String ubicacionCodigo;
    private String codigoBarras;
    
    @NotNull(message = "La fecha de ingreso es obligatoria")
    private LocalDateTime fechaIngreso;
    
    @NotNull(message = "La fecha de vencimiento es obligatoria")
    private LocalDate fechaVencimiento;
    
    @NotNull(message = "El estado es obligatorio")
    private EstadoInventario estado;
    
    private BigDecimal temperaturaAlmacenamiento;
    private String observaciones;
    
    private UUID usuarioRegistroId;
    private String usuarioRegistroNombre;
    
    private UUID usuarioUbicacionId;
    private String usuarioUbicacionNombre;
    
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public InventarioClienteDTO() {}
    
    // Getters y Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public UUID getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(UUID clienteId) {
        this.clienteId = clienteId;
    }
    
    public String getClienteNombre() {
        return clienteNombre;
    }
    
    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }
    
    public UUID getProductoId() {
        return productoId;
    }
    
    public void setProductoId(UUID productoId) {
        this.productoId = productoId;
    }
    
    public String getProductoNombre() {
        return productoNombre;
    }
    
    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }
    
    public String getProductoSku() {
        return productoSku;
    }
    
    public void setProductoSku(String productoSku) {
        this.productoSku = productoSku;
    }
    
    public UUID getLoteId() {
        return loteId;
    }
    
    public void setLoteId(UUID loteId) {
        this.loteId = loteId;
    }
    
    public String getLoteNumero() {
        return loteNumero;
    }
    
    public void setLoteNumero(String loteNumero) {
        this.loteNumero = loteNumero;
    }
    
    public UUID getRecepcionId() {
        return recepcionId;
    }
    
    public void setRecepcionId(UUID recepcionId) {
        this.recepcionId = recepcionId;
    }
    
    public String getNumeroOrdenCompra() {
        return numeroOrdenCompra;
    }
    
    public void setNumeroOrdenCompra(String numeroOrdenCompra) {
        this.numeroOrdenCompra = numeroOrdenCompra;
    }
    
    public UUID getDetalleRecepcionId() {
        return detalleRecepcionId;
    }
    
    public void setDetalleRecepcionId(UUID detalleRecepcionId) {
        this.detalleRecepcionId = detalleRecepcionId;
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
    
    public UUID getUbicacionId() {
        return ubicacionId;
    }
    
    public void setUbicacionId(UUID ubicacionId) {
        this.ubicacionId = ubicacionId;
    }
    
    public String getUbicacionCodigo() {
        return ubicacionCodigo;
    }
    
    public void setUbicacionCodigo(String ubicacionCodigo) {
        this.ubicacionCodigo = ubicacionCodigo;
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
    
    public UUID getUsuarioRegistroId() {
        return usuarioRegistroId;
    }
    
    public void setUsuarioRegistroId(UUID usuarioRegistroId) {
        this.usuarioRegistroId = usuarioRegistroId;
    }
    
    public String getUsuarioRegistroNombre() {
        return usuarioRegistroNombre;
    }
    
    public void setUsuarioRegistroNombre(String usuarioRegistroNombre) {
        this.usuarioRegistroNombre = usuarioRegistroNombre;
    }
    
    public UUID getUsuarioUbicacionId() {
        return usuarioUbicacionId;
    }
    
    public void setUsuarioUbicacionId(UUID usuarioUbicacionId) {
        this.usuarioUbicacionId = usuarioUbicacionId;
    }
    
    public String getUsuarioUbicacionNombre() {
        return usuarioUbicacionNombre;
    }
    
    public void setUsuarioUbicacionNombre(String usuarioUbicacionNombre) {
        this.usuarioUbicacionNombre = usuarioUbicacionNombre;
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
}
