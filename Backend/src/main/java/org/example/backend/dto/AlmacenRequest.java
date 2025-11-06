package org.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class AlmacenRequest {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
    private String direccion;
    
    @NotNull(message = "El operador logístico es obligatorio")
    private UUID operadorLogisticoId;
    
    @NotNull(message = "El cliente es obligatorio")
    private UUID clienteId;
    
    private Boolean tieneAreaControlados = false;
    
    // Constructores
    public AlmacenRequest() {}
    
    // Getters y Setters
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public UUID getOperadorLogisticoId() {
        return operadorLogisticoId;
    }
    
    public void setOperadorLogisticoId(UUID operadorLogisticoId) {
        this.operadorLogisticoId = operadorLogisticoId;
    }
    
    public UUID getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(UUID clienteId) {
        this.clienteId = clienteId;
    }
    
    public Boolean getTieneAreaControlados() {
        return tieneAreaControlados;
    }
    
    public void setTieneAreaControlados(Boolean tieneAreaControlados) {
        this.tieneAreaControlados = tieneAreaControlados;
    }
}
