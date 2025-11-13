# Guía para Generar Datos de Prueba

Esta guía te ayudará a generar datos de prueba para las tablas de **Operadores Logísticos**, **Zonas**, **Lotes** y **Palets**.

## 📋 Contenido

1. [Opción 1: Usando Postman](#opción-1-usando-postman)
2. [Opción 2: Usando SQL Directo](#opción-2-usando-sql-directo)
3. [Datos Generados](#datos-generados)

---

## Opción 1: Usando Postman

### Paso 1: Importar la Colección

1. Abre Postman
2. Click en **Import**
3. Selecciona el archivo `POSTMAN_DATA_GENERATION.json`
4. La colección se importará con 4 carpetas

### Paso 2: Configurar Variables

Antes de ejecutar las peticiones, necesitas configurar las variables de la colección:

1. Click derecho en la colección → **Edit**
2. Ve a la pestaña **Variables**
3. Reemplaza los valores de las variables:

```
almacen_id                    → ID de tu almacén existente
producto_paracetamol_id       → ID del producto Paracetamol
producto_ibuprofeno_id        → ID del producto Ibuprofeno
producto_amoxicilina_id       → ID del producto Amoxicilina
producto_insulina_id          → ID del producto Insulina
producto_omeprazol_id         → ID del producto Omeprazol
ubicacion_id                  → ID de una ubicación general
ubicacion_cadena_frio_id      → ID de una ubicación en cadena de frío
```

### Paso 3: Obtener IDs Necesarios

Ejecuta estos endpoints para obtener los IDs:

```bash
# Obtener almacenes
GET http://localhost:8080/api/almacenes

# Obtener productos
GET http://localhost:8080/api/productos

# Obtener ubicaciones
GET http://localhost:8080/api/ubicaciones
```

### Paso 4: Ejecutar las Peticiones

Ejecuta las carpetas en orden:

1. **1. Operadores Logísticos** - Crea 4 operadores logísticos
2. **2. Zonas** - Crea 5 zonas (requiere almacén)
3. **3. Lotes** - Crea 5 lotes (requiere productos)
4. **4. Palets** - Crea 6 palets (requiere lotes y ubicaciones)

Puedes ejecutar toda la carpeta usando **Run Collection** o ejecutar cada petición individualmente.

---

## Opción 2: Usando SQL Directo

### Paso 1: Abrir el Script

Abre el archivo `INSERT_DATA_SAMPLE.sql` en tu cliente MySQL favorito (MySQL Workbench, DBeaver, etc.)

### Paso 2: Verificar Prerequisitos

Asegúrate de tener:
- ✅ Al menos 1 almacén creado
- ✅ Al menos 5 productos creados (Paracetamol, Ibuprofeno, Amoxicilina, Insulina, Omeprazol)
- ✅ Al menos 1 ubicación creada

### Paso 3: Ejecutar el Script

1. Selecciona tu base de datos:
```sql
USE basededatoslogisticofarmaceutico;
```

2. Ejecuta el script completo o por secciones

El script usa variables automáticas para obtener los IDs necesarios:
```sql
SET @almacen_id = (SELECT id FROM almacenes LIMIT 1);
SET @producto_paracetamol = (SELECT id FROM productos WHERE nombre LIKE '%Paracetamol%' LIMIT 1);
```

### Paso 4: Verificar Datos

Al final del script hay queries de verificación que muestran:
- Cantidad de registros insertados
- Listado de datos insertados

---

## 📊 Datos Generados

### Almacenes (3 registros)

| Nombre | Dirección | Área Controlados |
|--------|-----------|------------------|
| Almacén Principal Lima | Av. Industrial 1234, Ate, Lima | ✅ Sí |
| Almacén Cadena de Frío | Av. Los Frutales 567, Santa Anita, Lima | ✅ Sí |
| Centro de Distribución Norte | Panamericana Norte Km 15, Puente Piedra, Lima | ❌ No |

### Operadores Logísticos (5 registros)

| Nombre | RUC | Teléfono | Email |
|--------|-----|----------|-------|
| DHL Express Perú | 20123456789 | 01-6157000 | contacto@dhl.com.pe |
| FedEx Perú | 20234567890 | 01-2117000 | info@fedex.com.pe |
| Olva Courier | 20345678901 | 01-7089000 | atencion@olvacourier.pe |
| Shalom Empresarial | 20456789012 | 01-5617000 | contacto@shalom.com.pe |

### Zonas (8 registros según TipoZona)

| Nombre | Tipo | Temperatura | Uso |
|--------|------|-------------|-----|
| Cámara de Congelación | CONGELADO | -20°C o menos | Vacunas, plasma |
| Cámara Refrigerada | REFRIGERADO | 2°C a 8°C | Insulina, biológicos |
| Almacenamiento Ambiente Controlado | SECO | 15°C a 25°C | Medicamentos generales |
| Ultra Low Temperature | ULT | -70°C o menos | Vacunas especiales |
| Almacén General Medicamentos | SECO | 15°C a 25°C | Medicamentos generales |
| Cámara Refrigerada Principal | REFRIGERADO | 2°C a 8°C | Cadena de frío |
| Cámara de Congelación Principal | CONGELADO | -20°C o menos | Cadena de frío |
| Cámara ULT Especializada | ULT | -70°C o menos | Cadena de frío |

### Lotes (7 registros)

| Número | Producto | Cantidad | Vencimiento | Zona Requerida | Proveedor |
|--------|----------|----------|-------------|----------------|-----------|
| LOTE-PAR-2024-001 | Paracetamol | 5000 | 2026-01-15 | SECO | Laboratorios Bagó |
| LOTE-IBU-2024-001 | Ibuprofeno | 3000 | 2026-02-10 | SECO | Laboratorios Roemmers |
| LOTE-AMO-2024-001 | Amoxicilina | 4000 | 2026-03-05 | SECO | Laboratorios Farmindustria |
| LOTE-INS-2024-001 | Insulina | 1000 | 2025-04-01 | REFRIGERADO | Novo Nordisk |
| LOTE-OME-2024-001 | Omeprazol | 6000 | 2026-05-20 | SECO | Laboratorios Tecnoquímicas |
| LOTE-PAR-2024-002 | Paracetamol | 4500 | 2026-06-10 | SECO | Laboratorios Bagó |
| LOTE-IBU-2024-002 | Ibuprofeno | 3500 | 2026-07-15 | SECO | Laboratorios Roemmers |

### Palets (8 registros)

| Código | Lote | Capacidad | Cajas Actuales | Zona | Disponible |
|--------|------|-----------|----------------|------|------------|
| PLT-PAR-001 | Paracetamol | 50 | 0 | SECO | ✅ |
| PLT-IBU-001 | Ibuprofeno | 40 | 0 | SECO | ✅ |
| PLT-AMO-001 | Amoxicilina | 45 | 0 | SECO | ✅ |
| PLT-OME-001 | Omeprazol | 60 | 0 | SECO | ✅ |
| PLT-INS-REF-001 | Insulina | 30 | 0 | REFRIGERADO | ✅ |
| PLT-PAR-002 | Paracetamol | 50 | 25 | SECO | ✅ |
| PLT-IBU-002 | Ibuprofeno | 40 | 15 | SECO | ✅ |
| PLT-AMO-002 | Amoxicilina | 45 | 30 | SECO | ✅ |

---

## 🔍 Verificación

### Verificar Operadores Logísticos
```sql
SELECT * FROM operadores_logisticos ORDER BY nombre;
```

### Verificar Zonas
```sql
SELECT z.nombre, z.tipo, a.nombre as almacen 
FROM zonas z 
INNER JOIN almacenes a ON z.almacen_id = a.id;
```

### Verificar Lotes
```sql
SELECT l.numero, p.nombre as producto, l.cantidad_disponible, l.fecha_vencimiento
FROM lotes l
INNER JOIN productos p ON l.producto_id = p.id;
```

### Verificar Palets
```sql
SELECT p.codigo, l.numero as lote, p.cajas_actuales, p.capacidad_maxima, p.disponible
FROM palets p
INNER JOIN lotes l ON p.lote_id = l.id;
```

---

## ⚠️ Notas Importantes

1. **Orden de Ejecución**: Respeta el orden de creación:
   - Operadores Logísticos (independiente)
   - Almacenes (requiere operador logístico y cliente)
   - Zonas (requiere almacén) - Usa TipoZona: CONGELADO, REFRIGERADO, SECO, ULT
   - Lotes (requiere productos)
   - Palets (requiere lotes y ubicaciones)

2. **IDs Únicos**: Los códigos y números deben ser únicos:
   - RUC de operadores
   - Número de lote
   - Código de palet

3. **Tipos de Zona según Temperatura**:
   - **CONGELADO**: -20°C o menos (vacunas, plasma)
   - **REFRIGERADO**: 2°C a 8°C (insulina, biológicos)
   - **SECO**: 15°C a 25°C (medicamentos generales)
   - **ULT**: -70°C o menos (vacunas especiales, muestras biológicas)

4. **Asignación de Productos a Zonas**:
   - Paracetamol, Ibuprofeno, Amoxicilina, Omeprazol → Zona SECO
   - Insulina → Zona REFRIGERADO
   - Vacunas especiales → Zona ULT o CONGELADO

5. **Fechas de Vencimiento**: Los lotes tienen fechas de vencimiento futuras (2025-2026)

---

## 🚀 Próximos Pasos

Después de generar los datos, puedes:

1. Crear recepciones de mercadería usando estos lotes
2. Asignar palets a ubicaciones específicas
3. Realizar movimientos de stock
4. Generar reportes de inventario

---

## 📞 Soporte

Si encuentras algún problema:
1. Verifica que los prerequisitos estén cumplidos
2. Revisa los logs del backend
3. Verifica que los endpoints existan en tu API
