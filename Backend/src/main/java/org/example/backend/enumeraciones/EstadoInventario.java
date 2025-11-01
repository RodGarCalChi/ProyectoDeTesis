package org.example.backend.enumeraciones;

public enum EstadoInventario {
    PENDIENTE_UBICACION,  // Registrado por administrativa, sin ubicación
    ALMACENADO,           // Con ubicación asignada por operaciones
    RESERVADO,            // Apartado para un pedido
    DESPACHADO,           // Ya salió del almacén
    VENCIDO               // Producto vencido
}
