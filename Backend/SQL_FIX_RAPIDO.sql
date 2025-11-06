-- FIX RÁPIDO: Limpiar referencias inválidas en almacenes
-- Ejecutar este script ANTES de iniciar la aplicación

USE BaseDeDatos;

-- Establecer NULL en referencias inválidas
UPDATE almacenes 
SET cliente_id = NULL 
WHERE cliente_id IS NOT NULL 
AND cliente_id NOT IN (SELECT id FROM clientes);

UPDATE almacenes 
SET operador_logistico_id = NULL 
WHERE operador_logistico_id IS NOT NULL 
AND operador_logistico_id NOT IN (SELECT id FROM operadores_logisticos);

-- Verificar
SELECT 'Datos limpiados correctamente' as resultado;
SELECT COUNT(*) as almacenes_totales FROM almacenes;
SELECT COUNT(*) as almacenes_sin_cliente FROM almacenes WHERE cliente_id IS NULL;
SELECT COUNT(*) as almacenes_sin_operador FROM almacenes WHERE operador_logistico_id IS NULL;
