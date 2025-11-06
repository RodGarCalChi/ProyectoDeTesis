package org.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class ZonaRequest {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    @NotBlank(message = "El tipo es obligatorio")
    @Size(max = 50, message = "El tipo no puede exceder 50 caracteres")
    private String tipo;
    
    @NotNull(message = "El almacén es obligatorio")
    private UUID almacenId;
    
    private Float temperaturaMin;
    private Float temperaturaMax;
    private Integer capacidadTotal;
    
    // Constructores
    public ZonaRequest() {}
    
    // Getters y Setters
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public UUID getAlmacenId() {
        return almacenId;
    }
    
    public void setAlmacenId(UUID almacenId) {
        this.almacenId = almacenId;
    }
    
    public Float getTemperaturaMin() {
        return temperaturaMin;
    }
    
    public void setTemperaturaMin(Float temperaturaMin) {
        this.temperaturaMin = temperaturaMin;
    }
    
    public Float getTemperaturaMax() {
        return temperaturaMax;
    }
    
    public void setTemperaturaMax(Float temperaturaMax) {
        this.temperaturaMax = temperaturaMax;
    }
    
    public Integer getCapacidadTotal() {
        return capacidadTotal;
    }
    
    public void setCapacidadTotal(Integer capacidadTotal) {
        this.capacidadTotal = capacidadTotal;
    }
}
