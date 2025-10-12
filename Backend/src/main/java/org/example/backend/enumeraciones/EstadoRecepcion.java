package org.example.backend.enumeraciones;

public enum EstadoRecepcion {
    PENDIENTE,           // Recepción registrada, pendiente de verificación
    EN_VERIFICACION,     // En proceso de verificación física y documental
    EN_CUARENTENA,       // Productos en cuarentena para control de calidad
    APROBADO,           // Aprobado por control de calidad
    RECHAZADO,          // Rechazado por control de calidad
    ALMACENADO,         // Productos ubicados en almacén
    DEVUELTO            // Productos devueltos al proveedor
}