# Guía: Generar Productos y Asociarlos a Clientes

## 📍 Endpoint

**POST** `http://localhost:8080/api/cliente-productos/generar-productos-con-clientes`

## 📋 Descripción

Este endpoint permite crear múltiples productos y asociarlos automáticamente a uno o más clientes en una sola operación.

## 🔧 Funcionalidades

- ✅ Crea múltiples productos en una sola petición
- ✅ Asocia cada producto a múltiples clientes automáticamente
- ✅ Valida que los clientes existan antes de crear productos
- ✅ Evita duplicados (verifica SKU y relaciones existentes)
- ✅ Retorna un resumen detallado con productos creados, asociaciones y errores
- ✅ Maneja errores parciales (si un producto falla, continúa con los demás)

## 📝 Estructura del JSON

### Campos Requeridos

- **productos** (array, requerido): Lista de productos a crear
  - **codigoSKU** (string, requerido): Código único del producto (máx. 20 caracteres)
  - **nombre** (string, requerido): Nombre del producto (máx. 100 caracteres)
  - **tipo** (enum, requerido): Tipo de producto. Valores: `Medicamento`, `Biologico`, `Dispositivo`, `Controlado`, `Cosmetico`, `Suplemento`
  - **condicionAlmacen** (enum, requerido): Condición de almacenamiento. Valores: `Ambiente_15_25`, `Refrigerado_2_8`, `Congelado_m20`, `ULT_m70`
  - **requiereCadenaFrio** (boolean, opcional): Si requiere cadena de frío (default: false)
  - **registroSanitario** (string, opcional): Registro sanitario (máx. 30 caracteres)
  - **unidadMedida** (string, opcional): Unidad de medida (máx. 20 caracteres)
  - **vidaUtilMeses** (integer, opcional): Vida útil en meses
  - **tempMin** (float, opcional): Temperatura mínima
  - **tempMax** (float, opcional): Temperatura máxima

- **clienteIds** (array, requerido): Lista de UUIDs de los clientes a los que se asociarán los productos
- **observaciones** (string, opcional): Observaciones para todas las asociaciones

## 📄 Ejemplo de JSON

```json
{
  "productos": [
    {
      "codigoSKU": "PAR500",
      "nombre": "Paracetamol 500mg",
      "tipo": "Medicamento",
      "condicionAlmacen": "Ambiente_15_25",
      "requiereCadenaFrio": false,
      "registroSanitario": "RS001234",
      "unidadMedida": "Tabletas",
      "vidaUtilMeses": 24,
      "tempMin": 15.0,
      "tempMax": 25.0
    },
    {
      "codigoSKU": "IBU400",
      "nombre": "Ibuprofeno 400mg",
      "tipo": "Medicamento",
      "condicionAlmacen": "Ambiente_15_25",
      "requiereCadenaFrio": false,
      "registroSanitario": "RS001235",
      "unidadMedida": "Tabletas",
      "vidaUtilMeses": 36,
      "tempMin": 15.0,
      "tempMax": 25.0
    }
  ],
  "clienteIds": [
    "550e8400-e29b-41d4-a716-446655440000",
    "660e8400-e29b-41d4-a716-446655440001"
  ],
  "observaciones": "Productos generados automáticamente"
}
```

## 🔍 Cómo Obtener los UUIDs de Clientes

### Opción 1: Usando el endpoint de clientes

**GET** `http://localhost:8080/api/clientes/activos`

Este endpoint retorna todos los clientes activos con sus UUIDs.

### Opción 2: Usando Postman o cURL

```bash
curl -X GET "http://localhost:8080/api/clientes/activos" \
  -H "Content-Type: application/json"
```

## 📤 Respuesta Exitosa (201 Created)

```json
{
  "success": true,
  "message": "Se crearon exitosamente 2 productos y 4 asociaciones",
  "data": {
    "productosCreados": [
      {
        "id": "uuid-producto-1",
        "codigoSKU": "PAR500",
        "nombre": "Paracetamol 500mg",
        "tipo": "Medicamento",
        "condicionAlmacen": "Ambiente_15_25",
        "requiereCadenaFrio": false,
        "registroSanitario": "RS001234",
        "unidadMedida": "Tabletas",
        "vidaUtilMeses": 24,
        "tempMin": 15.0,
        "tempMax": 25.0,
        "fechaCreacion": "2024-01-15T10:30:00",
        "fechaActualizacion": "2024-01-15T10:30:00"
      }
    ],
    "asociacionesCreadas": [
      {
        "id": "uuid-asociacion-1",
        "clienteId": "uuid-cliente-1",
        "clienteNombre": "Cliente ABC",
        "productoId": "uuid-producto-1",
        "productoNombre": "Paracetamol 500mg",
        "productoSku": "PAR500",
        "fechaAsignacion": "2024-01-15T10:30:00",
        "activo": true,
        "observaciones": "Productos generados automáticamente"
      }
    ],
    "totalProductosCreados": 2,
    "totalAsociacionesCreadas": 4,
    "errores": [],
    "exitoso": true,
    "mensaje": "Se crearon exitosamente 2 productos y 4 asociaciones"
  }
}
```

## ⚠️ Respuesta con Errores Parciales (206 Partial Content)

Si algunos productos fallan pero otros se crean exitosamente:

```json
{
  "success": true,
  "message": "Se crearon 2 productos y 4 asociaciones, pero hubo 1 errores",
  "data": {
    "productosCreados": [...],
    "asociacionesCreadas": [...],
    "totalProductosCreados": 2,
    "totalAsociacionesCreadas": 4,
    "errores": [
      "Producto 3 (SKU: AMX250) ya existe"
    ],
    "exitoso": false,
    "mensaje": "Se crearon 2 productos y 4 asociaciones, pero hubo 1 errores"
  }
}
```

## ❌ Errores Comunes

### 400 Bad Request - Lista vacía
```json
{
  "success": false,
  "message": "La lista de productos no puede estar vacía"
}
```

### 400 Bad Request - Cliente no encontrado
```json
{
  "success": false,
  "message": "Cliente no encontrado con ID: uuid-invalido"
}
```

### 400 Bad Request - Producto duplicado
El sistema omite productos con SKU duplicado y continúa con los demás.

## 🧪 Pruebas con cURL

```bash
curl -X POST "http://localhost:8080/api/cliente-productos/generar-productos-con-clientes" \
  -H "Content-Type: application/json" \
  -d @GENERAR_PRODUCTOS_CLIENTES.json
```

## 📝 Notas Importantes

1. **UUIDs de Clientes**: Debes reemplazar los UUIDs en el JSON con los IDs reales de tus clientes.
2. **SKU Único**: Cada producto debe tener un código SKU único. Si ya existe, se omite.
3. **Relaciones Duplicadas**: Si un producto ya está asociado a un cliente, se omite esa asociación.
4. **Transacciones**: Si todos los productos fallan, no se crea nada. Si algunos fallan, se crean los exitosos.
5. **Validaciones**: Todos los campos requeridos son validados antes de crear los productos.

## 🔗 Endpoints Relacionados

- **GET** `/api/clientes/activos` - Obtener lista de clientes activos
- **GET** `/api/productos` - Listar productos
- **GET** `/api/cliente-productos/cliente/{clienteId}/productos` - Ver productos de un cliente
- **POST** `/api/cliente-productos/asignar-masivo` - Asignar productos existentes a clientes

