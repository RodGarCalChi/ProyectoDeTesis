# 🔗 Integración API - Recepción de Mercadería

## ✅ Funcionalidades Implementadas

### 📋 Formulario Simplificado de Recepción
- **Número de Documento**: Campo obligatorio para identificar la recepción
- **Cliente**: Lista desplegable que carga desde la API `/api/clientes/activos`
- **Fecha de Llegada**: Campo de fecha con valor por defecto actual
- **Hora de Llegada**: Campo de hora con valor por defecto actual
- **Recepcionista**: Campo que se auto-completa con el usuario logueado
- **Productos**: Sistema para agregar múltiples productos con cantidades
- **Observaciones**: Campo opcional para notas generales

### 🔌 Integración con APIs Backend

#### Clientes API
```typescript
// Endpoint: GET /api/clientes/activos
clientesApi.obtenerActivos()
```
- Carga automática de clientes activos al inicializar la pantalla
- Lista desplegable con formato: "Razón Social - RUC/DNI"

#### Productos API
```typescript
// Endpoint: GET /api/productos?size=100
productosApi.obtenerTodos({ size: 100 })
```
- Carga automática de productos disponibles
- Lista desplegable con formato: "Código SKU - Nombre"

#### Recepciones API
```typescript
// Endpoint: POST /api/recepciones
recepcionesApi.crear(recepcionData)
```
- Envío de datos de recepción al backend
- Mapeo automático de datos del formulario al formato esperado por la API

### 📊 Estructura de Datos

#### Datos del Formulario
```typescript
interface RecepcionFormData {
  numeroDocumentoRecepcion: string;
  clienteId: string;
  clienteNombre: string;
  fechaLlegada: string;
  horaLlegada: string;
  responsableRecepcion: string;
  productos: ProductoRecepcion[];
  observaciones: string;
}
```

#### Mapeo a API Backend
```typescript
const recepcionData = {
  numeroOrdenCompra: formData.numeroDocumentoRecepcion,
  numeroGuiaRemision: formData.numeroDocumentoRecepcion,
  proveedorId: formData.clienteId, // Cliente como proveedor
  fechaRecepcion: `${formData.fechaLlegada}T${formData.horaLlegada}:00`,
  responsableRecepcion: formData.responsableRecepcion,
  estado: 'PENDIENTE',
  observaciones: formData.observaciones,
  verificacionDocumentos: false,
  verificacionFisica: false,
  verificacionTemperatura: false,
  aprobadoPorCalidad: false,
  detalles: formData.productos.map(producto => ({
    productoId: producto.productoId,
    cantidadEsperada: producto.cantidad,
    cantidadRecibida: producto.cantidad,
    conforme: true,
    observaciones: producto.observaciones
  }))
};
```

### 🛡️ Seguridad y Autenticación
- Todas las llamadas a la API incluyen el token de autenticación
- Función `fetchWithAuth` maneja automáticamente los headers de autorización
- Manejo de errores con mensajes informativos al usuario

### 🎯 Validaciones Implementadas
- **Campos obligatorios**: Número de documento y cliente
- **Productos mínimos**: Al menos un producto debe ser agregado
- **Cantidades válidas**: Solo números positivos
- **Feedback visual**: Estados de carga y mensajes de éxito/error

### 📱 Experiencia de Usuario
- **Carga automática**: Datos se cargan al abrir la pantalla
- **Formulario dinámico**: Agregar/eliminar productos en tiempo real
- **Validación en tiempo real**: Mensajes de error inmediatos
- **Estados de carga**: Indicadores visuales durante las operaciones
- **Limpieza automática**: Formulario se resetea después del envío exitoso

## 🔄 Flujo de Trabajo

1. **Inicialización**
   - Usuario accede a la pantalla de recepción
   - Se cargan automáticamente clientes y productos desde las APIs
   - Formulario se inicializa con valores por defecto

2. **Captura de Datos**
   - Usuario completa información básica de recepción
   - Selecciona cliente de la lista desplegable
   - Agrega productos uno por uno con sus cantidades

3. **Validación**
   - Sistema valida campos obligatorios
   - Verifica que al menos un producto esté agregado
   - Confirma que las cantidades sean válidas

4. **Envío a API**
   - Datos se mapean al formato esperado por el backend
   - Se envía petición POST a `/api/recepciones`
   - Sistema maneja respuesta y muestra resultado al usuario

5. **Finalización**
   - En caso de éxito: formulario se limpia y se muestra mensaje de confirmación
   - En caso de error: se muestra mensaje específico del problema

## 🚀 Próximas Mejoras

- [ ] Implementar tab de "Historial" con lista de recepciones
- [ ] Agregar filtros y búsqueda en el historial
- [ ] Implementar edición de recepciones existentes
- [ ] Agregar validación de stock disponible
- [ ] Implementar notificaciones push para nuevas recepciones
- [ ] Agregar exportación de reportes en PDF/Excel

## 🔧 Configuración Técnica

### URLs de API
- **Base URL**: `http://localhost:8080/api`
- **Clientes**: `/clientes/activos`
- **Productos**: `/productos?size=100`
- **Recepciones**: `/recepciones`

### Autenticación
- **Método**: Bearer Token
- **Header**: `Authorization: Bearer {token}`
- **Storage**: localStorage

### Manejo de Errores
- **Conexión**: "Error al conectar con el servidor"
- **Validación**: Mensajes específicos por campo
- **API**: Mensajes del backend se muestran al usuario