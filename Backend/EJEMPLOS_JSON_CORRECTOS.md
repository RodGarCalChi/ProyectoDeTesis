# 📝 Ejemplos JSON Correctos para APIs

## ❌ Errores Comunes

### Error 400 - Bad Request
Este error ocurre cuando:
- Faltan campos obligatorios
- Los tipos de datos no coinciden
- Los valores de enumeraciones son inválidos

## ✅ Ejemplos Correctos

### 1. 📦 Crear Producto

#### Campos Obligatorios:
- `codigoSKU` (String, máx 20 caracteres)
- `nombre` (String, máx 100 caracteres)
- `tipo` (Enum: TipoProducto)
- `condicionAlmacen` (Enum: CondicionAlmacen) ⚠️ **IMPORTANTE**

#### Valores Válidos para Enums:

**TipoProducto**:
- `MEDICAMENTO`
- `DISPOSITIVO_MEDICO`
- `PRODUCTO_SANITARIO`
- `REACTIVO`
- `MATERIAL_CURACION`

**CondicionAlmacen**:
- `AMBIENTE` (temperatura ambiente)
- `REFRIGERADO` (2-8°C)
- `CONGELADO` (-20°C)
- `CONTROLADO` (condiciones especiales)

#### JSON Correcto:
```json
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

#### Ejemplo Mínimo (solo campos obligatorios):
```json
{
  "codigoSKU": "IBU400MG",
  "nombre": "Ibuprofeno 400mg",
  "tipo": "MEDICAMENTO",
  "condicionAlmacen": "AMBIENTE"
}
```

---

### 2. 👥 Crear Cliente

#### Campos Obligatorios:
- `razonSocial` (String, máx 100 caracteres)
- `rucDni` (String, máx 20 caracteres)
- `direccionEntrega` (String, máx 200 caracteres) ⚠️ **IMPORTANTE**
- `distrito` (String, máx 50 caracteres)

#### JSON Correcto:
```json
{
  "razonSocial": "Farmacia Test S.A.C.",
  "rucDni": "20987654321",
  "direccionEntrega": "Av. Test 123, Lima",
  "distrito": "Lima",
  "telefono": "01-234-5678",
  "email": "contacto@farmaciatest.com",
  "tipoCliente": "FARMACIA",
  "formaPago": "CREDITO"
}
```

#### Ejemplo Mínimo (solo campos obligatorios):
```json
{
  "razonSocial": "Botica San Juan",
  "rucDni": "10123456789",
  "direccionEntrega": "Jr. Los Olivos 456",
  "distrito": "San Juan de Lurigancho"
}
```

---

### 3. 🏭 Crear Proveedor (Automático)

Este endpoint crea automáticamente un proveedor de prueba:

```
POST {{baseUrl}}/proveedores/crear-prueba
```

**No requiere body**. Respuesta:
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

---

### 4. 📥 Crear Recepción de Mercadería

#### Campos Obligatorios:
- `numeroOrdenCompra` (String, máx 50 caracteres)
- `numeroGuiaRemision` (String, máx 50 caracteres)
- `proveedorId` (UUID) ⚠️ **Debe existir en la BD**
- `fechaRecepcion` (LocalDateTime formato: `YYYY-MM-DDTHH:mm:ss`)
- `responsableRecepcion` (String, máx 100 caracteres)
- `estado` (Enum: EstadoRecepcion)

#### Valores Válidos para EstadoRecepcion:
- `PENDIENTE`
- `EN_VERIFICACION`
- `EN_CUARENTENA`
- `APROBADO`
- `RECHAZADO`
- `ALMACENADO`
- `DEVUELTO`

#### JSON Correcto:
```json
{
  "numeroOrdenCompra": "OC-2024-001",
  "numeroGuiaRemision": "GR-2024-001",
  "proveedorId": "uuid-del-proveedor",
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
      "productoId": "uuid-del-producto",
      "cantidadEsperada": 100,
      "cantidadRecibida": 100,
      "conforme": true,
      "observaciones": "Producto conforme"
    }
  ]
}
```

#### Ejemplo Mínimo (sin detalles):
```json
{
  "numeroOrdenCompra": "OC-TEST-001",
  "numeroGuiaRemision": "GR-TEST-001",
  "proveedorId": "uuid-del-proveedor",
  "fechaRecepcion": "2024-10-21T10:30:00",
  "responsableRecepcion": "Juan Pérez",
  "estado": "PENDIENTE",
  "verificacionDocumentos": false,
  "verificacionFisica": false,
  "verificacionTemperatura": false,
  "aprobadoPorCalidad": false,
  "detalles": []
}
```

---

## 🔍 Cómo Obtener IDs Necesarios

### Obtener Proveedor ID:
```
GET {{baseUrl}}/proveedores/activos
```
Respuesta:
```json
{
  "success": true,
  "data": [
    {
      "id": "copia-este-uuid",
      "razonSocial": "Distribuidora Farmacéutica S.A.C.",
      ...
    }
  ]
}
```

### Obtener Producto ID:
```
GET {{baseUrl}}/productos?size=10
```
Respuesta:
```json
{
  "success": true,
  "data": [
    {
      "id": "copia-este-uuid",
      "nombre": "Paracetamol 500mg",
      ...
    }
  ]
}
```

### Obtener Cliente ID:
```
GET {{baseUrl}}/clientes/activos
```
Respuesta:
```json
{
  "success": true,
  "data": [
    {
      "id": "copia-este-uuid",
      "razonSocial": "Farmacia Test S.A.C.",
      ...
    }
  ]
}
```

---

## 🧪 Secuencia de Prueba Recomendada

### Paso 1: Crear Proveedor
```
POST {{baseUrl}}/proveedores/crear-prueba
```
✅ Guarda el `id` del proveedor

### Paso 2: Crear Producto (si no existe)
```
POST {{baseUrl}}/productos
Body: {
  "codigoSKU": "PAR500MG",
  "nombre": "Paracetamol 500mg",
  "tipo": "MEDICAMENTO",
  "condicionAlmacen": "AMBIENTE"
}
```
✅ Guarda el `id` del producto

### Paso 3: Crear Cliente (si no existe)
```
POST {{baseUrl}}/clientes
Body: {
  "razonSocial": "Farmacia Test",
  "rucDni": "20987654321",
  "direccionEntrega": "Av. Test 123",
  "distrito": "Lima"
}
```
✅ Guarda el `id` del cliente

### Paso 4: Crear Recepción
```
POST {{baseUrl}}/recepciones
Body: {
  "numeroOrdenCompra": "OC-001",
  "numeroGuiaRemision": "GR-001",
  "proveedorId": "uuid-del-paso-1",
  "fechaRecepcion": "2024-10-21T10:30:00",
  "responsableRecepcion": "Juan Pérez",
  "estado": "PENDIENTE",
  "verificacionDocumentos": false,
  "verificacionFisica": false,
  "verificacionTemperatura": false,
  "aprobadoPorCalidad": false,
  "detalles": [
    {
      "productoId": "uuid-del-paso-2",
      "cantidadEsperada": 100,
      "cantidadRecibida": 100,
      "conforme": true
    }
  ]
}
```

---

## ⚠️ Errores Comunes y Soluciones

### Error: "El campo condicionAlmacen es obligatorio"
**Solución**: Agregar `"condicionAlmacen": "AMBIENTE"` al JSON de producto

### Error: "El campo direccionEntrega es obligatorio"
**Solución**: Cambiar `"direccion"` por `"direccionEntrega"` en el JSON de cliente

### Error: "Proveedor no encontrado"
**Solución**: Ejecutar primero `POST /proveedores/crear-prueba` y usar el ID retornado

### Error: "Invalid enum value"
**Solución**: Verificar que los valores de enums estén en MAYÚSCULAS y sean válidos

### Error: "Formato de fecha inválido"
**Solución**: Usar formato ISO: `"2024-10-21T10:30:00"` (sin Z al final)

---

## 📋 Checklist de Validación

Antes de hacer POST, verifica:

- [ ] Todos los campos obligatorios están presentes
- [ ] Los valores de enums están en MAYÚSCULAS
- [ ] Los UUIDs existen en la base de datos
- [ ] El formato de fecha es correcto
- [ ] Los strings no exceden la longitud máxima
- [ ] El email tiene formato válido (si aplica)
- [ ] Los números son del tipo correcto (Integer, Float, BigDecimal)

¡Con estos ejemplos deberías poder crear todos los registros sin errores! 🎉