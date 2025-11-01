package org.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class AsignarUbicacionRequest {
    
    @NotNull(message = "El ID de la ubicación es obligatorio")
    private UUID ubicacionId;
    
    private String codigoBarras;
    
    @NotBlank(message = "El nombre del usuario es obligatorio")
    private String usuarioNombre;
    
    private String observaciones;
    
    // Constructores
    public AsignarUbicacionRequest() {}
    
    public AsignarUbicacionRequest(UUID ubicacionId, String codigoBarras, 
                                  String usuarioNombre, String observaciones) {
        this.ubicacionId = ubicacionId;
        this.codigoBarras = codigoBarras;
        this.usuarioNombre = usuarioNombre;
        this.observaciones = observaciones;
    }
    
    // Getters y Setters
    public UUID getUbicacionId() {
        return ubicacionId;
    }
    
    public void setUbicacionId(UUID ubicacionId) {
        this.ubicacionId = ubicacionId;
    }
    
    public String getCodigoBarras() {
        return codigoBarras;
    }
    
    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }
    
    public String getUsuarioNombre() {
        return usuarioNombre;
    }
    
    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
