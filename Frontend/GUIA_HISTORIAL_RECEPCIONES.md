# 📋 Guía: Historial de Recepciones

## 🎯 Descripción

Pantalla para consultar y filtrar el historial completo de recepciones de mercadería registradas en el sistema.

## 🔑 Características Principales

### 1. **Visualización por Defecto**
- Muestra las recepciones del día actual
- Ordenadas por fecha de recepción (más recientes primero)
- Vista en tabla con información clave

### 2. **Filtros Disponibles**

#### Fecha
- **Fecha Inicio**: Filtrar desde una fecha específica
- **Fecha Fin**: Filtrar hasta una fecha específica
- Por defecto: Día actual

#### Estado
- Todos los estados
- Pendiente
- En Verificación
- En Cuarentena
- Aprobado
- Rechazado
- Almacenado
- Devuelto

#### Cliente
- Filtrar por cliente específico
- Lista desplegable con clientes activos

#### Número de Orden
- Búsqueda por texto
- Busca coincidencias parciales

### 3. **Información Mostrada**

Cada recepción muestra:
- **Nº Orden**: Número de orden de compra
- **Cliente**: Nombre del cliente
- **Fecha Recepción**: Fecha y hora de recepción
- **Responsable**: Persona que registró la recepción
- **Estado**: Estado actual con código de colores
- **Acciones**: Botón para ver detalles

### 4. **Códigos de Color por Estado**

| Estado | Color |
|--------|-------|
| Pendiente | Amarillo |
| En Verificación | Azul |
| En Cuarentena | Naranja |
| Aprobado | Verde |
| Rechazado | Rojo |
| Almacenado | Morado |
| Devuelto | Gris |

## 🚀 Uso

### Consultar Recepciones del Día
1. Acceder a "Historial" desde el menú de navegación
2. Por defecto muestra las recepciones del día actual
3. Revisar la tabla con los resultados

### Buscar por Rango de Fechas
1. Seleccionar "Fecha Inicio"
2. Seleccionar "Fecha Fin"
3. Hacer clic en "Buscar"

### Filtrar por Estado
1. Seleccionar el estado deseado del desplegable
2. Hacer clic en "Buscar"

### Filtrar por Cliente
1. Seleccionar el cliente del desplegable
2. Hacer clic en "Buscar"

### Buscar por Número de Orden
1. Escribir el número (o parte) en el campo
2. Hacer clic en "Buscar"

### Combinar Filtros
- Puedes usar múltiples filtros simultáneamente
- Ejemplo: Buscar recepciones de un cliente específico en un rango de fechas con estado "Aprobado"

### Limpiar Filtros
- Hacer clic en "Limpiar Filtros"
- Vuelve a mostrar las recepciones del día actual

### Ver Detalles de una Recepción
1. Hacer clic en "Ver Detalles" en la fila deseada
2. Redirige al Acta de Recepción con todos los detalles

## 📊 Información Adicional

### Resumen
- En la parte inferior se muestra el total de recepciones encontradas

### Ordenamiento
- Las recepciones se ordenan por fecha de recepción descendente (más recientes primero)

### Paginación
- Actualmente muestra hasta 100 recepciones
- Se puede extender con paginación si es necesario

## 🔐 Permisos

### Roles con Acceso
- **Recepción**: Acceso completo
- **Director Técnico**: Acceso completo

## 🛠️ Endpoints Utilizados

### Backend
```
GET /api/recepciones?page=0&size=100&sortBy=fechaRecepcion&sortDir=desc
GET /api/recepciones/buscar?numeroOrden=...&estado=...&clienteId=...
GET /api/clientes/activos
```

## 📱 Responsive

- La tabla es responsive con scroll horizontal en pantallas pequeñas
- Los filtros se adaptan a diferentes tamaños de pantalla

## 🎨 Interfaz

### Colores
- Fondo: Gris claro (#F9FAFB)
- Tarjetas: Blanco
- Botón principal: Azul (#2563EB)
- Botón secundario: Gris (#E5E7EB)

### Tipografía
- Título: 3xl, bold
- Subtítulos: lg, semibold
- Texto: sm, regular

## 🔄 Flujo de Trabajo

```
1. Usuario accede a Historial
   ↓
2. Sistema carga recepciones del día
   ↓
3. Usuario aplica filtros (opcional)
   ↓
4. Sistema muestra resultados filtrados
   ↓
5. Usuario hace clic en "Ver Detalles"
   ↓
6. Sistema redirige al Acta de Recepción
```

## 💡 Casos de Uso

### Caso 1: Revisar Recepciones del Día
**Actor**: Supervisor de Recepción
**Objetivo**: Ver todas las recepciones registradas hoy
**Pasos**:
1. Acceder a Historial
2. Revisar la tabla (ya muestra el día actual)

### Caso 2: Buscar Recepción Específica
**Actor**: Personal de Recepción
**Objetivo**: Encontrar una recepción por número de orden
**Pasos**:
1. Acceder a Historial
2. Escribir número de orden en el filtro
3. Hacer clic en "Buscar"
4. Hacer clic en "Ver Detalles"

### Caso 3: Auditoría por Cliente
**Actor**: Director Técnico
**Objetivo**: Revisar todas las recepciones de un cliente en un mes
**Pasos**:
1. Acceder a Historial
2. Seleccionar fecha inicio (primer día del mes)
3. Seleccionar fecha fin (último día del mes)
4. Seleccionar el cliente
5. Hacer clic en "Buscar"
6. Revisar resultados

### Caso 4: Seguimiento de Recepciones Pendientes
**Actor**: Supervisor de Calidad
**Objetivo**: Ver todas las recepciones pendientes de aprobación
**Pasos**:
1. Acceder a Historial
2. Seleccionar estado "En Verificación"
3. Hacer clic en "Buscar"
4. Revisar recepciones que requieren atención

## 🐛 Manejo de Errores

### Sin Resultados
- Muestra mensaje: "No se encontraron recepciones para los filtros seleccionados"

### Error de Conexión
- Muestra mensaje de error en banner rojo
- Sugiere verificar que el backend esté corriendo

### Carga
- Muestra spinner animado mientras carga datos

## 🔮 Mejoras Futuras

1. **Exportar a Excel**: Descargar resultados filtrados
2. **Paginación**: Para manejar grandes volúmenes de datos
3. **Filtros Avanzados**: Por responsable, temperatura, etc.
4. **Gráficos**: Estadísticas visuales de recepciones
5. **Búsqueda en Tiempo Real**: Filtrado automático al escribir
6. **Ordenamiento por Columna**: Click en headers para ordenar
7. **Vista de Detalles Rápida**: Modal con información sin cambiar de página

## 📝 Notas Técnicas

- Componente: `Frontend/src/app/historial-recepciones/page.tsx`
- Protegido con `ProtectedRoute`
- Usa hooks de React para estado y efectos
- Integrado con API de recepciones y clientes
- Formato de fechas: ISO 8601 para backend, localizado para UI
