-- ============================================
-- SCRIPT DE DATOS INICIALES - SISTEMA DE ALMACÉN FARMACÉUTICO
-- ============================================
-- Este script inserta datos completos según la estructura del backend:
-- - Almacenes
-- - Zonas
-- - Ubicaciones
-- - Clientes (Hospitales/Farmacias)
-- - Productos (Medicamentos)
-- ============================================

-- ============================================
-- 1. ALMACENES
-- ============================================

INSERT INTO almacenes (id, nombre, direccion, tiene_area_controlados, fecha_creacion, fecha_actualizacion)
VALUES 
(UNHEX(REPLACE('a1000000-0000-0000-0000-000000000001', '-', '')),
 'Almacén Central Lima',
 'Av. Industrial 1234, Callao',
 true,
 NOW(),
 NOW()),
(UNHEX(REPLACE('a1000000-0000-0000-0000-000000000002', '-', '')),
 'Almacén Regional Arequipa',
 'Calle Comercio 567, Arequipa',
 false,
 NOW(),
 NOW());

-- ============================================
-- 2. ZONAS
-- ============================================

-- Zonas del Almacén Central Lima
INSERT INTO zonas (id, nombre, tipo, almacen_id, fecha_creacion, fecha_actualizacion)
VALUES 
(UNHEX(REPLACE('z1000000-0000-0000-0000-000000000001', '-', '')),
 'Zona Ambiente',
 'AMBIENTE',
 UNHEX(REPLACE('a1000000-0000-0000-0000-000000000001', '-', '')),
 NOW(),
 NOW()),
(UNHEX(REPLACE('z1000000-0000-0000-0000-000000000002', '-', '')),
 'Zona Refrigerada',
 'REFRIGERADO',
 UNHEX(REPLACE('a1000000-0000-0000-0000-000000000001', '-', '')),
 NOW(),
 NOW()),
(UNHEX(REPLACE('z1000000-0000-0000-0000-000000000003', '-', '')),
 'Zona Controlados',
 'CONTROLADOS',
 UNHEX(REPLACE('a1000000-0000-0000-0000-000000000001', '-', '')),
 NOW(),
 NOW());

-- ============================================
-- 3. UBICACIONES
-- ============================================

-- Ubicaciones Zona Ambiente
INSERT INTO ubicaciones (id, codigo, capacidad, temp_objetivo_min, temp_objetivo_max, disponible, zona_id, fecha_creacion, fecha_actualizacion)
VALUES 
(UNHEX(REPLACE('u1000000-0000-0000-0000-000000000001', '-', '')), 'A-01-01', 1000, 15.0, 25.0, true, UNHEX(REPLACE('z1000000-0000-0000-0000-000000000001', '-', '')), NOW(), NOW()),
(UNHEX(REPLACE('u1000000-0000-0000-0000-000000000002', '-', '')), 'A-01-02', 1000, 15.0, 25.0, true, UNHEX(REPLACE('z1000000-0000-0000-0000-000000000001', '-', '')), NOW(), NOW()),
(UNHEX(REPLACE('u1000000-0000-0000-0000-000000000003', '-', '')), 'A-02-01', 1000, 15.0, 25.0, true, UNHEX(REPLACE('z1000000-0000-0000-0000-000000000001', '-', '')), NOW(), NOW()),
(UNHEX(REPLACE('u1000000-0000-0000-0000-000000000004', '-', '')), 'A-02-02', 1000, 15.0, 25.0, true, UNHEX(REPLACE('z1000000-0000-0000-0000-000000000001', '-', '')), NOW(), NOW());

-- Ubicaciones Zona Refrigerada
INSERT INTO ubicaciones (id, codigo, capacidad, temp_objetivo_min, temp_objetivo_max, disponible, zona_id, fecha_creacion, fecha_actualizacion)
VALUES 
(UNHEX(REPLACE('u1000000-0000-0000-0000-000000000005', '-', '')), 'R-01-01', 500, 2.0, 8.0, true, UNHEX(REPLACE('z1000000-0000-0000-0000-000000000002', '-', '')), NOW(), NOW()),
(UNHEX(REPLACE('u1000000-0000-0000-0000-000000000006', '-', '')), 'R-01-02', 500, 2.0, 8.0, true, UNHEX(REPLACE('z1000000-0000-0000-0000-000000000002', '-', '')), NOW(), NOW());

-- Ubicaciones Zona Controlados
INSERT INTO ubicaciones (id, codigo, capacidad, temp_objetivo_min, temp_objetivo_max, disponible, zona_id, fecha_creacion, fecha_actualizacion)
VALUES 
(UNHEX(REPLACE('u1000000-0000-0000-0000-000000000007', '-', '')), 'C-01-01', 300, 15.0, 25.0, true, UNHEX(REPLACE('z1000000-0000-0000-0000-000000000003', '-', '')), NOW(), NOW()),
(UNHEX(REPLACE('u1000000-0000-0000-0000-000000000008', '-', '')), 'C-01-02', 300, 15.0, 25.0, true, UNHEX(REPLACE('z1000000-0000-0000-0000-000000000003', '-', '')), NOW(), NOW());

-- ============================================
-- 4. CLIENTES (Hospitales y Farmacias)
-- ============================================

INSERT INTO clientes (id, razon_social, ruc_dni, direccion_entrega, distrito, telefono, email, tipo_cliente, forma_pago, activo, fecha_creacion, fecha_actualizacion)
VALUES 
(UNHEX(REPLACE('c1000000-0000-0000-0000-000000000001', '-', '')),
 'Hospital Nacional Dos de Mayo',
 '20123456789',
 'Av. Grau 13, Parque Historia de la Medicina',
 'Cercado de Lima',
 '01-3281010',
 'logistica@hdm.gob.pe',
 'HOSPITAL_PUBLICO',
 'CREDITO_30_DIAS',
 true,
 NOW(),
 NOW()),
(UNHEX(REPLACE('c1000000-0000-0000-0000-000000000002', '-', '')),
 'Clínica San Felipe',
 '20234567890',
 'Av. Gregorio Escobedo 650',
 'Jesús María',
 '01-2195000',
 'compras@sanfelipe.com.pe',
 'CLINICA_PRIVADA',
 'CREDITO_45_DIAS',
 true,
 NOW(),
 NOW()),
(UNHEX(REPLACE('c1000000-0000-0000-0000-000000000003', '-', '')),
 'Boticas Arcángel S.A.C.',
 '20345678901',
 'Av. Arequipa 2080',
 'Lince',
 '01-4401234',
 'almacen@arcangel.com.pe',
 'CADENA_FARMACIAS',
 'CONTADO',
 true,
 NOW(),
 NOW()),
(UNHEX(REPLACE('c1000000-0000-0000-0000-000000000004', '-', '')),
 'DIGEMID - Ministerio de Salud',
 '20456789012',
 'Av. Parque de las Leyendas 240',
 'San Miguel',
 '01-6314300',
 'almacen@digemid.minsa.gob.pe',
 'ENTIDAD_PUBLICA',
 'CREDITO_60_DIAS',
 true,
 NOW(),
 NOW());

-- ============================================
-- 5. PRODUCTOS - MEDICAMENTOS
-- ============================================

-- MEDICAMENTOS AMBIENTE (15-25°C)
-- ============================================

-- Paracetamol
INSERT INTO productos (id, codigo_sku, nombre, tipo, condicion_almacen, requiere_cadena_frio, 
                      registro_sanitario, unidad_medida, vida_util_meses, temp_min, temp_max, 
                      fecha_creacion, fecha_actualizacion)
VALUES 
(UNHEX(REPLACE('p1000000-0000-0000-0000-000000000001', '-', '')),
 'MED-PARA-500',
 'Paracetamol 500mg Tabletas',
 'Medicamento',
 'Ambiente_15_25',
 false,
 'EE-12345-2023',
 'Caja x 100 tabletas',
 36,
 15.0,
 25.0,
 NOW(),
 NOW());

-- Ibuprofeno
INSERT INTO productos (id, codigo_sku, nombre, tipo, condicion_almacen, requiere_cadena_frio, 
                      registro_sanitario, unidad_medida, vida_util_meses, temp_min, temp_max, 
                      fecha_creacion, fecha_actualizacion)
VALUES 
(UNHEX(REPLACE('p1000000-0000-0000-0000-000000000002', '-', '')),
 'MED-IBUP-400',
 'Ibuprofeno 400mg Tabletas',
 'Medicamento',
 'Ambiente_15_25',
 false,
 'EE-12346-2023',
 'Caja x 100 tabletas',
 36,
 15.0,
 25.0,
 NOW(),
 NOW());

-- Amoxicilina
INSERT INTO productos (id, codigo_sku, nombre, tipo, condicion_almacen, requiere_cadena_frio, 
                      registro_sanitario, unidad_medida, vida_util_meses, temp_min, temp_max, 
                      fecha_creacion, fecha_actualizacion)
VALUES 
(UNHEX(REPLACE('p1000000-0000-0000-0000-000000000003', '-', '')),
 'MED-AMOX-500',
 'Amoxicilina 500mg Cápsulas',
 'Medicamento',
 'Ambiente_15_25',
 false,
 'EE-12347-2023',
 'Caja x 100 cápsulas',
 24,
 15.0,
 25.0,
 NOW(),
 NOW());

-- Omeprazol
INSERT INTO productos (id, codigo_sku, nombre, tipo, condicion_almacen, requiere_cadena_frio, 
                      registro_sanitario, unidad_medida, vida_util_meses, temp_min, temp_max, 
                      fecha_creacion, fecha_actualizacion)
VALUES 
(UNHEX(REPLACE('p1000000-0000-0000-0000-000000000004', '-', '')),
 'MED-OMEP-20',
 'Omeprazol 20mg Cápsulas',
 'Medicamento',
 'Ambiente_15_25',
 false,
 'EE-12348-2023',
 'Caja x 100 cápsulas',
 36,
 15.0,
 25.0,
 NOW(),
 NOW());

-- Losartán
INSERT INTO productos (id, codigo_sku, nombre, tipo, condicion_almacen, requiere_cadena_frio, 
                      registro_sanitario, unidad_medida, vida_util_meses, temp_min, temp_max, 
                      fecha_creacion, fecha_actualizacion)
VALUES 
(UNHEX(REPLACE('p1000000-0000-0000-0000-000000000005', '-', '')),
 'MED-LOSA-50',
 'Losartán 50mg Tabletas',
 'Medicamento',
 'Ambiente_15_25',
 false,
 'EE-12349-2023',
 'Caja x 100 tabletas',
 36,
 15.0,
 25.0,
 NOW(),
 NOW());

-- Metformina
INSERT INTO productos (id, codigo_sku, nombre, tipo, condicion_almacen, requiere_cadena_frio, 
                      registro_sanitario, unidad_medida, vida_util_meses, temp_min, temp_max, 
                      fecha_creacion, fecha_actualizacion)
VALUES 
(UNHEX(REPLACE('p1000000-0000-0000-0000-000000000006', '-', '')),
 'MED-METF-850',
 'Metformina 850mg Tabletas',
 'Medicamento',
 'Ambiente_15_25',
 false,
 'EE-12350-2023',
 'Caja x 100 tabletas',
 36,
 15.0,
 25.0,
 NOW(),
 NOW());

-- MEDICAMENTOS REFRIGERADOS (2-8°C)
-- ============================================

-- Insulina
INSERT INTO productos (id, codigo_sku, nombre, tipo, condicion_almacen, requiere_cadena_frio, 
                      registro_sanitario, unidad_medida, vida_util_meses, temp_min, temp_max, 
                      fecha_creacion, fecha_actualizacion)
VALUES 
(UNHEX(REPLACE('p1000000-0000-0000-0000-000000000007', '-', '')),
 'BIO-INSU-100',
 'Insulina Humana 100UI/ml',
 'Biologico',
 'Refrigerado_2_8',
 true,
 'EE-13001-2023',
 'Frasco x 10ml',
 24,
 2.0,
 8.0,
 NOW(),
 NOW());

-- Vacuna Influenza
INSERT INTO productos (id, codigo_sku, nombre, tipo, condicion_almacen, requiere_cadena_frio, 
                      registro_sanitario, unidad_medida, vida_util_meses, temp_min, temp_max, 
                      fecha_creacion, fecha_actualizacion)
VALUES 
(UNHEX(REPLACE('p1000000-0000-0000-0000-000000000008', '-', '')),
 'BIO-VACU-FLU',
 'Vacuna Influenza Tetravalente',
 'Biologico',
 'Refrigerado_2_8',
 true,
 'EE-13002-2023',
 'Jeringa prellenada 0.5ml',
 12,
 2.0,
 8.0,
 NOW(),
 NOW());

-- Vacuna Hepatitis B
INSERT INTO productos (id, codigo_sku, nombre, tipo, condicion_almacen, requiere_cadena_frio, 
                      registro_sanitario, unidad_medida, vida_util_meses, temp_min, temp_max, 
                      fecha_creacion, fecha_actualizacion)
VALUES 
(UNHEX(REPLACE('p1000000-0000-0000-0000-000000000009', '-', '')),
 'BIO-VACU-HEP',
 'Vacuna Hepatitis B Recombinante',
 'Biologico',
 'Refrigerado_2_8',
 true,
 'EE-13003-2023',
 'Frasco x 1ml',
 36,
 2.0,
 8.0,
 NOW(),
 NOW());

-- MEDICAMENTOS CONTROLADOS
-- ============================================

-- Morfina
INSERT INTO productos (id, codigo_sku, nombre, tipo, condicion_almacen, requiere_cadena_frio, 
                      registro_sanitario, unidad_medida, vida_util_meses, temp_min, temp_max, 
                      fecha_creacion, fecha_actualizacion)
VALUES 
(UNHEX(REPLACE('p1000000-0000-0000-0000-000000000010', '-', '')),
 'CTRL-MORF-10',
 'Morfina Sulfato 10mg Ampolla',
 'Controlado',
 'Ambiente_15_25',
 false,
 'EE-14001-2023',
 'Caja x 100 ampollas',
 36,
 15.0,
 25.0,
 NOW(),
 NOW());

-- Diazepam
INSERT INTO productos (id, codigo_sku, nombre, tipo, condicion_almacen, requiere_cadena_frio, 
                      registro_sanitario, unidad_medida, vida_util_meses, temp_min, temp_max, 
                      fecha_creacion, fecha_actualizacion)
VALUES 
(UNHEX(REPLACE('p1000000-0000-0000-0000-000000000011', '-', '')),
 'CTRL-DIAZ-10',
 'Diazepam 10mg Tabletas',
 'Controlado',
 'Ambiente_15_25',
 false,
 'EE-14002-2023',
 'Caja x 100 tabletas',
 36,
 15.0,
 25.0,
 NOW(),
 NOW());

-- Tramadol
INSERT INTO productos (id, codigo_sku, nombre, tipo, condicion_almacen, requiere_cadena_frio, 
                      registro_sanitario, unidad_medida, vida_util_meses, temp_min, temp_max, 
                      fecha_creacion, fecha_actualizacion)
VALUES 
(UNHEX(REPLACE('p1000000-0000-0000-0000-000000000012', '-', '')),
 'CTRL-TRAM-50',
 'Tramadol 50mg Cápsulas',
 'Controlado',
 'Ambiente_15_25',
 false,
 'EE-14003-2023',
 'Caja x 100 cápsulas',
 36,
 15.0,
 25.0,
 NOW(),
 NOW());

-- DISPOSITIVOS MÉDICOS
-- ============================================

-- Jeringas
INSERT INTO productos (id, codigo_sku, nombre, tipo, condicion_almacen, requiere_cadena_frio, 
                      registro_sanitario, unidad_medida, vida_util_meses, temp_min, temp_max, 
                      fecha_creacion, fecha_actualizacion)
VALUES 
(UNHEX(REPLACE('p1000000-0000-0000-0000-000000000013', '-', '')),
 'DISP-JER-5ML',
 'Jeringa Descartable 5ml con Aguja',
 'Dispositivo',
 'Ambiente_15_25',
 false,
 'DM-15001-2023',
 'Caja x 100 unidades',
 60,
 15.0,
 25.0,
 NOW(),
 NOW());

-- Guantes
INSERT INTO productos (id, codigo_sku, nombre, tipo, condicion_almacen, requiere_cadena_frio, 
                      registro_sanitario, unidad_medida, vida_util_meses, temp_min, temp_max, 
                      fecha_creacion, fecha_actualizacion)
VALUES 
(UNHEX(REPLACE('p1000000-0000-0000-0000-000000000014', '-', '')),
 'DISP-GUAN-LAT',
 'Guantes de Látex Talla M',
 'Dispositivo',
 'Ambiente_15_25',
 false,
 'DM-15002-2023',
 'Caja x 100 pares',
 60,
 15.0,
 25.0,
 NOW(),
 NOW());

-- Mascarillas
INSERT INTO productos (id, codigo_sku, nombre, tipo, condicion_almacen, requiere_cadena_frio, 
                      registro_sanitario, unidad_medida, vida_util_meses, temp_min, temp_max, 
                      fecha_creacion, fecha_actualizacion)
VALUES 
(UNHEX(REPLACE('p1000000-0000-0000-0000-000000000015', '-', '')),
 'DISP-MASC-N95',
 'Mascarilla N95 Respirador',
 'Dispositivo',
 'Ambiente_15_25',
 false,
 'DM-15003-2023',
 'Caja x 20 unidades',
 60,
 15.0,
 25.0,
 NOW(),
 NOW());

-- ============================================
-- VERIFICACIÓN
-- ============================================

-- Ver todos los productos por tipo
SELECT 
    codigo_sku,
    nombre,
    tipo,
    condicion_almacen,
    requiere_cadena_frio,
    registro_sanitario,
    unidad_medida,
    vida_util_meses
FROM productos
ORDER BY tipo, nombre;

-- Ver estructura completa del almacén
SELECT 
    a.nombre AS almacen,
    z.nombre AS zona,
    z.tipo AS tipo_zona,
    u.codigo AS ubicacion,
    u.capacidad,
    u.temp_objetivo_min,
    u.temp_objetivo_max
FROM almacenes a
JOIN zonas z ON z.almacen_id = a.id
JOIN ubicaciones u ON u.zona_id = z.id
ORDER BY a.nombre, z.nombre, u.codigo;

-- Ver clientes
SELECT 
    razon_social,
    ruc_dni,
    tipo_cliente,
    distrito,
    telefono
FROM clientes
WHERE activo = true
ORDER BY razon_social;

-- ============================================
-- NOTAS IMPORTANTES:
-- ============================================
-- 1. Ejecutar DESPUÉS de SQL_CREAR_TABLAS_INVENTARIO.sql
-- 2. Los IDs son UUIDs fijos para facilitar pruebas
-- 3. Tipos de productos: Medicamento, Biologico, Dispositivo, Controlado, Cosmetico, Suplemento
-- 4. Condiciones: Ambiente_15_25, Refrigerado_2_8, Congelado_m20, ULT_m70
-- 5. Los productos refrigerados requieren cadena de frío
-- 6. Los controlados requieren área especial de seguridad
-- ============================================
