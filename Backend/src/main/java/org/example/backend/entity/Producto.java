package org.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.backend.enumeraciones.CondicionAlmacen;
import org.example.backend.enumeraciones.TipoProducto;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "productos")
public class Producto {
    
    @Id
    @UuidGenerator
    private UUID id;
    
    @NotBlank(message = "El código SKU es obligatorio")
    @Size(max = 20, message = "El código SKU no puede exceder 20 caracteres")
    @Column(name = "codigo_sku", nullable = false, length = 20, unique = true)
    private String codigoSKU;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @NotNull(message = "El tipo de producto es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoProducto tipo;
    
    @NotNull(message = "La condición de almacén es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(name = "condicion_almacen", nullable = false, length = 20)
    private CondicionAlmacen condicionAlmacen;
    
    @Column(name = "requiere_cadena_frio", nullable = false)
    private Boolean requiereCadenaFrio = false;
    
    @Size(max = 30, message = "El registro sanitario no puede exceder 30 caracteres")
    @Column(name = "registro_sanitario", length = 30)
    private String registroSanitario;
    
    @Size(max = 20, message = "La unidad de medida no puede exceder 20 caracteres")
    @Column(name = "unidad_medida", length = 20)
    private String unidadMedida;
    
    @Column(name = "vida_util_meses")
    private Integer vidaUtilMeses;
    
    @Column(name = "temp_min")
    private Float tempMin;
    
    @Column(name = "temp_max")
    private Float tempMax;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public Producto() {}
    
    public Producto(String codigoSKU, String nombre, TipoProducto tipo, 
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
    
    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return Objects.equals(id, producto.id) && Objects.equals(codigoSKU, producto.codigoSKU);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, codigoSKU);
    }
    
    // toString
    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", codigoSKU='" + codigoSKU + '\'' +
                ", nombre='" + nombre + '\'' +
                ", tipo=" + tipo +
                ", condicionAlmacen=" + condicionAlmacen +
                ", requiereCadenaFrio=" + requiereCadenaFrio +
                ", registroSanitario='" + registroSanitario + '\'' +
                ", unidadMedida='" + unidadMedida + '\'' +
                ", vidaUtilMeses=" + vidaUtilMeses +
                ", tempMin=" + tempMin +
                ", tempMax=" + tempMax +
                '}';
    }
}