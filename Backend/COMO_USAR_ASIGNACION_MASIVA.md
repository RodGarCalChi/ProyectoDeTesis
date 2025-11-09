# 🚀 Cómo Usar la Asignación Masiva de Productos a Clientes

## ✅ Solución a tu Problema

El error que tenías era porque estabas enviando un **array** `[{...}, {...}]` al endpoint que solo acepta **un objeto** `{...}`.

He creado un **nuevo endpoint** que acepta arrays para carga masiva.

---

## 📝 Pasos para Usar

### 1. Usa el Nuevo Endpoint

**Endpoint:** `POST http://localhost:8080/api/cliente-productos/asignar-masivo`

### 2. Copia el Array Completo

Usa el archivo `asignaciones_productos_clientes.json` que ya está listo con tus 15 asignaciones.

### 3. Envía la Petición

#### Opción A: Con Postman/Insomnia

1. Método: **POST**
2. URL: `http://localhost:8080/api/cliente-productos/asignar-masivo`
3. Headers: `Content-Type: application/json`
4. Body: Copia el contenido del archivo `asignaciones_productos_clientes.json`

#### Opción B: Con cURL

```bash
curl -X POST http://localhost:8080/api/cliente-productos/asignar-masivo \
  -H "Content-Type: application/json" \
  -d @asignaciones_productos_clientes.json
```

#### Opción C: Desde PowerShell (Windows)

```powershell
$body = Get-Content -Path "asignaciones_productos_clientes.json" -Raw
Invoke-RestMethod -Uri "http://localhost:8080/api/cliente-productos/asignar-masivo" `
  -Method POST `
  -ContentType "application/json" `
  -Body $body
```

---

## 📊 Ejemplo de Respuesta Exitosa

```json
{
  "success": true,
  "message": "Se procesaron 15 asignaciones exitosamente",
  "data": [
    {
      "id": "770e8400-e29b-41d4-a716-446655440002",
      "clienteId": "5f858864-d9e8-4408-8560-67c010546e3f",
      "clienteNombre": "Hospital Nacional Dos de Mayo",
      "productoId": "3798c0d8-7913-4507-925b-40afad693efb",
      "productoNombre": "Paracetamol 500mg",
      "productoSku": "PARA-500",
      "fechaAsignacion": "2025-01-08T10:30:00",
      "activo": true,
      "observaciones": "Paracetamol 500mg asignado al Hospital Nacional Dos de Mayo para uso en pacientes ambulatorios"
    }
    // ... 14 asignaciones más
  ],
  "total": 15
}
```

---

## ⚠️ Manejo de Errores

Si algunos productos ya están asignados o hay IDs inválidos, recibirás:

```json
{
  "success": false,
  "message": "Se procesaron 12 de 15 asignaciones. Errores: Índice 3: El producto ya está asignado a este cliente; Índice 7: Cliente no encontrado"
}
```

Esto significa que:
- ✅ 12 asignaciones se guardaron correctamente
- ❌ 3 asignaciones fallaron (con detalles específicos)

---

## 🔍 Verificar las Asignaciones

Después de la carga masiva, puedes verificar:

### Ver productos de un cliente específico:
```bash
GET http://localhost:8080/api/cliente-productos/cliente/{clienteId}/productos
```

### Contar productos asignados:
```bash
GET http://localhost:8080/api/cliente-productos/cliente/{clienteId}/count
```

### Ver todas las relaciones de un cliente:
```bash
GET http://localhost:8080/api/cliente-productos/cliente/{clienteId}
```

---

## 📁 Archivos Creados

1. **`asignaciones_productos_clientes.json`** - Tus 15 asignaciones listas para usar
2. **`GUIA_ASIGNACION_PRODUCTOS_CLIENTES.md`** - Guía completa de todos los endpoints
3. **`COMO_USAR_ASIGNACION_MASIVA.md`** - Esta guía rápida

---

## 🎯 Diferencias entre Endpoints

| Endpoint | Uso | Formato |
|----------|-----|---------|
| `POST /api/cliente-productos` | 1 producto → 1 cliente | `{productoId, clienteId, observaciones}` |
| `POST /api/cliente-productos/asignar-varios` | N productos → 1 cliente | `{clienteId, productosIds: [...]}` |
| `POST /api/cliente-productos/asignar-masivo` ⭐ | N productos → N clientes | `[{productoId, clienteId}, ...]` |

---

## 💡 Tips

1. **Validación previa**: Asegúrate de que los UUIDs de clientes y productos existan en la base de datos
2. **Duplicados**: El sistema no permite asignar el mismo producto dos veces al mismo cliente
3. **Transaccional**: Cada asignación se procesa independientemente, si una falla, las demás continúan
4. **Observaciones**: Son opcionales pero recomendadas para documentar el propósito de cada asignación

---

## 🐛 Solución de Problemas

### Error: "Internal Server Error"
- Verifica que el servidor esté corriendo
- Revisa los logs del backend para más detalles

### Error: "Cliente no encontrado" o "Producto no encontrado"
- Verifica que los UUIDs sean correctos
- Consulta los endpoints de clientes y productos para obtener IDs válidos

### Error: "El producto ya está asignado a este cliente"
- Esta relación ya existe en la base de datos
- Puedes consultar las relaciones existentes con `GET /api/cliente-productos/cliente/{clienteId}`

---

**¡Listo!** Ahora puedes cargar tus 15 asignaciones en una sola petición. 🎉
