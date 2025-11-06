# Documentación Swagger/OpenAPI

## Acceso a Swagger UI

Una vez que la aplicación esté corriendo, puedes acceder a la documentación interactiva de Swagger en:

### URL Principal
```
http://localhost:8080/swagger-ui.html
```

### Documentación JSON
```
http://localhost:8080/api-docs
```

## Características de Swagger UI

### 1. Exploración de APIs
- **Navegación por Tags**: Las APIs están organizadas por categorías:
  - 1. Operadores Logísticos
  - 2. Almacenes
  - 3. Zonas
  - 4. Ubicaciones
  - 5. Lotes
  - 6. Palets
  - 7. Cajas
  - Y más...

### 2. Probar APIs Directamente
- Click en cualquier endpoint
- Click en "Try it out"
- Completa los parámetros requeridos
- Click en "Execute"
- Ver la respuesta en tiempo real

### 3. Ejemplos de Datos
Cada endpoint incluye:
- Descripción detallada
- Parámetros requeridos y opcionales
- Esquemas de request/response
- Códigos de respuesta HTTP
- Ejemplos de JSON

## Grupos de APIs

### Jerarquía de Almacén
Endpoints relacionados con la estructura jerárquica:
- Operadores Logísticos
- Almacenes
- Zonas
- Ubicaciones
- Lotes
- Palets
- Cajas

### Gestión General
Endpoints de entidades base:
- Clientes
- Productos
- Usuarios

### Operaciones
Endpoints de procesos operativos:
- Recepciones de Mercadería
- Actas de Recepción
- Inventario por Cliente

## Ejemplos de Uso

### 1. Crear un Operador Logístico

**Endpoint**: `POST /api/operadores-logisticos`

**Request Body**:
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

**Response** (201 Created):
```json
{
  "success": true,
  "message": "Operador logístico creado exitosamente",
  "data": {
    "id": "uuid-generado",
    "nombre": "DHL Supply Chain Perú",
    "ruc": "20123456789",
    ...
  }
}
```

### 2. Crear un Almacén

**Endpoint**: `POST /api/almacenes`

**Request Body**:
```json
{
  "nombre": "Almacén Central Lima",
  "direccion": "Av. Los Frutales 456, Lima",
  "operadorLogisticoId": "uuid-del-operador",
  "clienteId": "uuid-del-cliente",
  "tieneAreaControlados": true
}
```

### 3. Listar Zonas de un Almacén

**Endpoint**: `GET /api/zonas/almacen/{almacenId}`

**Path Parameter**: 
- `almacenId`: UUID del almacén

**Response** (200 OK):
```json
{
  "success": true,
  "data": [
    {
      "id": "uuid-zona-1",
      "nombre": "Zona Ultra Congelada A",
      "tipo": "ULT",
      "almacenId": "uuid-almacen",
      "almacenNombre": "Almacén Central Lima"
    }
  ]
}
```

## Filtros y Búsquedas

Swagger UI incluye un filtro de búsqueda en la parte superior que permite:
- Buscar endpoints por nombre
- Filtrar por tags
- Buscar por descripción

## Exportar Documentación

### Formato OpenAPI JSON
```bash
curl http://localhost:8080/api-docs > openapi.json
```

### Formato OpenAPI YAML
```bash
curl http://localhost:8080/api-docs.yaml > openapi.yaml
```

## Configuración Personalizada

La configuración de Swagger se encuentra en:
- `OpenApiConfig.java` - Configuración principal
- `application-swagger.properties` - Propiedades adicionales

### Cambiar el Puerto
Si tu aplicación corre en un puerto diferente, actualiza la URL:
```
http://localhost:PUERTO/swagger-ui.html
```

### Deshabilitar Swagger en Producción
En `application-production.properties`:
```properties
springdoc.swagger-ui.enabled=false
springdoc.api-docs.enabled=false
```

## Seguridad

Actualmente, Swagger está configurado sin autenticación para facilitar el desarrollo.

Para agregar autenticación en producción, actualiza `OpenApiConfig.java`:

```java
@Bean
public OpenAPI customOpenAPI() {
    return new OpenAPI()
            .components(new Components()
                    .addSecuritySchemes("bearer-jwt",
                            new SecurityScheme()
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .bearerFormat("JWT")))
            .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));
}
```

## Troubleshooting

### Swagger UI no carga
1. Verificar que la aplicación esté corriendo
2. Verificar el puerto correcto
3. Limpiar caché del navegador
4. Verificar que la dependencia `springdoc-openapi-starter-webmvc-ui` esté en build.gradle

### Endpoints no aparecen
1. Verificar que los controladores tengan `@RestController`
2. Verificar que los métodos tengan anotaciones de mapping (`@GetMapping`, `@PostMapping`, etc.)
3. Reiniciar la aplicación

### Errores al ejecutar requests
1. Verificar que el servidor backend esté corriendo
2. Verificar los datos del request
3. Revisar los logs del servidor para más detalles

## Recursos Adicionales

- [Documentación SpringDoc](https://springdoc.org/)
- [Especificación OpenAPI](https://swagger.io/specification/)
- [Swagger UI](https://swagger.io/tools/swagger-ui/)

## Ventajas de Usar Swagger

1. **Documentación Automática**: Se genera automáticamente desde el código
2. **Pruebas Interactivas**: Probar APIs sin necesidad de Postman
3. **Sincronización**: Siempre está actualizada con el código
4. **Colaboración**: Fácil de compartir con el equipo
5. **Generación de Clientes**: Puede generar código cliente en varios lenguajes
