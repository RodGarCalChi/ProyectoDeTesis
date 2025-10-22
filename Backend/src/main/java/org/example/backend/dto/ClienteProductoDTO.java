package org.example.backend.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public class ClienteProductoDTO {
    
    private UUID id;
    
    @NotNull(message = "El ID del cliente es obligatorio")
    private UUID clienteId;
    
    private String clienteNombre;
    
    @NotNull(message = "El ID del producto es obligatorio")
    private UUID productoId;
    
    private String productoNombre;
    private String productoSku;
    
    private LocalDateTime fechaAsignacion;
    private Boolean activo;
    private String observaciones;
    
    // Constructores
    public ClienteProductoDTO() {}
    
    public ClienteProductoDTO(UUID id, UUID clienteId, String clienteNombre,
                             UUID productoId, String productoNombre, String productoSku,
                             LocalDateTime fechaAsignacion, Boolean activo, String observaciones) {
        this.id = id;
        this.clienteId = clienteId;
        this.clienteNombre = clienteNombre;
        this.productoId = productoId;
        this.productoNombre = productoNombre;
        this.productoSku = productoSku;
        this.fechaAsignacion = fechaAsignacion;
        this.activo = activo;
        this.observaciones = observaciones;
    }
    
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
    
    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }
    
    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }
    
    public Boolean getActivo() {
        return activo;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
