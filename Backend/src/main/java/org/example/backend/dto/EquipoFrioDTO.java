package org.example.backend.dto;

import lombok.Data;

@Data
public class EquipoFrioDTO {
    private Long id;
    private String modelo;
    private String numeroSerie;
    private Double temperaturaMinima;
    private Double temperaturaMaxima;
    private String ubicacion;
    private String estado;
}
