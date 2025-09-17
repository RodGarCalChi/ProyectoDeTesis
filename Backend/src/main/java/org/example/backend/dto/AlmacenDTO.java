package org.example.backend.dto;

import lombok.Data;

@Data
public class AlmacenDTO {
    private Long id;
    private String nombre;
    private String ubicacion;
    private Double capacidad;
    private String descripcion;
}
