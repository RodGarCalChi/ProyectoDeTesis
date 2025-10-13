# 🧪 Cómo Probar la Funcionalidad de Recepción

## 🚀 Pasos para Probar

### 1. Iniciar el Sistema
```bash
# Terminal 1 - Backend
cd Backend
./gradlew bootRun

# Terminal 2 - Frontend  
cd Frontend
npm run dev
```

### 2. Acceder al Sistema
1. Abrir navegador en `http://localhost:3000`
2. En la pantalla de login:
   - **Email**: Cualquier email (ej: `recepcion@pharmaflow.com`)
   - **Contraseña**: Cualquier contraseña (ej: `password`)
   - **Tipo de Usuario**: Seleccionar "👥 Recepción"
3. Hacer clic en "Iniciar Sesión"

### 3. Verificar Redirección Automática
- El sistema debería redirigir automáticamente a `/recepcion-mercaderia`
- Deberías ver la pantalla "Recepción de Mercadería"

### 4. Probar la Funcionalidad

#### ✅ Carga de Datos
- Al abrir la pantalla, el sistema debería cargar automáticamente:
  - Lista de clientes activos en el dropdown
  - Lista de productos disponibles en el dropdown

#### ✅ Completar Formulario
1. **Número de Documento**: Ingresar algo como "REC-2024-001"
2. **Cliente**: Seleccionar un cliente de la lista desplegable
3. **Fecha de Llegada**: Se auto-completa con la fecha actual
4. **Hora de Llegada**: Se auto-completa con la hora actual
5. **Recepcionista**: Se auto-completa con el usuario logueado

#### ✅ Agregar Productos
1. Seleccionar un producto de la lista desplegable
2. Ingresar cantidad (número positivo)
3. Agregar observaciones (opcional)
4. Hacer clic en "➕ Agregar"
5. El producto debería aparecer en la tabla inferior

#### ✅ Enviar Datos
1. Completar observaciones generales (opcional)
2. Hacer clic en "💾 Registrar Recepción"
3. El sistema debería mostrar mensaje de éxito
4. El formulario se debería limpiar automáticamente

## 🔍 Qué Verificar

### ✅ Navegación
- [ ] El usuario "Recepcion" puede ver el menú de navegación
- [ ] La opción "Recepción" está disponible en el menú
- [ ] Al hacer clic redirige correctamente

### ✅ Carga de Datos
- [ ] Los clientes se cargan desde la API `/api/clientes/activos`
- [ ] Los productos se cargan desde la API `/api/productos`
- [ ] Las listas desplegables muestran los datos correctamente

### ✅ Validaciones
- [ ] No permite enviar sin número de documento
- [ ] No permite enviar sin seleccionar cliente
- [ ] No permite enviar sin agregar al menos un producto
- [ ] No permite agregar productos sin cantidad válida

### ✅ Integración API
- [ ] Los datos se envían correctamente a `/api/recepciones`
- [ ] El mapeo de datos es correcto
- [ ] Se manejan los errores de conexión
- [ ] Se muestran mensajes informativos al usuario

## 🐛 Posibles Problemas y Soluciones

### Problema: "No se cargan los clientes"
**Causa**: API del backend no está funcionando
**Solución**: 
1. Verificar que el backend esté corriendo en puerto 8080
2. Revisar que el endpoint `/api/clientes/activos` esté disponible
3. Verificar la consola del navegador para errores de CORS

### Problema: "Error al registrar recepción"
**Causa**: Problema con el endpoint de recepciones
**Solución**:
1. Verificar que el endpoint `/api/recepciones` esté disponible
2. Revisar los logs del backend para errores
3. Verificar que los datos se estén mapeando correctamente

### Problema: "Usuario no puede acceder"
**Causa**: Problema con roles o autenticación
**Solución**:
1. Verificar que se seleccione "Recepcion" en el login
2. Revisar que el AuthContext esté funcionando
3. Verificar que el ProtectedRoute permita el acceso

### Problema: "Página no encontrada"
**Causa**: Ruta no configurada correctamente
**Solución**:
1. Verificar que el archivo `/recepcion-mercaderia/page.tsx` exista
2. Revisar que la ruta esté en el sistema de navegación
3. Verificar que no haya errores de compilación

## 📊 Datos de Prueba

### Usuarios de Prueba
```
Email: recepcion@pharmaflow.com
Contraseña: password
Rol: Recepcion
```

### Datos de Ejemplo para Formulario
```
Número de Documento: REC-2024-001
Cliente: [Seleccionar de la lista]
Fecha: [Auto-completada]
Hora: [Auto-completada]
Recepcionista: [Auto-completado]
Productos: [Seleccionar y agregar cantidades]
Observaciones: "Mercadería recibida en buen estado"
```

## 🎯 Resultado Esperado

Al completar todos los pasos, deberías poder:
1. ✅ Hacer login como personal de recepción
2. ✅ Acceder automáticamente a la pantalla de recepción
3. ✅ Ver los datos cargados desde las APIs
4. ✅ Completar y enviar el formulario exitosamente
5. ✅ Ver mensajes de confirmación
6. ✅ Tener el formulario limpio para la siguiente recepción

¡La integración está completa y lista para usar! 🎉