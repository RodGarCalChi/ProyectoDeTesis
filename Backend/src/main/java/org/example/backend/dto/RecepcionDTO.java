package org.example.backend.dto;

import lombok.Data;
import org.example.backend.enumeraciones.Rol;

@Data
public class RecepcionDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String username;
    private Rol rol;
}
