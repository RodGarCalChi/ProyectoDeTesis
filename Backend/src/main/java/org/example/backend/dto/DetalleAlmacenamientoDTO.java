package org.example.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class DetalleAlmacenamientoDTO {
    
    private UUID id;
    private UUID productoId;
    private String productoNombre;
    private String productoSku;
    private String productoTipo;
    private UUID loteId;
    private String loteNumero;
    private Integer cantidad;
    private BigDecimal pesoUnitario;
    private BigDecimal volumenUnitario;
    private LocalDate fechaVencimiento;
    private String ubicacionEspecifica;
    private BigDecimal temperaturaRequerida;
    private String observacionesProducto;
    private String estadoFisico;
    private Boolean requiereAtencionEspecial;
    private String motivoAtencionEspecial;
    
    // Constructors
    public DetalleAlmacenamientoDTO() {}
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
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
    
    public String getProductoTipo() {
        return productoTipo;
    }
    
    public void setProductoTipo(String productoTipo) {
        this.productoTipo = productoTipo;
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