# 🔗 Relación Cliente-Productos

## 📋 Descripción

Esta funcionalidad permite gestionar qué productos están asignados a cada cliente. Es útil para:
- Saber qué productos maneja cada cliente
- Filtrar productos por cliente en recepciones
- Controlar el catálogo de productos por cliente
- Generar reportes de productos por cliente

## 🏗️ Arquitectura

### Entidad: `ClienteProducto`
Tabla intermedia que relaciona clientes con productos (Many-to-Many)

**Campos**:
- `id`: UUID único
- `cliente_id`: Referencia al cliente
- `producto_id`: Referencia al producto
- `fecha_asignacion`: Cuándo se asignó
- `activo`: Si la relación está activa
- `observaciones`: Notas adicionales

## 🔌 Endpoints Disponibles

### 1. Asignar Producto a Cliente
```http
POST /api/cliente-productos
Content-Type: application/json

{
  "clienteId": "uuid-del-cliente",
  "productoId": "uuid-del-producto",
  "observaciones": "Producto asignado para distribución"
}
```

**Respuesta**:
```json
{
  "success": true,
  "message": "Producto asignado al cliente exitosamente",
  "data": {
    "id": "uuid-de-relacion",
    "clienteId": "uuid-del-cliente",
    "clienteNombre": "Farmacia Test S.A.C.",
    "productoId": "uuid-del-producto",
    "productoNombre": "Paracetamol 500mg",
    "productoSku": "PAR500MG",
    "fechaAsignacion": "2024-10-21T10:30:00",
    "activo": true,
    "observaciones": "Producto asignado para distribución"
  }
}
```

---

### 2. Obtener Productos de un Cliente
```http
GET /api/cliente-productos/cliente/{clienteId}/productos
```

**Respuesta**:
```json
{
  "success": true,
  "data": [
    {
      "id": "uuid-producto-1",
      "codigoSKU": "PAR500MG",
      "nombre": "Paracetamol 500mg",
      "tipo": "MEDICAMENTO",
      "condicionAlmacen": "AMBIENTE",
      "requiereCadenaFrio": false
    },
    {
      "id": "uuid-producto-2",
      "codigoSKU": "IBU400MG",
      "nombre": "Ibuprofeno 400mg",
      "tipo": "MEDICAMENTO",
      "condicionAlmacen": "AMBIENTE",
      "requiereCadenaFrio": false
    }
  ],
  "total": 2
}
```

---

### 3. Obtener Relaciones Completas de un Cliente
```http
GET /api/cliente-productos/cliente/{clienteId}
```

**Respuesta**:
```json
{
  "success": true,
  "data": [
    {
      "id": "uuid-relacion",
      "clienteId": "uuid-cliente",
      "clienteNombre": "Farmacia Test S.A.C.",
      "productoId": "uuid-producto",
      "productoNombre": "Paracetamol 500mg",
      "productoSku": "PAR500MG",
      "fechaAsignacion": "2024-10-21T10:30:00",
      "activo": true,
      "observaciones": "Producto asignado"
    }
  ],
  "total": 1
}
```

---

### 4. Obtener Clientes de un Producto
```http
GET /api/cliente-productos/producto/{productoId}
```

**Respuesta**: Lista de clientes que tienen asignado ese producto

---

### 5. Contar Productos de un Cliente
```http
GET /api/cliente-productos/cliente/{clienteId}/count
```

**Respuesta**:
```json
{
  "success": true,
  "count": 5
}
```

---

### 6. Desactivar Relación
```http
DELETE /api/cliente-productos/cliente/{clienteId}/producto/{productoId}
```

**Respuesta**:
```json
{
  "success": true,
  "message": "Relación desactivada exitosamente"
}
```

---

### 7. Activar Relación
```http
PATCH /api/cliente-productos/cliente/{clienteId}/producto/{productoId}/activar
```

**Respuesta**:
```json
{
  "success": true,
  "message": "Relación activada exitosamente"
}
```

---

## 🧪 Flujo de Prueba en Postman

### Paso 1: Crear Cliente
```http
POST /api/clientes
Body: {
  "razonSocial": "Farmacia Test",
  "rucDni": "20987654321",
  "direccionEntrega": "Av. Test 123",
  "distrito": "Lima"
}
```
✅ Guarda el `clienteId` de la respuesta

### Paso 2: Crear Producto
```http
POST /api/productos
Body: {
  "codigoSKU": "PAR500MG",
  "nombre": "Paracetamol 500mg",
  "tipo": "MEDICAMENTO",
  "condicionAlmacen": "AMBIENTE"
}
```
✅ Guarda el `productoId` de la respuesta

### Paso 3: Asignar Producto a Cliente
```http
POST /api/cliente-productos
Body: {
  "clienteId": "uuid-del-paso-1",
  "productoId": "uuid-del-paso-2",
  "observaciones": "Producto principal del cliente"
}
```

### Paso 4: Listar Productos del Cliente
```http
GET /api/cliente-productos/cliente/{clienteId}/productos
```

**Resultado**: Lista de todos los productos asignados al cliente

---

## 💻 Uso en Frontend

### Ejemplo en React/TypeScript:

```typescript
// Obtener productos de un cliente específico
const obtenerProductosDeCliente = async (clienteId: string) => {
  try {
    const response = await fetch(
      `http://localhost:8080/api/cliente-productos/cliente/${clienteId}/productos`
    );
    const data = await response.json();
    
    if (data.success) {
      console.log('Productos del cliente:', data.data);
      return data.data; // Array de ProductoDTO
    }
  } catch (error) {
    console.error('Error:', error);
  }
};

// Asignar producto a cliente
const asignarProducto = async (clienteId: string, productoId: string) => {
  try {
    const response = await fetch(
      'http://localhost:8080/api/cliente-productos',
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          clienteId: clienteId,
          productoId: productoId,
          observaciones: 'Asignación automática'
        })
      }
    );
    
    const data = await response.json();
    
    if (data.success) {
      console.log('Producto asignado exitosamente');
      return data.data;
    }
  } catch (error) {
    console.error('Error:', error);
  }
};
```

---

## 🎯 Casos de Uso

### Caso 1: Filtrar Productos por Cliente en Recepción
```typescript
// En la pantalla de recepción, después de seleccionar cliente
const handleClienteSelect = async (clienteId: string) => {
  // Cargar solo los productos de ese cliente
  const productosCliente = await obtenerProductosDeCliente(clienteId);
  setProductosDisponibles(productosCliente);
};
```

### Caso 2: Validar si un Cliente puede Recibir un Producto
```typescript
const validarProductoParaCliente = async (clienteId: string, productoId: string) => {
  const productosCliente = await obtenerProductosDeCliente(clienteId);
  const tieneProducto = productosCliente.some(p => p.id === productoId);
  
  if (!tieneProducto) {
    alert('Este producto no está asignado a este cliente');
    return false;
  }
  
  return true;
};
```

### Caso 3: Mostrar Catálogo de Productos por Cliente
```typescript
const mostrarCatalogoCliente = async (clienteId: string) => {
  const productos = await obtenerProductosDeCliente(clienteId);
  
  return (
    <div>
      <h3>Productos de {clienteNombre}</h3>
      <ul>
        {productos.map(producto => (
          <li key={producto.id}>
            {producto.codigoSKU} - {producto.nombre}
          </li>
        ))}
      </ul>
    </div>
  );
};
```

---

## 📊 Beneficios

✅ **Control de Catálogo**: Cada cliente tiene su propio catálogo de productos
✅ **Validación Automática**: Evita recepciones de productos no autorizados
✅ **Reportes Precisos**: Estadísticas de productos por cliente
✅ **Gestión Flexible**: Activar/desactivar productos sin eliminar historial
✅ **Trazabilidad**: Fecha de asignación y observaciones

---

## 🔄 Integración con Recepción de Mercadería

### Modificación Sugerida en la Pantalla de Recepción:

```typescript
// Cuando se selecciona un cliente, cargar solo sus productos
const handleClienteSelect = async (clienteId: string) => {
  setFormData(prev => ({ ...prev, clienteId }));
  
  // Cargar productos del cliente
  const response = await fetch(
    `http://localhost:8080/api/cliente-productos/cliente/${clienteId}/productos`
  );
  const data = await response.json();
  
  if (data.success) {
    setProductosDisponibles(data.data); // Solo productos del cliente
  }
};
```

---

## 🧪 Prueba Completa en Postman

### Secuencia Recomendada:

1. **Crear Cliente**
   ```
   POST /api/clientes
   ```

2. **Crear Productos** (varios)
   ```
   POST /api/productos (Paracetamol)
   POST /api/productos (Ibuprofeno)
   POST /api/productos (Amoxicilina)
   ```

3. **Asignar Productos al Cliente**
   ```
   POST /api/cliente-productos (Paracetamol)
   POST /api/cliente-productos (Ibuprofeno)
   ```

4. **Listar Productos del Cliente**
   ```
   GET /api/cliente-productos/cliente/{clienteId}/productos
   ```
   
   **Resultado**: Solo Paracetamol e Ibuprofeno (no Amoxicilina)

5. **Usar en Recepción**
   ```
   POST /api/recepciones
   ```
   Con productos del cliente

¡Ahora puedes gestionar qué productos pertenecen a cada cliente! 🎉