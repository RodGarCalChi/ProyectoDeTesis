package org.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.backend.enumeraciones.EstadoDocumento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para crear Acta de Recepción (Paso 1 - Sin productos)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActaRecepcionCreateDTO {
    
    @NotBlank(message = "El número de acta es obligatorio")
    @Size(max = 50, message = "El número de acta no puede exceder 50 caracteres")
    private String numeroActa;
    
    @NotNull(message = "La fecha de recepción es obligatoria")
    private LocalDateTime fechaRecepcion;
    
    @NotNull(message = "El cliente es obligatorio")
    private UUID clienteId;
    
    @NotBlank(message = "El responsable de recepción es obligatorio")
    @Size(max = 100, message = "El responsable no puede exceder 100 caracteres")
    private String responsableRecepcion;
    
    @Size(max = 200, message = "El lugar de recepción no puede exceder 200 caracteres")
    private String lugarRecepcion;
    
    private BigDecimal temperaturaLlegada;
    
    @Size(max = 500, message = "Las condiciones de transporte no pueden exceder 500 caracteres")
    private String condicionesTransporte;
    
    @Size(max = 1000, message = "Las observaciones no pueden exceder 1000 caracteres")
    private String observaciones;
    
    private EstadoDocumento estado; // Por defecto será BORRADOR
    
    @NotBlank(message = "El creador es obligatorio")
    @Size(max = 100, message = "El creador no puede exceder 100 caracteres")
    private String creadoPor;
}
