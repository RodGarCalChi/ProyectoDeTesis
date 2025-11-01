# 🔄 Flujo Completo: Recepción de Mercadería

## 📊 Resumen del Proceso

El proceso de recepción de mercadería se divide en **3 fases principales** con responsabilidades claramente definidas:

```
┌─────────────────────────────────────────────────────────────┐
│                    FASE 1: RECEPCIÓN                        │
│                  (Personal de Recepción)                    │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│              FASE 2: VALIDACIÓN ADMINISTRATIVA              │
│                (Área Administrativa)                        │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│            FASE 3: ASIGNACIÓN DE UBICACIONES                │
│                (Personal de Operaciones)                    │
└─────────────────────────────────────────────────────────────┘
```

---

## 🎯 FASE 1: RECEPCIÓN (Cuarentena)

### Responsable: **Personal de Recepción**

### Objetivo:
Registrar la llegada de mercadería y documentar el estado de los productos recibidos.

### Pasos:

#### 1.1 Registrar Recepción
**Pantalla**: `/recepcion-mercaderia`

**Datos a capturar**:
- Número de documento de recepción
- Cliente (dueño de la mercadería)
- Fecha y hora de llegada
- Responsable de recepción
- Observaciones generales

**Resultado**: Recepción creada con estado `PENDIENTE`

#### 1.2 Agregar Productos al Acta
**Pantalla**: `/acta-recepcion?id={recepcionId}`

**Para cada producto**:
- Seleccionar producto del catálogo
- Ingresar número de lote
- Registrar cantidades:
  - Cantidad esperada
  - Cantidad recibida
- Fecha de vencimiento
- Temperatura de recepción (opcional)
- Observaciones específicas
- **Marcar como**:
  - ✅ **Conforme**: Producto en buen estado
  - ❌ **No Conforme**: Producto con problemas (requiere motivo)

**Resultado**: Acta completada con estado `EN_CUARENTENA`

### Estados en esta fase:
- `PENDIENTE` → Recepción creada, sin productos
- `EN_VERIFICACION` → Agregando productos
- `EN_CUARENTENA` → Acta completa, esperando validación

---

## 🎯 FASE 2: VALIDACIÓN ADMINISTRATIVA

### Responsable: **Área Administrativa**

### Objetivo:
Validar el acta de recepción y registrar los productos conformes en el sistema de inventario.

### Pasos:

#### 2.1 Ver Actas Pendientes
**Pantalla**: `/validacion-actas`

**Información mostrada**:
- Lista de actas en estado `EN_CUARENTENA`
- Estadísticas (Pendientes, En Verificación, Aprobadas, Rechazadas)
- Filtros por estado

#### 2.2 Revisar Acta
**Pantalla**: `/validacion-actas/detalle?id={recepcionId}`

**Revisión incluye**:
- Información general de la recepción
- Lista de productos conformes y no conformes
- Cantidades y observaciones
- Motivos de rechazo (si aplica)

#### 2.3 Tomar Decisión

**Opción A: APROBAR**
- Validar que hay productos conformes
- Ingresar observaciones de validación (opcional)
- Hacer clic en "Aprobar y Registrar Productos"
- **Resultado**:
  - Acta cambia a estado `APROBADO`
  - Productos conformes se registran en inventario
  - Productos quedan pendientes de asignación de ubicación

**Opción B: RECHAZAR**
- Ingresar motivo del rechazo (obligatorio)
- Hacer clic en "Rechazar Acta"
- **Resultado**:
  - Acta cambia a estado `RECHAZADO`
  - Productos NO se registran
  - Se notifica al personal de recepción

### Estados en esta fase:
- `EN_CUARENTENA` → Esperando validación
- `APROBADO` → Validada, productos registrados
- `RECHAZADO` → Rechazada, no se registra

### ⚠️ Importante:
- El área administrativa **NO asigna ubicaciones físicas**
- Solo valida y registra productos en el sistema
- Las ubicaciones son responsabilidad de Operaciones

---

## 🎯 FASE 3: ASIGNACIÓN DE UBICACIONES

### Responsable: **Personal de Operaciones**

### Objetivo:
Asignar ubicaciones físicas en el almacén a los productos aprobados.

### Pasos:

#### 3.1 Ver Productos Pendientes
**Pantalla**: `/asignacion-ubicaciones`

**Información mostrada**:
- Lista de productos aprobados sin ubicación
- Estadísticas (Pendientes, Total Unidades, Clientes)
- Filtro por cliente
- Datos del producto:
  - Nombre y SKU
  - Cliente
  - Lote
  - Cantidad
  - Fecha de vencimiento
  - Número de orden

#### 3.2 Asignar Ubicación
**Pantalla**: `/asignacion-ubicaciones/asignar?id={productoId}` (próximamente)

**Datos a ingresar**:
- Ubicación en almacén (ej: A-01-05)
  - Formato: PASILLO-ESTANTE-NIVEL
- Código de barras (opcional)
- Observaciones de almacenamiento

**Resultado**: 
- Producto cambia a estado `ALMACENADO`
- Ubicación registrada en el sistema
- Producto disponible para despacho

### Estados en esta fase:
- `APROBADO` → Registrado, sin ubicación
- `ALMACENADO` → Con ubicación asignada, disponible

---

## 📋 Resumen de Responsabilidades

| Fase | Responsable | Acción Principal | Estado Inicial | Estado Final |
|------|-------------|------------------|----------------|--------------|
| 1 | Recepción | Registrar productos y conformidad | PENDIENTE | EN_CUARENTENA |
| 2 | Administrativa | Validar y registrar en inventario | EN_CUARENTENA | APROBADO/RECHAZADO |
| 3 | Operaciones | Asignar ubicación física | APROBADO | ALMACENADO |

---

## 🔐 Permisos por Rol

### Personal de Recepción
- ✅ Crear recepciones
- ✅ Agregar productos al acta
- ✅ Marcar conformidad
- ✅ Ver historial de recepciones
- ❌ Validar actas
- ❌ Asignar ubicaciones

### Área Administrativa
- ✅ Ver actas pendientes
- ✅ Validar actas
- ✅ Aprobar/Rechazar
- ✅ Registrar productos en inventario
- ❌ Crear recepciones
- ❌ Asignar ubicaciones

### Personal de Operaciones
- ✅ Ver productos pendientes de ubicación
- ✅ Asignar ubicaciones físicas
- ✅ Gestionar almacenamiento
- ❌ Crear recepciones
- ❌ Validar actas

### Director Técnico
- ✅ Acceso completo a todas las fases
- ✅ Supervisión y auditoría

---

## 📊 Estados del Producto

```
PENDIENTE
   ↓ (Recepción agrega al acta)
EN_VERIFICACION
   ↓ (Recepción completa el acta)
EN_CUARENTENA
   ↓ (Administrativa valida)
APROBADO (sin ubicación)
   ↓ (Operaciones asigna ubicación)
ALMACENADO (con ubicación)
   ↓ (Disponible para despacho)
DESPACHADO
```

---

## 🎨 Pantallas por Rol

### Recepción
1. `/recepcion-mercaderia` - Registrar nueva recepción
2. `/recepcion-mercaderia` (tab Historial) - Ver recepciones
3. `/acta-recepcion?id={id}` - Agregar productos

### Área Administrativa
1. `/validacion-actas` - Lista de actas pendientes
2. `/validacion-actas/detalle?id={id}` - Validar acta

### Operaciones
1. `/asignacion-ubicaciones` - Productos pendientes
2. `/asignacion-ubicaciones/asignar?id={id}` - Asignar ubicación

---

## 💡 Ventajas de este Flujo

### Separación de Responsabilidades
- ✅ Cada rol tiene tareas específicas
- ✅ No hay confusión de responsabilidades
- ✅ Mejor control y auditoría

### Trazabilidad
- ✅ Registro de quién hizo qué y cuándo
- ✅ Historial completo de cada producto
- ✅ Auditoría en cada fase

### Eficiencia
- ✅ Recepción puede trabajar sin esperar a Operaciones
- ✅ Administrativa valida sin necesidad de ir al almacén
- ✅ Operaciones se enfoca en ubicación física

### Flexibilidad
- ✅ Productos pueden ser validados antes de tener ubicación
- ✅ Operaciones puede priorizar según necesidad
- ✅ Mejor gestión del espacio en almacén

---

## 🔄 Flujo de Datos

```
┌──────────────┐
│  Recepción   │ → Crea acta con productos
└──────┬───────┘
       │
       ↓
┌──────────────┐
│  Base de     │ → Estado: EN_CUARENTENA
│  Datos       │
└──────┬───────┘
       │
       ↓
┌──────────────┐
│ Administrativa│ → Valida y aprueba
└──────┬───────┘
       │
       ↓
┌──────────────┐
│  Inventario  │ → Productos registrados (sin ubicación)
└──────┬───────┘   Estado: APROBADO
       │
       ↓
┌──────────────┐
│ Operaciones  │ → Asigna ubicación
└──────┬───────┘
       │
       ↓
┌──────────────┐
│  Inventario  │ → Productos con ubicación
└──────────────┘   Estado: ALMACENADO
```

---

## 📝 Próximas Mejoras

### Fase 4: Notificaciones
- Email al cliente cuando se aprueba
- Alerta a Operaciones de nuevos productos
- Notificación de productos almacenados

### Fase 5: Reportes
- Tiempo promedio por fase
- Productos más rechazados
- Eficiencia por operador
- Ocupación de almacén

### Fase 6: Optimización
- Sugerencias automáticas de ubicación
- Alertas de vencimiento próximo
- Rotación de inventario (FIFO/FEFO)
- Integración con código de barras

---

## ✅ Checklist de Implementación

### Backend Pendiente:
- [ ] Endpoint para aprobar acta
- [ ] Endpoint para rechazar acta
- [ ] Endpoint para registrar productos en inventario
- [ ] Endpoint para obtener productos sin ubicación
- [ ] Endpoint para asignar ubicación
- [ ] Actualización de estados automática

### Frontend Completado:
- [x] Pantalla de recepción
- [x] Pantalla de acta de recepción
- [x] Pantalla de validación administrativa
- [x] Pantalla de asignación de ubicaciones (lista)
- [ ] Pantalla de asignación individual (próxima)

### Documentación:
- [x] Guía de recepción
- [x] Guía de acta de recepción
- [x] Guía de validación administrativa
- [x] Flujo completo del proceso
- [ ] Manual de usuario por rol
- [ ] Guía de asignación de ubicaciones

---

## 🎓 Capacitación Recomendada

### Para Recepción:
1. Cómo registrar una recepción
2. Cómo agregar productos al acta
3. Cuándo marcar como conforme/no conforme
4. Importancia de las observaciones

### Para Administrativa:
1. Cómo revisar actas pendientes
2. Criterios de aprobación/rechazo
3. Qué hacer con productos no conformes
4. Documentación requerida

### Para Operaciones:
1. Cómo ver productos pendientes
2. Sistema de codificación de ubicaciones
3. Mejores prácticas de almacenamiento
4. Optimización de espacio

---

## 📞 Soporte

Para dudas o problemas:
1. Revisar esta documentación
2. Consultar las guías específicas por módulo
3. Contactar al administrador del sistema
4. Reportar bugs o sugerencias
