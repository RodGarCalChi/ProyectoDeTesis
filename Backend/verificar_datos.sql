-- Verificar si existen datos en la tabla cliente_producto
USE basededatoslogisticofarmaceutico;

-- 1. Contar registros en cliente_producto
SELECT COUNT(*) as total_relaciones FROM cliente_producto;

-- 2. Ver todas las relaciones
SELECT 
    cp.id,
    cp.cliente_id,
    cp.producto_id,
    cp.fecha_asignacion,
    cp.activo,
    cp.observaciones
FROM cliente_producto cp
LIMIT 20;

-- 3. Ver relaciones con nombres de clientes y productos
SELECT 
    c.razon_social AS cliente,
    p.nombre AS producto,
    p.codigo_sku,
    cp.fecha_asignacion,
    cp.activo
FROM cliente_producto cp
LEFT JOIN cliente c ON cp.cliente_id = c.id
LEFT JOIN producto p ON cp.producto_id = p.id
LIMIT 20;

-- 4. Verificar si existen clientes
SELECT COUNT(*) as total_clientes FROM cliente;

-- 5. Verificar si existen productos
SELECT COUNT(*) as total_productos FROM producto;
