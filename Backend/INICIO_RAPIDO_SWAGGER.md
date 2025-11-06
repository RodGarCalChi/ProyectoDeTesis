# 🚀 Inicio Rápido - Swagger UI

## Paso 1: Recargar Dependencias

### En IntelliJ IDEA:
1. Click derecho en `build.gradle`
2. Selecciona "Reload Gradle Project"
3. Espera a que descargue las dependencias

### Desde Terminal:
```bash
cd Backend
./gradlew build --refresh-dependencies
```

## Paso 2: Reiniciar la Aplicación

1. Detén la aplicación si está corriendo
2. Inicia nuevamente desde tu IDE o con:
```bash
./gradlew bootRun
```

## Paso 3: Acceder a Swagger

### Opción 1: Página Principal
Abre tu navegador y ve a:
```
http://localhost:8080
```

Verás una página de bienvenida con botones para acceder a Swagger.

### Opción 2: Swagger UI Directo
```
http://localhost:8080/swagger-ui.html
```

### Opción 3: OpenAPI JSON
```
http://localhost:8080/api-docs
```

## 🎯 Probar tu Primera API

### 1. En Swagger UI, busca "Operadores Logísticos"

### 2. Click en "POST /api/operadores-logisticos"

### 3. Click en "Try it out"

### 4. Pega este JSON en el Request body:
```json
{
  "nombre": "DHL Supply Chain Perú",
  "ruc": "20123456789",
  "direccion": "Av. Industrial 1234, Callao",
  "telefono": "+51 1 234-5678",
  "email": "contacto@dhl.com.pe",
  "activo": true
}
```

### 5. Click en "Execute"

### 6. Verás la respuesta:
```json
{
  "success": true,
  "message": "Operador logístico creado exitosamente",
  "data": {
    "id": "uuid-generado-aqui",
    "nombre": "DHL Supply Chain Perú",
    ...
  }
}
```

### 7. Copia el `id` de la respuesta para usarlo en los siguientes pasos

## 📋 Orden Recomendado de Pruebas

1. **Operadores Logísticos** - Crear operador
2. **Clientes** - Crear cliente  
3. **Productos** - Crear producto
4. **Almacenes** - Crear almacén (usa IDs de operador y cliente)
5. **Zonas** - Crear zona (usa ID de almacén)
6. **Ubicaciones** - Crear ubicación (usa ID de zona)
7. **Lotes** - Crear lote (usa IDs de cliente y producto)
8. **Palets** - Crear palet (usa IDs de lote y ubicación)
9. **Cajas** - Crear caja (usa IDs de palet, cliente y producto)

## 🔍 Características de Swagger UI

### Filtrar Endpoints
- Usa la barra de búsqueda en la parte superior
- Escribe palabras clave como "crear", "listar", "almacen"

### Ver Esquemas
- Scroll hasta el final de la página
- Sección "Schemas" muestra todos los modelos de datos

### Descargar Especificación
- Click en el link "OpenAPI definition" en la parte superior
- Descarga el JSON para importar en otras herramientas

### Probar con Diferentes Datos
- Cada endpoint tiene un botón "Try it out"
- Modifica los valores de ejemplo
- Click en "Execute" para probar

## ⚡ Atajos Útiles

### Ver Todos los Endpoints Expandidos
- Click en "Expand Operations" en la parte superior derecha

### Copiar cURL Command
- Después de ejecutar un request
- Busca "Curl" en la respuesta
- Copia el comando para usarlo en terminal

### Ver Response Headers
- Después de ejecutar un request
- Expande "Response headers"
- Útil para debugging

## 🐛 Solución de Problemas

### Swagger no carga
```bash
# Verificar que el servidor esté corriendo
curl http://localhost:8080/actuator/health

# Si no responde, reinicia la aplicación
```

### Error 404 en Swagger UI
```bash
# Verificar la URL correcta
http://localhost:8080/swagger-ui.html

# NO uses:
# http://localhost:8080/swagger-ui/
```

### Endpoints no aparecen
1. Verifica que los controladores tengan `@RestController`
2. Reinicia la aplicación
3. Limpia caché del navegador (Ctrl + Shift + R)

### Error al ejecutar requests
1. Verifica que el JSON sea válido
2. Revisa los logs del servidor
3. Verifica que los IDs de relaciones existan

## 📚 Recursos Adicionales

- **Documentación Completa**: Ver `SWAGGER_README.md`
- **Datos de Prueba**: Ver `DATOS_PRUEBA_API.json`
- **Scripts Automatizados**: Ver `TEST_API_COMMANDS.ps1` o `.sh`
- **Colección Postman**: Ver `POSTMAN_COLLECTION_JERARQUIA_COMPLETA.json`

## 💡 Tips

1. **Guarda los IDs**: Copia los IDs de las respuestas para usarlos en requests posteriores
2. **Usa el filtro**: Busca endpoints específicos con la barra de búsqueda
3. **Revisa los ejemplos**: Cada endpoint tiene ejemplos de request/response
4. **Prueba los GET primero**: Verifica que los datos se crearon correctamente
5. **Lee las descripciones**: Cada endpoint tiene información sobre qué hace

## 🎉 ¡Listo!

Ahora puedes explorar y probar todas las APIs desde Swagger UI de forma interactiva.

Para más información, consulta `SWAGGER_README.md`.
