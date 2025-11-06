package org.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class UbicacionRequest {
    
    @NotBlank(message = "El código es obligatorio")
    @Size(max = 50, message = "El código no puede exceder 50 caracteres")
    private String codigo;
    
    @NotNull(message = "La zona es obligatoria")
    private UUID zonaId;
    
    private String pasillo;
    private String estante;
    private String nivel;
    private Integer capacidadMaxima;
    private Boolean disponible = true;
    
    // Constructores
    public UbicacionRequest() {}
    
    // Getters y Setters
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public UUID getZonaId() {
        return zonaId;
    }
    
    public void setZonaId(UUID zonaId) {
        this.zonaId = zonaId;
    }
    
    public String getPasillo() {
        return pasillo;
    }
    
    public void setPasillo(String pasillo) {
        this.pasillo = pasillo;
    }
    
    public String getEstante() {
        return estante;
    }
    
    public void setEstante(String estante) {
        this.estante = estante;
    }
    
    public String getNivel() {
        return nivel;
    }
    
    public void setNivel(String nivel) {
        this.nivel = nivel;
    }
    
    public Integer getCapacidadMaxima() {
        return capacidadMaxima;
    }
    
    public void setCapacidadMaxima(Integer capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }
    
    public Boolean getDisponible() {
        return disponible;
    }
    
    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
}
