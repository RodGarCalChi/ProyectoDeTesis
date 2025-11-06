package org.example.backend.dto;

import org.example.backend.entity.Zona;

import java.time.LocalDateTime;
import java.util.UUID;

public class ZonaDTO {
    private UUID id;
    private String nombre;
    private String tipo;
    private UUID almacenId;
    private String almacenNombre;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    public ZonaDTO() {}
    
    public ZonaDTO(Zona zona) {
        this.id = zona.getId();
        this.nombre = zona.getNombre();
        this.tipo = zona.getTipo();
        this.almacenId = zona.getAlmacen() != null ? zona.getAlmacen().getId() : null;
        this.almacenNombre = zona.getAlmacen() != null ? zona.getAlmacen().getNombre() : null;
        this.fechaCreacion = zona.getFechaCreacion();
        this.fechaActualizacion = zona.getFechaActualizacion();
    }
    
    // Getters y Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public UUID getAlmacenId() { return almacenId; }
    public void setAlmacenId(UUID almacenId) { this.almacenId = almacenId; }
    
    public String getAlmacenNombre() { return almacenNombre; }
    public void setAlmacenNombre(String almacenNombre) { this.almacenNombre = almacenNombre; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
