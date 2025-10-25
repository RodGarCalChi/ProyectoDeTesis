-- ============================================
-- SCRIPT: Eliminación de Tablas Duplicadas
-- FASE 1: Eliminación Segura (Sin Implementación)
-- ============================================
-- 
-- IMPORTANTE: 
-- 1. Hacer BACKUP de la base de datos antes de ejecutar
-- 2. Ejecutar primero en ambiente de DESARROLLO
-- 3. Verificar que no haya datos importantes
-- 4. Confirmar que no hay referencias en código activo
--
-- ============================================

-- Verificar tablas existentes antes de eliminar
SELECT 
    TABLE_NAME,
    TABLE_ROWS,
    CREATE_TIME
FROM 
    information_schema.TABLES
WHERE 
    TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME IN (
        'recepcion_lineas',
        'recepciones',
        'oc_lineas',
        'ordenes_compra'
    );

-- ============================================
-- PASO 1: Eliminar tabla de detalles primero
-- ============================================

-- Eliminar RecepcionLinea (depende de RecepcionNew)
DROP TABLE IF EXISTS `recepcion_lineas`;
-- Razón: Duplica funcionalidad de DetalleRecepcion
-- Impacto: NINGUNO - No tiene implementación activa

-- ============================================
-- PASO 2: Eliminar tabla principal de recepciones
-- ============================================

-- Eliminar RecepcionNew
DROP TABLE IF EXISTS `recepciones`;
-- Razón: Duplica funcionalidad de RecepcionMercaderia
-- Impacto: NINGUNO - No tiene servicio/controlador implementado
-- Alternativa: Usar RecepcionMercaderia (tabla principal)

-- ============================================
-- PASO 3: Eliminar tabla de detalles de OC
-- ============================================

-- Eliminar OCLinea (depende de OrdenCompra)
DROP TABLE IF EXISTS `oc_lineas`;
-- Razón: Depende de OrdenCompra que no se usa
-- Impacto: NINGUNO - No tiene implementación activa

-- ============================================
-- PASO 4: Eliminar tabla de órdenes de compra
-- ============================================

-- Eliminar OrdenCompra
DROP TABLE IF EXISTS `ordenes_compra`;
-- Razón: No se usa en el flujo actual
-- Impacto: NINGUNO - RecepcionMercaderia usa número de OC como String
-- Nota: El número de OC ya está almacenado en RecepcionMercaderia.numeroOrdenCompra

-- ============================================
-- VERIFICACIÓN POST-ELIMINACIÓN
-- ============================================

-- Verificar que las tablas fueron eliminadas
SELECT 
    TABLE_NAME
FROM 
    information_schema.TABLES
WHERE 
    TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME IN (
        'recepcion_lineas',
        'recepciones',
        'oc_lineas',
        'ordenes_compra'
    );
-- Resultado esperado: 0 filas

-- ============================================
-- VERIFICAR TABLAS RESTANTES
-- ============================================

-- Listar todas las tablas después de la limpieza
SELECT 
    TABLE_NAME,
    TABLE_ROWS,
    ROUND(((DATA_LENGTH + INDEX_LENGTH) / 1024 / 1024), 2) AS 'Size_MB'
FROM 
    information_schema.TABLES
WHERE 
    TABLE_SCHEMA = DATABASE()
ORDER BY 
    TABLE_NAME;

-- ============================================
-- RESUMEN DE CAMBIOS
-- ============================================
-- 
-- TABLAS ELIMINADAS: 4
-- - recepcion_lineas
-- - recepciones
-- - oc_lineas
-- - ordenes_compra
--
-- TABLAS PRINCIPALES QUE SE MANTIENEN:
-- ✅ recepciones_mercaderia (principal para recepciones)
-- ✅ detalle_recepcion (detalles de recepciones)
-- ✅ actas_recepcion (documentos legales)
-- ✅ detalle_acta_recepcion (detalles de actas)
--
-- BENEFICIOS:
-- ✅ Menos duplicación de código
-- ✅ Arquitectura más clara
-- ✅ Menos tablas que mantener
-- ✅ Mejor rendimiento (menos joins)
--
-- ============================================

-- FIN DEL SCRIPT
