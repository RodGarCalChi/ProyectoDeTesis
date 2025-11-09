# 📊 Guía de Importación de Usuarios

## 📁 Archivos Creados

1. **`usuarios_data.csv`** - Archivo CSV para importar en Excel
2. **`insert_usuarios.sql`** - Script SQL para ejecutar directamente en MySQL

## 🔧 Método 1: Importar con SQL (Recomendado)

### Paso 1: Conectar a MySQL
```bash
mysql -u root -p -P 3308
```

### Paso 2: Ejecutar el script
```sql
source Backend/insert_usuarios.sql
```

O copiar y pegar el contenido del archivo en MySQL Workbench.

## 📊 Método 2: Importar desde CSV/Excel

### Paso 1: Abrir el CSV en Excel
1. Abrir `usuarios_data.csv` en Excel
2. Guardar como `.xlsx` si lo deseas

### Paso 2: Importar en MySQL Workbench
1. Abrir MySQL Workbench
2. Conectar a la base de datos `basededatoslogisticofarmaceutico`
3. Click derecho en la tabla `usuarios` → "Table Data Import Wizard"
4. Seleccionar el archivo CSV
5. Mapear las columnas correctamente
6. Ejecutar la importación

## 👥 Usuarios Incluidos

| Email | Contraseña | Rol | Documento | Acceso Movimientos |
|-------|------------|-----|-----------|-------------------|
| admin@pharmaflow.com | password | DirectorTecnico | 12345678 | ❌ No |
| recepcion@pharmaflow.com | password | Recepcion | 87654321 | ✅ Sí |
| operaciones@pharmaflow.com | password | Operaciones | 11223344 | ❌ No |
| calidad@pharmaflow.com | password | Calidad | 55667788 | ❌ No |
| despacho@pharmaflow.com | password | Despacho | 99887766 | ❌ No |
| cliente@pharmaflow.com | password | Cliente | 44332211 | ❌ No |
| recepcion2@pharmaflow.com | password | Recepcion | 12121212 | ✅ Sí |
| recepcion3@pharmaflow.com | password | Recepcion | 34343434 | ✅ Sí |
| operaciones2@pharmaflow.com | password | Operaciones | 56565656 | ❌ No |
| calidad2@pharmaflow.com | password | Calidad | 78787878 | ❌ No |

## 🔐 Información de Seguridad

⚠️ **IMPORTANTE**: Estas contraseñas son para desarrollo/pruebas únicamente.

En producción:
- Usar contraseñas seguras
- Implementar hashing con BCrypt
- Cambiar todas las contraseñas por defecto

## ✅ Verificar la Importación

Después de importar, ejecuta:

```sql
-- Ver todos los usuarios
SELECT 
    nombres,
    apellidos,
    email,
    rol,
    activo
FROM usuarios
ORDER BY rol, nombres;

-- Contar por rol
SELECT 
    rol,
    COUNT(*) as total
FROM usuarios
GROUP BY rol;

-- Verificar usuarios de Recepción
SELECT * FROM usuarios WHERE rol = 'Recepcion';
```

## 🧪 Probar Login

### Con Postman/Insomnia:
```http
POST http://localhost:8081/api/auth/login
Content-Type: application/json

{
    "email": "recepcion@pharmaflow.com",
    "password": "password"
}
```

### Con Frontend:
1. Ir a `http://localhost:3000`
2. Usar cualquiera de los emails de la tabla
3. Contraseña: `password`

## 📋 Estructura de la Tabla

```sql
CREATE TABLE usuarios (
    id BINARY(16) PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    documento VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT true,
    ultimo_acceso TIMESTAMP NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    tipo_usuario VARCHAR(50)
);
```

## 🔄 Actualizar Datos

### Cambiar contraseña:
```sql
UPDATE usuarios 
SET password_hash = 'nueva_password' 
WHERE email = 'usuario@ejemplo.com';
```

### Desactivar usuario:
```sql
UPDATE usuarios 
SET activo = 0 
WHERE email = 'usuario@ejemplo.com';
```

### Cambiar rol:
```sql
UPDATE usuarios 
SET rol = 'NuevoRol' 
WHERE email = 'usuario@ejemplo.com';
```

## 🎯 Roles Disponibles

- `DirectorTecnico` - Administrador del sistema
- `Recepcion` - Acceso a módulo de movimientos ✅
- `Operaciones` - Operaciones de almacén
- `Calidad` - Control de calidad
- `Despacho` - Gestión de despachos
- `Cliente` - Clientes externos
- `AreaAdministrativa` - Área administrativa

## 📞 Soporte

Si tienes problemas con la importación:
1. Verificar que la base de datos existe
2. Verificar que la tabla `usuarios` existe
3. Verificar permisos de usuario MySQL
4. Revisar logs del backend para errores