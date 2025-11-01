# 📊 Análisis de Tablas: Inventario y Recepción

## 🎯 Objetivo del Análisis

Identificar qué tablas:
1. ✅ **Mantener**: Son necesarias para el flujo actual
2. ❌ **Eliminar**: No se usan o son redundantes
3. ➕ **Agregar**: Faltan para completar el flujo

---

## 📋 Tablas Actuales en el Sistema

### ✅ TABLAS ESENCIALES (Mantener)

#### 1. **Gestión de Usuarios**
- `usuarios` - Tabla base de usuarios
- `directores_tecnicos` - Herencia de usuarios
- `transportistas` - Herencia de usuarios (si se usa para despacho)

**Razón**: Necesarias para autenticación y roles.

#### 2. **Gestión de Clientes**
- `clientes` - Información de clientes
- ~~`cliente_productos`~~ - **EVALUAR** (ver más abajo)

**Razón**: Los clientes son dueños de la mercadería.

#### 3. **Catálogo de Productos**
- `productos` - Catálogo de productos
- `lotes` - Información de lotes

**Razón**: Necesarias para registrar qué productos llegan.

#### 4. **Proceso de Recepción** ⭐ CORE
- `recepciones_mercaderia` - Registro de recepciones
- `detalles_recepcion` - Productos recibidos por recepción

**Razón**: Núcleo del proceso de recepción.

#### 5. **Inventario** ⭐ CORE
- `inventarios` - Stock actual de productos
- `movimientos_stock` - Historial de movimientos

**Razón**: Registro de productos en almacén.

#### 6. **Almacenamiento**
- `almacenes` - Información de almacenes
- `ubicaciones` - Ubicaciones físicas en almacén
- `zonas` - Zonas dentro del almacén

**Razón**: Necesarias para asignar ubicaciones físicas.

---

### ❌ TABLAS A ELIMINAR (No necesarias actualmente)

#### 1. **`proveedores`** ❌
**Razón para eliminar**:
- Ya no se usa (cambiamos a `clientes`)
- Redundante con el nuevo flujo
- Puede causar confusión

**Acción**: 
```sql
DROP TABLE IF EXISTS proveedores;
```

#### 2. **`detalles_acta_recepcion`** ❌
**Razón para eliminar**:
- Duplica funcionalidad de `detalles_recepcion`
- No se usa en el flujo actual
- Genera confusión

**Acción**:
```sql
DROP TABLE IF EXISTS detalles_acta_recepcion;
```

#### 3. **`registro_almacenamiento`** ❌
**Razón para eliminar**:
- Funcionalidad cubierta por `inventarios` + `movimientos_stock`
- Redundante
- Complica el modelo

**Acción**:
```sql
DROP TABLE IF EXISTS registro_almacenamiento;
```

#### 4. **`detalle_almacenamiento`** ❌
**Razón para eliminar**:
- Relacionada con `registro_almacenamiento`
- No se usa en el flujo actual

**Acción**:
```sql
DROP TABLE IF EXISTS detalle_almacenamiento;
```

#### 5. **`liberaciones_lote`** ❌ (Por ahora)
**Razón para eliminar**:
- Funcionalidad avanzada no implementada aún
- Se puede agregar después si es necesario

**Acción**:
```sql
DROP TABLE IF EXISTS liberaciones_lote;
```

#### 6. **`pedidos`** ❌ (Por ahora)
**Razón para eliminar**:
- No se usa en el flujo de recepción
- Es para otro módulo (despacho)
- Se puede agregar después

**Acción**:
```sql
-- Mantener si ya tienes el módulo de pedidos
-- Eliminar si no lo usas aún
DROP TABLE IF EXISTS pedidos;
```

#### 7. **`guias_remision_cliente`** ❌ (Por ahora)
**Razón para eliminar**:
- Para despacho, no para recepción
- Se puede agregar después

**Acción**:
```sql
DROP TABLE IF EXISTS guias_remision_cliente;
DROP TABLE IF EXISTS detalles_guia_remision_cliente;
```

#### 8. **`sensores_temperatura`** ❌ (Por ahora)
**Razón para eliminar**:
- Funcionalidad avanzada
- No crítica para el flujo básico

**Acción**:
```sql
DROP TABLE IF EXISTS registros_temperatura;
DROP TABLE IF EXISTS sensores_temperatura;
```

#### 9. **`equipos_frio`** ❌ (Por ahora)
**Razón para eliminar**:
- Funcionalidad avanzada
- No crítica para el flujo básico

**Acción**:
```sql
DROP TABLE IF EXISTS equipos_frio;
```

---

### ❓ TABLAS A EVALUAR

#### 1. **`cliente_productos`** ❓
**Propósito**: Relacionar clientes con sus productos específicos

**Evaluar**:
- ¿Los clientes tienen productos específicos?
- ¿O cualquier cliente puede tener cualquier producto?

**Recomendación**:
- **Mantener** si cada cliente tiene su propio catálogo
- **Eliminar** si todos los clientes pueden tener cualquier producto

---

## ➕ TABLAS QUE FALTAN (Agregar)

### 1. **`inventario_cliente`** ➕ CRÍTICA

**Propósito**: Registrar el inventario de productos de cada cliente

**Estructura sugerida**:
```sql
CREATE TABLE inventario_cliente (
    id BINARY(16) PRIMARY KEY,
    cliente_id BINARY(16) NOT NULL,
    producto_id BINARY(16) NOT NULL,
    lote_id BINARY(16) NOT NULL,
    recepcion_id BINARY(16) NOT NULL,
    cantidad_disponible INT NOT NULL DEFAULT 0,
    cantidad_reservada INT NOT NULL DEFAULT 0,
    cantidad_despachada INT NOT NULL DEFAULT 0,
    ubicacion_id BINARY(16),
    fecha_ingreso DATETIME NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    estado ENUM('PENDIENTE_UBICACION', 'ALMACENADO', 'RESERVADO', 'DESPACHADO') NOT NULL,
    temperatura_almacenamiento DECIMAL(5,2),
    observaciones VARCHAR(500),
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    FOREIGN KEY (producto_id) REFERENCES productos(id),
    FOREIGN KEY (lote_id) REFERENCES lotes(id),
    FOREIGN KEY (recepcion_id) REFERENCES recepciones_mercaderia(id),
    FOREIGN KEY (ubicacion_id) REFERENCES ubicaciones(id),
    INDEX idx_cliente_producto (cliente_id, producto_id),
    INDEX idx_estado (estado),
    INDEX idx_ubicacion (ubicacion_id)
);
```

**Razón**: 
- Separa el inventario por cliente
- Permite rastrear productos de cada cliente
- Facilita el despacho por cliente

### 2. **`historial_ubicaciones`** ➕ RECOMENDADA

**Propósito**: Auditoría de cambios de ubicación

**Estructura sugerida**:
```sql
CREATE TABLE historial_ubicaciones (
    id BINARY(16) PRIMARY KEY,
    inventario_cliente_id BINARY(16) NOT NULL,
    ubicacion_anterior_id BINARY(16),
    ubicacion_nueva_id BINARY(16) NOT NULL,
    motivo VARCHAR(200),
    usuario_id BINARY(16) NOT NULL,
    fecha_movimiento DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (inventario_cliente_id) REFERENCES inventario_cliente(id),
    FOREIGN KEY (ubicacion_anterior_id) REFERENCES ubicaciones(id),
    FOREIGN KEY (ubicacion_nueva_id) REFERENCES ubicaciones(id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
```

**Razón**:
- Trazabilidad de movimientos
- Auditoría completa
- Útil para resolver problemas

---

## 🔄 Flujo de Datos con las Nuevas Tablas

### FASE 1: Recepción registra productos
```
recepciones_mercaderia (PENDIENTE)
    ↓
detalles_recepcion (productos agregados)
    ↓
recepciones_mercaderia (EN_CUARENTENA)
```

### FASE 2: Administrativa valida y registra
```
recepciones_mercaderia (EN_CUARENTENA)
    ↓ (valida)
recepciones_mercaderia (APROBADO)
    ↓ (registra productos conformes)
inventario_cliente (PENDIENTE_UBICACION)
    ↓
lotes (actualiza o crea)
```

### FASE 3: Operaciones asigna ubicación
```
inventario_cliente (PENDIENTE_UBICACION)
    ↓ (asigna ubicacion_id)
inventario_cliente (ALMACENADO)
    ↓
historial_ubicaciones (registro del movimiento)
```

---

## 📊 Modelo de Datos Simplificado

### Tablas Core del Flujo:

```
clientes
    ↓
recepciones_mercaderia
    ↓
detalles_recepcion
    ↓ (aprobación)
inventario_cliente ← productos + lotes
    ↓ (asignación)
ubicaciones
```

---

## 🎯 Recomendaciones Finales

### Eliminar AHORA (Simplificar):
1. ❌ `proveedores`
2. ❌ `detalles_acta_recepcion`
3. ❌ `registro_almacenamiento`
4. ❌ `detalle_almacenamiento`
5. ❌ `liberaciones_lote`
6. ❌ `sensores_temperatura`
7. ❌ `registros_temperatura`
8. ❌ `equipos_frio`

### Eliminar SI NO SE USAN (Evaluar):
9. ❓ `pedidos` (si no tienes módulo de pedidos)
10. ❓ `guias_remision_cliente` (si no tienes módulo de despacho)
11. ❓ `detalles_guia_remision_cliente`
12. ❓ `cliente_productos` (si no necesitas catálogo por cliente)

### Agregar AHORA (Críticas):
1. ➕ `inventario_cliente` - **CRÍTICA**
2. ➕ `historial_ubicaciones` - **RECOMENDADA**

### Mantener (Esenciales):
- ✅ `usuarios` y herencias
- ✅ `clientes`
- ✅ `productos`
- ✅ `lotes`
- ✅ `recepciones_mercaderia`
- ✅ `detalles_recepcion`
- ✅ `inventarios` (si la usas para stock general)
- ✅ `movimientos_stock`
- ✅ `almacenes`
- ✅ `ubicaciones`
- ✅ `zonas`

---

## 📝 Script de Migración Sugerido

```sql
-- ============================================
-- PASO 1: ELIMINAR TABLAS INNECESARIAS
-- ============================================

-- Eliminar tablas relacionadas con proveedores
DROP TABLE IF EXISTS proveedores;

-- Eliminar duplicados de acta de recepción
DROP TABLE IF EXISTS detalles_acta_recepcion;

-- Eliminar tablas de almacenamiento redundantes
DROP TABLE IF EXISTS detalle_almacenamiento;
DROP TABLE IF EXISTS registro_almacenamiento;

-- Eliminar funcionalidades avanzadas no usadas
DROP TABLE IF EXISTS liberaciones_lote;
DROP TABLE IF EXISTS registros_temperatura;
DROP TABLE IF EXISTS sensores_temperatura;
DROP TABLE IF EXISTS equipos_frio;

-- OPCIONAL: Eliminar si no se usan
-- DROP TABLE IF EXISTS detalles_guia_remision_cliente;
-- DROP TABLE IF EXISTS guias_remision_cliente;
-- DROP TABLE IF EXISTS pedidos;

-- ============================================
-- PASO 2: CREAR TABLA DE INVENTARIO POR CLIENTE
-- ============================================

CREATE TABLE inventario_cliente (
    id BINARY(16) PRIMARY KEY,
    cliente_id BINARY(16) NOT NULL,
    producto_id BINARY(16) NOT NULL,
    lote_id BINARY(16) NOT NULL,
    recepcion_id BINARY(16) NOT NULL,
    detalle_recepcion_id BINARY(16),
    cantidad_disponible INT NOT NULL DEFAULT 0,
    cantidad_reservada INT NOT NULL DEFAULT 0,
    cantidad_despachada INT NOT NULL DEFAULT 0,
    ubicacion_id BINARY(16),
    codigo_barras VARCHAR(100),
    fecha_ingreso DATETIME NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    estado ENUM('PENDIENTE_UBICACION', 'ALMACENADO', 'RESERVADO', 'DESPACHADO', 'VENCIDO') NOT NULL DEFAULT 'PENDIENTE_UBICACION',
    temperatura_almacenamiento DECIMAL(5,2),
    observaciones VARCHAR(500),
    usuario_registro_id BINARY(16),
    usuario_ubicacion_id BINARY(16),
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    FOREIGN KEY (producto_id) REFERENCES productos(id),
    FOREIGN KEY (lote_id) REFERENCES lotes(id),
    FOREIGN KEY (recepcion_id) REFERENCES recepciones_mercaderia(id),
    FOREIGN KEY (detalle_recepcion_id) REFERENCES detalles_recepcion(id),
    FOREIGN KEY (ubicacion_id) REFERENCES ubicaciones(id),
    FOREIGN KEY (usuario_registro_id) REFERENCES usuarios(id),
    FOREIGN KEY (usuario_ubicacion_id) REFERENCES usuarios(id),
    
    INDEX idx_cliente_producto (cliente_id, producto_id),
    INDEX idx_estado (estado),
    INDEX idx_ubicacion (ubicacion_id),
    INDEX idx_lote (lote_id),
    INDEX idx_vencimiento (fecha_vencimiento)
);

-- ============================================
-- PASO 3: CREAR TABLA DE HISTORIAL DE UBICACIONES
-- ============================================

CREATE TABLE historial_ubicaciones (
    id BINARY(16) PRIMARY KEY,
    inventario_cliente_id BINARY(16) NOT NULL,
    ubicacion_anterior_id BINARY(16),
    ubicacion_nueva_id BINARY(16) NOT NULL,
    motivo VARCHAR(200),
    usuario_id BINARY(16) NOT NULL,
    fecha_movimiento DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (inventario_cliente_id) REFERENCES inventario_cliente(id) ON DELETE CASCADE,
    FOREIGN KEY (ubicacion_anterior_id) REFERENCES ubicaciones(id),
    FOREIGN KEY (ubicacion_nueva_id) REFERENCES ubicaciones(id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    
    INDEX idx_inventario (inventario_cliente_id),
    INDEX idx_fecha (fecha_movimiento)
);

-- ============================================
-- PASO 4: VERIFICACIÓN
-- ============================================

-- Ver todas las tablas
SHOW TABLES;

-- Ver estructura de la nueva tabla
DESCRIBE inventario_cliente;
DESCRIBE historial_ubicaciones;
```

---

## 🎓 Explicación del Modelo

### ¿Por qué `inventario_cliente` en lugar de `inventarios`?

**Tabla actual `inventarios`**:
- Probablemente es stock general
- No separa por cliente
- Dificulta el tracking por cliente

**Nueva tabla `inventario_cliente`**:
- ✅ Separa inventario por cliente
- ✅ Cada cliente ve solo sus productos
- ✅ Facilita facturación por cliente
- ✅ Mejor trazabilidad
- ✅ Permite diferentes precios por cliente

### Estados del Inventario:

1. **PENDIENTE_UBICACION**: Aprobado por administrativa, sin ubicación
2. **ALMACENADO**: Con ubicación asignada, disponible
3. **RESERVADO**: Apartado para un pedido
4. **DESPACHADO**: Ya salió del almacén
5. **VENCIDO**: Producto vencido

---

## 📞 Próximos Pasos

1. **Revisar** este análisis
2. **Decidir** qué tablas eliminar
3. **Ejecutar** script de migración
4. **Crear** entidades Java para las nuevas tablas
5. **Implementar** endpoints del backend
6. **Probar** el flujo completo

---

## ⚠️ IMPORTANTE

Antes de eliminar tablas:
1. ✅ Hacer BACKUP completo de la base de datos
2. ✅ Verificar que no haya datos importantes
3. ✅ Probar en ambiente de desarrollo primero
4. ✅ Documentar los cambios

```bash
# Backup de la base de datos
mysqldump -u usuario -p BaseDeDatos > backup_antes_limpieza.sql
```
