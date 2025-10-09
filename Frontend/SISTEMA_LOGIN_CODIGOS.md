# Sistema de Login - Códigos de Prueba

## Información General

El sistema PharmaFlow cuenta con un sistema de autenticación híbrido que funciona tanto con backend real como en modo demostración. Puedes usar cualquier combinación de email y contraseña para acceder al sistema.

## Roles Disponibles y Accesos

### 👤 Cliente
- **Acceso principal**: Dashboard de inventario
- **Funcionalidades**: Consulta de productos, stock y disponibilidad
- **Usuario demo**: Pedro

### 👥 Recepción  
- **Acceso principal**: Movimientos de inventario
- **Funcionalidades**: Registro de entradas y salidas de mercadería
- **Usuario demo**: María
- **Responsabilidades**: Control de recepción de productos farmacéuticos

### ⚙️ Operaciones
- **Acceso principal**: Dashboard general
- **Funcionalidades**: Gestión operativa del almacén
- **Usuario demo**: Juan
- **Responsabilidades**: Coordinación de actividades diarias

### 🔍 Control de Calidad
- **Acceso principal**: Panel de control y supervisión
- **Funcionalidades**: Auditoría, control de calidad, reportes regulatorios
- **Usuario demo**: Ana
- **Responsabilidades**: Cumplimiento BPAs y normativas DIGEMID

### 🚚 Despacho
- **Acceso principal**: Dashboard de inventario
- **Funcionalidades**: Gestión de salidas y distribución
- **Usuario demo**: Carlos
- **Responsabilidades**: Coordinación de entregas y despachos

### 👔 Área Administrativa
- **Acceso principal**: Gestión de órdenes
- **Funcionalidades**: Órdenes de compra, venta y transferencias
- **Usuario demo**: Patricia
- **Responsabilidades**: Gestión administrativa y financiera

### 👨‍⚕️ Director Técnico
- **Acceso principal**: Dashboard completo
- **Funcionalidades**: Acceso total al sistema, supervisión general
- **Usuario demo**: Director
- **Responsabilidades**: Supervisión técnica y cumplimiento normativo

## Códigos de Prueba Sugeridos

### Formato de Login
```
Email: [cualquier_email@ejemplo.com]
Contraseña: [cualquier_contraseña]
Tipo de Usuario: [seleccionar del dropdown]
```

### Ejemplos de Credenciales de Prueba

#### Para Recepción
```
Email: maria.recepcion@pharmaflow.com
Contraseña: recepcion123
Tipo de Usuario: 👥 Recepción
```

#### Para Control de Calidad
```
Email: ana.calidad@pharmaflow.com
Contraseña: calidad123
Tipo de Usuario: 🔍 Control de Calidad
```

#### Para Director Técnico
```
Email: director@pharmaflow.com
Contraseña: director123
Tipo de Usuario: 👨‍⚕️ Director Técnico
```

#### Para Área Administrativa
```
Email: patricia.admin@pharmaflow.com
Contraseña: admin123
Tipo de Usuario: 👔 Área Administrativa
```

## Flujo de Navegación por Rol

### Cliente → Dashboard
- Vista de inventario general
- Consulta de productos disponibles
- Información de stock y ubicaciones

### Recepción → Movimientos
- Registro de entradas de mercadería
- Control de documentación
- Verificaciones BPAs obligatorias

### Control de Calidad → Control
- Panel de auditoría completo
- Gestión de productos en cuarentena
- Generación de reportes DIGEMID

### Área Administrativa → Órdenes
- Gestión de órdenes de compra
- Control de órdenes de venta
- Seguimiento de transferencias

### Director Técnico → Dashboard
- Acceso completo a todas las funcionalidades
- Supervisión general del sistema
- Reportes ejecutivos

## Características del Sistema

### Modo Demostración
- ✅ Funciona sin conexión al backend
- ✅ Datos de ejemplo precargados
- ✅ Navegación completa entre pantallas
- ✅ Validaciones del lado cliente

### Modo Producción
- ✅ Integración con backend Spring Boot
- ✅ Autenticación con BCrypt
- ✅ Tokens JWT para sesiones
- ✅ Fallback automático a modo demo

### Seguridad
- 🔒 Protección de rutas por rol
- 🔒 Validación de permisos en cada pantalla
- 🔒 Logout automático en caso de error
- 🔒 Limpieza de datos de sesión

## Instrucciones de Uso

1. **Acceder al Login**: Navega a `/login`
2. **Ingresar Credenciales**: Usa cualquier email y contraseña
3. **Seleccionar Rol**: Elige el tipo de usuario del dropdown
4. **Iniciar Sesión**: El sistema te redirigirá según tu rol
5. **Navegar**: Usa el menú superior para cambiar entre pantallas

## Notas Técnicas

- El sistema detecta automáticamente si el backend está disponible
- En caso de error del backend, activa el modo demostración
- Los datos se persisten en localStorage para la sesión
- Cada rol tiene acceso a pantallas específicas según las normativas farmacéuticas

## Soporte y Desarrollo

Para más información sobre el desarrollo del sistema, consulta:
- `PANTALLAS_DESARROLLADAS.md` - Documentación completa de pantallas
- `Backend/TESTING_GUIDE.md` - Guía de pruebas del backend
- Código fuente en `Frontend/src/` y `Backend/src/`