package org.example.backend.enumeraciones;

public enum EstadoDocumento {
    BORRADOR("Borrador"),
    PENDIENTE("Pendiente"),
    PROCESADO("Procesado"),
    APROBADO("Aprobado"),
    RECHAZADO("Rechazado"),
    ANULADO("Anulado");
    
    private final String descripcion;
    
    EstadoDocumento(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}