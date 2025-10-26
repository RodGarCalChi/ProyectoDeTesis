# 📋 Guía: Registro de Acta de Recepción

## 🎯 Descripción

Sistema de registro de Actas de Recepción en **dos pasos**:
1. **Paso 1:** Registrar datos generales del acta (sin productos)
2. **Paso 2:** Agregar productos uno por uno

---

## 🚀 Cómo Usar

### Acceso al Sistema

1. Navegar a: `http://localhost:3000/acta-recepcion`
2. O desde el menú principal: **"Acta de Recepción"**

---

## 📝 Paso 1: Datos Generales

### Campos Obligatorios (*)

| Campo | Descripción | Ejemplo |
|-------|-------------|---------|
| **Número de Acta*** | Identificador único del acta | `ACT-2024-1021-1430` |
| **Fecha de Recepción*** | Fecha y hora de la recepción | `2024-10-21 14:30` |
| **Cliente*** | Cliente que recibe la mercadería | `Farmacia Test S.A.C.` |
| **Responsable*** | Persona que recibe | `Juan Pérez` |

### Campos Opcionales

| Campo | Descripción | Ejemplo |
|-------|-------------|---------|
| **Lugar de Recepción** | Ubicación física | `Almacén Principal - Lima` |
| **Temperatura** | Temperatura de llegada (°C) | `22.5` |
| **Condiciones de Transporte** | Descripción del transporte | `Transporte refrigerado` |
| **Observaciones** | Notas generales | `Mercadería en buen estado` |

### Funcionalidad

- ✅ **Número de acta auto-generado** con formato: `ACT-YYYY-MMDD-HHMM`
- ✅ **Fecha actual pre-cargada**
- ✅ **Validación de campos obligatorios**
- ✅ **Selección de cliente desde lista activa**

### Resultado

Al completar el Paso 1:
- ✅ Se crea el acta en estado **BORRADOR**
- ✅ Se obtiene un **ID único** del acta
- ✅ Se avanza automáticamente al **Paso 2**

---

## 📦 Paso 2: Agregar Productos

### Información del Acta

Se muestra un resumen del acta creada:
- Número de acta
- ID del acta
- Cliente seleccionado

### Campos del Producto

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| **Producto*** | Select | Producto a agregar | `PAR500MG - Paracetamol 500mg` |
| **Lote*** | Texto | Número de lote | `L2024001` |
| **Fecha Vencimiento*** | Fecha | Fecha de vencimiento | `2025-12-15` |
| **Cantidad Declarada*** | Número | Cantidad esperada | `100` |
| **Cantidad Recibida*** | Número | Cantidad real recibida | `100` |
| **Precio Unitario*** | Decimal | Precio por unidad | `12.50` |
| **Conforme** | Checkbox | ¿Producto conforme? | `✓ Sí` |
| **Observaciones** | Texto | Notas del producto | `Sin daños` |

### Funcionalidad

- ✅ **Agregar múltiples productos** uno por uno
- ✅ **Lista de productos agregados** se muestra en tiempo real
- ✅ **Validación de cantidades** y precios
- ✅ **Indicador de conformidad** por producto

### Resultado

Por cada producto agregado:
- ✅ Se guarda en la base de datos
- ✅ Se muestra en la lista de productos agregados
- ✅ El formulario se limpia para agregar el siguiente

---

## ✅ Finalizar Registro

### Botón "Finalizar Registro"

Al hacer clic:
- ✅ Redirige a la lista de actas
- ✅ El acta queda guardada en estado **BORRADOR**
- ✅ Se puede editar posteriormente

---

## 📊 Lista de Actas

### Acceso

- URL: `http://localhost:3000/actas-recepcion-lista`
- Menú: **"Lista de Actas"**

### Información Mostrada

| Columna | Descripción |
|---------|-------------|
| **Número Acta** | Identificador del acta |
| **Cliente** | Razón social del cliente |
| **Responsable** | Persona que recibió |
| **Fecha Recepción** | Fecha y hora |
| **Estado** | BORRADOR / PENDIENTE / APROBADO / RECHAZADO |
| **Acciones** | Ver Detalles |

### Estadísticas

- 📊 **Total de Actas**
- 📝 **Borradores**
- ⏳ **Pendientes**
- ✅ **Aprobadas**

---

## 🎨 Estados del Acta

| Estado | Color | Descripción |
|--------|-------|-------------|
| **BORRADOR** | Gris | Acta en proceso de creación |
| **PENDIENTE** | Amarillo | Acta completa, pendiente de aprobación |
| **APROBADO** | Verde | Acta aprobada |
| **RECHAZADO** | Rojo | Acta rechazada |

---

## 🔄 Flujo Completo

```
1. Inicio
   ↓
2. Crear Acta (Paso 1)
   - Ingresar datos generales
   - Click "Crear Acta y Continuar"
   ↓
3. Agregar Productos (Paso 2)
   - Seleccionar producto
   - Ingresar datos del producto
   - Click "+ Agregar Producto"
   - Repetir para cada producto
   ↓
4. Finalizar
   - Click "✓ Finalizar Registro"
   ↓
5. Ver Lista de Actas
   - Acta guardada en estado BORRADOR
```

---

## 💡 Características Principales

### Validaciones
- ✅ Campos obligatorios marcados con *
- ✅ Validación de formatos (números, fechas)
- ✅ Prevención de duplicados
- ✅ Mensajes de error claros

### Experiencia de Usuario
- ✅ Indicador visual de pasos
- ✅ Auto-generación de número de acta
- ✅ Fecha actual pre-cargada
- ✅ Mensajes de éxito/error
- ✅ Loading states
- ✅ Formulario se limpia después de agregar producto

### Datos Persistentes
- ✅ Acta se guarda en Paso 1
- ✅ Cada producto se guarda inmediatamente
- ✅ No se pierde información si se cierra el navegador
- ✅ Se puede continuar editando después

---

## 🔧 APIs Utilizadas

### Paso 1: Crear Acta
```typescript
POST /api/actas-recepcion/crear-sin-productos
```

### Paso 2: Agregar Producto
```typescript
POST /api/actas-recepcion/{actaId}/agregar-producto
```

### Listar Actas
```typescript
GET /api/actas-recepcion?page=0&size=50
```

---

## 📱 Responsive Design

- ✅ **Desktop:** Layout de 2 columnas
- ✅ **Tablet:** Layout adaptativo
- ✅ **Mobile:** Layout de 1 columna

---

## 🎯 Casos de Uso

### Caso 1: Recepción Normal
```
1. Crear acta con datos generales
2. Agregar 5 productos conformes
3. Finalizar registro
4. Acta queda en BORRADOR
```

### Caso 2: Recepción con No Conformidades
```
1. Crear acta con datos generales
2. Agregar producto 1: Conforme ✓
3. Agregar producto 2: No Conforme ✗
   - Desmarcar checkbox "Conforme"
   - Agregar observaciones del problema
4. Finalizar registro
```

### Caso 3: Recepción Parcial
```
1. Crear acta
2. Agregar productos disponibles
3. Finalizar (guardar como BORRADOR)
4. Volver después para agregar más productos
```

---

## ⚠️ Notas Importantes

1. **Estado BORRADOR:** Solo se pueden agregar/eliminar productos en estado BORRADOR
2. **Número de Acta:** Debe ser único en el sistema
3. **Cliente:** Debe estar activo en el sistema
4. **Productos:** Deben existir en el catálogo

---

## 🐛 Solución de Problemas

### Error: "Ya existe un acta con ese número"
**Solución:** Cambiar el número de acta a uno único

### Error: "Cliente no encontrado"
**Solución:** Verificar que el cliente esté activo en el sistema

### Error: "Producto no encontrado"
**Solución:** Verificar que el producto exista en el catálogo

### No se cargan los clientes/productos
**Solución:** 
1. Verificar que el backend esté corriendo
2. Verificar la conexión a la base de datos
3. Revisar la consola del navegador para errores

---

## ✅ Checklist de Prueba

### Paso 1
- [ ] Se genera número de acta automático
- [ ] Se carga fecha actual
- [ ] Se cargan clientes activos
- [ ] Validación de campos obligatorios funciona
- [ ] Se crea el acta exitosamente
- [ ] Se avanza al Paso 2

### Paso 2
- [ ] Se muestra información del acta creada
- [ ] Se cargan productos disponibles
- [ ] Se puede agregar un producto
- [ ] Se muestra en la lista de productos agregados
- [ ] Se puede agregar múltiples productos
- [ ] Formulario se limpia después de agregar

### Finalización
- [ ] Botón "Finalizar" funciona
- [ ] Redirige a lista de actas
- [ ] Acta aparece en la lista
- [ ] Estado es BORRADOR
- [ ] Estadísticas se actualizan

---

## 🚀 Próximas Mejoras

- [ ] Editar productos agregados
- [ ] Eliminar productos del acta
- [ ] Cambiar estado del acta (Aprobar/Rechazar)
- [ ] Exportar acta a PDF
- [ ] Búsqueda y filtros en lista
- [ ] Paginación en lista de actas

---

¡Sistema listo para usar! 🎉
