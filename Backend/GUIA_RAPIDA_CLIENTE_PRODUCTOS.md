# ⚡ Guía Rápida: APIs Cliente-Productos

## 🎯 3 Endpoints, 8 Casos de Uso

---

## 1️⃣ Asignar Varios Productos

**Cuándo usar:** Cliente existe, productos existen, quieres asignar varios

```bash
POST /api/cliente-productos/asignar-varios
```

```json
{
  "clienteId": "uuid-cliente",
  "productosIds": ["uuid-1", "uuid-2", "uuid-3"],
  "observaciones": "Asignación masiva"
}
```

---

## 2️⃣ Crear Cliente con Productos

**Cuándo usar:** Cliente nuevo con productos (existentes/nuevos/mixtos)

```bash
POST /api/cliente-productos/crear-cliente-con-productos
```

### Opción A: Solo productos existentes
```json
{
  "cliente": { /* datos del cliente */ },
  "productosExistentesIds": ["uuid-1", "uuid-2"],
  "productosNuevos": [],
  "observaciones": "..."
}
```

### Opción B: Solo productos nuevos
```json
{
  "cliente": { /* datos del cliente */ },
  "productosExistentesIds": [],
  "productosNuevos": [
    { /* datos producto 1 */ },
    { /* datos producto 2 */ }
  ],
  "observaciones": "..."
}
```

### Opción C: Productos mixtos
```json
{
  "cliente": { /* datos del cliente */ },
  "productosExistentesIds": ["uuid-1"],
  "productosNuevos": [
    { /* datos producto nuevo */ }
  ],
  "observaciones": "..."
}
```

---

## 3️⃣ Crear o Asociar (Flexible)

**Cuándo usar:** Cualquier combinación de cliente/producto nuevo o existente

```bash
POST /api/cliente-productos/crear-o-asociar
```

### Caso 1: Ambos existen
```json
{
  "clienteId": "uuid-cliente",
  "productoId": "uuid-producto",
  "observaciones": "..."
}
```

### Caso 2: Cliente existe, producto nuevo
```json
{
  "clienteId": "uuid-cliente",
  "productoNuevo": { /* datos del producto */ },
  "observaciones": "..."
}
```

### Caso 3: Cliente nuevo, producto existe
```json
{
  "clienteNuevo": { /* datos del cliente */ },
  "productoId": "uuid-producto",
  "observaciones": "..."
}
```

### Caso 4: Ambos nuevos
```json
{
  "clienteNuevo": { /* datos del cliente */ },
  "productoNuevo": { /* datos del producto */ },
  "observaciones": "..."
}
```

---

## 📊 Respuesta Estándar

Todos devuelven:

```json
{
  "success": true,
  "message": "...",
  "data": {
    "cliente": { /* datos completos */ },
    "productos": [ /* array de productos */ ],
    "totalAsignaciones": 3,
    "mensaje": "Se asignaron 3 productos al cliente"
  }
}
```

---

## 🧪 Probar en Postman

1. Importar: `Backend/POSTMAN_COLLECTION_COMPLETA.json`
2. Ir a: **"🚀 Flujos Avanzados Cliente-Productos"**
3. Ejecutar flujos de ejemplo

---

## 💡 Decisión Rápida

| Situación | Endpoint a Usar |
|-----------|----------------|
| Cliente existe + varios productos existen | `/asignar-varios` |
| Cliente nuevo + productos del catálogo | `/crear-cliente-con-productos` |
| Cliente nuevo + productos personalizados | `/crear-cliente-con-productos` |
| Cliente nuevo + productos mixtos | `/crear-cliente-con-productos` |
| Un cliente + un producto (cualquier combinación) | `/crear-o-asociar` |

---

## ✅ Validaciones Automáticas

- ✅ No permite duplicados
- ✅ Valida existencia de IDs
- ✅ Requiere datos mínimos
- ✅ SKU únicos

---

## 📚 Documentación Completa

Ver: `Backend/APIS_AVANZADAS_CLIENTE_PRODUCTOS.md`

---

¡Listo para usar! 🚀
