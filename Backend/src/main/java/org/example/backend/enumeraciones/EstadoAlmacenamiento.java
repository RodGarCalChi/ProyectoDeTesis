package org.example.backend.enumeraciones;

public enum EstadoAlmacenamiento {
    PENDIENTE("Pendiente"),
    EN_PROCESO("En Proceso"),
    ALMACENADO("Almacenado"),
    PENDIENTE_VERIFICACION("Pendiente de Verificación"),
    VERIFICADO("Verificado"),
    RECHAZADO("Rechazado"),
    REUBICADO("Reubicado");

    private final String descripcion;

    EstadoAlmacenamiento(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}