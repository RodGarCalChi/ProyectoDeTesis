package org.example.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class DetalleRecepcionDTO {
    
    private UUID id;
    
    @NotNull(message = "El ID del producto es obligatorio")
    private UUID productoId;
    
    private String productoNombre;
    private String productoSku;
    
    @NotNull(message = "El ID del lote es obligatorio")
    private UUID loteId;
    
    private String loteNumero;
    
    @NotNull(message = "La cantidad esperada es obligatoria")
    @Positive(message = "La cantidad esperada debe ser positiva")
    private Integer cantidadEsperada;
    
    @NotNull(message = "La cantidad recibida es obligatoria")
    private Integer cantidadRecibida;
    
    private Integer cantidadAceptada;
    private Integer cantidadRechazada;
    private LocalDate fechaVencimiento;
    private BigDecimal precioUnitario;
    private BigDecimal temperaturaRecepcion;
    private String observaciones;
    private Boolean conforme;
    private String motivoRechazo;
    
    // Constructores
    public DetalleRecepcionDTO() {}
    
    public DetalleRecepcionDTO(UUID id, UUID productoId, String productoNombre, String productoSku,
                              UUID loteId, String loteNumero, Integer cantidadEsperada,
                              Integer cantidadRecibida, Integer cantidadAceptada,
                              Integer cantidadRechazada, LocalDate fechaVencimiento,
                              BigDecimal precioUnitario, BigDecimal temperaturaRecepcion,
                              String observaciones, Boolean conforme, String motivoRechazo) {
        this.id = id;
        this.productoId = productoId;
        this.productoNombre = productoNombre;
        this.productoSku = productoSku;
        this.loteId = loteId;
        this.loteNumero = loteNumero;
        this.cantidadEsperada = cantidadEsperada;
        this.cantidadRecibida = cantidadRecibida;
        this.cantidadAceptada = cantidadAceptada;
        this.cantidadRechazada = cantidadRechazada;
        this.fechaVencimiento = fechaVencimiento;
        this.precioUnitario = precioUnitario;
        this.temperaturaRecepcion = temperaturaRecepcion;
        this.observaciones = observaciones;
        this.conforme = conforme;
        this.motivoRechazo = motivoRechazo;
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
    
    public Integer getCantidadEsperada() {
        return cantidadEsperada;
    }
    
    public void setCantidadEsperada(Integer cantidadEsperada) {
        this.cantidadEsperada = cantidadEsperada;
    }
    
    public Integer getCantidadRecibida() {
        return cantidadRecibida;
    }
    
    public void setCantidadRecibida(Integer cantidadRecibida) {
        this.cantidadRecibida = cantidadRecibida;
    }
    
    public Integer getCantidadAceptada() {
        return cantidadAceptada;
    }
    
    public void setCantidadAceptada(Integer cantidadAceptada) {
        this.cantidadAceptada = cantidadAceptada;
    }
    
    public Integer getCantidadRechazada() {
        return cantidadRechazada;
    }
    
    public void setCantidadRechazada(Integer cantidadRechazada) {
        this.cantidadRechazada = cantidadRechazada;
    }
    
    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }
    
    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    
    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }
    
    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
    
    public BigDecimal getTemperaturaRecepcion() {
        return temperaturaRecepcion;
    }
    
    public void setTemperaturaRecepcion(BigDecimal temperaturaRecepcion) {
        this.temperaturaRecepcion = temperaturaRecepcion;
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
    
    public String getMotivoRechazo() {
        return motivoRechazo;
    }
    
    public void setMotivoRechazo(String motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }
}