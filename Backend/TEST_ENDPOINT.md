# 🔍 Diagnóstico del Error 500

## ❗ Problema Actual

Estás recibiendo **500 Internal Server Error** en todos los endpoints de cliente-productos.

---

## 🔧 Pasos para Solucionar

### 1. **Reiniciar el Servidor Backend** ⭐ MÁS IMPORTANTE

Los cambios que hicimos NO estarán disponibles hasta que reinicies el servidor.

```bash
# Detener el servidor actual (Ctrl+C en la terminal donde corre)

# Recompilar y reiniciar
mvn clean install
mvn spring-boot:run

# O si usas el IDE
# Click en "Stop" y luego "Run" de nuevo
```

### 2. Verificar que el Servidor Esté Corriendo

```bash
# Prueba este endpoint simple
curl http://localhost:8080/api/clientes/activos
```

Si responde, el servidor está corriendo.

### 3. Verificar los Logs del Backend

Busca en la consola del servidor el error exacto. Debería mostrar algo como:

```
java.lang.NullPointerException: ...
o
java.lang.RuntimeException: Cliente no encontrado
```

---

## 🎯 Causas Comunes del Error 500

### Causa 1: Servidor No Reiniciado ⭐ **MÁS PROBABLE**
**Solución:** Reinicia el servidor backend

### Causa 2: IDs Inválidos
Los UUIDs de clientes o productos no existen en la base de datos.

**Solución:** Verifica que los IDs existan:
```bash
# Listar clientes
GET http://localhost:8080/api/clientes/activos

# Listar productos  
GET http://localhost:8080/api/productos
```

### Causa 3: Base de Datos No Conectada
**Solución:** Verifica la conexión en `application.properties`

### Causa 4: Error de Compilación
**Solución:** Recompila el proyecto:
```bash
mvn clean compile
```

---

## 📝 Test Paso a Paso

### Paso 1: Verificar Servidor
```bash
curl http://localhost:8080/api/clientes/activos
```

**Esperado:** Lista de clientes o array vacío `[]`

### Paso 2: Crear un Cliente de Prueba
```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "razonSocial": "Test Cliente",
    "rucDni": "20999999999",
    "direccionEntrega": "Test 123",
    "distrito": "Lima",
    "telefono": "999999999",
    "email": "test@test.com",
    "tipoCliente": "MINORISTA",
    "formaPago": "CONTADO"
  }'
```

**Esperado:** Respuesta con el cliente creado y su ID

### Paso 3: Crear un Producto de Prueba
```bash
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{
    "codigoSKU": "TEST-001",
    "nombre": "Producto Test",
    "tipo": "MEDICAMENTO",
    "condicionAlmacen": "TEMPERATURA_AMBIENTE",
    "requiereCadenaFrio": false,
    "registroSanitario": "RS-TEST-001",
    "unidadMedida": "CAJA",
    "vidaUtilMeses": 24
  }'
```

**Esperado:** Respuesta con el producto creado y su ID

### Paso 4: Asignar Producto a Cliente (Simple)
```bash
curl -X POST http://localhost:8080/api/cliente-productos \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": "ID_DEL_CLIENTE_CREADO",
    "productoId": "ID_DEL_PRODUCTO_CREADO",
    "observaciones": "Test de asignación"
  }'
```

**Esperado:** Respuesta exitosa con la asignación

### Paso 5: Asignación Masiva
```bash
curl -X POST http://localhost:8080/api/cliente-productos/asignar-masivo \
  -H "Content-Type: application/json" \
  -d '[
    {
      "clienteId": "ID_DEL_CLIENTE",
      "productoId": "ID_DEL_PRODUCTO",
      "observaciones": "Test masivo"
    }
  ]'
```

**Esperado:** Respuesta exitosa con las asignaciones

---

## 🔍 Verificar Endpoint Existe

Después de reiniciar el servidor, verifica que el endpoint esté registrado:

```bash
# En los logs del servidor, busca:
Mapped "{[/api/cliente-productos/asignar-masivo],methods=[POST]}"
```

Si NO aparece, significa que el servidor no se reinició correctamente.

---

## 📊 Checklist de Diagnóstico

- [ ] ¿Reiniciaste el servidor después de los cambios?
- [ ] ¿El servidor está corriendo sin errores?
- [ ] ¿La base de datos está conectada?
- [ ] ¿Los IDs de cliente y producto existen?
- [ ] ¿El endpoint `/asignar-masivo` aparece en los logs?
- [ ] ¿Probaste primero con un cliente y producto nuevos?

---

## 🆘 Si Sigue Fallando

1. **Copia el error completo** de los logs del servidor
2. **Verifica la URL** exacta que estás usando
3. **Prueba primero** los endpoints simples (crear cliente, crear producto)
4. **Comparte** el stack trace completo del error

---

## 💡 Comando Rápido de Diagnóstico

```bash
# Verifica que el servidor responda
curl -v http://localhost:8080/api/clientes/activos

# Si responde 200 OK, el servidor está bien
# Si responde 404, verifica la URL
# Si no responde, el servidor no está corriendo
```

---

## ⚡ Solución Rápida (90% de los casos)

```bash
# 1. Detén el servidor (Ctrl+C)
# 2. Recompila
mvn clean install
# 3. Reinicia
mvn spring-boot:run
# 4. Espera a que termine de iniciar (verás "Started BackendApplication")
# 5. Prueba de nuevo en Postman
```

---

**Nota:** El error 500 NO es de autenticación (eso sería 401 o 403). Es un error interno del servidor, probablemente porque no se reinició después de agregar el nuevo endpoint.
