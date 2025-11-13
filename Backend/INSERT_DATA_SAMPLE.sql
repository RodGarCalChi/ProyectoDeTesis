-- ============================================
-- Script para insertar datos de prueba
-- Tablas: Almacenes, Operadores Logísticos, Zonas, Lotes y Palets
-- ============================================

-- ============================================
-- 0. PREREQUISITOS
-- ============================================
-- Asegúrate de tener al menos:
-- - 1 Cliente creado
-- - Productos creados (Paracetamol, Ibuprofeno, Amoxicilina, Insulina, Omeprazol)

-- ============================================
-- 1. OPERADORES LOGÍSTICOS
-- ============================================

INSERT INTO operadores_logisticos (id, nombre, ruc, direccion, telefono, email, activo, fecha_creacion, fecha_actualizacion)
VALUES 
(UUID(), 'DHL Express Perú', '20123456789', 'Av. Javier Prado Este 4200, Santiago de Surco, Lima', '01-6157000', 'contacto@dhl.com.pe', TRUE, NOW(), NOW()),
(UUID(), 'FedEx Perú', '20234567890', 'Av. República de Panamá 3591, San Isidro, Lima', '01-2117000', 'info@fedex.com.pe', TRUE, NOW(), NOW()),
(UUID(), 'Olva Courier', '20345678901', 'Av. Angamos Este 2681, Surquillo, Lima', '01-7089000', 'atencion@olvacourier.pe', TRUE, NOW(), NOW()),
(UUID(), 'Shalom Empresarial', '20456789012', 'Av. Universitaria 1801, San Miguel, Lima', '01-5617000', 'contacto@shalom.com.pe', TRUE, NOW(), NOW()),
(UUID(), 'Transportes Cruz del Sur Cargo', '20567890123', 'Av. Javier Prado Este 1109, La Victoria, Lima', '01-3116000', 'cargo@cruzdelsur.com.pe', TRUE, NOW(), NOW());

-- ============================================
-- 2. ALMACENES
-- ============================================

SET @operador_logistico_id = (SELECT id FROM operadores_logisticos LIMIT 1);
SET @cliente_id = (SELECT id FROM clientes LIMIT 1);

INSERT INTO almacenes (id, nombre, direccion, operador_logistico_id, cliente_id, tiene_area_controlados, fecha_creacion, fecha_actualizacion)
VALUES 
(UUID(), 'Almacén Principal Lima', 'Av. Industrial 1234, Ate, Lima', @operador_logistico_id, @cliente_id, TRUE, NOW(), NOW()),
(UUID(), 'Almacén Cadena de Frío', 'Av. Los Frutales 567, Santa Anita, Lima', @operador_logistico_id, @cliente_id, TRUE, NOW(), NOW()),
(UUID(), 'Centro de Distribución Norte', 'Panamericana Norte Km 15, Puente Piedra, Lima', @operador_logistico_id, @cliente_id, FALSE, NOW(), NOW());

-- ============================================
-- 3. ZONAS (Según TipoZona enum)
-- ============================================
-- TipoZona: 
--   CONGELADO   = -20°C o menos
--   REFRIGERADO = 2°C a 8°C
--   SECO        = 15°C a 25°C (ambiente controlado)
--   ULT         = -70°C o menos (Ultra Low Temperature)

SET @almacen_id = (SELECT id FROM almacenes WHERE nombre = 'Almacén Principal Lima' LIMIT 1);
SET @almacen_frio_id = (SELECT id FROM almacenes WHERE nombre = 'Almacén Cadena de Frío' LIMIT 1);

INSERT INTO zonas (id, nombre, tipo, almacen_id, fecha_creacion, fecha_actualizacion)
VALUES 
-- Zonas en Almacén Principal
(UUID(), 'Cámara de Congelación', 'CONGELADO', @almacen_id, NOW(), NOW()),
(UUID(), 'Cámara Refrigerada', 'REFRIGERADO', @almacen_id, NOW(), NOW()),
(UUID(), 'Almacenamiento Ambiente Controlado', 'SECO', @almacen_id, NOW(), NOW()),
(UUID(), 'Ultra Low Temperature', 'ULT', @almacen_id, NOW(), NOW()),
(UUID(), 'Almacén General Medicamentos', 'SECO', @almacen_id, NOW(), NOW()),

-- Zonas en Almacén Cadena de Frío
(UUID(), 'Cámara Refrigerada Principal', 'REFRIGERADO', @almacen_frio_id, NOW(), NOW()),
(UUID(), 'Cámara de Congelación Principal', 'CONGELADO', @almacen_frio_id, NOW(), NOW()),
(UUID(), 'Cámara ULT Especializada', 'ULT', @almacen_frio_id, NOW(), NOW());

-- ============================================
-- 4. LOTES (Requiere tener productos creados)
-- ============================================

-- Obtener IDs de productos
SET @producto_paracetamol = (SELECT id FROM productos WHERE nombre LIKE '%Paracetamol%' LIMIT 1);
SET @producto_ibuprofeno = (SELECT id FROM productos WHERE nombre LIKE '%Ibuprofeno%' LIMIT 1);
SET @producto_amoxicilina = (SELECT id FROM productos WHERE nombre LIKE '%Amoxicilina%' LIMIT 1);
SET @producto_insulina = (SELECT id FROM productos WHERE nombre LIKE '%Insulina%' LIMIT 1);
SET @producto_omeprazol = (SELECT id FROM productos WHERE nombre LIKE '%Omeprazol%' LIMIT 1);

-- Insertar lotes
INSERT INTO lotes (id, numero, producto_id, fecha_fabricacion, fecha_vencimiento, cantidad_inicial, cantidad_disponible, estado, proveedor, observaciones, fecha_creacion, fecha_actualizacion)
VALUES 
(UUID(), 'LOTE-PAR-2024-001', @producto_paracetamol, '2024-01-15', '2026-01-15', 5000, 5000, 'DISPONIBLE', 'Laboratorios Bagó', 'Lote en condiciones óptimas - Almacenar en SECO', NOW(), NOW()),
(UUID(), 'LOTE-IBU-2024-001', @producto_ibuprofeno, '2024-02-10', '2026-02-10', 3000, 3000, 'DISPONIBLE', 'Laboratorios Roemmers', 'Almacenar en lugar fresco y seco - SECO', NOW(), NOW()),
(UUID(), 'LOTE-AMO-2024-001', @producto_amoxicilina, '2024-03-05', '2026-03-05', 4000, 4000, 'DISPONIBLE', 'Laboratorios Farmindustria', 'Antibiótico - Requiere receta médica - SECO', NOW(), NOW()),
(UUID(), 'LOTE-INS-2024-001', @producto_insulina, '2024-04-01', '2025-04-01', 1000, 1000, 'DISPONIBLE', 'Novo Nordisk', 'REFRIGERADO - Mantener entre 2°C y 8°C', NOW(), NOW()),
(UUID(), 'LOTE-OME-2024-001', @producto_omeprazol, '2024-05-20', '2026-05-20', 6000, 6000, 'DISPONIBLE', 'Laboratorios Tecnoquímicas', 'Proteger de la luz y humedad - SECO', NOW(), NOW()),
(UUID(), 'LOTE-PAR-2024-002', @producto_paracetamol, '2024-06-10', '2026-06-10', 4500, 4500, 'DISPONIBLE', 'Laboratorios Bagó', 'Segundo lote de Paracetamol - SECO', NOW(), NOW()),
(UUID(), 'LOTE-IBU-2024-002', @producto_ibuprofeno, '2024-07-15', '2026-07-15', 3500, 3500, 'DISPONIBLE', 'Laboratorios Roemmers', 'Segundo lote de Ibuprofeno - SECO', NOW(), NOW());

-- ============================================
-- 5. PALETS (Requiere tener lotes y ubicaciones creadas)
-- ============================================

-- Obtener IDs de lotes
SET @lote_paracetamol = (SELECT id FROM lotes WHERE numero = 'LOTE-PAR-2024-001' LIMIT 1);
SET @lote_ibuprofeno = (SELECT id FROM lotes WHERE numero = 'LOTE-IBU-2024-001' LIMIT 1);
SET @lote_amoxicilina = (SELECT id FROM lotes WHERE numero = 'LOTE-AMO-2024-001' LIMIT 1);
SET @lote_insulina = (SELECT id FROM lotes WHERE numero = 'LOTE-INS-2024-001' LIMIT 1);
SET @lote_omeprazol = (SELECT id FROM lotes WHERE numero = 'LOTE-OME-2024-001' LIMIT 1);

-- Obtener IDs de ubicaciones según tipo de zona
SET @ubicacion_seco = (SELECT u.id FROM ubicaciones u 
                       INNER JOIN zonas z ON u.zona_id = z.id 
                       WHERE z.tipo = 'SECO' LIMIT 1);
                       
SET @ubicacion_refrigerado = (SELECT u.id FROM ubicaciones u 
                              INNER JOIN zonas z ON u.zona_id = z.id 
                              WHERE z.tipo = 'REFRIGERADO' LIMIT 1);

-- Insertar palets
INSERT INTO palets (id, codigo, lote_id, ubicacion_id, capacidad_maxima, cajas_actuales, peso_maximo_kg, peso_actual_kg, disponible, observaciones, fecha_creacion, fecha_actualizacion)
VALUES 
-- Palets en zona SECO
(UUID(), 'PLT-PAR-001', @lote_paracetamol, @ubicacion_seco, 50, 0, 1000.0, 0.0, TRUE, 'Palet para Paracetamol - Zona SECO', NOW(), NOW()),
(UUID(), 'PLT-IBU-001', @lote_ibuprofeno, @ubicacion_seco, 40, 0, 800.0, 0.0, TRUE, 'Palet para Ibuprofeno - Zona SECO', NOW(), NOW()),
(UUID(), 'PLT-AMO-001', @lote_amoxicilina, @ubicacion_seco, 45, 0, 900.0, 0.0, TRUE, 'Palet para Amoxicilina - Zona SECO', NOW(), NOW()),
(UUID(), 'PLT-OME-001', @lote_omeprazol, @ubicacion_seco, 60, 0, 1200.0, 0.0, TRUE, 'Palet para Omeprazol - Zona SECO', NOW(), NOW()),

-- Palet en zona REFRIGERADO
(UUID(), 'PLT-INS-REF-001', @lote_insulina, @ubicacion_refrigerado, 30, 0, 500.0, 0.0, TRUE, 'Palet para Insulina - Zona REFRIGERADO 2-8°C', NOW(), NOW()),

-- Palets parcialmente ocupados
(UUID(), 'PLT-PAR-002', @lote_paracetamol, @ubicacion_seco, 50, 25, 1000.0, 500.0, TRUE, 'Palet parcialmente ocupado - Zona SECO', NOW(), NOW()),
(UUID(), 'PLT-IBU-002', @lote_ibuprofeno, @ubicacion_seco, 40, 15, 800.0, 300.0, TRUE, 'Palet parcialmente ocupado - Zona SECO', NOW(), NOW()),
(UUID(), 'PLT-AMO-002', @lote_amoxicilina, @ubicacion_seco, 45, 30, 900.0, 600.0, TRUE, 'Palet parcialmente ocupado - Zona SECO', NOW(), NOW());

-- ============================================
-- VERIFICACIÓN DE DATOS INSERTADOS
-- ============================================

-- Contar registros insertados
SELECT 'Operadores Logísticos' AS Tabla, COUNT(*) AS Total FROM operadores_logisticos
UNION ALL
SELECT 'Almacenes', COUNT(*) FROM almacenes
UNION ALL
SELECT 'Zonas', COUNT(*) FROM zonas
UNION ALL
SELECT 'Lotes', COUNT(*) FROM lotes
UNION ALL
SELECT 'Palets', COUNT(*) FROM palets;

-- Ver datos insertados
SELECT '=== OPERADORES LOGÍSTICOS ===' AS Info;
SELECT nombre, ruc, telefono, email FROM operadores_logisticos ORDER BY nombre;

SELECT '=== ALMACENES ===' AS Info;
SELECT nombre, direccion, tiene_area_controlados FROM almacenes ORDER BY nombre;

SELECT '=== ZONAS POR TIPO ===' AS Info;
SELECT z.nombre, z.tipo, a.nombre as almacen 
FROM zonas z 
INNER JOIN almacenes a ON z.almacen_id = a.id 
ORDER BY z.tipo, z.nombre;

SELECT '=== LOTES ===' AS Info;
SELECT l.numero, p.nombre as producto, l.fecha_vencimiento, l.cantidad_disponible, l.estado, l.proveedor 
FROM lotes l
INNER JOIN productos p ON l.producto_id = p.id
ORDER BY l.numero;

SELECT '=== PALETS ===' AS Info;
SELECT p.codigo, l.numero as lote, p.capacidad_maxima, p.cajas_actuales, p.disponible 
FROM palets p
INNER JOIN lotes l ON p.lote_id = l.id
ORDER BY p.codigo;

-- Verificar zonas por tipo
SELECT '=== RESUMEN ZONAS POR TIPO ===' AS Info;
SELECT tipo, COUNT(*) as cantidad FROM zonas GROUP BY tipo ORDER BY tipo;
