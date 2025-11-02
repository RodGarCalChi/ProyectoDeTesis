# ✅ Flujo Correcto del Sistema de Almacén

## ❌ Error Común: "405 Method Not Allowed"

### Causa del Error
El endpoint `/api/inventario/registrar` **NO EXISTE** en el sistema.

El sistema NO permite registrar productos directamente en el inventario. Debe seguirse el flujo completo de recepción de mercadería.

---

## ✅ Flujo Correcto

### Diagrama del Flujo

```
1. RECEPCIÓN DE MERCADERÍA
   ↓
2. VERIFICACIONES (Documental, Física, Temperatura)
   ↓
3. CONTROL DE CALIDAD
   ↓
4. APROBAR ACTA → REGISTRA AUTOMÁTICAMENTE EN INVENTARIO
   ↓
5. ASIGNAR UBICACIÓN FÍSICA
   ↓
6. PRODUCTO ALMACENADO
```

---

## 📋 Paso a Paso Detallado

### PASO 1: Crear Recepción de Mercadería

**Endpoint:**
```
POST /api/recepciones
```

**Body:**
```json
{
  "numeroOrden": "OC-2025-001",
  "numeroGuia": "GR-2025-001",
  "clienteId": "c1000000-0000-0000-0000-000000000001",
  "transportista": "Transportes Rápidos S.A.",
  "placaVehiculo": "ABC-123",
  "conductor": "Carlos Ramírez",
  "responsableRecepcion": "Ana Torres",
  "observaciones": "Mercadería en buen estado"
}
```

**Respuesta:**
```json
{
  "success": true,
  "message": "Recepción creada exitosamente",
  "data": {
    "id": "rec-12345-67890",  // ⭐ GUARDAR ESTE ID
    "estado": "PENDIENTE",
    ...
  }
}
```

---

### PASO 2: Iniciar Verificación

**Endpoint:**
```
POST /api/recepciones/{recepcionId}/iniciar-verificacion
```

**Body:**
```json
{
  "inspector": "Pedro Sánchez"
}
```

**Estado cambia a:** `EN_VERIFICACION`

---

### PASO 3: Completar Verificaciones

#### 3.1 Verificación Documental
```
POST /api/recepciones/{recepcionId}/verificacion-documental
```
```json
{
  "aprobado": true
}
```

#### 3.2 Verificación Física
```
POST /api/recepciones/{recepcionId}/verificacion-fisica
```
```json
{
  "aprobado": true
}
```

#### 3.3 Verificación de Temperatura (si aplica)
```
POST /api/recepciones/{recepcionId}/verificacion-temperatura
```
```json
{
  "aprobado": true
}
```

---

### PASO 4: Aprobar por Control de Calidad

**Endpoint:**
```
POST /api/recepciones/{recepcionId}/aprobar-calidad
```

**Body:**
```json
{
  "inspector": "Laura Méndez"
}
```

**Estado cambia a:** `APROBADO`

---

### PASO 5: Aprobar Acta y Registrar en Inventario ⭐

**Este es el paso clave que registra los productos en el inventario**

**Endpoint:**
```
POST /api/inventario/aprobar-acta/{recepcionId}
```

**Body:**
```json
{
  "usuarioNombre": "María González",
  "observaciones": "Documentación completa y conforme, productos aprobados para almacenamiento"
}
```

**Respuesta:**
```json
{
  "success": true,
  "message": "Acta aprobada y 3 producto(s) registrado(s) en inventario",
  "data": [
    {
      "id": "inv-11111-22222",  // ⭐ GUARDAR ESTOS IDs
      "estado": "PENDIENTE_UBICACION",
      "producto": {
        "nombre": "Paracetamol 500mg"
      },
      ...
    },
    {
      "id": "inv-33333-44444",
      "estado": "PENDIENTE_UBICACION",
      ...
    }
  ]
}
```

**Lo que hace este endpoint:**
1. ✅ Aprueba el acta de recepción
2. ✅ Registra TODOS los productos de la recepción en el inventario
3. ✅ Cambia el estado a `PENDIENTE_UBICACION`
4. ✅ Genera registros de historial

---

### PASO 6: Ver Productos Pendientes de Ubicación

**Endpoint:**
```
GET /api/inventario/pendientes-ubicacion
```

**Respuesta:**
```json
{
  "success": true,
  "data": [
    {
      "id": "inv-11111-22222",
      "producto": {
        "nombre": "Paracetamol 500mg",
        "codigoSKU": "MED-PARA-500"
      },
      "cantidad": 500,
      "lote": "PARA-2025-001",
      "estado": "PENDIENTE_UBICACION"
    }
  ],
  "total": 1
}
```

---

### PASO 7: Asignar Ubicación Física

**Endpoint:**
```
PUT /api/inventario/{inventarioId}/asignar-ubicacion
```

**Body:**
```json
{
  "ubicacionId": "u1000000-0000-0000-0000-000000000001",
  "codigoBarras": "BAR-2025-001",
  "usuarioNombre": "Juan Pérez",
  "observaciones": "Ubicación asignada en zona ambiente A-01-01"
}
```

**Respuesta:**
```json
{
  "success": true,
  "message": "Ubicación asignada exitosamente",
  "data": {
    "id": "inv-11111-22222",
    "estado": "ALMACENADO",  // ✅ ESTADO FINAL
    "ubicacion": {
      "codigo": "A-01-01"
    }
  }
}
```

---

## 🎯 Resumen del Flujo

| Paso | Endpoint | Método | Estado Resultante |
|------|----------|--------|-------------------|
| 1 | `/api/recepciones` | POST | PENDIENTE |
| 2 | `/api/recepciones/{id}/iniciar-verificacion` | POST | EN_VERIFICACION |
| 3 | `/api/recepciones/{id}/verificacion-*` | POST | EN_VERIFICACION |
| 4 | `/api/recepciones/{id}/aprobar-calidad` | POST | APROBADO |
| 5 | `/api/inventario/aprobar-acta/{recepcionId}` | POST | PENDIENTE_UBICACION |
| 6 | `/api/inventario/{id}/asignar-ubicacion` | PUT | ALMACENADO |

---

## 🔑 Puntos Clave

1. **NO existe** `/api/inventario/registrar`
2. Los productos se registran automáticamente al **aprobar el acta**
3. El endpoint correcto es: `/api/inventario/aprobar-acta/{recepcionId}`
4. Primero debes crear una **recepción de mercadería**
5. Luego completar las **verificaciones**
6. Finalmente **aprobar el acta** (esto registra en inventario)

---

## 📊 Estados del Inventario

```
PENDIENTE_UBICACION → Después de aprobar acta
         ↓
    ALMACENADO → Después de asignar ubicación
```

---

## 🚨 Errores Comunes

### Error: "405 Method Not Allowed" en `/api/inventario/registrar`
**Solución:** Usar el flujo correcto empezando por crear una recepción

### Error: "Recepción no encontrada"
**Solución:** Primero crear la recepción con `POST /api/recepciones`

### Error: "Acta no encontrada"
**Solución:** La recepción debe estar en estado APROBADO antes de aprobar el acta

### Error: "Inventario no encontrado"
**Solución:** Primero aprobar el acta para que se registre en inventario

---

## 💡 Tips

1. **Guarda los IDs**: Necesitarás el ID de la recepción para aprobar el acta
2. **Sigue el orden**: No puedes saltar pasos del flujo
3. **Verifica estados**: Cada paso cambia el estado, verifica que sea el correcto
4. **Usa Postman**: La colección tiene el flujo completo configurado

---

¡Ahora el flujo está claro! 🎉
