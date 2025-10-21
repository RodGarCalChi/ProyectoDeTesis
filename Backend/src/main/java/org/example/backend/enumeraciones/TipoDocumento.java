package org.example.backend.enumeraciones;

public enum TipoDocumento {
    ACTA_RECEPCION("Acta de Recepción"),
    GUIA_REMISION_CLIENTE("Guía de Remisión del Cliente"),
    MOVIMIENTO_MERCADERIA("Movimiento de Mercadería"),
    GUIA_REMISION_OPERADOR("Guía de Remisión del Operador Logístico");
    
    private final String descripcion;
    
    TipoDocumento(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}