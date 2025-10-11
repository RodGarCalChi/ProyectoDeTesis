package org.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.backend.enumeraciones.CondicionAlmacen;
import org.example.backend.enumeraciones.TipoProducto;

public class ProductoCreateDTO {
    
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
    
    private Boolean requiereCadenaFrio = false;
    
    @Size(max = 30, message = "El registro sanitario no puede exceder 30 caracteres")
    private String registroSanitario;
    
    @Size(max = 20, message = "La unidad de medida no puede exceder 20 caracteres")
    private String unidadMedida;
    
    private Integer vidaUtilMeses;
    private Float tempMin;
    private Float tempMax;
    
    // Constructores
    public ProductoCreateDTO() {}
    
    public ProductoCreateDTO(String codigoSKU, String nombre, TipoProducto tipo,
                            CondicionAlmacen condicionAlmacen, Boolean requiereCadenaFrio,
                            String registroSanitario, String unidadMedida, Integer vidaUtilMeses,
                            Float tempMin, Float tempMax) {
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
    }
    
    // Getters y Setters
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
}