package org.example.backend.enumeraciones;

public enum EstadoLote {
    DISPONIBLE("Disponible"),
    AGOTADO("Agotado"),
    VENCIDO("Vencido"),
    BLOQUEADO("Bloqueado"),
    EN_CUARENTENA("En Cuarentena");

    private final String descripcion;

    EstadoLote(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}