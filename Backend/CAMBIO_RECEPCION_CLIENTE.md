# 🔄 Cambio: Recepción de Mercadería - De Proveedor a Cliente

## 📋 Problema Identificado

La entidad `RecepcionMercaderia` actualmente tiene una relación con `Proveedor`, pero **debería tener una relación con `Cliente`**.

### Contexto del Negocio
- **Recepción de Mercadería:** Es cuando un **CLIENTE** deja su mercadería en el almacén para que sea almacenada
- **NO es:** Cuando un proveedor entrega mercadería

---

## 🔧 Cambios Necesarios

### 1. Base de Datos

#### Tabla Actual
```sql
CREATE TABLE recepciones_mercaderia (
    id UUID PRIMARY KEY,
    numero_orden_compra VARCHAR(50),
    numero_guia_remision VARCHAR(50),
    proveedor_id UUID NOT NULL,  -- ❌ INCORRECTO
    fecha_recepcion TIMESTAMP,
    ...
    FOREIGN KEY (proveedor_id) REFERENCES proveedores(id)
);
```

#### Tabla Correcta
```sql
CREATE TABLE recepciones_mercaderia (
    id UUID PRIMARY KEY,
    numero_orden_compra VARCHAR(50),
    numero_guia_remision VARCHAR(50),
    cliente_id UUID NOT NULL,  -- ✅ CORRECTO
    fecha_recepcion TIMESTAMP,
    ...
    FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);
```

#### Script de Migración
```sql
-- Paso 1: Agregar nueva columna
ALTER TABLE recepciones_mercaderia 
ADD COLUMN cliente_id UUID;

-- Paso 2: (Opcional) Migrar datos si existen
-- UPDATE recepciones_mercaderia SET cliente_id = ... WHERE ...;

-- Paso 3: Eliminar constraint antigua
ALTER TABLE recepciones_mercaderia 
DROP CONSTRAINT IF EXISTS fk_recepcion_proveedor;

-- Paso 4: Eliminar columna antigua
ALTER TABLE recepciones_mercaderia 
DROP COLUMN proveedor_id;

-- Paso 5: Agregar constraint nueva
ALTER TABLE recepciones_mercaderia 
ADD CONSTRAINT fk_recepcion_cliente 
FOREIGN KEY (cliente_id) REFERENCES clientes(id);

-- Paso 6: Hacer NOT NULL
ALTER TABLE recepciones_mercaderia 
ALTER COLUMN cliente_id SET NOT NULL;
```

---

### 2. Entidad Java

#### Cambio en RecepcionMercaderia.java

**ANTES:**
```java
@NotNull(message = "El proveedor es obligatorio")
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "proveedor_id", nullable = false)
private Proveedor proveedor;
```

**DESPUÉS:**
```java
@NotNull(message = "El cliente es obligatorio")
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "cliente_id", nullable = false)
private Cliente cliente;
```

---

### 3. DTO

#### RecepcionMercaderiaDTO.java

**ANTES:**
```java
private UUID proveedorId;
private String proveedorNombre;
```

**DESPUÉS:**
```java
private UUID clienteId;
private String clienteNombre;
```

---

### 4. Servicio

#### RecepcionMercaderiaService.java

**ANTES:**
```java
Proveedor proveedor = proveedorRepository.findById(dto.getProveedorId())
    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

recepcion.setProveedor(proveedor);
```

**DESPUÉS:**
```java
Cliente cliente = clienteRepository.findById(dto.getClienteId())
    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

recepcion.setCliente(cliente);
```

---

### 5. Controlador

#### RecepcionMercaderiaController.java

No requiere cambios significativos, solo validaciones de mensajes.

---

### 6. Frontend

#### ✅ YA ACTUALIZADO

El frontend ya fue actualizado para usar `clienteId` en lugar de `proveedorId`.

---

## 📝 Pasos para Aplicar los Cambios

### Opción A: Migración (Si hay datos en producción)

1. **Backup de la base de datos**
```bash
mysqldump -u usuario -p basededatos > backup_antes_migracion.sql
```

2. **Ejecutar script de migración**
```bash
mysql -u usuario -p basededatos < SCRIPT_MIGRACION_CLIENTE.sql
```

3. **Actualizar código Java**
   - Modificar entidad
   - Modificar DTO
   - Modificar servicio
   - Recompilar

4. **Probar**
   - Verificar que las recepciones existentes funcionen
   - Crear nueva recepción de prueba

---

### Opción B: Recrear (Si NO hay datos importantes)

1. **Eliminar tabla actual**
```sql
DROP TABLE IF EXISTS detalle_recepcion;
DROP TABLE IF EXISTS recepciones_mercaderia;
```

2. **Dejar que JPA la recree**
   - Modificar entidad Java
   - Reiniciar aplicación
   - JPA creará la tabla correctamente

3. **Crear datos de prueba**

---

## 🎯 Archivos a Modificar

### Backend
- [ ] `RecepcionMercaderia.java` - Cambiar Proveedor por Cliente
- [ ] `RecepcionMercaderiaDTO.java` - Cambiar proveedorId por clienteId
- [ ] `RecepcionMercaderiaService.java` - Cambiar lógica de proveedor a cliente
- [ ] `RecepcionMercaderiaRepository.java` - Actualizar queries si las hay
- [ ] Script SQL de migración

### Frontend
- [x] `recepcion-mercaderia/page.tsx` - ✅ YA ACTUALIZADO

---

## ⚠️ Consideraciones

1. **Datos Existentes:** Si hay recepciones registradas, necesitas decidir qué hacer con ellas
2. **Foreign Keys:** Asegúrate de eliminar constraints antes de eliminar columnas
3. **Índices:** Puede haber índices en `proveedor_id` que también deben eliminarse
4. **Vistas/Procedures:** Si hay vistas o stored procedures que usan `proveedor_id`, también deben actualizarse

---

## 🧪 Testing

Después de aplicar los cambios, probar:

1. **Crear nueva recepción**
   - Seleccionar cliente
   - Agregar productos
   - Guardar
   - Verificar en BD que `cliente_id` esté correcto

2. **Listar recepciones**
   - Verificar que muestre el nombre del cliente
   - No debe mostrar errores

3. **Ver detalle de recepción**
   - Debe mostrar información del cliente correctamente

---

## 📊 Impacto

| Componente | Impacto | Acción |
|------------|---------|--------|
| Base de Datos | ALTO | Migración necesaria |
| Entidad Java | ALTO | Modificación necesaria |
| DTO | MEDIO | Modificación necesaria |
| Servicio | MEDIO | Modificación necesaria |
| Controlador | BAJO | Mensajes de validación |
| Frontend | NINGUNO | Ya actualizado ✅ |

---

## ✅ Checklist de Implementación

### Pre-implementación
- [ ] Backup de base de datos
- [ ] Documentar datos existentes
- [ ] Revisar dependencias

### Implementación
- [ ] Ejecutar script de migración SQL
- [ ] Modificar entidad RecepcionMercaderia
- [ ] Modificar DTO
- [ ] Modificar servicio
- [ ] Actualizar repositorio si es necesario
- [ ] Recompilar aplicación

### Post-implementación
- [ ] Verificar que la aplicación inicie sin errores
- [ ] Probar crear nueva recepción
- [ ] Probar listar recepciones
- [ ] Verificar datos en base de datos
- [ ] Actualizar documentación

---

## 🚀 Próximos Pasos

1. Decidir: ¿Migración o Recreación?
2. Aplicar cambios en base de datos
3. Modificar código Java
4. Probar funcionalidad completa
5. Actualizar documentación

---

¿Deseas que proceda con la implementación de estos cambios?
