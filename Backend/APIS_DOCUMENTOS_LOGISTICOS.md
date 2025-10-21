# 📋 APIs para Documentos Logísticos

## 🎯 Documentos Implementados

### 1. ✅ Acta de Recepción
**Endpoint Base**: `/api/actas-recepcion`

#### Funcionalidades Principales:
- ✅ Crear nueva acta de recepción
- ✅ Obtener actas con paginación y filtros
- ✅ Buscar por número, cliente, estado, responsable
- ✅ Aprobar/Rechazar actas
- ✅ Cambiar estados del documento
- ✅ Estadísticas por estado y cliente

#### Endpoints Disponibles:

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/api/actas-recepcion` | Crear nueva acta |
| `GET` | `/api/actas-recepcion` | Obtener todas las actas (paginado) |
| `GET` | `/api/actas-recepcion/{id}` | Obtener acta por ID |
| `GET` | `/api/actas-recepcion/numero/{numeroActa}` | Obtener acta por número |
| `GET` | `/api/actas-recepcion/buscar` | Buscar con filtros múltiples |
| `GET` | `/api/actas-recepcion/cliente/{clienteId}` | Actas por cliente |
| `GET` | `/api/actas-recepcion/estado/{estado}` | Actas por estado |
| `GET` | `/api/actas-recepcion/pendientes-aprobacion` | Actas pendientes |
| `PUT` | `/api/actas-recepcion/{id}` | Actualizar acta |
| `PATCH` | `/api/actas-recepcion/{id}/estado` | Cambiar estado |
| `POST` | `/api/actas-recepcion/{id}/aprobar` | Aprobar acta |
| `POST` | `/api/actas-recepcion/{id}/rechazar` | Rechazar acta |
| `GET` | `/api/actas-recepcion/estadisticas/estado` | Estadísticas por estado |
| `GET` | `/api/actas-recepcion/estadisticas/cliente` | Estadísticas por cliente |
| `GET` | `/api/actas-recepcion/estados` | Estados disponibles |

#### Estructura del DTO:
```json
{
  "id": "uuid",
  "numeroActa": "ACT-2024-001",
  "fechaRecepcion": "2024-10-15T10:30:00",
  "clienteId": "uuid",
  "clienteNombre": "Farmacia Central S.A.C.",
  "responsableRecepcion": "Juan Pérez",
  "lugarRecepcion": "Almacén Principal",
  "temperaturaLlegada": 22.5,
  "condicionesTransporte": "Transporte refrigerado",
  "observaciones": "Mercadería en buen estado",
  "estado": "PENDIENTE",
  "fechaCreacion": "2024-10-15T10:30:00",
  "creadoPor": "recepcion@pharmaflow.com",
  "detalles": [
    {
      "id": "uuid",
      "productoId": "uuid",
      "productoNombre": "Paracetamol 500mg",
      "productoSku": "PAR500MG",
      "lote": "L2024001",
      "fechaVencimiento": "2025-12-15",
      "cantidadDeclarada": 100,
      "cantidadRecibida": 100,
      "cantidadConforme": 100,
      "cantidadNoConforme": 0,
      "precioUnitario": 12.50,
      "observaciones": "Conforme",
      "conforme": true,
      "motivoNoConformidad": null
    }
  ]
}
```

### 2. 🚧 Guía de Remisión del Cliente
**Endpoint Base**: `/api/guias-remision-cliente` (En desarrollo)

#### Funcionalidades Planificadas:
- Crear guía de remisión del cliente
- Registrar datos de transporte y destinatario
- Detalles de productos a trasladar
- Control de estados del documento

### 3. 🚧 Movimiento de Mercadería
**Endpoint Base**: `/api/movimientos-mercaderia` (En desarrollo)

#### Funcionalidades Planificadas:
- Registrar movimientos internos
- Entrada y salida de productos
- Transferencias entre almacenes
- Trazabilidad de movimientos

### 4. 🚧 Guía de Remisión del Operador Logístico
**Endpoint Base**: `/api/guias-remision-operador` (En desarrollo)

#### Funcionalidades Planificadas:
- Generar guías para despacho
- Datos del transportista y vehículo
- Ruta y destino de entrega
- Seguimiento de entregas

## 🔧 Estados de Documentos

```java
public enum EstadoDocumento {
    BORRADOR("Borrador"),
    PENDIENTE("Pendiente"),
    PROCESADO("Procesado"),
    APROBADO("Aprobado"),
    RECHAZADO("Rechazado"),
    ANULADO("Anulado");
}
```

## 🔧 Tipos de Documentos

```java
public enum TipoDocumento {
    ACTA_RECEPCION("Acta de Recepción"),
    GUIA_REMISION_CLIENTE("Guía de Remisión del Cliente"),
    MOVIMIENTO_MERCADERIA("Movimiento de Mercadería"),
    GUIA_REMISION_OPERADOR("Guía de Remisión del Operador Logístico");
}
```

## 📊 Características Técnicas

### ✅ Implementado en Acta de Recepción:
- **Validaciones**: Jakarta Validation con mensajes personalizados
- **Paginación**: Soporte completo con ordenamiento
- **Filtros**: Búsqueda por múltiples criterios
- **Auditoría**: Campos de creación y actualización automáticos
- **Relaciones**: JPA con Cliente y Producto
- **Transacciones**: Manejo transaccional completo
- **DTOs**: Separación clara entre entidades y DTOs
- **Manejo de Errores**: Respuestas consistentes con códigos HTTP apropiados

### 🔄 Flujo de Estados:
```
BORRADOR → PENDIENTE → PROCESADO → APROBADO
    ↓           ↓           ↓
  ANULADO   RECHAZADO   RECHAZADO
```

## 🧪 Ejemplos de Uso

### Crear Acta de Recepción:
```bash
curl -X POST http://localhost:8080/api/actas-recepcion \
  -H "Content-Type: application/json" \
  -d '{
    "numeroActa": "ACT-2024-001",
    "fechaRecepcion": "2024-10-15T10:30:00",
    "clienteId": "cliente-uuid",
    "responsableRecepcion": "Juan Pérez",
    "lugarRecepcion": "Almacén Principal",
    "temperaturaLlegada": 22.5,
    "condicionesTransporte": "Transporte refrigerado",
    "observaciones": "Mercadería en buen estado",
    "estado": "PENDIENTE",
    "creadoPor": "recepcion@pharmaflow.com",
    "detalles": [...]
  }'
```

### Buscar Actas con Filtros:
```bash
curl "http://localhost:8080/api/actas-recepcion/buscar?clienteId=uuid&estado=PENDIENTE&page=0&size=10"
```

### Aprobar Acta:
```bash
curl -X POST http://localhost:8080/api/actas-recepcion/{id}/aprobar \
  -H "Content-Type: application/json" \
  -d '{"aprobadoPor": "supervisor@pharmaflow.com"}'
```

## 🚀 Próximos Pasos

1. **Completar Guía de Remisión del Cliente**
   - Crear entidades y DTOs
   - Implementar servicios y controladores
   - Agregar validaciones específicas

2. **Implementar Movimiento de Mercadería**
   - Diseñar flujo de movimientos internos
   - Integrar con sistema de inventario
   - Crear reportes de trazabilidad

3. **Desarrollar Guía de Remisión del Operador**
   - Integrar con sistema de despacho
   - Agregar seguimiento GPS (futuro)
   - Notificaciones automáticas

4. **Mejoras Generales**
   - Agregar autenticación JWT
   - Implementar cache con Redis
   - Crear documentación Swagger
   - Agregar tests unitarios e integración

## 📋 Base de Datos

### Tablas Creadas:
- ✅ `actas_recepcion`
- ✅ `detalles_acta_recepcion`
- 🚧 `guias_remision_cliente`
- 🚧 `detalles_guia_remision_cliente`
- 🚧 `movimientos_mercaderia`
- 🚧 `guias_remision_operador`

### Relaciones:
- ActaRecepcion → Cliente (ManyToOne)
- ActaRecepcion → DetalleActaRecepcion (OneToMany)
- DetalleActaRecepcion → Producto (ManyToOne)

¡La API de Acta de Recepción está completamente funcional y lista para usar! 🎉