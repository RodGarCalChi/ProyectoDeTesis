package org.example.backend.dto;

import org.example.backend.entity.Cliente;
import org.example.backend.entity.Producto;

import java.util.List;

public class ClienteConProductosResponseDTO {
    private Cliente cliente;
    private List<Producto> productos;
    private int totalAsignaciones;
    private String mensaje;

    // Constructores
    public ClienteConProductosResponseDTO() {
    }

    public ClienteConProductosResponseDTO(Cliente cliente, List<Producto> productos, 
                                          int totalAsignaciones, String mensaje) {
        this.cliente = cliente;
        this.productos = productos;
        this.totalAsignaciones = totalAsignaciones;
        this.mensaje = mensaje;
    }

    // Getters y Setters
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public int getTotalAsignaciones() {
        return totalAsignaciones;
    }

    public void setTotalAsignaciones(int totalAsignaciones) {
        this.totalAsignaciones = totalAsignaciones;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
