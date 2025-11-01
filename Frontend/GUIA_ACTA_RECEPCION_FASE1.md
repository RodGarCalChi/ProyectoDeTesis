# 📋 Guía: Acta de Recepción - Fase 1 (Cuarentena)

## 🎯 Objetivo

Permitir al personal de recepción registrar los productos recibidos, validar cantidades y condiciones, y registrar observaciones durante el proceso de cuarentena.

## 🔄 Flujo del Proceso

```
1. Recepción registra mercadería (ya implementado)
   ↓
2. Personal accede al Acta de Recepción ← FASE 1 (ACTUAL)
   ↓
3. Agrega productos recibidos
   ↓
4. Valida cantidades (esperada vs recibida)
   ↓
5. Registra observaciones
   ↓
6. Marca como Conforme / No Conforme
   ↓
7. Guarda el acta
   ↓
8. Área Administrativa valida (Fase 2 - próxima)
```

## 📱 Pantalla: Acta de Recepción

### Acceso
- **Ruta**: `/acta-recepcion?id={recepcionId}`
- **Desde**: Historial de Recepciones → "Ver Detalles"
- **Roles**: Recepción, Director Técnico

### Secciones

#### 1. **Información General** (Solo lectura)
Muestra datos de la recepción:
- Número de orden
- Cliente
- Fecha de recepción
- Responsable
- Estado actual

#### 2. **Productos Recibidos**
Lista de productos agregados al acta con:
- Nombre del producto y SKU
- Número de lote
- Cantidad esperada
- Cantidad recibida
- Fecha de vencimiento
- Estado (Conforme/No Conforme)
- Acciones (Eliminar)

#### 3. **Formulario para Agregar Producto**
Campos:
- **Producto*** (desplegable)
- **Nº Lote*** (texto)
- **Cantidad Esperada*** (número)
- **Cantidad Recibida*** (número)
- **Fecha Vencimiento*** (fecha)
- **Temperatura °C** (número decimal, opcional)
- **Observaciones** (texto, opcional)
- **Estado*** (Conforme/No Conforme)
- **Motivo de Rechazo*** (si No Conforme)

## 🎨 Características

### ✅ Funcionalidades Implementadas

1. **Visualización de Recepción**
   - Carga automática de datos
   - Información general visible
   - Estado actual

2. **Agregar Productos**
   - Formulario dinámico
   - Validación de campos obligatorios
   - Selección de productos del catálogo
   - Registro de lote y vencimiento

3. **Validación de Conformidad**
   - Marcar como Conforme/No Conforme
   - Campo obligatorio de motivo si es No Conforme
   - Cálculo automático de cantidades aceptadas/rechazadas

4. **Registro de Observaciones**
   - Campo de texto libre
   - Registro de temperatura
   - Notas específicas por producto

5. **Gestión de Detalles**
   - Eliminar productos agregados
   - Vista en tabla
   - Indicadores visuales de estado

### 🎨 Interfaz

#### Estados Visuales
- **Conforme**: Badge verde
- **No Conforme**: Badge rojo
- **Pendiente**: Badge amarillo

#### Botones
- **+ Agregar Producto**: Azul, abre formulario
- **Agregar**: Verde, guarda el producto
- **Cancelar**: Gris, cierra formulario
- **Guardar Acta**: Azul, guarda todos los cambios
- **Eliminar**: Rojo, elimina un producto

## 📊 Datos Capturados

### Por Producto
```typescript
{
  productoId: UUID,
  loteNumero: string,
  cantidadEsperada: number,
  cantidadRecibida: number,
  cantidadAceptada: number,  // Calculado
  cantidadRechazada: number, // Calculado
  fechaVencimiento: date,
  temperaturaRecepcion: decimal (opcional),
  observaciones: string (opcional),
  conforme: boolean,
  motivoRechazo: string (si no conforme)
}
```

## 🔐 Validaciones

### Campos Obligatorios
- ✅ Producto
- ✅ Número de lote
- ✅ Cantidad esperada
- ✅ Cantidad recibida
- ✅ Fecha de vencimiento
- ✅ Estado (Conforme/No Conforme)
- ✅ Motivo de rechazo (si No Conforme)

### Reglas de Negocio
1. No se puede guardar el acta sin productos
2. Si un producto es "No Conforme", debe tener motivo
3. Las cantidades deben ser números positivos
4. La fecha de vencimiento debe ser futura
5. La temperatura es opcional pero recomendada

## 🚀 Uso

### Caso 1: Agregar Producto Conforme
1. Hacer clic en "+ Agregar Producto"
2. Seleccionar producto del desplegable
3. Ingresar número de lote
4. Ingresar cantidad esperada y recibida
5. Seleccionar fecha de vencimiento
6. (Opcional) Ingresar temperatura y observaciones
7. Dejar estado en "Conforme"
8. Hacer clic en "Agregar"

### Caso 2: Agregar Producto No Conforme
1. Hacer clic en "+ Agregar Producto"
2. Completar datos del producto
3. Cambiar estado a "No Conforme"
4. **Ingresar motivo de rechazo** (obligatorio)
5. Hacer clic en "Agregar"

### Caso 3: Eliminar Producto
1. Localizar el producto en la tabla
2. Hacer clic en "Eliminar"
3. Confirmar la eliminación

### Caso 4: Guardar Acta
1. Agregar todos los productos recibidos
2. Verificar que la información sea correcta
3. Hacer clic en "Guardar Acta"
4. Esperar confirmación

## 🔄 Estados de la Recepción

| Estado | Descripción | Acciones Disponibles |
|--------|-------------|---------------------|
| PENDIENTE | Recién creada | Agregar productos |
| EN_VERIFICACION | Con productos agregados | Editar, Guardar |
| EN_CUARENTENA | Guardada, esperando validación | Solo lectura |
| APROBADO | Validada por administración | Solo lectura |
| RECHAZADO | Rechazada | Solo lectura |

## 📋 Próximos Pasos (Fase 2)

1. **Validación Administrativa**
   - Revisar actas en cuarentena
   - Aprobar o rechazar
   - Registrar productos en inventario

2. **Generación de PDF**
   - Crear documento formal del acta
   - Incluir firma digital
   - Enviar al cliente

3. **Notificaciones**
   - Alertar al área administrativa
   - Notificar al cliente
   - Registro de auditoría

## 🛠️ Endpoints Necesarios (Backend)

### Actuales (Ya implementados)
```
GET /api/recepciones/{id}
GET /api/productos
```

### Por Implementar
```
POST /api/recepciones/{id}/detalles
PUT /api/recepciones/{id}/detalles/{detalleId}
DELETE /api/recepciones/{id}/detalles/{detalleId}
POST /api/recepciones/{id}/guardar-acta
GET /api/productos/activos
```

## 💡 Mejoras Futuras

1. **Validación en Tiempo Real**
   - Verificar stock disponible
   - Validar lotes duplicados
   - Alertas de vencimiento próximo

2. **Carga Masiva**
   - Importar desde Excel
   - Escaneo de códigos de barras
   - Integración con documentos del cliente

3. **Historial de Cambios**
   - Auditoría de modificaciones
   - Quién agregó/eliminó productos
   - Timestamp de cada acción

4. **Fotos y Documentos**
   - Adjuntar fotos de productos
   - Subir documentación
   - Evidencia de no conformidades

5. **Cálculos Automáticos**
   - Diferencias de cantidad
   - Porcentaje de conformidad
   - Valor total de la recepción

## 🐛 Manejo de Errores

### Sin Productos
- Mensaje: "No hay productos registrados"
- Acción: Botón "Agregar Producto" destacado

### Error al Guardar
- Mensaje: "Error al guardar el acta"
- Acción: Reintentar o contactar soporte

### Producto No Encontrado
- Mensaje: "No se pudo cargar la recepción"
- Acción: Volver al historial

## 📝 Notas Técnicas

- **Componente**: `Frontend/src/app/acta-recepcion/page.tsx`
- **Protección**: `ProtectedRoute`
- **Parámetro URL**: `id` (UUID de la recepción)
- **Estado Local**: React hooks (useState, useEffect)
- **Navegación**: Next.js router
- **Validación**: Cliente-side (por ahora)

## 🎓 Capacitación

### Para Personal de Recepción
1. Cómo acceder al acta desde el historial
2. Cómo agregar productos correctamente
3. Cuándo marcar como No Conforme
4. Importancia de las observaciones
5. Cómo guardar y finalizar el acta

### Para Supervisores
1. Revisar actas completadas
2. Validar conformidad de productos
3. Seguimiento de no conformidades
4. Reportes y estadísticas
