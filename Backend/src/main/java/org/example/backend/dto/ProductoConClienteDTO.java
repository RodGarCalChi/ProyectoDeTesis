package org.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoConClienteDTO {
    private String clienteId; // ID si el cliente ya existe
    private ClienteCreateDTO clienteNuevo; // Datos si el cliente es nuevo
    private String productoId; // ID si el producto ya existe
    private ProductoCreateDTO productoNuevo; // Datos si el producto es nuevo
    private String observaciones;
}
