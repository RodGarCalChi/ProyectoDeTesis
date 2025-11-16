# Guía: Generar Inventario Completo Jerárquico

## 📍 Endpoint

**POST** `http://localhost:8080/api/inventario/generar-completo`

## 📋 Descripción

Este endpoint genera el inventario completo de forma jerárquica para uno o más clientes, creando automáticamente toda la estructura logística:

**Jerarquía de creación:**
1. **OperadorLogistico** → Crea o busca operador logístico
2. **Almacen** → Crea un almacén para cada cliente
3. **Zona** → Crea zonas dentro de cada almacén
4. **Ubicacion** → Crea ubicaciones dentro de cada zona
5. **Producto** → Crea productos (o usa existentes)
6. **Lote** → Crea lotes para cada producto
7. **Palet** → Crea palets para cada lote
8. **InventarioCliente** → Crea registros de inventario para cada cliente

## 🔧 Funcionalidades

- ✅ Genera toda la estructura logística en una sola petición
- ✅ Crea almacenes específicos para cada cliente
- ✅ Crea zonas y ubicaciones para cada almacén
- ✅ Crea productos, lotes, palets e inventario
- ✅ Valida que los clientes existan
- ✅ Evita duplicados (busca operadores por RUC, productos por SKU, lotes por número)
- ✅ Retorna un resumen completo de todo lo creado

## 📝 Estructura del JSON

### Campos Requeridos

#### 1. clienteIds (array, requerido)
Lista de UUIDs de los clientes para los que se generará el inventario.

#### 2. operadorLogistico (objeto, requerido)
- **nombre** (string, requerido): Nombre del operador logístico
- **ruc** (string, requerido): RUC único del operador
- **direccion** (string, opcional): Dirección
- **telefono** (string, opcional): Teléfono
- **email** (string, opcional): Email

#### 3. almacen (objeto, requerido)
- **nombre** (string, requerido): Nombre del almacén
- **direccion** (string, requerido): Dirección del almacén
- **tieneAreaControlados** (boolean, opcional): Si tiene área para productos controlados

#### 4. zonas (array, requerido)
Lista de zonas a crear. Cada zona contiene:
- **nombre** (string, requerido): Nombre de la zona
- **tipo** (enum, requerido): Tipo de zona. Valores: `ULT`, `REFRIGERADO`, `SECO`, `CONGELADO`
- **ubicaciones** (array, requerido): Lista de ubicaciones dentro de la zona
  - **codigo** (string, requerido): Código de la ubicación
  - **capacidadMaxima** (integer, opcional): Capacidad máxima
  - **tempObjetivoMin** (float, opcional): Temperatura objetivo mínima
  - **tempObjetivoMax** (float, opcional): Temperatura objetivo máxima
  - **disponible** (boolean, opcional): Si está disponible

#### 5. productosInventario (array, requerido)
Lista de productos con su inventario completo. Cada producto contiene:

**Datos del Producto:**
- **codigoSKU** (string, requerido): Código SKU único
- **nombre** (string, requerido): Nombre del producto
- **tipo** (enum, requerido): Tipo de producto. Valores: `Medicamento`, `Biologico`, `Dispositivo`, `Controlado`, `Cosmetico`, `Suplemento`
- **condicionAlmacen** (enum, requerido): Condición de almacén. Valores: `Ambiente_15_25`, `Refrigerado_2_8`, `Congelado_m20`, `ULT_m70`
- **requiereCadenaFrio** (boolean, opcional): Si requiere cadena de frío
- **registroSanitario** (string, opcional): Registro sanitario
- **unidadMedida** (string, opcional): Unidad de medida
- **vidaUtilMeses** (integer, opcional): Vida útil en meses
- **tempMin** (float, opcional): Temperatura mínima
- **tempMax** (float, opcional): Temperatura máxima

**Datos del Lote:**
- **numeroLote** (string, requerido): Número único del lote
- **fechaFabricacion** (date, opcional): Fecha de fabricación (YYYY-MM-DD)
- **fechaVencimiento** (date, requerido): Fecha de vencimiento (YYYY-MM-DD)
- **cantidadInicial** (integer, requerido): Cantidad inicial del lote
- **proveedor** (string, opcional): Proveedor del lote
- **observacionesLote** (string, opcional): Observaciones del lote

**Datos del Palet:**
- **codigoPalet** (string, requerido): Código único del palet
- **capacidadMaximaPalet** (integer, opcional): Capacidad máxima del palet
- **cajasActuales** (integer, opcional): Cajas actuales (default: 0)
- **pesoMaximoKg** (float, opcional): Peso máximo en kg
- **pesoActualKg** (float, opcional): Peso actual en kg (default: 0.0)
- **observacionesPalet** (string, opcional): Observaciones del palet

**Datos del Inventario:**
- **cantidadDisponible** (integer, requerido): Cantidad disponible en inventario
- **codigoBarras** (string, opcional): Código de barras
- **temperaturaAlmacenamiento** (float, opcional): Temperatura de almacenamiento
- **observacionesInventario** (string, opcional): Observaciones del inventario

## 📄 Ejemplo de JSON Completo

```json
{
  "clienteIds": [
    "550e8400-e29b-41d4-a716-446655440000",
    "660e8400-e29b-41d4-a716-446655440001"
  ],
  "operadorLogistico": {
    "nombre": "Logística Farmacéutica S.A.",
    "ruc": "20123456789",
    "direccion": "Av. Industrial 123, Lima",
    "telefono": "01-2345678",
    "email": "contacto@logisticafarmaceutica.com"
  },
  "almacen": {
    "nombre": "Almacén Central Farmacéutico",
    "direccion": "Av. Los Frutales 456, Ate, Lima",
    "tieneAreaControlados": true
  },
  "zonas": [
    {
      "nombre": "Zona Ultra Congelada A",
      "tipo": "ULT",
      "ubicaciones": [
        {
          "codigo": "ULT-A-01-01",
          "capacidadMaxima": 50,
          "tempObjetivoMin": -70.0,
          "tempObjetivoMax": -65.0,
          "disponible": true
        }
      ]
    },
    {
      "nombre": "Zona Refrigerada B",
      "tipo": "REFRIGERADO",
      "ubicaciones": [
        {
          "codigo": "REF-B-01-01",
          "capacidadMaxima": 100,
          "tempObjetivoMin": 2.0,
          "tempObjetivoMax": 8.0,
          "disponible": true
        }
      ]
    }
  ],
  "productosInventario": [
    {
      "codigoSKU": "VAC-COVID-001",
      "nombre": "Vacuna COVID-19 Pfizer",
      "tipo": "Biologico",
      "condicionAlmacen": "ULT_m70",
      "requiereCadenaFrio": true,
      "registroSanitario": "RS-VAC-001",
      "unidadMedida": "Viales",
      "vidaUtilMeses": 12,
      "tempMin": -70.0,
      "tempMax": -65.0,
      "numeroLote": "LOTE-VAC-2025-001",
      "fechaFabricacion": "2025-01-15",
      "fechaVencimiento": "2026-01-15",
      "cantidadInicial": 10000,
      "proveedor": "Pfizer Inc.",
      "observacionesLote": "Vacuna COVID-19",
      "codigoPalet": "PAL-VAC-2025-001",
      "capacidadMaximaPalet": 20,
      "cajasActuales": 0,
      "pesoMaximoKg": 500.0,
      "pesoActualKg": 0.0,
      "observacionesPalet": "Palet para vacunas",
      "cantidadDisponible": 10000,
      "codigoBarras": "7891234567890",
      "temperaturaAlmacenamiento": -68.0,
      "observacionesInventario": "Inventario inicial"
    }
  ]
}
```

## 🔍 Cómo Obtener los UUIDs de Clientes

**GET** `http://localhost:8080/api/clientes/activos`

Este endpoint retorna todos los clientes activos con sus UUIDs.

## 📤 Respuesta Exitosa (201 Created)

```json
{
  "success": true,
  "message": "Inventario completo generado exitosamente",
  "data": {
    "operadorLogistico": {
      "id": "uuid-operador",
      "nombre": "Logística Farmacéutica S.A.",
      ...
    },
    "almacenes": [
      {
        "id": "uuid-almacen-1",
        "nombre": "Almacén Central Farmacéutico - Cliente ABC",
        ...
      }
    ],
    "zonas": {
      "uuid-almacen-1": [
        {
          "id": "uuid-zona-1",
          "nombre": "Zona Ultra Congelada A",
          ...
        }
      ]
    },
    "ubicaciones": {
      "uuid-zona-1": [
        {
          "id": "uuid-ubicacion-1",
          "codigo": "ULT-A-01-01",
          ...
        }
      ]
    },
    "productosCreados": [
      {
        "id": "uuid-producto-1",
        "codigoSKU": "VAC-COVID-001",
        "nombre": "Vacuna COVID-19 Pfizer",
        ...
      }
    ],
    "lotesCreados": [
      {
        "id": "uuid-lote-1",
        "numero": "LOTE-VAC-2025-001",
        ...
      }
    ],
    "paletsCreados": [
      {
        "id": "uuid-palet-1",
        "codigo": "PAL-VAC-2025-001",
        ...
      }
    ],
    "inventariosCreados": [
      {
        "id": "uuid-inventario-1",
        "cantidadDisponible": 10000,
        "estado": "ALMACENADO",
        ...
      }
    ],
    "totalProductos": 1,
    "totalLotes": 1,
    "totalPalets": 1,
    "totalInventarios": 2,
    "errores": [],
    "exitoso": true,
    "mensaje": "Inventario completo generado exitosamente"
  }
}
```

## ⚠️ Respuesta con Errores Parciales (206 Partial Content)

Si algunos productos fallan pero otros se crean exitosamente:

```json
{
  "success": true,
  "message": "Se generó el inventario con 1 errores",
  "data": {
    ...
    "errores": [
      "Error procesando producto Vacuna COVID-19: SKU ya existe"
    ],
    "exitoso": false
  }
}
```

## ❌ Errores Comunes

### 400 Bad Request - Cliente no encontrado
```json
{
  "success": false,
  "message": "Cliente no encontrado con ID: uuid-invalido"
}
```

### 400 Bad Request - Lista vacía
```json
{
  "success": false,
  "message": "La lista de clientes no puede estar vacía"
}
```

## 🧪 Pruebas con cURL

```bash
curl -X POST "http://localhost:8080/api/inventario/generar-completo" \
  -H "Content-Type: application/json" \
  -d @GENERAR_INVENTARIO_COMPLETO.json
```

## 📝 Notas Importantes

1. **UUIDs de Clientes**: Debes reemplazar los UUIDs en el JSON con los IDs reales de tus clientes.
2. **Operador Logístico**: Si ya existe un operador con el mismo RUC, se usará el existente.
3. **Productos**: Si un producto con el mismo SKU ya existe, se usará el existente.
4. **Lotes**: Si un lote con el mismo número ya existe, se usará el existente.
5. **Palets**: Si un palet con el mismo código ya existe, se usará el existente.
6. **Almacenes**: Se crea un almacén separado para cada cliente especificado.
7. **Zonas y Ubicaciones**: Se crean para cada almacén de cada cliente.
8. **Inventario**: Se crea un registro de inventario para cada combinación cliente-producto.

## 🔗 Endpoints Relacionados

- **GET** `/api/clientes/activos` - Obtener lista de clientes activos
- **GET** `/api/inventario/cliente/{clienteId}` - Ver inventario de un cliente
- **GET** `/api/almacenes` - Listar almacenes
- **GET** `/api/zonas` - Listar zonas
- **GET** `/api/ubicaciones` - Listar ubicaciones
- **GET** `/api/productos` - Listar productos
- **GET** `/api/palets` - Listar palets

## 📊 Jerarquía Creada

```
Cliente
  └── Almacen (por cliente)
      └── Zona
          └── Ubicacion
              └── Palet
                  └── Lote
                      └── Producto
                          └── InventarioCliente (por cliente)
```

## 🎯 Casos de Uso

1. **Inicialización del sistema**: Crear toda la estructura logística para nuevos clientes
2. **Migración de datos**: Importar inventario desde sistemas legacy
3. **Pruebas**: Generar datos de prueba completos para desarrollo
4. **Expansión**: Agregar nuevos almacenes y productos a clientes existentes



