# Guía de Uso - Colección Postman PharmaFlow Completa

## 📋 Descripción
Esta colección de Postman contiene TODOS los endpoints del sistema PharmaFlow para gestión logística farmacéutica, incluyendo:
- Autenticación JWT
- Gestión de Clientes y Productos
- Proveedores
- Recepciones de Mercadería
- Actas de Recepción
- Asignación Masiva Cliente-Producto
- Flujos Completos de Prueba

## 🚀 Configuración Inicial

### 1. Importar la Colección
1. Abre Postman
2. Click en "Import"
3. Selecciona el archivo `POSTMAN_CLIENTES_PRODUCTOS_COMPLETE.json`
4. La colección se importará con todas las variables configuradas

### 2. Variables de Entorno
La colección incluye las siguientes variables que se actualizan automáticamente:

- `base_url`: http://localhost:8080/api
- `jwt_token`: Se guarda automáticamente al hacer login
- `cliente_id`: Se guarda al crear un cliente
- `producto_id`: Se guarda al crear un producto
- `proveedor_id`: Se guarda al crear un proveedor
- `recepcion_id`: Se guarda al crear una recepción
- `acta_id`: Se guarda al crear un acta

## 📝 Orden de Ejecución Recomendado

### Paso 1: Autenticación
Ejecuta primero uno de estos endpoints para obtener el token JWT:

**Login como Admin:**
```
POST {{base_url}}/auth/login
Body:
{
    "username": "admin",
    "password": "admin123"
}
```

**Login como Recepción:**
```
POST {{base_url}}/auth/login
Body:
{
    "username": "recepcion",
    "password": "recepcion123"
}
```

✅ El token se guardará automáticamente en la variable `jwt_token`

### Paso 2: Crear Clientes
Ejecuta los endpoints de creación de clientes en orden:

1. **Crear Cliente 1 - Farmacia Central**
2. **Crear Cliente 2 - Boticas del Pueblo**
3. **Crear Cliente 3 - Clínica San Juan**

✅ El ID del primer cliente se guardará en `cliente_id`

### Paso 3: Crear Productos
Ejecuta los endpoints de creación de productos:

1. **Crear Producto 1 - Paracetamol**
2. **Crear Producto 2 - Insulina**
3. **Crear Producto 3 - Alcohol en Gel**
4. **Crear Producto 4 - Amoxicilina**
5. **Crear Producto 5 - Vacuna COVID**

✅ El ID del primer producto se guardará en `producto_id`

### Paso 4: Asignar Productos a Clientes
Usa los endpoints de asignación para relacionar clientes con productos:

```
POST {{base_url}}/clientes/{{cliente_id}}/productos/{{producto_id}}
```

## 📊 Endpoints Disponibles

### 1. Autenticación
- `POST /api/auth/login` - Login de usuario (Admin o Recepción)

### 2. Gestión de Clientes
- `POST /api/clientes` - Crear cliente
- `GET /api/clientes` - Listar todos los clientes
- `GET /api/clientes/{id}` - Obtener cliente por ID
- `GET /api/clientes/buscar?razonSocial=nombre` - Buscar por nombre
- `PUT /api/clientes/{id}` - Actualizar cliente
- `DELETE /api/clientes/{id}` - Eliminar cliente

### 3. Gestión de Productos
- `POST /api/productos` - Crear producto
- `GET /api/productos` - Listar todos los productos
- `GET /api/productos/{id}` - Obtener producto por ID
- `GET /api/productos/buscar?nombre=nombre` - Buscar por nombre
- `GET /api/productos/tipo/{tipo}` - Filtrar por tipo
- `GET /api/productos/cadena-frio?requiere=true` - Productos con cadena de frío
- `PUT /api/productos/{id}` - Actualizar producto
- `DELETE /api/productos/{id}` - Eliminar producto

### 4. Relación Cliente-Producto
- `POST /api/clientes/{clienteId}/productos/{productoId}` - Asignar producto a cliente
- `GET /api/clientes/{clienteId}/productos` - Listar productos de un cliente
- `GET /api/clientes/producto/{productoId}/clientes` - Listar clientes de un producto
- `DELETE /api/clientes/{clienteId}/productos/{productoId}` - Desasignar producto

### 5. Gestión de Proveedores
- `GET /api/proveedores/test` - Test de conectividad
- `POST /api/proveedores/crear-prueba` - Crear proveedor de prueba
- `GET /api/proveedores/activos` - Listar proveedores activos
- `GET /api/proveedores` - Listar todos los proveedores

### 6. Recepciones de Mercadería
- `POST /api/recepciones` - Crear recepción
- `GET /api/recepciones` - Listar todas las recepciones
- `GET /api/recepciones/{id}` - Obtener recepción por ID
- `GET /api/recepciones/pendientes` - Recepciones pendientes
- `GET /api/recepciones/cuarentena` - Recepciones en cuarentena
- `GET /api/recepciones/buscar` - Buscar con filtros
- `POST /api/recepciones/{id}/iniciar-verificacion` - Iniciar verificación
- `POST /api/recepciones/{id}/aprobar-calidad` - Aprobar por calidad
- `GET /api/recepciones/estados` - Estados disponibles

### 7. Actas de Recepción
- `POST /api/actas-recepcion` - Crear acta
- `GET /api/actas-recepcion` - Listar todas las actas
- `GET /api/actas-recepcion/{id}` - Obtener acta por ID
- `GET /api/actas-recepcion/estados` - Estados disponibles

### 8. Búsquedas Avanzadas
- `GET /api/productos/buscar-filtros` - Búsqueda con múltiples filtros
- `GET /api/productos/estadisticas/por-tipo` - Estadísticas de productos

### 9. Asignación Masiva Cliente-Producto
- `POST /api/clientes/asignar-productos-masivo` - Asignar varios productos a un cliente
- `POST /api/clientes/crear-con-productos` - Crear cliente con productos (existentes/nuevos/mixtos)

### 10. Flujo Completo de Prueba
- Secuencia completa de 9 pasos para probar todo el sistema

## 🔐 Autenticación

Todos los endpoints (excepto login) requieren autenticación JWT. La colección está configurada para usar automáticamente el token guardado.

**Header automático:**
```
Authorization: Bearer {{jwt_token}}
```

## 📦 Ejemplos de Datos

### Crear Cliente
```json
{
    "razonSocial": "Farmacia Central SAC",
    "rucDni": "20123456789",
    "direccionEntrega": "Av. Arequipa 1234",
    "distrito": "Lima",
    "telefono": "01-4567890",
    "email": "ventas@farmaciacentral.com",
    "tipoCliente": "MAYORISTA",
    "formaPago": "CREDITO_30_DIAS"
}
```

### Crear Producto
```json
{
    "codigoSKU": "MED-PAR-500",
    "nombre": "Paracetamol 500mg",
    "tipo": "MEDICAMENTO",
    "condicionAlmacen": "AMBIENTE",
    "requiereCadenaFrio": false,
    "registroSanitario": "EE-12345-2024",
    "unidadMedida": "TABLETA",
    "vidaUtilMeses": 36,
    "tempMin": 15.0,
    "tempMax": 30.0
}
```

## 🎯 Tipos de Producto Disponibles
- `MEDICAMENTO`
- `INSUMO`
- `VACUNA`
- `DISPOSITIVO_MEDICO`

## 🌡️ Condiciones de Almacén
- `AMBIENTE` - Temperatura ambiente (15-30°C)
- `REFRIGERADO` - Refrigerado (2-8°C)
- `CONGELADO` - Congelado (-80 a -60°C)

## 💡 Tips de Uso

1. **Ejecuta el login primero** - Sin token, los demás endpoints fallarán
2. **Usa las variables** - Los IDs se guardan automáticamente
3. **Revisa las respuestas** - Los scripts automáticos guardan datos importantes
4. **Orden lógico** - Crea clientes y productos antes de asignarlos
5. **Token expira** - Si recibes error 401, vuelve a hacer login

## 🔄 Flujos Completos Disponibles

### Flujo 1: Básico (Clientes y Productos)
```
1. Login (Admin o Recepción)
   ↓
2. Crear 3 Clientes
   ↓
3. Crear 5 Productos
   ↓
4. Asignar productos a clientes
   ↓
5. Consultar productos de un cliente
   ↓
6. Buscar productos con filtros
```

### Flujo 2: Completo (Todo el Sistema)
```
1. Login
   ↓
2. Crear Proveedor
   ↓
3. Crear Cliente
   ↓
4. Crear Producto
   ↓
5. Asignar Producto a Cliente
   ↓
6. Verificar Productos del Cliente
   ↓
7. Crear Recepción de Mercadería
   ↓
8. Crear Acta de Recepción
   ↓
9. Verificar Todo Creado
```

### Flujo 3: Asignación Masiva
```
1. Crear Cliente
   ↓
2. Listar Productos Disponibles
   ↓
3. Asignar Varios Productos al Cliente
```

### Flujo 4: Cliente con Productos Nuevos
```
1. Ver Catálogo de Productos
   ↓
2. Crear Cliente con Productos Personalizados
```

## ⚠️ Errores Comunes

### Error 401 - Unauthorized
- **Causa**: Token no válido o expirado
- **Solución**: Ejecuta el endpoint de login nuevamente

### Error 404 - Not Found
- **Causa**: ID no existe o variable no configurada
- **Solución**: Verifica que las variables `cliente_id` y `producto_id` tengan valores

### Error 409 - Conflict
- **Causa**: RUC/DNI o código SKU duplicado
- **Solución**: Usa valores únicos para estos campos

### Error 400 - Bad Request
- **Causa**: Datos inválidos en el body
- **Solución**: Verifica que todos los campos requeridos estén presentes

## 📞 Soporte

Si encuentras problemas:
1. Verifica que el backend esté corriendo en `http://localhost:8080`
2. Revisa los logs del servidor
3. Confirma que la base de datos esté activa
4. Verifica que los usuarios admin/recepcion existan en la BD

## 🚀 Casos de Uso Especiales

### Crear Cliente con Productos Existentes
Usa el endpoint `POST /api/clientes/crear-con-productos` con:
```json
{
  "cliente": { ... },
  "productosExistentesIds": ["id1", "id2"],
  "productosNuevos": [],
  "observaciones": "..."
}
```

### Crear Cliente con Productos Nuevos
Usa el mismo endpoint pero con:
```json
{
  "cliente": { ... },
  "productosExistentesIds": [],
  "productosNuevos": [{ ... }, { ... }],
  "observaciones": "..."
}
```

### Crear Cliente con Productos Mixtos
Combina ambos arrays:
```json
{
  "cliente": { ... },
  "productosExistentesIds": ["id1"],
  "productosNuevos": [{ ... }],
  "observaciones": "..."
}
```

## 📈 Estadísticas y Reportes

La colección incluye endpoints para:
- Contar productos por cliente
- Estadísticas de productos por tipo
- Listar recepciones pendientes
- Listar recepciones en cuarentena
- Buscar con filtros avanzados

## 🎉 ¡Listo!

Ahora tienes acceso a TODA la API de PharmaFlow con:
- ✅ 80+ endpoints listos para usar
- ✅ Variables automáticas
- ✅ Scripts de prueba integrados
- ✅ 4 flujos completos de ejemplo
- ✅ Documentación detallada

¡Comienza con el "Flujo Completo de Prueba" para ver todo el sistema en acción!
