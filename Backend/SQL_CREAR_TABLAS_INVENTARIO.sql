-- ============================================
-- SCRIPT: Crear Tablas de Inventario Cliente
-- ============================================
-- IMPORTANTE: Hacer BACKUP antes de ejecutar
-- ============================================

USE BaseDeDatos;

-- ============================================
-- TABLA: inventario_cliente
-- ============================================

CREATE TABLE IF NOT EXISTS inventario_cliente (
    id BINARY(16) PRIMARY KEY,
    cliente_id BINARY(16) NOT NULL,
    producto_id BINARY(16) NOT NULL,
    lote_id BINARY(16) NOT NULL,
    recepcion_id BINARY(16) NOT NULL,
    detalle_recepcion_id BINARY(16),
    cantidad_disponible INT NOT NULL DEFAULT 0,
    cantidad_reservada INT NOT NULL DEFAULT 0,
    cantidad_despachada INT NOT NULL DEFAULT 0,
    ubicacion_id BINARY(16),
    codigo_barras VARCHAR(100),
    fecha_ingreso DATETIME NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    estado ENUM('PENDIENTE_UBICACION', 'ALMACENADO', 'RESERVADO', 'DESPACHADO', 'VENCIDO') NOT NULL DEFAULT 'PENDIENTE_UBICACION',
    temperatura_almacenamiento DECIMAL(5,2),
    observaciones VARCHAR(500),
    usuario_registro_id BINARY(16),
    usuario_ubicacion_id BINARY(16),
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    FOREIGN KEY (producto_id) REFERENCES productos(id),
    FOREIGN KEY (lote_id) REFERENCES lotes(id),
    FOREIGN KEY (recepcion_id) REFERENCES recepciones_mercaderia(id),
    FOREIGN KEY (detalle_recepcion_id) REFERENCES detalles_recepcion(id),
    FOREIGN KEY (ubicacion_id) REFERENCES ubicaciones(id),
    FOREIGN KEY (usuario_registro_id) REFERENCES usuarios(id),
    FOREIGN KEY (usuario_ubicacion_id) REFERENCES usuarios(id),
    
    INDEX idx_cliente_producto (cliente_id, producto_id),
    INDEX idx_estado (estado),
    INDEX idx_ubicacion (ubicacion_id),
    INDEX idx_lote (lote_id),
    INDEX idx_vencimiento (fecha_vencimiento),
    INDEX idx_recepcion (recepcion_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: historial_ubicaciones
-- ============================================

CREATE TABLE IF NOT EXISTS historial_ubicaciones (
    id BINARY(16) PRIMARY KEY,
    inventario_cliente_id BINARY(16) NOT NULL,
    ubicacion_anterior_id BINARY(16),
    ubicacion_nueva_id BINARY(16) NOT NULL,
    motivo VARCHAR(200),
    usuario_id BINARY(16) NOT NULL,
    fecha_movimiento DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (inventario_cliente_id) REFERENCES inventario_cliente(id) ON DELETE CASCADE,
    FOREIGN KEY (ubicacion_anterior_id) REFERENCES ubicaciones(id),
    FOREIGN KEY (ubicacion_nueva_id) REFERENCES ubicaciones(id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    
    INDEX idx_inventario (inventario_cliente_id),
    INDEX idx_fecha (fecha_movimiento),
    INDEX idx_usuario (usuario_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- VERIFICACIÓN
-- ============================================

-- Ver estructura de las tablas
DESCRIBE inventario_cliente;
DESCRIBE historial_ubicaciones;

-- Ver todas las tablas
SHOW TABLES;

SELECT 'Tablas creadas exitosamente' AS mensaje;
