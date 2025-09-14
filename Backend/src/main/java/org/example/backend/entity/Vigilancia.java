package org.example.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.example.backend.enumeraciones.Rol;

@Entity
@Table(name="Vigilancia")
public class Vigilancia extends Usuario {
    // Puedes agregar atributos específicos de Vigilancia aquí
    private String turno;

    public Vigilancia() {}

    public Vigilancia(String nombre, String apellido, String email, String telefono, Rol rol, String turno) {
        super(nombre, apellido, email, telefono, rol);
        this.turno = turno;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }
}
