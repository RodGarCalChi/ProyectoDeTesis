package org.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalTime;

public class MovimientoSalidaDTO {
    
    @NotBlank(message = "El número de referencia es obligatorio")
    private String referencia;
    
    @NotBlank(message = "El destino es obligatorio")
    private String destino;
    
    @NotBlank(message = "El producto es obligatorio")
    private String producto;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
    
    @NotBlank(message = "La ubicación es obligatoria")
    private String ubicacion;
    
    @NotBlank(message = "El campo 'Autorizado por' es obligatorio")
    private String autorizadoPor;
    
    private LocalDate dia;
    
    private LocalTime hora;
    
    private String notas;
    
    // Constructores
    public MovimientoSalidaDTO() {}
    
    public MovimientoSalidaDTO(String referencia, String destino, String producto, 
                              Integer cantidad, String ubicacion, String autorizadoPor,
                              LocalDate dia, LocalTime hora, String notas) {
        this.referencia = referencia;
        this.destino = destino;
        this.producto = producto;
        this.cantidad = cantidad;
        this.ubicacion = ubicacion;
        this.autorizadoPor = autorizadoPor;
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
    
    public String getDestino() {
        return destino;
    }
    
    public void setDestino(String destino) {
        this.destino = destino;
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
    
    public String getAutorizadoPor() {
        return autorizadoPor;
    }
    
    public void setAutorizadoPor(String autorizadoPor) {
        this.autorizadoPor = autorizadoPor;
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
        return "MovimientoSalidaDTO{" +
                "referencia='" + referencia + '\'' +
                ", destino='" + destino + '\'' +
                ", producto='" + producto + '\'' +
                ", cantidad=" + cantidad +
                ", ubicacion='" + ubicacion + '\'' +
                ", autorizadoPor='" + autorizadoPor + '\'' +
                ", dia=" + dia +
                ", hora=" + hora +
                ", notas='" + notas + '\'' +
                '}';
    }
}