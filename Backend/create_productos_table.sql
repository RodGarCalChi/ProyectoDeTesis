-- Script para crear la tabla productos manualmente
-- Ejecutar este script en tu base de datos MySQL

USE BaseDeDatos;

-- Verificar si la tabla existe
SHOW TABLES LIKE 'productos';

-- Crear la tabla productos si no existe
CREATE TABLE IF NOT EXISTS productos (
    id BINARY(16) PRIMARY KEY,
    codigo_sku VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    condicion_almacen VARCHAR(20) NOT NULL,
    requiere_cadena_frio BOOLEAN DEFAULT FALSE,
    registro_sanitario VARCHAR(30),
    unidad_medida VARCHAR(20),
    vida_util_meses INT,
    temp_min FLOAT,
    temp_max FLOAT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Verificar que la tabla se creó correctamente
DESCRIBE productos;

-- Insertar un producto de prueba
INSERT INTO productos (
    id, 
    codigo_sku, 
    nombre, 
    tipo, 
    condicion_almacen, 
    requiere_cadena_frio, 
    unidad_medida
) VALUES (
    UNHEX(REPLACE(UUID(), '-', '')),
    'TEST_MANUAL',
    'Producto de Prueba Manual',
    'Medicamento',
    'Ambiente_15_25',
    FALSE,
    'Unidades'
);

-- Verificar que el producto se insertó
SELECT * FROM productos;