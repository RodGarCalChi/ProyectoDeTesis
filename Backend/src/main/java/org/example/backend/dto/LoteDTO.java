package org.example.backend.dto;

import lombok.Data;
import org.example.backend.enumeraciones.EstadoLote;
import java.time.LocalDate;

@Data
public class LoteDTO {
    private Long id;
    private String numeroLote;
    private LocalDate fechaFabricacion;
    private LocalDate fechaVencimiento;
    private Integer cantidad;
    private EstadoLote estado;
    private Long productoId;
    private Long ubicacionId;
}
