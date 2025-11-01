# 📋 Guía: Validación Administrativa y Registro de Productos

## 🎯 Objetivo

Permitir al área administrativa validar las actas de recepción completadas por el personal de recepción y registrar los productos conformes en el sistema de inventario.

## 🔄 Flujo Completo del Proceso

```
FASE 1: RECEPCIÓN (Cuarentena)
1. Personal de recepción registra mercadería
2. Agrega productos al acta
3. Marca como Conforme/No Conforme
4. Guarda el acta → Estado: EN_CUARENTENA
   ↓
FASE 2: VALIDACIÓN ADMINISTRATIVA (ACTUAL)
5. Área administrativa revisa actas pendientes
6. Valida productos conformes
7. Asigna ubicaciones de almacén
8. Aprueba o rechaza el acta
9. Sistema registra productos en inventario
   ↓
RESULTADO
- Productos conformes → Inventario
- Acta aprobada → PDF generado
- Cliente notificado
```

## 📱 Pantallas Implementadas

### 1. **Lista de Actas Pendientes**
**Ruta**: `/validacion-actas`

#### Características:
- ✅ Vista de todas las actas por estado
- ✅ Filtro por estado (Pendientes, En Verificación, Aprobadas, Rechazadas)
- ✅ Estadísticas en tiempo real
- ✅ Acceso rápido a validación

#### Estadísticas Mostradas:
- **Pendientes**: Actas en cuarentena esperando validación
- **En Verificación**: Actas siendo revisadas
- **Aprobadas**: Actas validadas y productos registrados
- **Rechazadas**: Actas rechazadas con motivo

#### Información por Acta:
- Número de orden
- Cliente
- Fecha de recepción
- Responsable de recepción
- Estado actual
- Botón de acción (Validar/Ver Detalle)

### 2. **Detalle de Validación**
**Ruta**: `/validacion-actas/detalle?id={recepcionId}`

#### Secciones:

##### A. Información General (Solo lectura)
- Número de orden
- Cliente
- Fecha de recepción
- Responsable
- Estado

##### B. Estadísticas de Productos
- Total de productos
- Productos conformes
- Productos no conformes

##### C. Lista de Productos
Para cada producto muestra:
- **Información básica**:
  - Nombre y SKU
  - Número de lote
  - Fecha de vencimiento
  
- **Cantidades**:
  - Esperada
  - Recibida
  - Aceptada/Rechazada
  
- **Estado**:
  - Conforme (verde)
  - No Conforme (rojo)
  
- **Datos adicionales**:
  - Temperatura de recepción
  - Observaciones
  - Motivo de rechazo (si aplica)

##### D. Registro en Inventario (Solo productos conformes)
Campos editables:
- **Ubicación en Almacén*** (obligatorio)
  - Formato sugerido: A-01-05 (Pasillo-Estante-Nivel)
- **Código de Barras** (opcional)
  - Para identificación rápida

##### E. Observaciones de Validación
- Campo de texto libre
- Obligatorio si se rechaza el acta
- Recomendado para aprobaciones

##### F. Acciones
- **Aprobar y Registrar**: Valida y registra productos en inventario
- **Rechazar Acta**: Rechaza el acta completa
- **Cancelar**: Vuelve sin guardar cambios

## 🎨 Características de la Interfaz

### Códigos de Color

#### Estados de Acta:
- 🟠 **EN_CUARENTENA**: Naranja - Pendiente de validación
- 🔵 **EN_VERIFICACION**: Azul - En proceso de revisión
- 🟢 **APROBADO**: Verde - Validada y registrada
- 🔴 **RECHAZADO**: Rojo - Rechazada

#### Estados de Producto:
- 🟢 **Conforme**: Fondo verde claro, borde verde
- 🔴 **No Conforme**: Fondo rojo claro, borde rojo

### Validaciones

#### Antes de Aprobar:
1. ✅ Todos los productos conformes deben tener ubicación asignada
2. ✅ Las ubicaciones deben seguir un formato válido
3. ✅ Debe haber al menos un producto conforme

#### Antes de Rechazar:
1. ✅ Debe ingresar observaciones explicando el motivo
2. ✅ Confirmación obligatoria

## 🚀 Uso del Sistema

### Caso 1: Aprobar Acta y Registrar Productos

**Paso 1**: Acceder a Validación
1. Iniciar sesión como Área Administrativa o Director Técnico
2. Hacer clic en "Validación Actas" en el menú
3. Ver lista de actas pendientes

**Paso 2**: Seleccionar Acta
1. Localizar el acta a validar
2. Hacer clic en "Validar"
3. Revisar información general

**Paso 3**: Revisar Productos
1. Verificar cada producto en la lista
2. Revisar cantidades y observaciones
3. Identificar productos conformes y no conformes

**Paso 4**: Asignar Ubicaciones
Para cada producto conforme:
1. Ingresar ubicación en almacén (ej: A-01-05)
2. (Opcional) Ingresar código de barras
3. Verificar que todos tengan ubicación

**Paso 5**: Aprobar
1. Ingresar observaciones de validación (opcional pero recomendado)
2. Hacer clic en "Aprobar y Registrar en Inventario"
3. Confirmar la acción
4. Esperar confirmación

**Resultado**:
- ✅ Acta cambia a estado APROBADO
- ✅ Productos conformes se registran en inventario
- ✅ Se asignan ubicaciones de almacén
- ✅ Sistema genera PDF del acta (próximamente)
- ✅ Cliente recibe notificación (próximamente)

### Caso 2: Rechazar Acta

**Paso 1-3**: Igual que Caso 1

**Paso 4**: Identificar Problemas
1. Revisar productos no conformes
2. Verificar observaciones de recepción
3. Identificar motivos de rechazo

**Paso 5**: Rechazar
1. **Ingresar observaciones** explicando el motivo (obligatorio)
2. Hacer clic en "Rechazar Acta"
3. Confirmar la acción

**Resultado**:
- ❌ Acta cambia a estado RECHAZADO
- ❌ Productos NO se registran en inventario
- ❌ Se notifica al personal de recepción
- ❌ Se notifica al cliente

### Caso 3: Revisar Acta Ya Procesada

1. Cambiar filtro a "Aprobadas" o "Rechazadas"
2. Hacer clic en "Ver Detalle"
3. Revisar información (solo lectura)
4. No se pueden hacer cambios

## 📊 Datos Registrados en Inventario

### Por Producto Conforme:
```typescript
{
  productoId: UUID,
  clienteId: UUID,
  loteNumero: string,
  cantidad: number,
  fechaVencimiento: date,
  ubicacionAlmacen: string,
  codigoBarras: string (opcional),
  fechaIngreso: timestamp,
  responsableRegistro: string,
  recepcionId: UUID,
  estado: 'DISPONIBLE'
}
```

## 🔐 Permisos y Roles

### Acceso a Validación:
- ✅ **Área Administrativa**: Acceso completo
- ✅ **Director Técnico**: Acceso completo
- ❌ **Recepción**: Sin acceso
- ❌ **Otros roles**: Sin acceso

### Acciones Permitidas:
| Rol | Ver Lista | Validar | Aprobar | Rechazar |
|-----|-----------|---------|---------|----------|
| Área Administrativa | ✅ | ✅ | ✅ | ✅ |
| Director Técnico | ✅ | ✅ | ✅ | ✅ |
| Recepción | ❌ | ❌ | ❌ | ❌ |

## 🔄 Estados del Acta

| Estado | Descripción | Siguiente Acción |
|--------|-------------|------------------|
| PENDIENTE | Recién creada, sin productos | Agregar productos |
| EN_VERIFICACION | Con productos, en edición | Completar productos |
| EN_CUARENTENA | Completa, esperando validación | Validar |
| APROBADO | Validada y registrada | Solo consulta |
| RECHAZADO | Rechazada | Solo consulta |

## 📋 Reglas de Negocio

### Validaciones del Sistema:

1. **Ubicación de Almacén**:
   - Obligatoria para productos conformes
   - Formato recomendado: LETRA-NÚMERO-NÚMERO
   - Ejemplo: A-01-05, B-12-03

2. **Productos No Conformes**:
   - NO se registran en inventario
   - Se mantiene registro del rechazo
   - Se incluyen en el acta para auditoría

3. **Aprobación**:
   - Requiere al menos un producto conforme
   - Todos los conformes deben tener ubicación
   - Acción irreversible

4. **Rechazo**:
   - Requiere observaciones obligatorias
   - Rechaza el acta completa
   - Acción irreversible

## 🛠️ Endpoints Necesarios (Backend)

### Por Implementar:
```
POST /api/recepciones/{id}/aprobar
  Body: {
    observaciones: string,
    detalles: [{
      detalleId: UUID,
      ubicacionAlmacen: string,
      codigoBarras: string
    }]
  }

POST /api/recepciones/{id}/rechazar
  Body: {
    motivo: string
  }

POST /api/inventario/registrar-lote
  Body: {
    productoId: UUID,
    clienteId: UUID,
    loteNumero: string,
    cantidad: number,
    fechaVencimiento: date,
    ubicacionAlmacen: string,
    codigoBarras: string,
    recepcionId: UUID
  }
```

## 💡 Mejoras Futuras

### Fase 3: Funcionalidades Adicionales

1. **Generación de PDF**
   - Acta de recepción formal
   - Firma digital
   - Logo de la empresa
   - Código QR para verificación

2. **Notificaciones**
   - Email al cliente cuando se aprueba
   - Alerta al personal de recepción si se rechaza
   - Notificación a almacén de nuevos productos

3. **Auditoría**
   - Historial de cambios
   - Quién aprobó/rechazó
   - Timestamp de cada acción
   - Razones de modificaciones

4. **Reportes**
   - Actas aprobadas por período
   - Productos más rechazados
   - Tiempo promedio de validación
   - Eficiencia por validador

5. **Integración con Inventario**
   - Actualización automática de stock
   - Alertas de stock bajo
   - Sugerencias de ubicación
   - Optimización de espacio

6. **Validación Avanzada**
   - Verificación de duplicados
   - Validación de fechas de vencimiento
   - Alertas de productos próximos a vencer
   - Sugerencias de rotación

## 🐛 Manejo de Errores

### Errores Comunes:

1. **"Todos los productos conformes deben tener ubicación"**
   - Causa: Falta asignar ubicación a algún producto
   - Solución: Revisar y completar todas las ubicaciones

2. **"Debe ingresar el motivo del rechazo"**
   - Causa: Intentar rechazar sin observaciones
   - Solución: Escribir el motivo en el campo de observaciones

3. **"No se pudo cargar la recepción"**
   - Causa: ID inválido o recepción no existe
   - Solución: Verificar el ID o volver a la lista

4. **"Error al aprobar el acta"**
   - Causa: Error de conexión o validación en backend
   - Solución: Verificar conexión y reintentar

## 📝 Notas Técnicas

### Componentes:
- **Lista**: `Frontend/src/app/validacion-actas/page.tsx`
- **Detalle**: `Frontend/src/app/validacion-actas/detalle/page.tsx`
- **Protección**: `ProtectedRoute`
- **Navegación**: Integrada en `Navigation.tsx`

### Estado Local:
- React hooks (useState, useEffect)
- Gestión de formularios
- Validaciones cliente-side

### Parámetros URL:
- `id`: UUID de la recepción a validar

## 🎓 Capacitación

### Para Área Administrativa:

1. **Acceso al Sistema**
   - Cómo iniciar sesión
   - Navegación al módulo de validación

2. **Revisión de Actas**
   - Interpretar información de productos
   - Identificar conformidades y no conformidades
   - Verificar observaciones de recepción

3. **Asignación de Ubicaciones**
   - Sistema de codificación de almacén
   - Mejores prácticas de organización
   - Optimización de espacio

4. **Toma de Decisiones**
   - Cuándo aprobar un acta
   - Cuándo rechazar un acta
   - Documentación adecuada

5. **Registro en Inventario**
   - Qué datos se registran
   - Cómo se actualiza el stock
   - Trazabilidad de productos

### Para Supervisores:

1. **Monitoreo de Proceso**
   - Revisar estadísticas
   - Identificar cuellos de botella
   - Métricas de eficiencia

2. **Auditoría**
   - Revisar decisiones de validación
   - Verificar cumplimiento de procedimientos
   - Análisis de rechazos

3. **Mejora Continua**
   - Identificar patrones de problemas
   - Proponer mejoras al proceso
   - Capacitación del personal

## ✅ Checklist de Validación

Antes de aprobar un acta, verificar:

- [ ] Información general es correcta
- [ ] Todos los productos están listados
- [ ] Cantidades coinciden con documentación
- [ ] Productos conformes tienen ubicación asignada
- [ ] Ubicaciones siguen el formato correcto
- [ ] Productos no conformes tienen motivo claro
- [ ] Observaciones están completas
- [ ] No hay duplicados
- [ ] Fechas de vencimiento son válidas
- [ ] Se ingresaron observaciones de validación

## 🔗 Integración con Otros Módulos

### Módulos Relacionados:
1. **Recepción de Mercadería**: Origen de las actas
2. **Inventario**: Destino de productos aprobados
3. **Almacenamiento**: Gestión de ubicaciones
4. **Reportes**: Estadísticas y análisis
5. **Notificaciones**: Comunicación con clientes

### Flujo de Datos:
```
Recepción → Validación → Inventario → Almacenamiento → Despacho
```
