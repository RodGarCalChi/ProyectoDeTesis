# 🚀 Guía de API - Clientes con Productos

## 📋 Descripción
Esta guía explica cómo usar las APIs para crear clientes con sus productos y listarlos.

## 🎯 Endpoints Disponibles

### 1. Crear Cliente con Productos
**POST** `/api/clientes/crear-con-productos`

Crea un cliente y le asigna productos (existentes del catálogo y/o nuevos) en una sola operación.

#### Request Body:
```json
{
  "cliente": {
    "razonSocial": "Farmacia Nueva SAC",
    "rucDni": "20123456789",
    "direccionEntrega": "Av. Principal 456, Lima",
    "distrito": "Miraflores",
    "telefono": "01-555-1234",
    "email": "contacto@farmanueva.com",
    "tipoCliente": "FARMACIA",
    "formaPago": "CREDITO"
  },
  "productosExistentesIds": [
    "uuid-producto-1",
    "uuid-producto-2"
  ],
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

#### Response:
```json
{
  "success": true,
  "message": "Cliente creado exitosamente con sus productos",
  "data": {
    "cliente": {
      "id": "uuid-cliente",
      "razonSocial": "Farmacia Nueva SAC",
      "rucDni": "20123456789"
    },
    "totalAsignaciones": 3,
    "observaciones": "Cliente con productos mixtos"
  }
}
```

---

### 2. Asignar Productos Masivamente
**POST** `/api/clientes/asignar-productos-masivo`

Asigna múltiples productos existentes a un cliente.

#### Request Body:
```json
{
  "clienteId": "uuid-del-cliente",
  "productosIds": [
    "uuid-producto-1",
    "uuid-producto-2",
    "uuid-producto-3"
  ]
}
```

#### Response:
```json
{
  "success": true,
  "message": "Productos asignados masivamente",
  "total_productos": 5
}
```

---

### 3. Listar Clientes con sus Productos
**GET** `/api/clientes/con-productos`

Obtiene todos los clientes con sus productos asignados.

#### Response:
```json
{
  "success": true,
  "total": 3,
  "clientes": [
    {
      "id": "uuid-cliente-1",
      "razonSocial": "Farmacia Central SAC",
      "rucDni": "20123456789",
      "email": "ventas@farmaciacentral.com",
      "activo": true,
      "productos": [
        {
          "id": "uuid-producto-1",
          "codigoSKU": "MED-PAR-500",
          "nombre": "Paracetamol 500mg",
          "tipo": "MEDICAMENTO"
        },
        {
          "id": "uuid-producto-2",
          "codigoSKU": "MED-AMX-500",
          "nombre": "Amoxicilina 500mg",
          "tipo": "MEDICAMENTO"
        }
      ],
      "totalProductos": 2
    }
  ]
}
```

---

### 4. Generar Datos de Prueba
**POST** `/api/clientes/generar-datos-prueba`

Genera 3 clientes de prueba con sus productos si no hay clientes en la base de datos.

#### Response (Éxito):
```json
{
  "success": true,
  "message": "Datos de prueba generados exitosamente",
  "total_creados": 3,
  "clientes": [
    {
      "id": "uuid-1",
      "razonSocial": "Farmacia Central SAC",
      "rucDni": "20123456789",
      "totalProductos": 1
    },
    {
      "id": "uuid-2",
      "razonSocial": "Boticas del Pueblo EIRL",
      "rucDni": "20987654321",
      "totalProductos": 1
    },
    {
      "id": "uuid-3",
      "razonSocial": "Clínica San Juan SA",
      "rucDni": "20456789123",
      "totalProductos": 1
    }
  ]
}
```

#### Response (Ya existen datos):
```json
{
  "success": false,
  "message": "Ya existen clientes en la base de datos",
  "total_existentes": 5
}
```

---

## 🎨 Casos de Uso

### Caso 1: Cliente con Productos del Catálogo
```json
POST /api/clientes/crear-con-productos
{
  "cliente": { ... },
  "productosExistentesIds": ["uuid-1", "uuid-2"],
  "productosNuevos": [],
  "observaciones": "Cliente con productos del catálogo"
}
```

### Caso 2: Cliente con Productos Nuevos
```json
POST /api/clientes/crear-con-productos
{
  "cliente": { ... },
  "productosExistentesIds": [],
  "productosNuevos": [
    { "codigoSKU": "...", "nombre": "...", ... }
  ],
  "observaciones": "Cliente con productos exclusivos"
}
```

### Caso 3: Cliente con Productos Mixtos
```json
POST /api/clientes/crear-con-productos
{
  "cliente": { ... },
  "productosExistentesIds": ["uuid-1"],
  "productosNuevos": [
    { "codigoSKU": "...", "nombre": "...", ... }
  ],
  "observaciones": "Cliente con productos del catálogo y exclusivos"
}
```

---

## 🔄 Flujo Recomendado

### Opción A: Empezar desde Cero
```
1. POST /api/clientes/generar-datos-prueba
   ↓
2. GET /api/clientes/con-productos
   ↓
3. Verificar que se crearon los datos
```

### Opción B: Crear Cliente Personalizado
```
1. GET /api/productos (ver catálogo)
   ↓
2. POST /api/clientes/crear-con-productos
   ↓
3. GET /api/clientes/con-productos
```

### Opción C: Asignar Productos a Cliente Existente
```
1. GET /api/clientes (obtener ID del cliente)
   ↓
2. GET /api/productos (obtener IDs de productos)
   ↓
3. POST /api/clientes/asignar-productos-masivo
```

---

## 📊 Tipos de Cliente Disponibles
- `FARMACIA`
- `BOTICA`
- `MAYORISTA`
- `MINORISTA`
- `INSTITUCIONAL`

## 💳 Formas de Pago Disponibles
- `CONTADO`
- `CREDITO`
- `CREDITO_30_DIAS`
- `CREDITO_60_DIAS`

## 🏷️ Tipos de Producto
- `MEDICAMENTO`
- `INSUMO`
- `VACUNA`
- `DISPOSITIVO_MEDICO`

## 🌡️ Condiciones de Almacén
- `AMBIENTE` (15-30°C)
- `REFRIGERADO` (2-8°C)
- `CONGELADO` (-80 a -60°C)

---

## ⚠️ Errores Comunes

### Error: "Ya existe un cliente con el RUC/DNI"
**Causa:** El RUC/DNI ya está registrado  
**Solución:** Usa un RUC/DNI diferente o actualiza el cliente existente

### Error: "Ya existen clientes en la base de datos"
**Causa:** Intentas generar datos de prueba cuando ya hay clientes  
**Solución:** Usa `GET /api/clientes/con-productos` para ver los existentes

### Error: "Producto no encontrado"
**Causa:** El ID del producto no existe  
**Solución:** Verifica los IDs con `GET /api/productos`

---

## 🎉 Ejemplo Completo en Postman

### 1. Generar Datos de Prueba
```
POST http://localhost:8080/api/clientes/generar-datos-prueba
Headers:
  Authorization: Bearer {token}
```

### 2. Listar Clientes con Productos
```
GET http://localhost:8080/api/clientes/con-productos
Headers:
  Authorization: Bearer {token}
```

### 3. Crear Cliente Personalizado
```
POST http://localhost:8080/api/clientes/crear-con-productos
Headers:
  Authorization: Bearer {token}
  Content-Type: application/json
Body: {ver ejemplo arriba}
```

---

## 📞 Soporte

Si tienes problemas:
1. Verifica que el backend esté corriendo
2. Confirma que tienes un token JWT válido
3. Revisa los logs del servidor para más detalles
4. Usa `GET /api/clientes` para verificar que los endpoints básicos funcionan

---

**Archivo de Postman:** `POSTMAN_CLIENTES_PRODUCTOS_COMPLETE.json`  
**Referencia Rápida:** `REFERENCIA_RAPIDA_API.md`
