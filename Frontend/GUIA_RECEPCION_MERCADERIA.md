# 📦 Guía: Recepción de Mercadería

## 🎯 Descripción

Sistema para registrar la recepción de mercadería de proveedores con todos los productos recibidos.

---

## 🚀 Cómo Usar

### Acceso al Sistema

1. Navegar a: `http://localhost:3000/recepcion-mercaderia`
2. O desde el menú principal: **"Recepción de Mercadería"**

---

## 📝 Formulario de Registro

### Sección 1: Información de Recepción

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| **Número de Documento*** | Texto | Identificador único | `REC-2024-001` |
| **Proveedor*** | Select | Proveedor que envía | `Laboratorios ABC S.A.` |
| **Fecha de Llegada*** | Fecha | Fecha de recepción | `2024-10-21` |
| **Hora de Llegada*** | Hora | Hora de recepción | `14:30` |
| **Recepcionista*** | Texto | Persona que recibe | `Juan Pérez` |

### Sección 2: Agregar Productos

| Campo | Tipo | Descripción | Ejemplo |
|-------|------|-------------|---------|
| **Producto*** | Select | Producto recibido | `PAR500MG - Paracetamol 500mg` |
| **Cantidad*** | Número | Cantidad recibida | `100` |
| **Observaciones** | Texto | Notas del producto | `Embalaje intacto` |

### Sección 3: Observaciones Generales

- Campo de texto libre para notas adicionales sobre la recepción

---

## 🔄 Flujo de Trabajo

```
1. Completar Información de Recepción
   ↓
2. Agregar Productos (uno por uno)
   - Seleccionar producto
   - Ingresar cantidad
   - Agregar observaciones (opcional)
   - Click "➕ Agregar"
   ↓
3. Revisar Lista de Productos Agregados
   ↓
4. Agregar Observaciones Generales (opcional)
   ↓
5. Click "💾 Registrar Recepción"
   ↓
6. Confirmación de Registro Exitoso
```

---

## ✅ Validaciones

### Campos Obligatorios
- ✅ Número de Documento de Recepción
- ✅ Proveedor
- ✅ Fecha de Llegada
- ✅ Hora de Llegada
- ✅ Recepcionista
- ✅ Al menos un producto

### Validaciones de Productos
- ✅ Producto debe estar seleccionado
- ✅ Cantidad debe ser mayor a 0
- ✅ No se puede agregar el mismo producto dos veces

---

## 📊 Características

### Auto-completado
- ✅ **Fecha actual** se carga automáticamente
- ✅ **Hora actual** se carga automáticamente
- ✅ **Nombre del usuario** se carga como recepcionista

### Gestión de Productos
- ✅ **Agregar múltiples productos** uno por uno
- ✅ **Ver lista** de productos agregados en tiempo real
- ✅ **Eliminar productos** de la lista antes de guardar
- ✅ **Observaciones** por producto

### Mensajes Visuales
- ✅ **Mensaje de éxito** (verde) cuando se registra correctamente
- ✅ **Mensaje de error** (rojo) si hay problemas
- ✅ **Loading state** mientras se procesa

---

## 🔌 Integración con API

### Endpoint Utilizado
```
POST /api/recepciones
```

### Datos Enviados
```json
{
  "numeroOrdenCompra": "REC-2024-001",
  "numeroGuiaRemision": "REC-2024-001",
  "proveedorId": "uuid-del-proveedor",
  "fechaRecepcion": "2024-10-21T14:30:00",
  "responsableRecepcion": "Juan Pérez",
  "estado": "PENDIENTE",
  "observaciones": "Mercadería en buen estado",
  "verificacionDocumentos": false,
  "verificacionFisica": false,
  "verificacionTemperatura": false,
  "aprobadoPorCalidad": false,
  "detalles": [
    {
      "productoId": "uuid-del-producto",
      "cantidadEsperada": 100,
      "cantidadRecibida": 100,
      "conforme": true,
      "observaciones": "Embalaje intacto"
    }
  ]
}
```

### Respuesta Exitosa
```json
{
  "success": true,
  "message": "Recepción creada exitosamente",
  "data": {
    "id": "uuid-de-la-recepcion",
    "numeroOrdenCompra": "REC-2024-001",
    ...
  }
}
```

---

## 💡 Casos de Uso

### Caso 1: Recepción Normal
```
1. Ingresar número de documento: REC-2024-001
2. Seleccionar proveedor: Laboratorios ABC
3. Confirmar fecha y hora actual
4. Agregar productos:
   - Paracetamol 500mg: 100 unidades
   - Ibuprofeno 400mg: 50 unidades
5. Observaciones: "Mercadería en buen estado"
6. Registrar
```

### Caso 2: Recepción con Observaciones
```
1. Completar datos generales
2. Agregar productos con observaciones específicas:
   - Producto 1: "Embalaje con leve daño"
   - Producto 2: "Conforme"
3. Observaciones generales: "Revisar embalaje del producto 1"
4. Registrar
```

### Caso 3: Recepción Múltiple
```
1. Completar datos generales
2. Agregar 10 productos diferentes
3. Revisar lista completa
4. Eliminar productos incorrectos si es necesario
5. Registrar
```

---

## 🎨 Interfaz de Usuario

### Colores de Estado
- **Azul:** Botones principales y acciones
- **Verde:** Mensajes de éxito y botón agregar
- **Rojo:** Mensajes de error y botón eliminar
- **Gris:** Botones secundarios

### Iconos
- 📦 Nueva Recepción
- 📋 Historial
- ➕ Agregar Producto
- 💾 Registrar Recepción
- 🔄 Limpiar Formulario

---

## ⚠️ Notas Importantes

1. **Proveedores:** Deben estar activos en el sistema
2. **Productos:** Deben existir en el catálogo
3. **Número de Documento:** Debe ser único
4. **Fecha/Hora:** Se registra en formato ISO 8601
5. **Estado Inicial:** Todas las recepciones se crean en estado "PENDIENTE"

---

## 🐛 Solución de Problemas

### Error: "No se cargan los proveedores"
**Solución:**
1. Verificar que el backend esté corriendo
2. Verificar conexión a base de datos
3. Revisar consola del navegador (F12)
4. Verificar que existan proveedores activos

### Error: "No se cargan los productos"
**Solución:**
1. Verificar que existan productos en el sistema
2. Revisar consola del navegador
3. Verificar endpoint `/api/productos`

### Error: "Error al registrar recepción"
**Solución:**
1. Verificar que todos los campos obligatorios estén completos
2. Verificar que haya al menos un producto agregado
3. Revisar consola del navegador para detalles
4. Verificar que el proveedor y productos existan

### No aparece mensaje de éxito
**Solución:**
1. Verificar respuesta del servidor en consola
2. Verificar que `result.success` sea `true`
3. Revisar logs del backend

---

## 📱 Responsive Design

- ✅ **Desktop:** Layout de 2-4 columnas
- ✅ **Tablet:** Layout adaptativo
- ✅ **Mobile:** Layout de 1 columna

---

## 🔧 Configuración Técnica

### Variables de Estado
```typescript
- formData: Datos del formulario principal
- nuevoProducto: Datos del producto a agregar
- proveedores: Lista de proveedores activos
- productos: Lista de productos disponibles
- loading: Estado de carga
- successMessage: Mensaje de éxito
- errorMessage: Mensaje de error
```

### Funciones Principales
```typescript
- cargarProveedores(): Carga proveedores desde API
- cargarProductos(): Carga productos desde API
- agregarProducto(): Agrega producto a la lista
- eliminarProducto(): Elimina producto de la lista
- handleSubmit(): Envía datos al backend
```

---

## ✅ Checklist de Prueba

### Carga Inicial
- [ ] Se cargan proveedores activos
- [ ] Se cargan productos disponibles
- [ ] Fecha actual se carga automáticamente
- [ ] Hora actual se carga automáticamente
- [ ] Nombre de usuario se carga como recepcionista

### Agregar Productos
- [ ] Se puede seleccionar un producto
- [ ] Se puede ingresar cantidad
- [ ] Se puede agregar observaciones
- [ ] Botón "Agregar" funciona
- [ ] Producto aparece en la lista
- [ ] Formulario se limpia después de agregar

### Validaciones
- [ ] No permite enviar sin proveedor
- [ ] No permite enviar sin productos
- [ ] No permite cantidad 0 o negativa
- [ ] Valida campos obligatorios

### Registro
- [ ] Botón "Registrar" funciona
- [ ] Muestra loading mientras procesa
- [ ] Muestra mensaje de éxito
- [ ] Limpia formulario después de éxito
- [ ] Muestra mensaje de error si falla

### Interfaz
- [ ] Tabs funcionan correctamente
- [ ] Botón "Limpiar" funciona
- [ ] Botón "Eliminar" producto funciona
- [ ] Mensajes son visibles y claros
- [ ] Responsive en móvil

---

## 🚀 Próximas Mejoras

- [ ] Historial de recepciones
- [ ] Búsqueda y filtros
- [ ] Exportar a PDF
- [ ] Editar recepción
- [ ] Cambiar estado de recepción
- [ ] Notificaciones en tiempo real
- [ ] Validación de stock
- [ ] Integración con código de barras

---

¡Sistema listo para registrar recepciones! 📦✅
