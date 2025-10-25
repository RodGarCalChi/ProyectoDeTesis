# 📋 Guía: Eliminación de Tablas Duplicadas

## 🎯 Objetivo

Eliminar 4 tablas que duplican funcionalidad y no tienen implementación activa, simplificando la arquitectura del sistema.

---

## ⚠️ IMPORTANTE: Leer Antes de Ejecutar

### Precauciones Obligatorias

1. ✅ **Hacer BACKUP completo de la base de datos**
2. ✅ **Ejecutar primero en ambiente de DESARROLLO**
3. ✅ **Verificar que no haya datos importantes**
4. ✅ **Confirmar que no hay referencias en código activo**
5. ✅ **Tener plan de rollback preparado**

---

## 📊 Tablas a Eliminar (Fase 1)

| # | Tabla | Razón | Alternativa | Riesgo |
|---|-------|-------|-------------|--------|
| 1 | `recepcion_lineas` | Duplica DetalleRecepcion | Usar `detalle_recepcion` | BAJO |
| 2 | `recepciones` | Duplica RecepcionMercaderia | Usar `recepciones_mercaderia` | BAJO |
| 3 | `oc_lineas` | Depende de OrdenCompra | Número en RecepcionMercaderia | BAJO |
| 4 | `ordenes_compra` | No se usa | Número en RecepcionMercaderia | BAJO |

---

## 🔍 Paso 1: Verificación Pre-Eliminación

### 1.1 Verificar Existencia de Tablas

```sql
SELECT 
    TABLE_NAME,
    TABLE_ROWS,
    CREATE_TIME,
    ROUND(((DATA_LENGTH + INDEX_LENGTH) / 1024 / 1024), 2) AS 'Size_MB'
FROM 
    information_schema.TABLES
WHERE 
    TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME IN (
        'recepcion_lineas',
        'recepciones',
        'oc_lineas',
        'ordenes_compra'
    );
```

### 1.2 Verificar Datos en las Tablas

```sql
-- Verificar si hay datos
SELECT 'recepcion_lineas' as tabla, COUNT(*) as registros FROM recepcion_lineas
UNION ALL
SELECT 'recepciones', COUNT(*) FROM recepciones
UNION ALL
SELECT 'oc_lineas', COUNT(*) FROM oc_lineas
UNION ALL
SELECT 'ordenes_compra', COUNT(*) FROM ordenes_compra;
```

**Acción:** Si hay datos importantes, hacer backup específico de esas tablas.

### 1.3 Verificar Referencias en Código

```bash
# Buscar referencias en el código Java
cd Backend/src
grep -r "RecepcionNew" .
grep -r "RecepcionLinea" .
grep -r "OrdenCompra" .
grep -r "OCLinea" .
```

**Resultado Esperado:** Solo encontrar las definiciones de entidades, sin servicios/controladores activos.

---

## 💾 Paso 2: Backup

### 2.1 Backup Completo de la Base de Datos

```bash
# MySQL
mysqldump -u usuario -p basededatoslogisticofarmaceutico > backup_antes_limpieza_$(date +%Y%m%d_%H%M%S).sql

# O desde MySQL Workbench:
# Server > Data Export > Export to Self-Contained File
```

### 2.2 Backup Específico de Tablas a Eliminar (Opcional)

```bash
mysqldump -u usuario -p basededatoslogisticofarmaceutico \
  recepcion_lineas \
  recepciones \
  oc_lineas \
  ordenes_compra \
  > backup_tablas_eliminadas_$(date +%Y%m%d_%H%M%S).sql
```

---

## 🗑️ Paso 3: Eliminación de Tablas

### 3.1 Ejecutar Script SQL

**Opción A: Desde MySQL Workbench**
1. Abrir `SCRIPT_ELIMINAR_TABLAS_FASE1.sql`
2. Revisar el script completo
3. Ejecutar paso por paso (recomendado) o todo junto
4. Verificar resultados

**Opción B: Desde línea de comandos**
```bash
mysql -u usuario -p basededatoslogisticofarmaceutico < SCRIPT_ELIMINAR_TABLAS_FASE1.sql
```

### 3.2 Verificar Eliminación

```sql
-- Debe devolver 0 filas
SELECT TABLE_NAME
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = DATABASE()
  AND TABLE_NAME IN (
      'recepcion_lineas',
      'recepciones',
      'oc_lineas',
      'ordenes_compra'
  );
```

---

## 🧹 Paso 4: Limpieza de Código

### 4.1 Eliminar Archivos de Entidades

```bash
cd Backend/src/main/java/org/example/backend/entity

# Eliminar entidades
rm RecepcionNew.java
rm RecepcionLinea.java
rm OrdenCompra.java
rm OCLinea.java
```

**O desde tu IDE:**
- Eliminar `RecepcionNew.java`
- Eliminar `RecepcionLinea.java`
- Eliminar `OrdenCompra.java`
- Eliminar `OCLinea.java`

### 4.2 Buscar y Eliminar Referencias

```bash
# Buscar posibles imports o referencias
cd Backend/src
grep -r "import.*RecepcionNew" .
grep -r "import.*RecepcionLinea" .
grep -r "import.*OrdenCompra" .
grep -r "import.*OCLinea" .
```

**Acción:** Eliminar cualquier import o referencia encontrada.

### 4.3 Verificar Compilación

```bash
cd Backend
./mvnw clean compile

# O desde tu IDE: Build > Rebuild Project
```

**Resultado Esperado:** Compilación exitosa sin errores.

---

## ✅ Paso 5: Verificación Post-Eliminación

### 5.1 Verificar Estructura de Base de Datos

```sql
-- Listar todas las tablas restantes
SELECT 
    TABLE_NAME,
    TABLE_ROWS,
    ROUND(((DATA_LENGTH + INDEX_LENGTH) / 1024 / 1024), 2) AS 'Size_MB'
FROM 
    information_schema.TABLES
WHERE 
    TABLE_SCHEMA = DATABASE()
ORDER BY 
    TABLE_NAME;
```

### 5.2 Verificar Tablas Principales Intactas

```sql
-- Verificar que las tablas importantes siguen existiendo
SELECT TABLE_NAME
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = DATABASE()
  AND TABLE_NAME IN (
      'recepciones_mercaderia',
      'detalle_recepcion',
      'actas_recepcion',
      'detalle_acta_recepcion',
      'clientes',
      'productos',
      'cliente_productos'
  );
```

**Resultado Esperado:** 7 filas (todas las tablas principales existen).

### 5.3 Probar Funcionalidad Principal

**Desde Postman:**
1. Crear una recepción de mercadería
2. Listar recepciones
3. Crear un acta de recepción
4. Verificar que todo funciona correctamente

---

## 🔄 Paso 6: Plan de Rollback (Si algo sale mal)

### Si necesitas restaurar:

```bash
# Restaurar backup completo
mysql -u usuario -p basededatoslogisticofarmaceutico < backup_antes_limpieza_YYYYMMDD_HHMMSS.sql

# O restaurar solo las tablas eliminadas
mysql -u usuario -p basededatoslogisticofarmaceutico < backup_tablas_eliminadas_YYYYMMDD_HHMMSS.sql
```

---

## 📊 Resultados Esperados

### Antes de la Limpieza
- **Total de tablas:** 31
- **Tablas con duplicación:** 4
- **Complejidad:** Alta

### Después de la Limpieza
- **Total de tablas:** 27
- **Reducción:** 13%
- **Complejidad:** Media
- **Beneficios:**
  - ✅ Menos duplicación
  - ✅ Arquitectura más clara
  - ✅ Código más mantenible
  - ✅ Mejor rendimiento

---

## 📝 Checklist de Ejecución

### Pre-Eliminación
- [ ] Backup completo realizado
- [ ] Verificadas tablas a eliminar
- [ ] Confirmado que no hay datos críticos
- [ ] Verificado que no hay referencias en código
- [ ] Ambiente de desarrollo preparado

### Eliminación
- [ ] Script SQL ejecutado
- [ ] Tablas eliminadas verificadas
- [ ] Archivos Java eliminados
- [ ] Compilación exitosa

### Post-Eliminación
- [ ] Estructura de BD verificada
- [ ] Tablas principales intactas
- [ ] Funcionalidad probada
- [ ] Documentación actualizada

### Finalización
- [ ] Cambios commiteados en Git
- [ ] Equipo notificado
- [ ] Documentación actualizada
- [ ] Backup archivado

---

## 🚨 Problemas Comunes y Soluciones

### Problema 1: Error de Foreign Key
**Síntoma:** No se puede eliminar tabla por restricción de clave foránea

**Solución:**
```sql
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE nombre_tabla;
SET FOREIGN_KEY_CHECKS = 1;
```

### Problema 2: Tabla no existe
**Síntoma:** Error "Table doesn't exist"

**Solución:** La tabla ya fue eliminada o nunca existió. Continuar con el siguiente paso.

### Problema 3: Error de compilación después de eliminar
**Síntoma:** Errores de compilación en Java

**Solución:**
1. Buscar imports de las clases eliminadas
2. Eliminar referencias
3. Limpiar y recompilar: `./mvnw clean compile`

---

## 📞 Soporte

Si encuentras problemas:
1. Revisar logs de MySQL
2. Verificar backup disponible
3. Consultar documentación de análisis
4. Restaurar backup si es necesario

---

## ✅ Conclusión

Esta limpieza elimina **4 tablas duplicadas** sin impacto en la funcionalidad, simplificando la arquitectura y mejorando la mantenibilidad del sistema.

**Tiempo estimado:** 30-60 minutos  
**Riesgo:** BAJO  
**Beneficio:** ALTO  

¡Listo para ejecutar! 🚀
