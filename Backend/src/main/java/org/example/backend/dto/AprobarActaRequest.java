package org.example.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class AprobarActaRequest {
    
    @NotBlank(message = "El nombre del usuario es obligatorio")
    private String usuarioNombre;
    
    private String observaciones;
    
    // Constructores
    public AprobarActaRequest() {}
    
    public AprobarActaRequest(String usuarioNombre, String observaciones) {
        this.usuarioNombre = usuarioNombre;
        this.observaciones = observaciones;
    }
    
    // Getters y Setters
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
