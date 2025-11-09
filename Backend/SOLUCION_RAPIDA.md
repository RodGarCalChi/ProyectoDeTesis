# 🚨 SOLUCIÓN RÁPIDA AL ERROR 500

## ❗ Problema Identificado

El código tiene **errores de compilación** por problemas con Lombok. El servidor no puede iniciar correctamente.

## ✅ Solución Inmediata

### Opción 1: Revertir los Cambios Problemáticos (RECOMENDADO)

Elimina o comenta los 3 endpoints que tienen problemas en `ClienteProductoController.java`:

1. `/asignar-varios`
2. `/crear-cliente-con-productos`  
3. `/crear-o-asociar`

**Deja solo estos endpoints funcionando:**
- `POST /api/cliente-productos` - Asignar 1 producto a 1 cliente
- `POST /api/cliente-productos/asignar-masivo` - Asignación masiva ⭐
- `GET /api/cliente-productos/cliente/{id}/productos`
- `GET /api/cliente-productos/cliente/{id}`
- `DELETE /api/cliente-productos/cliente/{cId}/producto/{pId}`
- `PATCH /api/cliente-productos/cliente/{cId}/producto/{pId}/activar`

### Opción 2: Usar el Endpoint que SÍ Funciona

El endpoint de **asignación masiva** NO tiene problemas de compilación:

```
POST http://localhost:8080/api/cliente-productos/asignar-masivo

Body:
[
  {
    "productoId": "...",
    "clienteId": "...",
    "observaciones": "..."
  }
]
```

## 🔧 Pasos para Arreglar

### 1. Detén el Servidor

Presiona `Ctrl+C` en la terminal donde corre el servidor.

### 2. Comenta los Endpoints Problemáticos

Abre `Backend/src/main/java/org/example/backend/controller/ClienteProductoController.java`

Busca estas líneas (alrededor de la línea 150) y coméntalas:

```java
// COMENTAR DESDE AQUÍ
@PostMapping("/asignar-varios")
public ResponseEntity<?> asignarVariosProductos(...) {
    ...
}

@PostMapping("/crear-cliente-con-productos")
public ResponseEntity<?> crearClienteConProductos(...) {
    ...
}

@PostMapping("/crear-o-asociar")
public ResponseEntity<?> crearOAsociarProductoConCliente(...) {
    ...
}
// HASTA AQUÍ
```

Agrégales `/*` al inicio y `*/` al final para comentarlos.

### 3. Recompila y Reinicia

```bash
mvn clean compile
mvn spring-boot:run
```

### 4. Prueba el Endpoint que Funciona

```bash
curl -X POST http://localhost:8080/api/cliente-productos/asignar-masivo \
  -H "Content-Type: application/json" \
  -d '[
    {
      "productoId": "3798c0d8-7913-4507-925b-40afad693efb",
      "clienteId": "5f858864-d9e8-4408-8560-67c010546e3f",
      "observaciones": "Test"
    }
  ]'
```

## 📝 Endpoints que SÍ Funcionan

Estos endpoints NO tienen problemas y deberían funcionar:

### Clientes
```
POST   /api/clientes
GET    /api/clientes
GET    /api/clientes/{id}
GET    /api/clientes/ruc/{ruc}
PUT    /api/clientes/{id}
DELETE /api/clientes/{id}
```

### Productos
```
POST   /api/productos
GET    /api/productos
GET    /api/productos/{id}
GET    /api/productos/sku/{sku}
PUT    /api/productos/{id}
DELETE /api/productos/{id}
```

### Cliente-Productos (Básicos)
```
POST   /api/cliente-productos                    ← Asignar 1 a 1
POST   /api/cliente-productos/asignar-masivo     ← Asignación masiva ⭐
GET    /api/cliente-productos/cliente/{id}/productos
GET    /api/cliente-productos/cliente/{id}
GET    /api/cliente-productos/producto/{id}
DELETE /api/cliente-productos/cliente/{cId}/producto/{pId}
PATCH  /api/cliente-productos/cliente/{cId}/producto/{pId}/activar
```

## 🎯 Para Tu Caso Específico

Usa el endpoint de **asignación masiva** con tu archivo JSON:

```
POST http://localhost:8080/api/cliente-productos/asignar-masivo
Content-Type: application/json

[Contenido de asignaciones_productos_clientes.json]
```

Este endpoint **NO tiene problemas** y debería funcionar una vez que:
1. Comentes los 3 endpoints problemáticos
2. Recompiles el proyecto
3. Reinicies el servidor

## ⚡ Comando Rápido

```bash
# 1. Detén el servidor (Ctrl+C)

# 2. Limpia y recompila
cd Backend
mvn clean compile

# 3. Si hay errores, coméntalos endpoints problemáticos y vuelve a compilar

# 4. Reinicia
mvn spring-boot:run

# 5. Espera a ver "Started BackendApplication"

# 6. Prueba
curl http://localhost:8080/api/clientes/activos
```

## 🔍 Verificar que Funciona

```bash
# Test 1: Servidor responde
curl http://localhost:8080/api/clientes/activos

# Test 2: Crear cliente
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "razonSocial": "Test",
    "rucDni": "20999999999",
    "direccionEntrega": "Test 123",
    "distrito": "Lima",
    "telefono": "999999999",
    "email": "test@test.com",
    "tipoCliente": "MINORISTA",
    "formaPago": "CONTADO"
  }'

# Test 3: Crear producto
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{
    "codigoSKU": "TEST-001",
    "nombre": "Test Producto",
    "tipo": "MEDICAMENTO",
    "condicionAlmacen": "TEMPERATURA_AMBIENTE",
    "requiereCadenaFrio": false,
    "registroSanitario": "RS-TEST",
    "unidadMedida": "CAJA",
    "vidaUtilMeses": 24
  }'
```

## 💡 Resumen

**Problema:** Errores de compilación por Lombok en 3 endpoints avanzados  
**Solución:** Comentar esos 3 endpoints y usar solo los básicos  
**Endpoint para tu caso:** `POST /api/cliente-productos/asignar-masivo` ⭐

Este endpoint es el que necesitas y NO tiene problemas.
