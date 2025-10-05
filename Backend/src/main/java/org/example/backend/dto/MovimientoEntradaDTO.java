package org.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalTime;

public class MovimientoEntradaDTO {
    
    @NotBlank(message = "El número de referencia es obligatorio")
    private String referencia;
    
    @NotBlank(message = "El proveedor es obligatorio")
    private String proveedor;
    
    @NotBlank(message = "El producto es obligatorio")
    private String producto;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
    
    @NotBlank(message = "La ubicación es obligatoria")
    private String ubicacion;
    
    @NotBlank(message = "El campo 'Recibido por' es obligatorio")
    private String recibidoPor;
    
    private LocalDate dia;
    
    private LocalTime hora;
    
    private String notas;
    
    // Constructores
    public MovimientoEntradaDTO() {}
    
    public MovimientoEntradaDTO(String referencia, String proveedor, String producto, 
                               Integer cantidad, String ubicacion, String recibidoPor,
                               LocalDate dia, LocalTime hora, String notas) {
        this.referencia = referencia;
        this.proveedor = proveedor;
        this.producto = producto;
        this.cantidad = cantidad;
        this.ubicacion = ubicacion;
        this.recibidoPor = recibidoPor;
        this.dia = dia;
        this.hora = hora;
        this.notas = notas;
    }
    
    // Getters y Setters
    public String getReferencia() {
        return referencia;
    }
    
    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
    
    public String getProveedor() {
        return proveedor;
    }
    
    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }
    
    public String getProducto() {
        return producto;
    }
    
    public void setProducto(String producto) {
        this.producto = producto;
    }
    
    public Integer getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
    
    public String getUbicacion() {
        return ubicacion;
    }
    
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
    
    public String getRecibidoPor() {
        return recibidoPor;
    }
    
    public void setRecibidoPor(String recibidoPor) {
        this.recibidoPor = recibidoPor;
    }
    
    public LocalDate getDia() {
        return dia;
    }
    
    public void setDia(LocalDate dia) {
        this.dia = dia;
    }
    
    public LocalTime getHora() {
        return hora;
    }
    
    public void setHora(LocalTime hora) {
        this.hora = hora;
    }
    
    public String getNotas() {
        return notas;
    }
    
    public void setNotas(String notas) {
        this.notas = notas;
    }
    
    @Override
    public String toString() {
        return "MovimientoEntradaDTO{" +
                "referencia='" + referencia + '\'' +
                ", proveedor='" + proveedor + '\'' +
                ", producto='" + producto + '\'' +
                ", cantidad=" + cantidad +
                ", ubicacion='" + ubicacion + '\'' +
                ", recibidoPor='" + recibidoPor + '\'' +
                ", dia=" + dia +
                ", hora=" + hora +
                ", notas='" + notas + '\'' +
                '}';
    }
}