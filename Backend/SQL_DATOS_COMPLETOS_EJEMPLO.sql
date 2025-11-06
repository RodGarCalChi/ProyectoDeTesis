-- ============================================
-- DATOS COMPLETOS DE EJEMPLO - SISTEMA DE ALMACÉN
-- ============================================
-- Orden de inserción: Operador → Cliente → Almacén → Zona → Ubicación → 
--                     Producto → Lote → Palet → Caja
-- ============================================

SET FOREIGN_KEY_CHECKS = 0;

-- ============================================
-- 1. OPERADORES LOGÍSTICOS
-- ============================================
INSERT INTO operadores_logisticos ( nombre, ruc, direccion, telefono, email, activo)
VALUES 
(UNHEX(REPLACE('op000000-0000-0000-0000-000000000001', '-', '')),
 'Operador Logístico Principal S.A.C.',
 '20987654321',
 'Av. Logística 123, Callao',
 '01-5551234',
 'contacto@operadorlogistico.com',
 TRUE),
(UNHEX(REPLACE('op000000-0000-0000-0000-000000000002', '-', '')),
 'Almacenes Frigoríficos del Perú',
 '20876543210',
 'Av. Industrial 456, Lima',
 '01-5555678',
 'info@almacenesfrio.com',
 TRUE);

-- ============================================
-- 2. CLIENTES
-- ============================================
INSERT INTO clientes (id, razon_social, ruc_dni, direccion_entrega, distrito, telefono, email, tipo_cliente, forma_pago, activo)
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
 TRUE),
(UNHEX(REPLACE('c1000000-0000-0000-0000-000000000002', '-', '')),
 'Clínica San Felipe',
 '20234567890',
 'Av. Gregorio Escobedo 650',
 'Jesús María',
 '01-2195000',
 'compras@sanfelipe.com.pe',
 'CLINICA_PRIVADA',
 'CREDITO_45_DIAS',
 TRUE),
(UNHEX(REPLACE('c1000000-0000-0000-0000-000000000003', '-', '')),
 'Boticas Arcángel S.A.C.',
 '20345678901',
 'Av. Arequipa 2080',
 'Lince',
 '01-4401234',
 'almacen@arcangel.com.pe',
 'CADENA_FARMACIAS',
 'CONTADO',
 TRUE);

-- ============================================
-- 3. ALMACENES (por cliente)
-- ============================================
INSERT INTO almacenes (id, nombre, direccion, operador_logistico_id, cliente_id, tiene_area_controlados)
VALUES 
(UNHEX(REPLACE('a1000000-0000-0000-0000-000000000001', '-', '')),
 'Almacén Hospital Dos de Mayo',
 'Av. Industrial 1234, Callao',
 UNHEX(REPLACE('op000000-0000-0000-0000-000000000001', '-', '')),
 UNHEX(REPLACE('c1000000-0000-0000-0000-000000000001', '-', '')),
 TRUE),
(UNHEX(REPLACE('a1000000-0000-0000-0000-000000000002', '-', '')),
 'Almacén Clínica San Felipe',
 'Av. Industrial 1250, Callao',
 UNHEX(REPLACE('op000000-0000-0000-0000-000000000001', '-', '')),
 UNHEX(REPLACE('c1000000-0000-0000-0000-000000000002', '-', '')),
 FALSE),
(UNHEX(REPLACE('a1000000-0000-0000-0000-000000000003', '-', '')),
 'Almacén Boticas Arcángel',
 'Av. Logística 789, Callao',
 UNHEX(REPLACE('op000000-0000-0000-0000-000000000002', '-', '')),
 UNHEX(REPLACE('c1000000-0000-0000-0000-000000000003', '-', '')),
 FALSE);

-- ============================================
-- 4. ZONAS
-- ============================================
-- Zonas del Almacén Hospital Dos de MayTO zonas (id, nombre, tipo, almacen_id)
VALUES 
(UNHEX(REPLACE('z1000000-0000-0000-0000-0000000000'')),
 'Zona Refrigerada Hospital',
 'REFRIGERADO',
 UNHEa1000000-0000-0000-0000-000000000001', '-', ''))),
(UNHEX(REPLACE('z1000000-0000-0000-0000-000000000002', '-', '')),
 'Zona Seca Hospital',
 'SECO',
 UNHEX(REPLACE('a1000000-0000-0000-0000-000000000001', '-', ''))),
(UNHEX(REPLACE('z1000000-0000-0000-0000-000000000003', '-', '')),
 'Zona Congelada Hospital',
 'CONGELADO',
 UNHEX(REPLACE('a1000000-0000-0000-0000-000000000001', '-', '')));

-- Zonas del Almacén Clínica San Felipe
INSERT INTO zonas (id, nombre, tipo, almacen_id)
VALUES 
(UNHEX(REPLACE('z1000000-0000-0000-0000-000000000004', '-', '')),
 'Zona Refrigerada Clínica',
 'REFRIGERADO',
 UNHEX(REPLACE('a1000000-0000-0000-0000-000000000002', '-', ''))),
(UNHEX(REPLACE('z1000000-0000-0000-0000-000000000005', '-', '')),
 'Zona Seca Clínica',
 'SECO',
 UNHEX(REPLACE('a1000000-0000-0000-0000-000000000002', '-', '')));

-- Zonas del Almacén Boticas Arcángel
INSERT INTO zonas (id, nombre, tipo, almacen_id)
VALUES 
(UNHEX(REPLACE('z1000000-0000-0000-0000-000000000006', '-', '')),
 'Zona Seca Farmacia',
 'SECO',
 UNHEX(REPLACE('a1000000-0000-0000-0000-000000000003', '-', '')));

