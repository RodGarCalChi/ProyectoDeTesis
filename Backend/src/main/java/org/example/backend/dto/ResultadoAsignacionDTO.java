package org.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoAsignacionDTO {
    private ClienteDTO cliente;
    private List<ProductoDTO> productos;
    private Integer totalAsignaciones;
    private String mensaje;
}
