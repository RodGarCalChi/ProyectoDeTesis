package org.example.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RegistroTemperaturaDTO {
    private Long id;
    private Double temperatura;
    private LocalDateTime fechaRegistro;
    private String observaciones;
    private Long sensorId;
    private Long equipoFrioId;
}
