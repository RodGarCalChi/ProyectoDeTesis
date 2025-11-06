-- ============================================
-- SCRIPT: JERARQUÍA COMPLETA DEL SISTEMA
-- ============================================
-- Operador Logístico → Almacén (por Cliente) → Zona → Lote → Palet → Caja → Productos
-- ============================================

-- 1. OPERADORES LOGÍSTICOS
CREATE TABLE IF NOT EXISTS operadores_logisticos (
    id BINARY(16) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ruc VARCHAR(20) NOT NULL UNIQUE,
    direccion VARCHAR(200),
    telefono VARCHAR(20),
    email VARCHAR(100),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_ruc (ruc),
    INDEX idx_activo (activo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. ACTUALIZAR TABLA ALMACENES (agregar relaciones)
ALTER TABLE almacenes 
ADD COLUMN IF NOT EXISTS operador_logistico_id BINARY(16) AFTER direccion,
ADD COLUMN IF NOT EXISTS cliente_id BINARY(16) AFTER operador_logistico_id,
ADD CONSTRAINT fk_almacen_operador FOREIGN KEY (operador_logistico_id) REFERENCES operadores_logisticos(id),
ADD CONSTRAINT fk_almacen_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id),
ADD INDEX idx_almacen_operador (operador_logistico_id),
ADD INDEX idx_almacen_cliente (cliente_id);

-- 3. PALETS
CREATE TABLE IF NOT EXISTS palets (
    id BINARY(16) PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    lote_id BINARY(16) NOT NULL,
    ubicacion_id BINARY(16),
    capacidad_maxima INT COMMENT 'Número máximo de cajas',
    cajas_actuales INT DEFAULT 0,
    peso_maximo_kg FLOAT,
    peso_actual_kg FLOAT DEFAULT 0.0,
    disponible BOOLEAN NOT NULL DEFAULT TRUE,
    observaciones VARCHAR(500),
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_palet_lote FOREIGN KEY (lote_id) REFERENCES lotes(id),
    CONSTRAINT fk_palet_ubicacion FOREIGN KEY (ubicacion_id) REFERENCES ubicaciones(id),
    INDEX idx_palet_codigo (codigo),
    INDEX idx_palet_lote (lote_id),
    INDEX idx_palet_ubicacion (ubicacion_id),
    INDEX idx_palet_disponible (disponible)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. CAJAS
CREATE TABLE IF NOT EXISTS cajas (
    id BINARY(16) PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    palet_id BINARY(16) NOT NULL,
    cliente_id BINARY(16) NOT NULL,
    producto_id BINARY(16) NOT NULL,
    cantidad INT NOT NULL COMMENT 'Cantidad de productos en la caja (ej: 10 vacunas)',
    lote_producto VARCHAR(50) NOT NULL,
    fecha_vencimiento DATE,
    tamano ENUM('PEQUENA', 'MEDIANA', 'GRANDE', 'EXTRA_GRANDE') NOT NULL,
    peso_kg FLOAT,
    largo_cm FLOAT,
    ancho_cm FLOAT,
    alto_cm FLOAT,
    temperatura_requerida_min FLOAT,
    temperatura_requerida_max FLOAT,
    requiere_cadena_frio BOOLEAN NOT NULL DEFAULT FALSE,
    observaciones VARCHAR(500),
    sellada BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_sellado TIMESTAMP,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_caja_palet FOREIGN KEY (palet_id) REFERENCES palets(id),
    CONSTRAINT fk_caja_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    CONSTRAINT fk_caja_producto FOREIGN KEY (producto_id) REFERENCES productos(id),
    INDEX idx_caja_codigo (codigo),
    INDEX idx_caja_palet (palet_id),
    INDEX idx_caja_cliente (cliente_id),
    INDEX idx_caja_producto (producto_id),
    INDEX idx_caja_lote (lote_producto),
    INDEX idx_caja_vencimiento (fecha_vencimiento),
    INDEX idx_caja_cadena_frio (requiere_cadena_frio)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- DATOS DE EJEMPLO
-- ============================================

-- Operador Logístico de ejemplo
INSERT INTO operadores_logisticos (id, nombre, ruc, direccion, telefono, email, activo)
VALUES 
(UNHEX(REPLACE('op000000-0000-0000-0000-000000000001', '-', '')),
 'Operador Logístico Principal',
 '20987654321',
 'Av. Logística 123, Lima',
 '01-5551234',
 'contacto@operadorlogistico.com',
 TRUE);

-- ============================================
-- CONSULTAS ÚTILES
-- ============================================

-- Ver jerarquía completa
SELECT 
    ol.nombre AS operador,
    a.nombre AS almacen,
    c.razon_social AS cliente,
    z.nombre AS zona,
    l.numero AS lote,
    p.codigo AS palet,
    cj.codigo AS caja,
    pr.nombre AS producto,
    cj.cantidad AS cantidad_productos
FROM operadores_logisticos ol
JOIN almacenes a ON a.operador_logistico_id = ol.id
JOIN clientes c ON a.cliente_id = c.id
JOIN zonas z ON z.almacen_id = a.id
LEFT JOIN lotes l ON l.producto_id IS NOT NULL
LEFT JOIN palets p ON p.lote_id = l.id
LEFT JOIN cajas cj ON cj.palet_id = p.id
LEFT JOIN productos pr ON cj.producto_id = pr.id
ORDER BY ol.nombre, a.nombre, z.nombre, p.codigo, cj.codigo;

-- Ver cajas por cliente
SELECT 
    c.razon_social AS cliente,
    cj.codigo AS caja,
    p.nombre AS producto,
    cj.cantidad,
    cj.lote_producto,
    cj.fecha_vencimiento,
    cj.tamano,
    pal.codigo AS palet,
    u.codigo AS ubicacion
FROM cajas cj
JOIN clientes c ON cj.cliente_id = c.id
JOIN productos p ON cj.producto_id = p.id
JOIN palets pal ON cj.palet_id = pal.id
LEFT JOIN ubicaciones u ON pal.ubicacion_id = u.id
ORDER BY c.razon_social, cj.codigo;

-- Ver ocupación de palets
SELECT 
    p.codigo AS palet,
    p.capacidad_maxima,
    p.cajas_actuales,
    ROUND((p.cajas_actuales / p.capacidad_maxima * 100), 2) AS porcentaje_ocupacion,
    p.peso_maximo_kg,
    p.peso_actual_kg,
    p.disponible
FROM palets p
ORDER BY porcentaje_ocupacion DESC;

-- Ver productos próximos a vencer por caja
SELECT 
    cj.codigo AS caja,
    c.razon_social AS cliente,
    p.nombre AS producto,
    cj.cantidad,
    cj.fecha_vencimiento,
    DATEDIFF(cj.fecha_vencimiento, CURDATE()) AS dias_restantes
FROM cajas cj
JOIN clientes c ON cj.cliente_id = c.id
JOIN productos p ON cj.producto_id = p.id
WHERE cj.fecha_vencimiento <= DATE_ADD(CURDATE(), INTERVAL 30 DAY)
ORDER BY cj.fecha_vencimiento ASC;

-- ============================================
-- NOTAS IMPORTANTES
-- ============================================
-- 1. Ejecutar DESPUÉS de tener las tablas base creadas
-- 2. La jerarquía es: Operador → Almacén → Zona → Lote → Palet → Caja → Productos
-- 3. Cada almacén pertenece a UN cliente específico
-- 4. Cada caja contiene productos de UN cliente específico
-- 5. Los palets agrupan cajas y se ubican en zonas específicas
-- ============================================
