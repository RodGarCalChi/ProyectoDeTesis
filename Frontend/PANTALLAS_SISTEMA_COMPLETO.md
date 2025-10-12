# Sistema PharmaFlow - Pantallas por Rol de Usuario

## Resumen del Sistema

El sistema PharmaFlow es una aplicación completa para la gestión logística farmacéutica en Perú, diseñada siguiendo las normativas DIGEMID y Buenas Prácticas de Almacenamiento (BPAs).

## Roles de Usuario y Pantallas Asignadas

### 1. **Cliente** 🏥
**Pantallas disponibles:**
- `/cliente-portal` - Portal del Cliente
- `/dashboard` - Dashboard General

**Funcionalidades:**
- Consultar catálogo de productos disponibles
- Realizar pedidos online
- Seguimiento de pedidos en tiempo real
- Consultar facturas y estado de pagos
- Gestionar información del perfil
- Ver historial de compras

---

### 2. **Recepción** 📦
**Pantallas disponibles:**
- `/recepcion-mercaderia` - Recepción de Mercadería
- `/movimientos` - Movimientos de Inventario
- `/dashboard` - Dashboard General

**Funcionalidades:**
- Registrar recepción de mercadería
- Verificar documentos de proveedores
- Control de temperatura en recepción
- Inspección física de productos
- Generar actas de recepción
- Gestionar detalles por lote y producto

---

### 3. **Operaciones** 🏪
**Pantallas disponibles:**
- `/almacenamiento` - Gestión de Almacenamiento
- `/movimientos-stock` - Movimientos de Stock
- `/registro-inventario` - Registro de Inventario
- `/dashboard` - Dashboard General

**Funcionalidades:**
- Gestión completa de almacenamiento
- Control de ubicaciones y zonas
- Movimientos de entrada, salida, transferencia y ajustes
- Registro y actualización de inventario
- Reubicación de productos
- Control de estados de almacenamiento

---

### 4. **Calidad** 🔬
**Pantallas disponibles:**
- `/control` - Control de Calidad
- `/dashboard` - Dashboard General

**Funcionalidades:**
- Inspecciones de calidad por lotes
- Control de temperatura y condiciones ambientales
- Aprobación/rechazo de productos
- Gestión de documentos técnicos
- Reportes de calidad
- Seguimiento de certificaciones

---

### 5. **Despacho** 🚚
**Pantallas disponibles:**
- `/despacho` - Centro de Despacho
- `/movimientos` - Movimientos de Inventario
- `/dashboard` - Dashboard General

**Funcionalidades:**
- Gestión de órdenes de despacho
- Planificación de rutas de entrega
- Control de estados de despacho
- Generación de guías de remisión
- Seguimiento de entregas
- Gestión de transportistas y vehículos

---

### 6. **Área Administrativa** 📋
**Pantallas disponibles:**
- `/ordenes` - Gestión de Órdenes
- `/registro-inventario` - Registro de Inventario
- `/dashboard` - Dashboard General

**Funcionalidades:**
- Gestión completa de órdenes de compra y venta
- Seguimiento de estados de órdenes
- Control de prioridades
- Reportes administrativos
- Gestión de clientes y proveedores

---

### 7. **Director Técnico** 👨‍💼
**Pantallas disponibles (acceso completo):**
- `/dashboard` - Dashboard General
- `/cliente-portal` - Portal del Cliente (vista)
- `/recepcion-mercaderia` - Recepción de Mercadería
- `/almacenamiento` - Gestión de Almacenamiento
- `/movimientos-stock` - Movimientos de Stock
- `/despacho` - Centro de Despacho
- `/control` - Control de Calidad
- `/ordenes` - Gestión de Órdenes
- `/movimientos` - Movimientos de Inventario
- `/registro-inventario` - Registro de Inventario

**Funcionalidades:**
- Acceso completo a todas las funcionalidades del sistema
- Supervisión de todos los procesos
- Reportes ejecutivos
- Configuración del sistema
- Gestión de usuarios y permisos

---

## Características Técnicas del Sistema

### Diseño y UX
- **Diseño responsivo** adaptado a tablets y dispositivos móviles
- **Iconos intuitivos** con emojis para mejor usabilidad
- **Colores diferenciados** por estado y prioridad
- **Navegación contextual** según el rol del usuario
- **Feedback visual** en tiempo real

### Funcionalidades Transversales
- **Sistema de autenticación** con roles y permisos
- **Navegación protegida** por rol
- **Estados en tiempo real** con colores diferenciados
- **Búsquedas y filtros** avanzados
- **Exportación de datos** en múltiples formatos
- **Notificaciones** y alertas automáticas

### Cumplimiento Normativo
- **Trazabilidad completa** de productos y lotes
- **Control de temperatura** y condiciones ambientales
- **Documentación** según normativas DIGEMID
- **Buenas Prácticas de Almacenamiento** (BPAs)
- **Auditoría** de todas las operaciones
- **Reportes regulatorios** automatizados

### Tecnologías Utilizadas
- **Frontend:** Next.js 14, React, TypeScript, Tailwind CSS
- **Backend:** Spring Boot, Java, JPA/Hibernate
- **Base de Datos:** MySQL con UUID como identificadores
- **Autenticación:** JWT con roles y permisos
- **Arquitectura:** RESTful API con separación de capas

---

## Flujos de Trabajo Principales

### 1. Flujo de Recepción
Cliente → Proveedor → Recepción → Control Calidad → Almacenamiento

### 2. Flujo de Despacho
Cliente → Orden → Preparación → Control Calidad → Despacho → Entrega

### 3. Flujo de Inventario
Recepción → Almacenamiento → Movimientos → Control → Reportes

### 4. Flujo de Calidad
Recepción → Inspección → Aprobación/Rechazo → Documentación → Liberación

---

## Próximas Mejoras Sugeridas

1. **Integración con APIs externas** (SUNAT, bancos, transportistas)
2. **Módulo de reportes avanzados** con gráficos y dashboards
3. **Aplicación móvil** para operadores de campo
4. **Integración con sensores IoT** para monitoreo automático
5. **Sistema de notificaciones push** en tiempo real
6. **Módulo de facturación electrónica** integrado
7. **Sistema de gestión documental** con firma digital

---

*Documento generado automáticamente - Sistema PharmaFlow v1.0*