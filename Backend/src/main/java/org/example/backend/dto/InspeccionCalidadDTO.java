package org.example.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class InspeccionCalidadDTO {
    private Long id;
    private LocalDateTime fechaInspeccion;
    private String resultado;
    private String observaciones;
    private String inspector;
    private Long loteId;
}
