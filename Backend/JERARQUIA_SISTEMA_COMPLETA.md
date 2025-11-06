# 📊 Jerarquía Completa del Sistema de Almacén

## 🏗️ Estructura Jerárquica

```
Operador Logístico
  └── Almacén (dedicado a un Cliente específico)
      └── Zona (Congelado, Refrigerado, Seco, ULT)
          └── Lote
              └── Palet
                  └── Caja (productos de un cliente)
                      └── Productos (cantidad definida)
```

---

## 📦 Entidades Creadas

### 1. **OperadorLogistico**
Empresa que gestiona los almacenes.

**Campos:**
- `id`: UUID
- `nombre`: Nombre del operador
- `ruc`: RUC único
- `direccion`, `telefono`, `email`
- `activo`: Estado

**Ejemplo:**
```
Operador Logístico Principal
RUC: 20987654321
```

---

### 2. **Almacen** (Actualizado)
Almacén dedicado a un cliente específico dentro de un operador logístico.

**Nuevos campos:**
- `operador_logistico_id`: FK a OperadorLogistico
- `cliente_id`: FK a Cliente

**Concepto:**
- Cada almacén pertenece a UN operador logístico
- Cada almacén está dedicado a UN cliente específico
- Un cliente puede tener múltiples almacenes

**Ejemplo:**
```
Almacén: "Almacén Hospital Dos de Mayo"
Operador: Operador Logístico Principal
Cliente: Hospital Nacional Dos de Mayo
```

---

### 3. **Zona** (Existente)
Áreas dentro del almacén con condiciones específicas.

**Tipos:**
- `CONGELADO`: -20°C o menos
- `REFRIGERADO`: 2°C a 8°C
- `SECO`: 15°C a 25°C (ambiente controlado)
- `ULT`: Ultra Low Temperature (-70°C o menos)

---

### 4. **Lote** (Existente)
Agrupación de productos con características comunes.

---

### 5. **Palet** (Nueva)
Plataforma que agrupa múltiples cajas.

**Campos:**
- `id`: UUID
- `codigo`: Código único (ej: "PAL-2025-001")
- `lote_id`: FK a Lote
- `ubicacion_id`: FK a Ubicacion
- `capacidad_maxima`: Número máximo de cajas
- `cajas_actuales`: Cajas actualmente en el palet
- `peso_maximo_kg`: Peso máximo soportado
- `peso_actual_kg`: Peso actual
- `disponible`: Si acepta más cajas

**Ejemplo:**
```
Código: PAL-2025-001
Capacidad: 20 cajas
Cajas actuales: 15
Peso máximo: 500 kg
Peso actual: 375 kg
```

---

### 6. **Caja** (Nueva)
Contenedor con productos específicos de un cliente.

**Campos:**
- `id`: UUID
- `codigo`: Código único (ej: "CAJA-2025-001")
- `palet_id`: FK a Palet
- `cliente_id`: FK a Cliente
- `producto_id`: FK a Producto
- `cantidad`: Cantidad de productos (ej: 10 vacunas)
- `lote_producto`: Lote del producto
- `fecha_vencimiento`: Fecha de vencimiento
- `tamano`: PEQUENA, MEDIANA, GRANDE, EXTRA_GRANDE
- `peso_kg`: Peso de la caja
- `largo_cm`, `ancho_cm`, `alto_cm`: Dimensiones
- `temperatura_requerida_min/max`: Rango de temperatura
- `requiere_cadena_frio`: Boolean
- `sellada`: Si la caja está sellada
- `fecha_sellado`: Cuándo se selló

**Ejemplo:**
```
Código: CAJA-VAC-001
Cliente: Hospital Dos de Mayo
Producto: Vacuna Influenza
Cantidad: 10 vacunas
Lote: VAC-2025-A
Vencimiento: 2026-06-30
Tamaño: PEQUENA
Peso: 2.5 kg
Dimensiones: 30x30x20 cm
Temperatura: 2°C a 8°C
Requiere cadena de frío: Sí
```

---

## 🔄 Flujo de Operación

### 1. Recepción de Mercadería

```
1. Llega mercadería del Cliente A
2. Se crea/selecciona el Almacén dedicado al Cliente A
3. Se identifica la Zona según temperatura requerida
4. Se asigna un Lote
5. Se crea/selecciona un Palet disponible
6. Se crea una Caja con:
   - Productos específicos
   - Cantidad definida
   - Información del cliente
7. La Caja se coloca en el Palet
8. El Palet se ubica en la Zona
```

### 2. Almacenamiento

```
Operador Logístico: "Operador Principal"
  └── Almacén: "Almacén Hospital Dos de Mayo"
      └── Zona: "Refrigerado (2-8°C)"
          └── Lote: "LOTE-2025-001"
              └── Palet: "PAL-2025-001"
                  ├── Caja: "CAJA-VAC-001" (10 vacunas)
                  ├── Caja: "CAJA-VAC-002" (10 vacunas)
                  └── Caja: "CAJA-VAC-003" (10 vacunas)
```

---

## 📏 Tamaños de Caja

| Tamaño | Dimensiones Máximas | Uso Típico |
|--------|---------------------|------------|
| PEQUENA | 30x30x30 cm | Vacunas, medicamentos pequeños |
| MEDIANA | 50x50x50 cm | Cajas de medicamentos estándar |
| GRANDE | 80x80x80 cm | Equipos médicos, suministros |
| EXTRA_GRANDE | >80x80x80 cm | Equipos grandes, pallets completos |

---

## 🎯 Casos de Uso

### Caso 1: Vacunas para Hospital

```
Cliente: Hospital Nacional Dos de Mayo
Producto: Vacuna Influenza Tetravalente
Cantidad por caja: 10 vacunas
Número de cajas: 50
Total vacunas: 500

Almacenamiento:
- Almacén dedicado al hospital
- Zona: REFRIGERADO (2-8°C)
- 3 Palets (cada uno con ~17 cajas)
- Cada caja sellada y etiquetada
- Monitoreo de temperatura continuo
```

### Caso 2: Medicamentos para Farmacia

```
Cliente: Boticas Arcángel
Producto: Paracetamol 500mg
Cantidad por caja: 100 cajas de tabletas
Número de cajas: 20
Total: 2000 cajas de tabletas

Almacenamiento:
- Almacén dedicado a la farmacia
- Zona: SECO (15-25°C)
- 1 Palet con 20 cajas
- Sin requisitos especiales de temperatura
```

---

## 🔍 Consultas Útiles

### Ver jerarquía completa de un cliente

```sql
SELECT 
    c.razon_social AS cliente,
    a.nombre AS almacen,
    z.nombre AS zona,
    p.codigo AS palet,
    cj.codigo AS caja,
    pr.nombre AS producto,
    cj.cantidad
FROM clientes c
JOIN almacenes a ON a.cliente_id = c.id
JOIN zonas z ON z.almacen_id = a.id
LEFT JOIN palets p ON p.ubicacion_id IN (
    SELECT id FROM ubicaciones WHERE zona_id = z.id
)
LEFT JOIN cajas cj ON cj.palet_id = p.id
LEFT JOIN productos pr ON cj.producto_id = pr.id
WHERE c.id = 'cliente-uuid'
ORDER BY z.nombre, p.codigo, cj.codigo;
```

### Ver ocupación de almacenes

```sql
SELECT 
    a.nombre AS almacen,
    c.razon_social AS cliente,
    COUNT(DISTINCT p.id) AS total_palets,
    COUNT(DISTINCT cj.id) AS total_cajas,
    SUM(cj.cantidad) AS total_productos
FROM almacenes a
JOIN clientes c ON a.cliente_id = c.id
LEFT JOIN zonas z ON z.almacen_id = a.id
LEFT JOIN ubicaciones u ON u.zona_id = z.id
LEFT JOIN palets p ON p.ubicacion_id = u.id
LEFT JOIN cajas cj ON cj.palet_id = p.id
GROUP BY a.id, a.nombre, c.razon_social
ORDER BY total_productos DESC;
```

### Ver productos próximos a vencer

```sql
SELECT 
    c.razon_social AS cliente,
    cj.codigo AS caja,
    p.nombre AS producto,
    cj.cantidad,
    cj.fecha_vencimiento,
    DATEDIFF(cj.fecha_vencimiento, CURDATE()) AS dias_restantes
FROM cajas cj
JOIN clientes c ON cj.cliente_id = c.id
JOIN productos p ON cj.producto_id = p.id
WHERE cj.fecha_vencimiento <= DATE_ADD(CURDATE(), INTERVAL 30 DAY)
ORDER BY cj.fecha_vencimiento ASC;
```

---

## 📋 Archivos Creados

1. **OperadorLogistico.java** - Entidad operador logístico
2. **Palet.java** - Entidad palet
3. **Caja.java** - Entidad caja
4. **TamanoCaja.java** - Enum tamaños de caja
5. **TipoZona.java** - Enum tipos de zona
6. **SQL_CREAR_JERARQUIA_COMPLETA.sql** - Script SQL
7. **Almacen.java** - Actualizado con relaciones

---

## 🚀 Próximos Pasos

1. **Ejecutar SQL**: `SQL_CREAR_JERARQUIA_COMPLETA.sql`
2. **Crear Repositorios**: Para Palet y Caja
3. **Crear Servicios**: Lógica de negocio
4. **Crear Controladores**: APIs REST
5. **Actualizar Frontend**: Pantallas de gestión

---

¡Sistema de jerarquía completa implementado! 🎉
