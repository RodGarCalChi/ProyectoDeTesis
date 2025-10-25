# 🚀 APIs Avanzadas Cliente-Productos

## Descripción General

Este documento describe las APIs avanzadas para gestionar la relación entre clientes y productos, cubriendo todos los casos posibles de creación y asociación.

---

## 📋 Casos de Uso Cubiertos

### 1. **Asignar Múltiples Productos a Cliente Existente**
- Cliente ya existe
- Productos ya existen
- Asignación masiva

### 2. **Crear Cliente con Productos Existentes**
- Cliente nuevo
- Productos del catálogo existente

### 3. **Crear Cliente con Productos Nuevos**
- Cliente nuevo
- Productos nuevos (se crean automáticamente)

### 4. **Crear Cliente con Productos Mixtos**
- Cliente nuevo
- Algunos productos del catálogo + algunos productos nuevos

### 5. **Asociar Producto Existente con Cliente Existente**
- Ambos ya existen
- Asociación simple

### 6. **Crear Producto y Asociar a Cliente Existente**
- Cliente existe
- Producto nuevo

### 7. **Crear Cliente y Asociar Producto Existente**
- Cliente nuevo
- Producto del catálogo

### 8. **Crear Cliente y Producto Nuevos**
- Ambos son nuevos
- Creación y asociación completa

---

## 🔌 Endpoints Disponibles

### 1. Asignar Varios Productos a Cliente

**Endpoint:** `POST /api/cliente-productos/asignar-varios`

**Caso de Uso:** Asignar múltiples productos existentes a un cliente existente

**Request Body:**
```json
{
  "clienteId": "uuid-del-cliente",
  "productosIds": [
    "uuid-producto-1",
    "uuid-producto-2",
    "uuid-producto-3"
  ],
  "observaciones": "Asignación masiva de productos"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Se asignaron 3 productos al cliente",
  "data": {
    "cliente": {
      "id": "uuid-del-cliente",
      "razonSocial": "Farmacia Test S.A.C.",
      "rucDni": "20987654321",
      ...
    },
    "productos": [
      {
        "id": "uuid-producto-1",
        "codigoSKU": "PAR500MG",
        "nombre": "Paracetamol 500mg",
        ...
      },
      ...
    ],
    "totalAsignaciones": 3,
    "mensaje": "Se asignaron 3 productos al cliente"
  }
}
```

---

### 2. Crear Cliente con Productos

**Endpoint:** `POST /api/cliente-productos/crear-cliente-con-productos`

**Caso de Uso:** Crear un cliente nuevo y asignarle productos (existentes y/o nuevos)

#### Opción A: Con Productos Existentes

```json
{
  "cliente": {
    "razonSocial": "Farmacia Nueva S.A.C.",
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
  "productosNuevos": [],
  "observaciones": "Cliente nuevo con productos del catálogo"
}
```

#### Opción B: Con Productos Nuevos

```json
{
  "cliente": {
    "razonSocial": "Botica Express S.A.C.",
    "rucDni": "20987654321",
    "direccionEntrega": "Jr. Comercio 789, Lima",
    "distrito": "San Isidro",
    "telefono": "01-777-8888",
    "email": "ventas@boticaexpress.com",
    "tipoCliente": "BOTICA",
    "formaPago": "CONTADO"
  },
  "productosExistentesIds": [],
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
    },
    {
      "codigoSKU": "LORAT10MG",
      "nombre": "Loratadina 10mg",
      "tipo": "MEDICAMENTO",
      "condicionAlmacen": "AMBIENTE",
      "requiereCadenaFrio": false,
      "registroSanitario": "RS-LOR-10",
      "unidadMedida": "TABLETA",
      "vidaUtilMeses": 36,
      "tempMin": 15.0,
      "tempMax": 30.0
    }
  ],
  "observaciones": "Cliente nuevo con productos exclusivos"
}
```

#### Opción C: Con Productos Mixtos

```json
{
  "cliente": {
    "razonSocial": "Farmacia Integral S.A.C.",
    "rucDni": "20555666777",
    "direccionEntrega": "Av. Salud 321, Lima",
    "distrito": "Surco",
    "telefono": "01-999-0000",
    "email": "info@farmaintegral.com",
    "tipoCliente": "FARMACIA",
    "formaPago": "CREDITO"
  },
  "productosExistentesIds": [
    "uuid-producto-existente"
  ],
  "productosNuevos": [
    {
      "codigoSKU": "DICLOF50MG",
      "nombre": "Diclofenaco 50mg",
      "tipo": "MEDICAMENTO",
      "condicionAlmacen": "AMBIENTE",
      "requiereCadenaFrio": false,
      "registroSanitario": "RS-DIC-50",
      "unidadMedida": "TABLETA",
      "vidaUtilMeses": 24,
      "tempMin": 15.0,
      "tempMax": 30.0
    }
  ],
  "observaciones": "Cliente con productos del catálogo y exclusivos"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Cliente creado con 3 productos asignados",
  "data": {
    "cliente": {
      "id": "uuid-nuevo-cliente",
      "razonSocial": "Farmacia Integral S.A.C.",
      ...
    },
    "productos": [...],
    "totalAsignaciones": 3,
    "mensaje": "Cliente creado con 3 productos asignados"
  }
}
```

---

### 3. Crear o Asociar (Flexible)

**Endpoint:** `POST /api/cliente-productos/crear-o-asociar`

**Caso de Uso:** Endpoint flexible que maneja todos los casos posibles

#### Caso 1: Cliente Existente + Producto Existente

```json
{
  "clienteId": "uuid-cliente-existente",
  "productoId": "uuid-producto-existente",
  "observaciones": "Asociación de producto existente"
}
```

#### Caso 2: Cliente Existente + Producto Nuevo

```json
{
  "clienteId": "uuid-cliente-existente",
  "productoNuevo": {
    "codigoSKU": "OMEP20MG",
    "nombre": "Omeprazol 20mg",
    "tipo": "MEDICAMENTO",
    "condicionAlmacen": "AMBIENTE",
    "requiereCadenaFrio": false,
    "registroSanitario": "RS-OME-20",
    "unidadMedida": "CAPSULA",
    "vidaUtilMeses": 36,
    "tempMin": 15.0,
    "tempMax": 30.0
  },
  "observaciones": "Producto nuevo para cliente existente"
}
```

#### Caso 3: Cliente Nuevo + Producto Existente

```json
{
  "clienteNuevo": {
    "razonSocial": "Farmacia Rápida S.A.C.",
    "rucDni": "20111222333",
    "direccionEntrega": "Av. Rápida 111, Lima",
    "distrito": "La Molina",
    "telefono": "01-444-5555",
    "email": "contacto@farmarapida.com",
    "tipoCliente": "FARMACIA",
    "formaPago": "CONTADO"
  },
  "productoId": "uuid-producto-existente",
  "observaciones": "Cliente nuevo con producto del catálogo"
}
```

#### Caso 4: Cliente Nuevo + Producto Nuevo

```json
{
  "clienteNuevo": {
    "razonSocial": "Botica Salud Total S.A.C.",
    "rucDni": "20888999000",
    "direccionEntrega": "Jr. Salud 999, Lima",
    "distrito": "San Borja",
    "telefono": "01-666-7777",
    "email": "ventas@saludtotal.com",
    "tipoCliente": "BOTICA",
    "formaPago": "CREDITO"
  },
  "productoNuevo": {
    "codigoSKU": "METF850MG",
    "nombre": "Metformina 850mg",
    "tipo": "MEDICAMENTO",
    "condicionAlmacen": "AMBIENTE",
    "requiereCadenaFrio": false,
    "registroSanitario": "RS-MET-850",
    "unidadMedida": "TABLETA",
    "vidaUtilMeses": 24,
    "tempMin": 15.0,
    "tempMax": 30.0
  },
  "observaciones": "Cliente y producto completamente nuevos"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Producto asociado exitosamente al cliente",
  "data": {
    "cliente": {...},
    "productos": [{...}],
    "totalAsignaciones": 1,
    "mensaje": "Producto asociado exitosamente al cliente"
  }
}
```

---

## 🧪 Flujo de Prueba en Postman

La colección actualizada incluye ejemplos para todos los casos:

### Sección: "🔗 Cliente-Productos (Relaciones)"

1. **Asignar Producto a Cliente** (básico)
2. **🆕 Asignar Varios Productos a Cliente**
3. **🆕 Crear Cliente con Productos Existentes**
4. **🆕 Crear Cliente con Productos Nuevos**
5. **🆕 Crear Cliente con Productos Mixtos**
6. **🆕 Asociar Producto Existente con Cliente Existente**
7. **🆕 Crear Producto y Asociar a Cliente Existente**
8. **🆕 Crear Cliente y Asociar Producto Existente**
9. **🆕 Crear Cliente y Producto Nuevos**

---

## 💡 Casos de Uso Prácticos

### Escenario 1: Onboarding de Cliente Nuevo
```
1. Cliente nuevo se registra
2. Selecciona productos del catálogo existente
3. Solicita algunos productos exclusivos
→ Usar: POST /crear-cliente-con-productos (con productos mixtos)
```

### Escenario 2: Expansión de Catálogo
```
1. Cliente existente
2. Quiere agregar varios productos nuevos a su catálogo
→ Usar: POST /asignar-varios
```

### Escenario 3: Producto Personalizado
```
1. Cliente existente
2. Necesita un producto que no está en el catálogo
→ Usar: POST /crear-o-asociar (con productoNuevo)
```

### Escenario 4: Migración de Datos
```
1. Importar clientes desde sistema legacy
2. Algunos productos existen, otros no
→ Usar: POST /crear-cliente-con-productos (con productos mixtos)
```

---

## ⚠️ Validaciones

- **Duplicados:** No se permite asignar el mismo producto dos veces al mismo cliente
- **Existencia:** Se valida que clientes/productos existan cuando se usan IDs
- **Datos Requeridos:** Se valida que se proporcione al menos un método de identificación (ID o datos nuevos)
- **SKU Único:** Los códigos SKU de productos deben ser únicos

---

## 📊 Respuesta Estándar

Todas las APIs devuelven el mismo formato de respuesta:

```json
{
  "success": true/false,
  "message": "Mensaje descriptivo",
  "data": {
    "cliente": {...},
    "productos": [...],
    "totalAsignaciones": número,
    "mensaje": "Resumen de la operación"
  }
}
```

---

## 🔧 Integración en Frontend

### Ejemplo: Formulario de Registro de Cliente

```typescript
const registrarClienteConProductos = async (formData) => {
  const payload = {
    cliente: {
      razonSocial: formData.razonSocial,
      rucDni: formData.rucDni,
      // ... otros campos
    },
    productosExistentesIds: formData.productosSeleccionados, // IDs del catálogo
    productosNuevos: formData.productosPersonalizados, // Productos nuevos
    observaciones: formData.observaciones
  };
  
  const response = await fetch(
    'http://localhost:8080/api/cliente-productos/crear-cliente-con-productos',
    {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    }
  );
  
  const result = await response.json();
  if (result.success) {
    console.log(`Cliente creado con ${result.data.totalAsignaciones} productos`);
  }
};
```

---

## ✅ Ventajas

1. **Flexibilidad Total:** Cubre todos los casos posibles
2. **Operaciones Atómicas:** Todo se crea/asocia en una sola transacción
3. **Menos Llamadas API:** Reduce el número de requests necesarios
4. **Validación Automática:** Previene duplicados y errores
5. **Respuestas Completas:** Devuelve toda la información necesaria
6. **Fácil Integración:** APIs intuitivas y bien documentadas

---

## 🎯 Próximos Pasos

1. Importar la colección de Postman actualizada
2. Probar cada endpoint con los ejemplos proporcionados
3. Integrar en el frontend según tus necesidades
4. Personalizar las validaciones según reglas de negocio

¡Todas las APIs están listas para usar! 🚀
