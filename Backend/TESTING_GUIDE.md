# 🧪 Guía de Pruebas - PharmaFlow API

## 📋 Preparación del Entorno

### 1. Base de Datos
```sql
-- Ejecutar el archivo database_inserts.sql en tu base de datos
-- Esto creará los usuarios de prueba necesarios
```

### 2. Iniciar el Backend
```bash
cd Backend
./gradlew bootRun
# El servidor debe estar corriendo en http://localhost:8080
```

### 3. Iniciar el Frontend
```bash
cd Frontend
npm run dev
# El frontend debe estar corriendo en http://localhost:3000
```

## 🔐 Usuarios de Prueba Disponibles

| Email | Contraseña | Rol | Acceso a Movimientos |
|-------|------------|-----|---------------------|
| `recepcion@pharmaflow.com` | `password` | Recepcion | ✅ SÍ |
| `admin@pharmaflow.com` | `password` | DirectorTecnico | ❌ NO |
| `operaciones@pharmaflow.com` | `password` | Operaciones | ❌ NO |
| `calidad@pharmaflow.com` | `password` | Calidad | ❌ NO |
| `despacho@pharmaflow.com` | `password` | Despacho | ❌ NO |
| `cliente@pharmaflow.com` | `password` | Cliente | ❌ NO |

## 🌐 Pruebas Frontend (Navegador)

### ✅ Caso 1: Login Exitoso con Usuario de Recepción
1. Ir a `http://localhost:3000`
2. Usar credenciales:
   - Email: `recepcion@pharmaflow.com`
   - Contraseña: `password`
3. **Resultado esperado**: Redirección automática a `/movimientos`

### ❌ Caso 2: Login con Usuario Sin Permisos
1. Ir a `http://localhost:3000/login`
2. Usar credenciales:
   - Email: `admin@pharmaflow.com`
   - Contraseña: `password`
3. **Resultado esperado**: Redirección a `/dashboard` (no a movimientos)

### 🔒 Caso 3: Acceso Directo Sin Autenticación
1. Ir directamente a `http://localhost:3000/movimientos`
2. **Resultado esperado**: Redirección automática a `/login`

### 🚫 Caso 4: Acceso con Usuario Incorrecto
1. Hacer login con `admin@pharmaflow.com`
2. Intentar ir a `http://localhost:3000/movimientos`
3. **Resultado esperado**: Redirección a `/unauthorized`

## 🔧 Pruebas API (Postman/Insomnia)

### Importar Colección
1. Abrir Postman/Insomnia
2. Importar el archivo `postman_collection.json`
3. Configurar variable `baseUrl` = `http://localhost:8080/api`

### 🧪 Secuencia de Pruebas Recomendada

#### 1. Autenticación Básica
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "recepcion@pharmaflow.com",
  "password": "password"
}
```
**Resultado esperado**: Status 200, token en la respuesta

#### 2. Validar Acceso a Movimientos
```http
GET /api/movimientos/validar-acceso
Authorization: Bearer {token_obtenido}
```
**Resultado esperado**: Status 200, `"success": true`

#### 3. Registrar Entrada de Mercadería
```http
POST /api/movimientos/entrada
Authorization: Bearer {token_obtenido}
Content-Type: application/json

{
  "referencia": "PO-12345",
  "proveedor": "proveedor1",
  "producto": "producto1",
  "cantidad": 100,
  "ubicacion": "almacen1",
  "recibidoPor": "María García",
  "dia": "2024-10-04",
  "hora": "14:30",
  "notas": "Producto en perfecto estado"
}
```
**Resultado esperado**: Status 200, entrada registrada exitosamente

#### 4. Prueba de Seguridad - Usuario Sin Permisos
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "admin@pharmaflow.com",
  "password": "password"
}
```
Luego usar el token obtenido:
```http
GET /api/movimientos/validar-acceso
Authorization: Bearer {token_admin}
```
**Resultado esperado**: Status 403, acceso denegado

## 📊 Casos de Prueba Detallados

### ✅ Casos Exitosos

| Prueba | Endpoint | Usuario | Resultado Esperado |
|--------|----------|---------|-------------------|
| Login Recepción | `/auth/login` | recepcion@pharmaflow.com | Status 200, token válido |
| Validar Acceso | `/movimientos/validar-acceso` | Token Recepción | Status 200, acceso autorizado |
| Registrar Entrada | `/movimientos/entrada` | Token Recepción | Status 200, entrada registrada |
| Obtener Proveedores | `/movimientos/proveedores` | Token Recepción | Status 200, lista de proveedores |

### ❌ Casos de Error

| Prueba | Endpoint | Condición | Resultado Esperado |
|--------|----------|-----------|-------------------|
| Sin Token | `/movimientos/validar-acceso` | Sin Authorization header | Status 401, token requerido |
| Token Inválido | `/movimientos/validar-acceso` | Token malformado | Status 401, token inválido |
| Usuario Admin | `/movimientos/validar-acceso` | Token de admin | Status 403, acceso denegado |
| Datos Incompletos | `/movimientos/entrada` | Sin referencia | Status 400, campo obligatorio |
| Cantidad Negativa | `/movimientos/entrada` | cantidad: -10 | Status 400, cantidad inválida |

## 🐛 Troubleshooting

### Problema: Error de CORS
**Solución**: Verificar que el backend esté configurado para permitir `http://localhost:3000`

### Problema: Usuario no encontrado
**Solución**: Ejecutar los INSERTs de la base de datos

### Problema: Token inválido
**Solución**: Hacer login nuevamente para obtener un token fresco

### Problema: 404 en endpoints
**Solución**: Verificar que el backend esté corriendo en puerto 8080

## 📈 Métricas de Éxito

- ✅ Login con usuario de recepción funciona
- ✅ Acceso a movimientos solo para rol Recepcion
- ✅ Otros roles son rechazados correctamente
- ✅ Validaciones de datos funcionan
- ✅ Tokens se manejan correctamente
- ✅ Redirecciones automáticas funcionan

## 🔄 Flujo Completo de Prueba

1. **Preparar datos**: Ejecutar INSERTs
2. **Iniciar servicios**: Backend (8080) + Frontend (3000)
3. **Probar frontend**: Login y navegación
4. **Probar API**: Usar Postman con la colección
5. **Verificar seguridad**: Intentar accesos no autorizados
6. **Validar datos**: Probar casos de error

¡Todas las pruebas deberían pasar correctamente si la integración está funcionando bien! 🎉