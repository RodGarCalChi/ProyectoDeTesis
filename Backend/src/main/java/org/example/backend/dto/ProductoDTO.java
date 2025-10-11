package org.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.backend.enumeraciones.CondicionAlmacen;
import org.example.backend.enumeraciones.TipoProducto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProductoDTO {
    
    private UUID id;
    
    @NotBlank(message = "El código SKU es obligatorio")
    @Size(max = 20, message = "El código SKU no puede exceder 20 caracteres")
    private String codigoSKU;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    @NotNull(message = "El tipo de producto es obligatorio")
    private TipoProducto tipo;
    
    @NotNull(message = "La condición de almacén es obligatoria")
    private CondicionAlmacen condicionAlmacen;
    
    private Boolean requiereCadenaFrio;
    
    @Size(max = 30, message = "El registro sanitario no puede exceder 30 caracteres")
    private String registroSanitario;
    
    @Size(max = 20, message = "La unidad de medida no puede exceder 20 caracteres")
    private String unidadMedida;
    
    private Integer vidaUtilMeses;
    private Float tempMin;
    private Float tempMax;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public ProductoDTO() {}
    
    public ProductoDTO(UUID id, String codigoSKU, String nombre, TipoProducto tipo,
                      CondicionAlmacen condicionAlmacen, Boolean requiereCadenaFrio,
                      String registroSanitario, String unidadMedida, Integer vidaUtilMeses,
                      Float tempMin, Float tempMax, LocalDateTime fechaCreacion,
                      LocalDateTime fechaActualizacion) {
        this.id = id;
        this.codigoSKU = codigoSKU;
        this.nombre = nombre;
        this.tipo = tipo;
        this.condicionAlmacen = condicionAlmacen;
        this.requiereCadenaFrio = requiereCadenaFrio;
        this.registroSanitario = registroSanitario;
        this.unidadMedida = unidadMedida;
        this.vidaUtilMeses = vidaUtilMeses;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }
    
    // Getters y Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getCodigoSKU() {
        return codigoSKU;
    }
    
    public void setCodigoSKU(String codigoSKU) {
        this.codigoSKU = codigoSKU;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public TipoProducto getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoProducto tipo) {
        this.tipo = tipo;
    }
    
    public CondicionAlmacen getCondicionAlmacen() {
        return condicionAlmacen;
    }
    
    public void setCondicionAlmacen(CondicionAlmacen condicionAlmacen) {
        this.condicionAlmacen = condicionAlmacen;
    }
    
    public Boolean getRequiereCadenaFrio() {
        return requiereCadenaFrio;
    }
    
    public void setRequiereCadenaFrio(Boolean requiereCadenaFrio) {
        this.requiereCadenaFrio = requiereCadenaFrio;
    }
    
    public String getRegistroSanitario() {
        return registroSanitario;
    }
    
    public void setRegistroSanitario(String registroSanitario) {
        this.registroSanitario = registroSanitario;
    }
    
    public String getUnidadMedida() {
        return unidadMedida;
    }
    
    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }
    
    public Integer getVidaUtilMeses() {
        return vidaUtilMeses;
    }
    
    public void setVidaUtilMeses(Integer vidaUtilMeses) {
        this.vidaUtilMeses = vidaUtilMeses;
    }
    
    public Float getTempMin() {
        return tempMin;
    }
    
    public void setTempMin(Float tempMin) {
        this.tempMin = tempMin;
    }
    
    public Float getTempMax() {
        return tempMax;
    }
    
    public void setTempMax(Float tempMax) {
        this.tempMax = tempMax;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
    
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}