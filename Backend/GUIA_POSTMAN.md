# 📮 Guía de Uso - Postman Collection

## 🚀 Inicio Rápido

### 1. Importar la Colección
1. Abrir Postman
2. Click en "Import"
3. Seleccionar el archivo `POSTMAN_COLLECTION_COMPLETA.json`
4. La colección se importará con todas las variables configuradas

### 2. Configurar Variables
La colección ya tiene configuradas las siguientes variables:
- `baseUrl`: `http://localhost:8080/api`
- `clienteId`: Se auto-completa al crear un cliente
- `productoId`: Se auto-completa al crear un producto
- `proveedorId`: Se auto-completa al crear un proveedor
- `recepcionId`: Se auto-completa al crear una recepción

## 🧪 Flujo de Prueba Recomendado

### Opción 1: Flujo Completo Automatizado
Ejecuta las peticiones en la carpeta "🧪 Flujo Completo de Prueba" en orden:

1. **Crear Proveedor** → Crea automáticamente un proveedor de prueba
2. **Listar Proveedores** → Verifica que el proveedor se creó
3. **Listar Productos** → Obtiene los productos disponibles
4. **Crear Recepción** → Registra una nueva recepción

### Opción 2: Prueba Manual Paso a Paso

#### Paso 1: Verificar Conectividad
```
GET {{baseUrl}}/proveedores/test
```
**Resultado esperado**: 
```json
{
  "success": true,
  "message": "ProveedorController funcionando correctamente",
  "timestamp": "2024-10-21T10:30:00"
}
```

#### Paso 2: Crear Proveedor de Prueba
```
POST {{baseUrl}}/proveedores/crear-prueba
```
**Resultado esperado**:
```json
{
  "success": true,
  "message": "Proveedor de prueba creado exitosamente",
  "data": {
    "id": "uuid-generado",
    "razonSocial": "Distribuidora Farmacéutica S.A.C.",
    "ruc": "20123456789",
    "contacto": "Juan Pérez",
    "telefono": "01-234-5678",
    "email": "contacto@distribuidora.com",
    "direccion": "Av. Principal 123, Lima",
    "habilitado": true
  }
}
```

**⚠️ IMPORTANTE**: Copia el `id` del proveedor y guárdalo en la variable `proveedorId`

#### Paso 3: Listar Proveedores Activos
```
GET {{baseUrl}}/proveedores/activos
```
**Resultado esperado**:
```json
{
  "success": true,
  "data": [
    {
      "id": "uuid-del-proveedor",
      "razonSocial": "Distribuidora Farmacéutica S.A.C.",
      "ruc": "20123456789",
      ...
    }
  ]
}
```

#### Paso 4: Listar Productos
```
GET {{baseUrl}}/productos?size=10
```
**Resultado esperado**:
```json
{
  "success": true,
  "data": [
    {
      "id": "uuid-del-producto",
      "nombre": "Paracetamol 500mg",
      "codigoSKU": "PAR500MG",
      ...
    }
  ]
}
```

**⚠️ IMPORTANTE**: Copia el `id` de un producto y guárdalo en la variable `productoId`

#### Paso 5: Crear Recepción de Mercadería
```
POST {{baseUrl}}/recepciones
```
**Body**:
```json
{
  "numeroOrdenCompra": "OC-2024-001",
  "numeroGuiaRemision": "GR-2024-001",
  "proveedorId": "{{proveedorId}}",
  "fechaRecepcion": "2024-10-21T10:30:00",
  "responsableRecepcion": "Juan Pérez",
  "estado": "PENDIENTE",
  "temperaturaLlegada": 22.5,
  "observaciones": "Mercadería en buen estado",
  "verificacionDocumentos": false,
  "verificacionFisica": false,
  "verificacionTemperatura": false,
  "aprobadoPorCalidad": false,
  "detalles": [
    {
      "productoId": "{{productoId}}",
      "cantidadEsperada": 100,
      "cantidadRecibida": 100,
      "conforme": true,
      "observaciones": "Producto conforme"
    }
  ]
}
```

**Resultado esperado**:
```json
{
  "success": true,
  "message": "Recepción creada exitosamente",
  "data": {
    "id": "uuid-de-recepcion",
    "numeroOrdenCompra": "OC-2024-001",
    "estado": "PENDIENTE",
    ...
  }
}
```

## 📊 Endpoints Principales

### 🏭 Proveedores
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/proveedores/test` | Verificar conectividad |
| `POST` | `/proveedores/crear-prueba` | Crear proveedor de prueba |
| `GET` | `/proveedores/activos` | Listar proveedores activos |
| `GET` | `/proveedores` | Listar todos los proveedores |

### 📦 Productos
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/productos/test` | Verificar conectividad |
| `GET` | `/productos` | Listar productos |
| `POST` | `/productos` | Crear producto |

### 👥 Clientes
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/clientes/activos` | Listar clientes activos |
| `GET` | `/clientes` | Listar todos los clientes |
| `POST` | `/clientes` | Crear cliente |

### 📥 Recepciones
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/recepciones` | Crear recepción |
| `GET` | `/recepciones` | Listar recepciones |
| `GET` | `/recepciones/{id}` | Obtener recepción por ID |
| `GET` | `/recepciones/pendientes` | Listar pendientes |
| `GET` | `/recepciones/estados` | Obtener estados disponibles |

## 🐛 Solución de Problemas

### Problema: "Proveedor no encontrado"
**Solución**: 
1. Ejecutar `POST /proveedores/crear-prueba`
2. Verificar con `GET /proveedores/activos`
3. Copiar el ID del proveedor a la variable `proveedorId`

### Problema: "Producto no encontrado"
**Solución**:
1. Ejecutar `GET /productos`
2. Si no hay productos, crear uno con `POST /productos`
3. Copiar el ID del producto a la variable `productoId`

### Problema: "Error 404"
**Solución**:
- Verificar que el backend esté corriendo en `http://localhost:8080`
- Verificar que la URL base sea correcta

### Problema: "Error 500"
**Solución**:
- Verificar los logs del backend
- Asegurarse de que la base de datos esté corriendo
- Verificar que las tablas existan

## 📝 Notas Importantes

### Variables de Colección
Las variables se auto-completan automáticamente cuando:
- Creas un proveedor → `proveedorId` se guarda
- Creas un producto → `productoId` se guarda
- Creas un cliente → `clienteId` se guarda
- Creas una recepción → `recepcionId` se guarda

### Scripts de Test
Algunas peticiones tienen scripts de test que:
- Extraen IDs de las respuestas
- Los guardan en variables de colección
- Muestran mensajes en la consola

### Estados de Recepción
Estados válidos para recepciones:
- `PENDIENTE`: Recepción registrada
- `EN_VERIFICACION`: En proceso de verificación
- `EN_CUARENTENA`: En cuarentena
- `APROBADO`: Aprobado por calidad
- `RECHAZADO`: Rechazado
- `ALMACENADO`: Productos almacenados
- `DEVUELTO`: Devuelto al proveedor

## 🎯 Ejemplo Completo para Frontend

### Datos Necesarios para el POST desde Frontend

1. **Obtener Proveedor ID**:
```javascript
const response = await fetch('http://localhost:8080/api/proveedores/activos');
const data = await response.json();
const proveedorId = data.data[0].id; // Primer proveedor
```

2. **Obtener Producto ID**:
```javascript
const response = await fetch('http://localhost:8080/api/productos?size=10');
const data = await response.json();
const productoId = data.data[0].id; // Primer producto
```

3. **Crear Recepción**:
```javascript
const recepcionData = {
  numeroOrdenCompra: "OC-2024-001",
  numeroGuiaRemision: "GR-2024-001",
  proveedorId: proveedorId, // ID obtenido en paso 1
  fechaRecepcion: "2024-10-21T10:30:00",
  responsableRecepcion: "Juan Pérez",
  estado: "PENDIENTE",
  observaciones: "Mercadería en buen estado",
  verificacionDocumentos: false,
  verificacionFisica: false,
  verificacionTemperatura: false,
  aprobadoPorCalidad: false,
  detalles: [
    {
      productoId: productoId, // ID obtenido en paso 2
      cantidadEsperada: 100,
      cantidadRecibida: 100,
      conforme: true,
      observaciones: "Conforme"
    }
  ]
};

const response = await fetch('http://localhost:8080/api/recepciones', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify(recepcionData)
});

const result = await response.json();
console.log(result);
```

## ✅ Checklist de Verificación

Antes de hacer el POST desde el frontend, verifica:

- [ ] Backend corriendo en puerto 8080
- [ ] Base de datos conectada
- [ ] Proveedor creado (ejecutar `/proveedores/crear-prueba`)
- [ ] Proveedores listándose correctamente (`/proveedores/activos`)
- [ ] Productos disponibles (`/productos`)
- [ ] IDs copiados correctamente
- [ ] Formato de fecha correcto: `YYYY-MM-DDTHH:mm:ss`
- [ ] Estado válido: `PENDIENTE`

¡Con esta colección puedes probar todas las APIs del sistema! 🚀