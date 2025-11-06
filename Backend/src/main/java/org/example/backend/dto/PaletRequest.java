package org.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class PaletRequest {
    
    @NotBlank(message = "El código es obligatorio")
    @Size(max = 50, message = "El código no puede exceder 50 caracteres")
    private String codigo;
    
    @NotNull(message = "El lote es obligatorio")
    private UUID loteId;
    
    private UUID ubicacionId;
    
    private Integer capacidadMaxima;
    private Integer cajasActuales = 0;
    private Float pesoMaximoKg;
    private Float pesoActualKg = 0.0f;
    private Boolean disponible = true;
    
    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    private String observaciones;
    
    // Constructores
    public PaletRequest() {}
    
    // Getters y Setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    
    public UUID getLoteId() { return loteId; }
    public void setLoteId(UUID loteId) { this.loteId = loteId; }
    
    public UUID getUbicacionId() { return ubicacionId; }
    public void setUbicacionId(UUID ubicacionId) { this.ubicacionId = ubicacionId; }
    
    public Integer getCapacidadMaxima() { return capacidadMaxima; }
    public void setCapacidadMaxima(Integer capacidadMaxima) { this.capacidadMaxima = capacidadMaxima; }
    
    public Integer getCajasActuales() { return cajasActuales; }
    public void setCajasActuales(Integer cajasActuales) { this.cajasActuales = cajasActuales; }
    
    public Float getPesoMaximoKg() { return pesoMaximoKg; }
    public void setPesoMaximoKg(Float pesoMaximoKg) { this.pesoMaximoKg = pesoMaximoKg; }
    
    public Float getPesoActualKg() { return pesoActualKg; }
    public void setPesoActualKg(Float pesoActualKg) { this.pesoActualKg = pesoActualKg; }
    
    public Boolean getDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }
    
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
