package org.example.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MovimientoStockDTO {
    private Long id;
    private String tipoMovimiento;
    private Integer cantidad;
    private LocalDateTime fecha;
    private String descripcion;
    private Long loteId;
    private Long ubicacionOrigenId;
    private Long ubicacionDestinoId;
}
