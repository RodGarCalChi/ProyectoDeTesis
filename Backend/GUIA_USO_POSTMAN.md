# 📮 Guía de Uso: Colección Postman - Flujo Completo

## 🎯 Objetivo

Esta guía te ayudará a probar el flujo completo de Recepción → Inventario → Almacenamiento usando Postman.

---

## 📋 Prerequisitos

### 1. **Base de Datos**
Ejecutar el script SQL para crear las tablas:
```bash
mysql -u usuario -p BaseDeDatos < Backend/SQL_CREAR_TABLAS_INVENTARIO.sql
```

### 2. **Backend**
Asegurarse de que el backend esté corriendo:
```bash
cd Backend
./gradlew bootRun
```

### 3. **Postman**
- Tener Postman instalado
- Importar la colección: `Backend/POSTMAN_FLUJO_RECEPCION_INVENTARIO.json`

---

## 🚀 Paso a Paso

### PASO 0: Configurar Variables

Antes de empezar, necesitas obtener los IDs de:
- Cliente
- Producto
- Lote
- Ubicación

**Ejecuta estos requests en la carpeta "CONSULTAS":**

1. **Clientes Activos** → Copia un `id` de cliente
2. **Productos** → Copia un `id` de producto
3. **Ubicaciones** → Copia un `id` de ubicación

**Actualiza las variables de entorno en Postman:**
- `clienteId`: ID del cliente
- `productoId`: ID del producto
- `ubicacionId`: ID de la ubicación

---

### FASE 1: RECEPCIÓN (Personal de Recepción)

#### 1.1 Crear Recepción
**Request**: `POST /api/recepciones`

**Body**:
```json
{
  "numeroOrdenCompra": "ORD-2024-001",
  "numeroGuiaRemision": "GR-2024-001",
  "clienteId": "{{clienteId}}",
  "fechaRecepcion": "2024-10-27T10:30:00",
  "responsableRecepcion": "Juan Pérez",
  "estado": "PENDIENTE",
  "temperaturaLlegada": 5.5,
  "observaciones": "Mercadería llegó en buen estado",
  "detalles": []
}
```

**Resultado**: Guarda el `id` de la recepción creada en la variable `recepcionId`

#### 1.2 Agregar Producto Conforme
**Request**: `POST /api/recepciones/{{recepcionId}}/detalles`

**Body**:
```json
{
  "productoId": "{{productoId}}",
  "loteId": "{{loteId}}",
  "cantidadEsperada": 100,
  "cantidadRecibida": 100,
  "cantidadAceptada": 100,
  "cantidadRechazada": 0,
  "fechaVencimiento": "2025-12-31",
  "temperaturaRecepcion": 5.0,
  "observaciones": "Producto en perfecto estado",
  "conforme": true
}
```

#### 1.3 Agregar Producto No Conforme (Opcional)
**Request**: `POST /api/recepciones/{{recepcionId}}/detalles`

**Body**:
```json
{
  "productoId": "{{productoId2}}",
  "loteId": "{{loteId2}}",
  "cantidadEsperada": 50,
  "cantidadRecibida": 50,
  "cantidadAceptada": 0,
  "cantidadRechazada": 50,
  "fechaVencimiento": "2025-06-30",
  "temperaturaRecepcion": 12.0,
  "observaciones": "Temperatura fuera de rango",
  "conforme": false,
  "motivoRechazo": "Temperatura de transporte inadecuada"
}
```

#### 1.4 Completar Acta
**Request**: `PUT /api/recepciones/{{recepcionId}}/estado`

**Body**:
```json
{
  "estado": "EN_CUARENTENA"
}
```

**Resultado**: La recepción ahora está lista para validación administrativa

---

### FASE 2: VALIDACIÓN ADMINISTRATIVA (Área Administrativa)

#### 2.1 Listar Actas Pendientes
**Request**: `GET /api/recepciones?estado=EN_CUARENTENA`

**Resultado**: Ver todas las actas pendientes de validación

#### 2.2 Aprobar Acta y Registrar en Inventario ⭐
**Request**: `POST /api/inventario/aprobar-acta/{{recepcionId}}`

**Body**:
```json
{
  "usuarioNombre": "admin@example.com",
  "observaciones": "Acta revisada y aprobada. Productos conformes registrados en inventario."
}
```

**Nota**: `usuarioNombre` debe ser el **email** del usuario registrado en el sistema.

**Resultado**: 
- Acta cambia a estado `APROBADO`
- Productos conformes se registran en `inventario_cliente`
- Estado de inventario: `PENDIENTE_UBICACION`
- Guarda el `id` del inventario en la variable `inventarioId`

**Respuesta esperada**:
```json
{
  "success": true,
  "message": "Acta aprobada y 1 producto(s) registrado(s) en inventario",
  "data": [
    {
      "id": "...",
      "clienteNombre": "...",
      "productoNombre": "...",
      "cantidadDisponible": 100,
      "estado": "PENDIENTE_UBICACION",
      ...
    }
  ]
}
```

---

### FASE 3: ASIGNACIÓN DE UBICACIONES (Personal de Operaciones)

#### 3.1 Listar Productos Pendientes de Ubicación
**Request**: `GET /api/inventario/pendientes-ubicacion`

**Resultado**: Ver todos los productos que necesitan ubicación física

#### 3.2 Asignar Ubicación a Producto ⭐
**Request**: `PUT /api/inventario/{{inventarioId}}/asignar-ubicacion`

**Body**:
```json
{
  "ubicacionId": "{{ubicacionId}}",
  "codigoBarras": "7501234567890",
  "usuarioNombre": "operador1@example.com",
  "observaciones": "Producto almacenado en zona refrigerada"
}
```

**Nota**: `usuarioNombre` debe ser el **email** del usuario registrado en el sistema.

**Resultado**:
- Inventario actualizado con ubicación
- Estado cambia a `ALMACENADO`
- Se registra en `historial_ubicaciones`

**Respuesta esperada**:
```json
{
  "success": true,
  "message": "Ubicación asignada exitosamente",
  "data": {
    "id": "...",
    "ubicacionCodigo": "A-01-05",
    "estado": "ALMACENADO",
    ...
  }
}
```

#### 3.3 Verificar Inventario
**Request**: `GET /api/inventario/{{inventarioId}}`

**Resultado**: Ver el producto con ubicación asignada

---

## 📊 Flujo Completo Resumido

```
1. POST /api/recepciones
   → Crear recepción (PENDIENTE)

2. POST /api/recepciones/{id}/detalles
   → Agregar productos (conformes y no conformes)

3. PUT /api/recepciones/{id}/estado
   → Cambiar a EN_CUARENTENA

4. POST /api/inventario/aprobar-acta/{id}
   → Aprobar y registrar en inventario (PENDIENTE_UBICACION)

5. GET /api/inventario/pendientes-ubicacion
   → Ver productos sin ubicación

6. PUT /api/inventario/{id}/asignar-ubicacion
   → Asignar ubicación física (ALMACENADO)

7. GET /api/inventario/{id}
   → Verificar producto almacenado
```

---

## 🔍 Consultas Útiles

### Ver Inventario por Cliente
```
GET /api/inventario/cliente/{{clienteId}}
```

### Ver Todas las Recepciones
```
GET /api/recepciones?page=0&size=10&sortBy=fechaRecepcion&sortDir=desc
```

### Ver Productos Pendientes
```
GET /api/inventario/pendientes-ubicacion
```

---

## ⚠️ Errores Comunes

### Error: "Recepción no encontrada"
**Causa**: El `recepcionId` no es válido
**Solución**: Verifica que hayas guardado el ID correcto después de crear la recepción

### Error: "La recepción debe estar en estado EN_CUARENTENA"
**Causa**: Intentas aprobar una recepción que no está en el estado correcto
**Solución**: Ejecuta el request 1.4 para cambiar el estado

### Error: "No hay productos conformes para registrar"
**Causa**: Todos los productos están marcados como no conformes
**Solución**: Agrega al menos un producto con `"conforme": true`

### Error: "El inventario debe estar en estado PENDIENTE_UBICACION"
**Causa**: Intentas asignar ubicación a un producto que ya la tiene
**Solución**: Verifica el estado del inventario

### Error: "Ubicación no encontrada"
**Causa**: El `ubicacionId` no es válido
**Solución**: Ejecuta `GET /api/ubicaciones` para obtener IDs válidos

---

## 💡 Tips

1. **Usa variables de entorno** en Postman para los IDs
2. **Guarda los IDs** después de cada creación
3. **Verifica el estado** antes de cada operación
4. **Revisa los logs** del backend si algo falla
5. **Usa la carpeta CONSULTAS** para obtener datos de referencia

---

## 📝 Notas

- Los productos **no conformes** NO se registran en el inventario
- Solo los productos **conformes** pasan a la fase de asignación de ubicación
- El estado del inventario sigue este flujo:
  - `PENDIENTE_UBICACION` → `ALMACENADO` → `RESERVADO` → `DESPACHADO`

---

## 🎓 Ejemplo Completo

### Escenario: Recepción de 100 unidades de Paracetamol

1. **Crear recepción** con orden ORD-2024-001
2. **Agregar producto**: 100 unidades de Paracetamol (conforme)
3. **Completar acta**: Cambiar a EN_CUARENTENA
4. **Aprobar acta**: Administrativa registra en inventario
5. **Asignar ubicación**: Operaciones coloca en A-01-05
6. **Verificar**: Producto ahora está ALMACENADO en A-01-05

---

## 📞 Soporte

Si encuentras problemas:
1. Verifica que el backend esté corriendo
2. Revisa los logs del backend
3. Verifica que las tablas estén creadas
4. Asegúrate de tener datos de prueba (clientes, productos, ubicaciones)
