package org.example.backend.dto;

import org.example.backend.entity.Cliente;
import org.example.backend.dto.ProductoCreateDTO;

import java.util.List;

public class ClienteConProductosDTO {
    private Cliente cliente;
    private List<String> productosExistentesIds; // IDs de productos que ya existen
    private List<ProductoCreateDTO> productosNuevos; // Productos nuevos a crear
    private String observaciones;

    // Constructores
    public ClienteConProductosDTO() {
    }

    public ClienteConProductosDTO(Cliente cliente, List<String> productosExistentesIds, 
                                  List<ProductoCreateDTO> productosNuevos, String observaciones) {
        this.cliente = cliente;
        this.productosExistentesIds = productosExistentesIds;
        this.productosNuevos = productosNuevos;
        this.observaciones = observaciones;
    }

    // Getters y Setters
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<String> getProductosExistentesIds() {
        return productosExistentesIds;
    }

    public void setProductosExistentesIds(List<String> productosExistentesIds) {
        this.productosExistentesIds = productosExistentesIds;
    }

    public List<ProductoCreateDTO> getProductosNuevos() {
        return productosNuevos;
    }

    public void setProductosNuevos(List<ProductoCreateDTO> productosNuevos) {
        this.productosNuevos = productosNuevos;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
