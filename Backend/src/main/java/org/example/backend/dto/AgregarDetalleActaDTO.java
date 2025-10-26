package org.example.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO para agregar un detalle (producto) a un Acta de Recepción existente
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgregarDetalleActaDTO {
    
    @NotNull(message = "El producto es obligatorio")
    private UUID productoId;
    
    @NotNull(message = "El lote es obligatorio")
    @Size(max = 50, message = "El lote no puede exceder 50 caracteres")
    private String lote;
    
    @NotNull(message = "La fecha de vencimiento es obligatoria")
    private LocalDate fechaVencimiento;
    
    @NotNull(message = "La cantidad declarada es obligatoria")
    @Positive(message = "La cantidad declarada debe ser positiva")
    private Integer cantidadDeclarada;
    
    @NotNull(message = "La cantidad recibida es obligatoria")
    @Positive(message = "La cantidad recibida debe ser positiva")
    private Integer cantidadRecibida;
    
    @NotNull(message = "El precio unitario es obligatorio")
    @Positive(message = "El precio unitario debe ser positivo")
    private BigDecimal precioUnitario;
    
    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    private String observaciones;
    
    @NotNull(message = "El campo conforme es obligatorio")
    private Boolean conforme;
}
