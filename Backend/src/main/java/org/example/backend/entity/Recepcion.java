package org.example.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "recepcion")
public class Recepcion extends Usuario {
    private String areaAsignada;

    public Recepcion() {}

    public Recepcion(String nombre, String apellido, String email, String telefono, String rol, String areaAsignada) {
        super(nombre, apellido, email, telefono, rol);
        this.areaAsignada = areaAsignada;
    }

    public String getAreaAsignada() {
        return areaAsignada;
    }

    public void setAreaAsignada(String areaAsignada) {
        this.areaAsignada = areaAsignada;
    }
}

