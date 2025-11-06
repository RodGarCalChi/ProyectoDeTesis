-- Script para limpiar datos inconsistentes antes de agregar foreign keys
-- Ejecutar este script en la base de datos antes de iniciar la aplicación

USE BaseDeDatos;

-- 1. Verificar datos inconsistentes en almacenes
SELECT 'Almacenes con cliente_id inválido:' as mensaje;
SELECT a.id, a.nombre, a.cliente_id 
FROM almacenes a 
LEFT JOIN clientes c ON a.cliente_id = c.id 
WHERE a.cliente_id IS NOT NULL AND c.id IS NULL;

SELECT 'Almacenes con operador_logistico_id inválido:' as mensaje;
SELECT a.id, a.nombre, a.operador_logistico_id 
FROM almacenes a 
LEFT JOIN operadores_logisticos o ON a.operador_logistico_id = o.id 
WHERE a.operador_logistico_id IS NOT NULL AND o.id IS NULL;

-- 2. OPCIÓN A: Eliminar almacenes con referencias inválidas (RECOMENDADO para desarrollo)
-- Descomentar las siguientes líneas si quieres eliminar los datos inconsistentes

-- DELETE FROM almacenes 
-- WHERE cliente_id IS NOT NULL 
-- AND cliente_id NOT IN (SELECT id FROM clientes);

-- DELETE FROM almacenes 
-- WHERE operador_logistico_id IS NOT NULL 
-- AND operador_logistico_id NOT IN (SELECT id FROM operadores_logisticos);

-- 3. OPCIÓN B: Establecer valores NULL temporalmente
-- Descomentar si prefieres mantener los almacenes pero limpiar las referencias

UPDATE almacenes 
SET cliente_id = NULL 
WHERE cliente_id IS NOT NULL 
AND cliente_id NOT IN (SELECT id FROM clientes);

UPDATE almacenes 
SET operador_logistico_id = NULL 
WHERE operador_logistico_id IS NOT NULL 
AND operador_logistico_id NOT IN (SELECT id FROM operadores_logisticos);

-- 4. Verificar que ya no hay datos inconsistentes
SELECT 'Verificación final - Almacenes con referencias inválidas:' as mensaje;
SELECT COUNT(*) as total_inconsistentes
FROM almacenes a 
WHERE (a.cliente_id IS NOT NULL AND a.cliente_id NOT IN (SELECT id FROM clientes))
   OR (a.operador_logistico_id IS NOT NULL AND a.operador_logistico_id NOT IN (SELECT id FROM operadores_logisticos));

-- 5. Mostrar resumen
SELECT 'Resumen de datos:' as mensaje;
SELECT 
    (SELECT COUNT(*) FROM operadores_logisticos) as total_operadores,
    (SELECT COUNT(*) FROM clientes) as total_clientes,
    (SELECT COUNT(*) FROM almacenes) as total_almacenes,
    (SELECT COUNT(*) FROM almacenes WHERE cliente_id IS NULL) as almacenes_sin_cliente,
    (SELECT COUNT(*) FROM almacenes WHERE operador_logistico_id IS NULL) as almacenes_sin_operador;
