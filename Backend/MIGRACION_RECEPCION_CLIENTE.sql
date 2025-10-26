-- ============================================
-- MIGRACIÓN: Cambiar Proveedor por Cliente en RecepcionMercaderia
-- ============================================
-- IMPORTANTE: Hacer BACKUP antes de ejecutar
-- ============================================

-- Paso 1: Agregar nueva columna cliente_id
ALTER TABLE recepciones_mercaderia 
ADD COLUMN cliente_id BINARY(16) AFTER proveedor_id;

-- Paso 2: (OPCIONAL) Migrar datos existentes si los hay
-- Si tienes datos y quieres mantenerlos, necesitas mapear proveedor_id a cliente_id
-- UPDATE recepciones_mercaderia SET cliente_id = proveedor_id WHERE proveedor_id IS NOT NULL;

-- Paso 3: Eliminar constraint de proveedor
ALTER TABLE recepciones_mercaderia 
DROP FOREIGN KEY IF EXISTS fk_recepcion_proveedor;

-- Paso 4: Eliminar índice de proveedor si existe
ALTER TABLE recepciones_mercaderia 
DROP INDEX IF EXISTS idx_proveedor_id;

-- Paso 5: Eliminar columna proveedor_id
ALTER TABLE recepciones_mercaderia 
DROP COLUMN proveedor_id;

-- Paso 6: Agregar constraint para cliente
ALTER TABLE recepciones_mercaderia 
ADD CONSTRAINT fk_recepcion_cliente 
FOREIGN KEY (cliente_id) REFERENCES clientes(id);

-- Paso 7: Hacer NOT NULL la columna cliente_id
ALTER TABLE recepciones_mercaderia 
MODIFY COLUMN cliente_id BINARY(16) NOT NULL;

-- Paso 8: Agregar índice para mejor rendimiento
CREATE INDEX idx_cliente_id ON recepciones_mercaderia(cliente_id);

-- ============================================
-- VERIFICACIÓN
-- ============================================

-- Verificar estructura de la tabla
DESCRIBE recepciones_mercaderia;

-- Verificar constraints
SELECT 
    CONSTRAINT_NAME,
    TABLE_NAME,
    COLUMN_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM 
    INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE 
    TABLE_NAME = 'recepciones_mercaderia'
    AND REFERENCED_TABLE_NAME IS NOT NULL;

-- ============================================
-- ROLLBACK (Si algo sale mal)
-- ============================================
-- Descomentar y ejecutar si necesitas revertir:

-- ALTER TABLE recepciones_mercaderia ADD COLUMN proveedor_id BINARY(16);
-- ALTER TABLE recepciones_mercaderia DROP FOREIGN KEY fk_recepcion_cliente;
-- ALTER TABLE recepciones_mercaderia DROP COLUMN cliente_id;
-- ALTER TABLE recepciones_mercaderia ADD CONSTRAINT fk_recepcion_proveedor FOREIGN KEY (proveedor_id) REFERENCES proveedores(id);

-- ============================================
-- FIN DEL SCRIPT
-- ============================================
