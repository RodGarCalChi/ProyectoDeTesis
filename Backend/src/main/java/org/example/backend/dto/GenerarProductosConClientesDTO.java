package org.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.backend.enumeraciones.CondicionAlmacen;
import org.example.backend.enumeraciones.TipoProducto;

import java.util.List;
import java.util.UUID;

public class GenerarProductosConClientesDTO {
    
    @NotEmpty(message = "La lista de productos es obligatoria")
    private List<ProductoGeneracionDTO> productos;
    
    @NotEmpty(message = "La lista de clientes es obligatoria")
    private List<UUID> clienteIds;
    
    private String observaciones; // Observaciones para todas las asociaciones
    
    // Clase interna para los datos del producto a generar
    public static class ProductoGeneracionDTO {
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
    
    // Getters y Setters
    public List<ProductoGeneracionDTO> getProductos() {
        return productos;
    }
    
    public void setProductos(List<ProductoGeneracionDTO> productos) {
        this.productos = productos;
    }
    
    public List<UUID> getClienteIds() {
        return clienteIds;
    }
    
    public void setClienteIds(List<UUID> clienteIds) {
        this.clienteIds = clienteIds;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}





