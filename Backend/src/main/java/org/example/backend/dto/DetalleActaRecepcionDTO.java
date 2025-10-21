package org.example.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class DetalleActaRecepcionDTO {
    
    private UUID id;
    
    @NotNull(message = "El ID del producto es obligatorio")
    private UUID productoId;
    
    private String productoNombre;
    private String productoSku;
    
    @Size(max = 50, message = "El lote no puede exceder 50 caracteres")
    private String lote;
    
    private LocalDate fechaVencimiento;
    
    @NotNull(message = "La cantidad declarada es obligatoria")
    @Positive(message = "La cantidad declarada debe ser positiva")
    private BigDecimal cantidadDeclarada;
    
    @NotNull(message = "La cantidad recibida es obligatoria")
    @Positive(message = "La cantidad recibida debe ser positiva")
    private BigDecimal cantidadRecibida;
    
    private BigDecimal cantidadConforme;
    private BigDecimal cantidadNoConforme;
    private BigDecimal precioUnitario;
    
    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    private String observaciones;
    
    @NotNull(message = "El campo conforme es obligatorio")
    private Boolean conforme;
    
    @Size(max = 300, message = "El motivo de no conformidad no puede exceder 300 caracteres")
    private String motivoNoConformidad;
    
    // Constructores
    public DetalleActaRecepcionDTO() {}
    
    public DetalleActaRecepcionDTO(UUID id, UUID productoId, String productoNombre, String productoSku,
                                  String lote, LocalDate fechaVencimiento, BigDecimal cantidadDeclarada,
                                  BigDecimal cantidadRecibida, BigDecimal cantidadConforme,
                                  BigDecimal cantidadNoConforme, BigDecimal precioUnitario,
                                  String observaciones, Boolean conforme, String motivoNoConformidad) {
        this.id = id;
        this.productoId = productoId;
        this.productoNombre = productoNombre;
        this.productoSku = productoSku;
        this.lote = lote;
        this.fechaVencimiento = fechaVencimiento;
        this.cantidadDeclarada = cantidadDeclarada;
        this.cantidadRecibida = cantidadRecibida;
        this.cantidadConforme = cantidadConforme;
        this.cantidadNoConforme = cantidadNoConforme;
        this.precioUnitario = precioUnitario;
        this.observaciones = observaciones;
        this.conforme = conforme;
        this.motivoNoConformidad = motivoNoConformidad;
    }
    
    // Getters y Setters
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