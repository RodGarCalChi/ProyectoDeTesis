USE basededatoslogisticofarmaceutico;

-- VER TODAS LAS RELACIONES CLIENTE-PRODUCTO
SELECT 
    c.razon_social AS CLIENTE,
    p.nombre AS PRODUCTO,
    p.codigo_sku AS SKU,
    cp.fecha_asignacion AS FECHA,
    cp.activo AS ACTIVO,
    cp.observaciones AS OBSERVACIONES
FROM cliente_producto cp
INNER JOIN cliente c ON cp.cliente_id = c.id
INNER JOIN producto p ON cp.producto_id = p.id
ORDER BY c.razon_social, p.nombre;
