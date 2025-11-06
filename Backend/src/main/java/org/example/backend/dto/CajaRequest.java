package org.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.backend.enumeraciones.TamanoCaja;

import java.time.LocalDate;
import java.util.UUID;

public class CajaRequest {
    
    @NotBlank(message = "El código es obligatorio")
    @Size(max = 50, message = "El código no puede exceder 50 caracteres")
    private String codigo;
    
    @NotNull(message = "El palet es obligatorio")
    private UUID paletId;
    
    @NotNull(message = "El cliente es obligatorio")
    private UUID clienteId;
    
    @NotNull(message = "El producto es obligatorio")
    private UUID productoId;
    
    @NotNull(message = "La cantidad es obligatoria")
    private Integer cantidad;
    
    @NotBlank(message = "El lote del producto es obligatorio")
    @Size(max = 50, message = "El lote no puede exceder 50 caracteres")
    private String loteProducto;
    
    private LocalDate fechaVencimiento;
    
    @NotNull(message = "El tamaño de caja es obligatorio")
    private TamanoCaja tamano;
    
    private Float pesoKg;
    private Float largoCm;
    private Float anchoCm;
    private Float altoCm;
    private Float temperaturaRequeridaMin;
    private Float temperaturaRequeridaMax;
    private Boolean requiereCadenaFrio = false;
    
    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    private String observaciones;
    
    private Boolean sellada = true;
    
    // Constructores
    public CajaRequest() {}
    
    // Getters y Setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    
    public UUID getPaletId() { return paletId; }
    public void setPaletId(UUID paletId) { this.paletId = paletId; }
    
    public UUID getClienteId() { return clienteId; }
    public void setClienteId(UUID clienteId) { this.clienteId = clienteId; }
    
    public UUID getProductoId() { return productoId; }
    public void setProductoId(UUID productoId) { this.productoId = productoId; }
    
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
}
