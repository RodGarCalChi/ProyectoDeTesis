# 📚 Referencia Rápida - API PharmaFlow

## 🔑 Autenticación

```bash
POST /api/auth/login
Body: { "username": "admin", "password": "admin123" }
```

**Usuarios disponibles:**
- Admin: `admin` / `admin123`
- Recepción: `recepcion` / `recepcion123`

## 📦 Clientes

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/clientes` | Crear cliente |
| GET | `/api/clientes` | Listar todos |
| GET | `/api/clientes/{id}` | Obtener por ID |
| GET | `/api/clientes/activos` | Solo activos |
| GET | `/api/clientes/buscar?razonSocial=X` | Buscar por nombre |
| PUT | `/api/clientes/{id}` | Actualizar |
| DELETE | `/api/clientes/{id}` | Eliminar |

## 🏷️ Productos

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/productos` | Crear producto |
| GET | `/api/productos` | Listar todos |
| GET | `/api/productos/{id}` | Obtener por ID |
| GET | `/api/productos/buscar?nombre=X` | Buscar por nombre |
| GET | `/api/productos/tipo/{tipo}` | Filtrar por tipo |
| GET | `/api/productos/cadena-frio?requiere=true` | Con cadena de frío |
| PUT | `/api/productos/{id}` | Actualizar |
| DELETE | `/api/productos/{id}` | Eliminar |

## 🔗 Relación Cliente-Producto

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/clientes/{clienteId}/productos/{productoId}` | Asignar producto |
| GET | `/api/clientes/{clienteId}/productos` | Productos del cliente |
| GET | `/api/clientes/producto/{productoId}/clientes` | Clientes del producto |
| DELETE | `/api/clientes/{clienteId}/productos/{productoId}` | Desasignar |

## 🚀 Asignación Masiva

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/clientes/asignar-productos-masivo` | Asignar varios productos |
| POST | `/api/clientes/crear-con-productos` | Cliente + productos |

## 🏭 Proveedores

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/proveedores/crear-prueba` | Crear proveedor test |
| GET | `/api/proveedores/activos` | Listar activos |
| GET | `/api/proveedores` | Listar todos |

## 📥 Recepciones

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/recepciones` | Crear recepción |
| GET | `/api/recepciones` | Listar todas |
| GET | `/api/recepciones/{id}` | Obtener por ID |
| GET | `/api/recepciones/pendientes` | Solo pendientes |
| GET | `/api/recepciones/cuarentena` | En cuarentena |
| POST | `/api/recepciones/{id}/iniciar-verificacion` | Iniciar verificación |
| POST | `/api/recepciones/{id}/aprobar-calidad` | Aprobar |

## 📋 Actas de Recepción

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/actas-recepcion` | Crear acta |
| GET | `/api/actas-recepcion` | Listar todas |
| GET | `/api/actas-recepcion/{id}` | Obtener por ID |
| GET | `/api/actas-recepcion/estados` | Estados disponibles |

## 📊 Tipos de Datos

### TipoProducto
- `MEDICAMENTO`
- `INSUMO`
- `VACUNA`
- `DISPOSITIVO_MEDICO`

### CondicionAlmacen
- `AMBIENTE` (15-30°C)
- `REFRIGERADO` (2-8°C)
- `CONGELADO` (-80 a -60°C)

### TipoCliente
- `FARMACIA`
- `BOTICA`
- `MAYORISTA`
- `MINORISTA`
- `INSTITUCIONAL`

### FormaPago
- `CONTADO`
- `CREDITO`
- `CREDITO_30_DIAS`
- `CREDITO_60_DIAS`

## 🎯 Ejemplos Rápidos

### Crear Cliente
```json
POST /api/clientes
{
  "razonSocial": "Farmacia Test SAC",
  "rucDni": "20123456789",
  "direccionEntrega": "Av. Test 123",
  "distrito": "Lima",
  "telefono": "01-234-5678",
  "email": "test@farmacia.com",
  "tipoCliente": "FARMACIA",
  "formaPago": "CREDITO"
}
```

### Crear Producto
```json
POST /api/productos
{
  "codigoSKU": "PAR500MG",
  "nombre": "Paracetamol 500mg",
  "tipo": "MEDICAMENTO",
  "condicionAlmacen": "AMBIENTE",
  "requiereCadenaFrio": false,
  "registroSanitario": "RS-12345",
  "unidadMedida": "TABLETA",
  "vidaUtilMeses": 36,
  "tempMin": 15.0,
  "tempMax": 30.0
}
```

### Asignar Producto a Cliente
```bash
POST /api/clientes/{clienteId}/productos/{productoId}
```

### Asignación Masiva
```json
POST /api/clientes/asignar-productos-masivo
{
  "clienteId": "uuid-cliente",
  "productosIds": ["uuid-prod-1", "uuid-prod-2", "uuid-prod-3"],
  "observaciones": "Asignación inicial"
}
```

### Crear Cliente con Productos
```json
POST /api/clientes/crear-con-productos
{
  "cliente": {
    "razonSocial": "Farmacia Nueva SAC",
    "rucDni": "20987654321",
    "direccionEntrega": "Av. Nueva 456",
    "distrito": "Miraflores",
    "telefono": "01-555-1234",
    "email": "nueva@farmacia.com",
    "tipoCliente": "FARMACIA",
    "formaPago": "CREDITO"
  },
  "productosExistentesIds": ["uuid-prod-1", "uuid-prod-2"],
  "productosNuevos": [
    {
      "codigoSKU": "AMX500MG",
      "nombre": "Amoxicilina 500mg",
      "tipo": "MEDICAMENTO",
      "condicionAlmacen": "AMBIENTE",
      "requiereCadenaFrio": false,
      "registroSanitario": "RS-AMX-500",
      "unidadMedida": "CAPSULA",
      "vidaUtilMeses": 24,
      "tempMin": 15.0,
      "tempMax": 30.0
    }
  ],
  "observaciones": "Cliente con productos mixtos"
}
```

## 🔐 Headers Requeridos

Todos los endpoints (excepto login) requieren:
```
Authorization: Bearer {jwt_token}
Content-Type: application/json
```

## 🌐 Base URL

```
http://localhost:8080/api
```

## ⚡ Tips Rápidos

1. **Siempre haz login primero** para obtener el token JWT
2. **Guarda los IDs** que retornan los endpoints de creación
3. **Usa paginación** en endpoints que listan muchos datos: `?page=0&size=10`
4. **Verifica el estado** de recepciones y actas antes de procesarlas
5. **Usa asignación masiva** para eficiencia al crear clientes con múltiples productos

## 📞 Códigos de Respuesta

- `200` - OK
- `201` - Creado exitosamente
- `400` - Datos inválidos
- `401` - No autenticado
- `404` - No encontrado
- `409` - Conflicto (duplicado)
- `500` - Error del servidor

## 🎯 Flujo Recomendado

```
1. Login → Obtener token
2. Crear Proveedor (si es necesario)
3. Crear Cliente
4. Crear Productos
5. Asignar Productos a Cliente
6. Crear Recepción de Mercadería
7. Crear Acta de Recepción
```

---

**Archivo de Postman:** `POSTMAN_CLIENTES_PRODUCTOS_COMPLETE.json`  
**Guía Completa:** `GUIA_USO_POSTMAN_CLIENTES_PRODUCTOS.md`
