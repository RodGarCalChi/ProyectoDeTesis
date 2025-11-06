# 🔧 Solución de Errores

## Errores Encontrados y Soluciones

### Error 1: Conflicto de Versiones SpringDoc + HATEOAS

**Error:**
```
The calling method's class, org.springdoc.core.providers.HateoasHalProvider
The called method's class, org.springframework.boot.autoconfigure.hateoas.HateoasProperties
```

**Solución Aplicada:**
- ✅ Actualizada versión de SpringDoc de `2.3.0` a `2.7.0`
- ✅ Eliminada dependencia de HATEOAS (no es necesaria para este proyecto)
- ✅ Actualizado `OpenApiConfig.java` para no depender de HATEOAS

### Error 2: Foreign Key Constraint Violation

**Error:**
```
Cannot add or update a child row: a foreign key constraint fails
(`BaseDeDatos`.`#sql-3cc_4ec0`, CONSTRAINT `FKl24lky4d9yft74p0lpuqg4rgl` 
FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`))
```

**Causa:**
La tabla `almacenes` tiene registros con `cliente_id` o `operador_logistico_id` que no existen en las tablas `clientes` u `operadores_logisticos`.

**Solución:**

## 🚀 Pasos para Solucionar

### Paso 1: Ejecutar Script SQL de Limpieza

Abre tu cliente MySQL (MySQL Workbench, DBeaver, etc.) y ejecuta:

```sql
USE BaseDeDatos;

-- Limpiar referencias inválidas
UPDATE almacenes 
SET cliente_id = NULL 
WHERE cliente_id IS NOT NULL 
AND cliente_id NOT IN (SELECT id FROM clientes);

UPDATE almacenes 
SET operador_logistico_id = NULL 
WHERE operador_logistico_id IS NOT NULL 
AND operador_logistico_id NOT IN (SELECT id FROM operadores_logisticos);
```

**O ejecuta el archivo:**
```bash
mysql -u root -p BaseDeDatos < Backend/SQL_FIX_RAPIDO.sql
```

### Paso 2: Recargar Dependencias de Gradle

#### En IntelliJ IDEA:
1. Click derecho en `build.gradle`
2. Selecciona "Reload Gradle Project"
3. Espera a que termine

#### Desde Terminal:
```bash
cd Backend
./gradlew clean build --refresh-dependencies
```

### Paso 3: Reiniciar la Aplicación

```bash
./gradlew bootRun
```

## ✅ Verificación

Si todo está correcto, deberías ver:

```
Started BackendApplication in X.XXX seconds
```

Y podrás acceder a:
- http://localhost:8080 (Página principal)
- http://localhost:8080/swagger-ui.html (Swagger UI)

## 🔍 Verificar Base de Datos

Para verificar que los datos están limpios:

```sql
-- Ver almacenes con referencias NULL
SELECT id, nombre, cliente_id, operador_logistico_id 
FROM almacenes 
WHERE cliente_id IS NULL OR operador_logistico_id IS NULL;

-- Contar registros
SELECT 
    (SELECT COUNT(*) FROM operadores_logisticos) as operadores,
    (SELECT COUNT(*) FROM clientes) as clientes,
    (SELECT COUNT(*) FROM almacenes) as almacenes;
```

## 📝 Notas Importantes

### Sobre los Datos NULL

Los almacenes con `cliente_id` o `operador_logistico_id` NULL:
- ✅ No causarán errores al iniciar la aplicación
- ⚠️ No podrán ser usados hasta que se les asigne un cliente y operador válidos
- 💡 Puedes actualizarlos después usando la API

### Crear Nuevos Datos

Después de que la aplicación inicie correctamente, usa Swagger UI para crear:

1. **Operadores Logísticos** (sin dependencias)
2. **Clientes** (sin dependencias)
3. **Almacenes** (con IDs válidos de operador y cliente)

## 🐛 Si Persisten los Errores

### Error: "Gradle sync failed"
```bash
cd Backend
./gradlew clean
./gradlew build --refresh-dependencies
```

### Error: "Port 8080 already in use"
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:8080 | xargs kill -9
```

### Error: "Cannot connect to database"
1. Verifica que MySQL esté corriendo
2. Verifica las credenciales en `application.properties`
3. Verifica que la base de datos `BaseDeDatos` exista

## 📚 Archivos de Ayuda Creados

- `SQL_FIX_RAPIDO.sql` - Script rápido de limpieza
- `SQL_LIMPIAR_DATOS_INCONSISTENTES.sql` - Script detallado con verificaciones
- `INICIO_RAPIDO_SWAGGER.md` - Guía para usar Swagger
- `SWAGGER_README.md` - Documentación completa de Swagger

## 🎯 Próximos Pasos

Una vez que la aplicación inicie correctamente:

1. Accede a http://localhost:8080/swagger-ui.html
2. Prueba crear un Operador Logístico
3. Prueba crear un Cliente
4. Prueba crear un Almacén con los IDs anteriores
5. Continúa con la jerarquía completa

## 💡 Prevenir Futuros Errores

Para evitar estos problemas en el futuro:

1. **Siempre crea las entidades en orden:**
   - Operadores Logísticos
   - Clientes
   - Productos
   - Almacenes (requiere operador + cliente)
   - Zonas (requiere almacén)
   - etc.

2. **Usa los scripts de prueba:**
   - `TEST_API_COMMANDS.ps1` (Windows)
   - `TEST_API_COMMANDS.sh` (Linux/Mac)

3. **Verifica los IDs antes de crear relaciones:**
   - Copia los IDs de las respuestas
   - Úsalos en los siguientes requests

## 🆘 Soporte

Si los errores persisten:
1. Revisa los logs completos en la consola
2. Verifica que todas las tablas existan en la base de datos
3. Verifica que las dependencias de Gradle se hayan descargado correctamente
