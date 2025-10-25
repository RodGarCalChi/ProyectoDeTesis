package org.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteConProductosDTO {
    private ClienteCreateDTO cliente;
    private List<String> productosExistentesIds; // IDs de productos que ya existen
    private List<ProductoCreateDTO> productosNuevos; // Productos a crear
    private String observaciones;
}
