# Guía de Pruebas de API - Sistema de Almacén Farmacéutico

## Archivos de Prueba Creados

1. **DATOS_PRUEBA_API.json** - Datos de ejemplo en formato JSON
2. **TEST_API_COMMANDS.sh** - Script bash para Linux/Mac
3. **TEST_API_COMMANDS.ps1** - Script PowerShell para Windows
4. **POSTMAN_COLLECTION_JERARQUIA_COMPLETA.json** - Colección de Postman

## Opción 1: Usar Scripts Automatizados

### En Windows (PowerShell):
```powershell
cd Backend
.\TEST_API_COMMANDS.ps1
```

### En Linux/Mac (Bash):
```bash
cd Backend
chmod +x TEST_API_COMMANDS.sh
./TEST_API_COMMANDS.sh
```

## Opción 2: Usar Postman

1. Abre Postman
2. Click en "Import"
3. Selecciona el archivo `POSTMAN_COLLECTION_JERARQUIA_COMPLETA.json`
4. Ejecuta los requests en orden:
   - 1. Operadores Logísticos → Crear Operador Logístico
   - 2. Almacenes → Crear Almacén
   - 3. Zonas → Crear Zona
   - etc.

## Opción 3: Usar cURL Manualmente

### Paso 1: Crear Operador Logístico
```bash
curl -X POST http://localhost:8080/api/operadores-logisticos \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "DHL Supply Chain Perú",
    "ruc": "20123456789",
    "direccion": "Av. Industrial 1234, Callao",
    "telefono": "+51 1 234-5678",
    "email": "contacto@dhl.com.pe",
    "activo": true
  }'
```

**Guardar el `id` de la respuesta como `OPERADOR_ID`**

### Paso 2: Crear Cliente
```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "razonSocial": "Ministerio de Salud del Perú",
    "rucDni": "20987654321",
    "direccionEntrega": "Av. Salaverry 801, Jesús María, Lima",
    "distrito": "Jesús María",
    "telefono": "+51 1 315-6600",
    "email": "logistica@minsa.gob.pe",
    "tipoCliente": "CORPORATIVO",
    "formaPago": "CREDITO_30_DIAS",
    "activo": true
  }'
```

**Guardar el `id` de la respuesta como `CLIENTE_ID`**

### Paso 3: Crear Producto
```bash
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Vacuna COVID-19 Pfizer-BioNTech",
    "descripcion": "Vacuna de ARNm contra COVID-19",
    "codigoBarras": "7501234567890",
    "unidadMedida": "DOSIS",
    "requiereCadenaFrio": true,
    "temperaturaMinima": -80.0,
    "temperaturaMaxima": -60.0
  }'
```

**Guardar el `id` de la respuesta como `PRODUCTO_ID`**

### Paso 4: Crear Almacén
```bash
curl -X POST http://localhost:8080/api/almacenes \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Almacén Central Lima - MINSA",
    "direccion": "Av. Los Frutales 456, Ate, Lima",
    "operadorLogisticoId": "REEMPLAZAR_CON_OPERADOR_ID",
    "clienteId": "REEMPLAZAR_CON_CLIENTE_ID",
    "tieneAreaControlados": true
  }'
```

**Reemplazar los IDs con los valores guardados. Guardar el `id` como `ALMACEN_ID`**

### Paso 5: Crear Zona
```bash
curl -X POST http://localhost:8080/api/zonas \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Zona Ultra Congelada A",
    "tipo": "ULT",
    "almacenId": "REEMPLAZAR_CON_ALMACEN_ID"
  }'
```

**Guardar el `id` como `ZONA_ID`**

### Paso 6: Crear Ubicación
```bash
curl -X POST http://localhost:8080/api/ubicaciones \
  -H "Content-Type: application/json" \
  -d '{
    "codigo": "ULT-A-01-01",
    "capacidadMaxima": 50,
    "disponible": true,
    "zonaId": "REEMPLAZAR_CON_ZONA_ID"
  }'
```

**Guardar el `id` como `UBICACION_ID`**

### Paso 7: Crear Lote
```bash
curl -X POST http://localhost:8080/api/lotes \
  -H "Content-Type: application/json" \
  -d '{
    "numeroLote": "LOTE-COVID-2025-001",
    "fechaIngreso": "2025-02-01",
    "clienteId": "REEMPLAZAR_CON_CLIENTE_ID",
    "productoId": "REEMPLAZAR_CON_PRODUCTO_ID",
    "cantidadTotal": 10000,
    "condicionAlmacen": "ULT"
  }'
```

**Guardar el `id` como `LOTE_ID`**

### Paso 8: Crear Palet
```bash
curl -X POST http://localhost:8080/api/palets \
  -H "Content-Type: application/json" \
  -d '{
    "codigo": "PAL-2025-001",
    "loteId": "REEMPLAZAR_CON_LOTE_ID",
    "ubicacionId": "REEMPLAZAR_CON_UBICACION_ID",
    "capacidadMaxima": 20,
    "cajasActuales": 0,
    "pesoMaximoKg": 500.0,
    "pesoActualKg": 0.0,
    "disponible": true,
    "observaciones": "Palet para vacunas COVID-19"
  }'
```

**Guardar el `id` como `PALET_ID`**

### Paso 9: Crear Caja
```bash
curl -X POST http://localhost:8080/api/cajas \
  -H "Content-Type: application/json" \
  -d '{
    "codigo": "CAJA-2025-001",
    "paletId": "REEMPLAZAR_CON_PALET_ID",
    "clienteId": "REEMPLAZAR_CON_CLIENTE_ID",
    "productoId": "REEMPLAZAR_CON_PRODUCTO_ID",
    "cantidad": 500,
    "loteProducto": "PFIZER-2025-A",
    "fechaVencimiento": "2026-12-31",
    "tamano": "MEDIANA",
    "pesoKg": 25.5,
    "largoCm": 60.0,
    "anchoCm": 40.0,
    "altoCm": 30.0,
    "temperaturaRequeridaMin": -80.0,
    "temperaturaRequeridaMax": -60.0,
    "requiereCadenaFrio": true,
    "sellada": true,
    "observaciones": "Caja sellada con 500 dosis de vacuna COVID-19"
  }'
```

## Consultas de Verificación

### Listar todos los almacenes
```bash
curl http://localhost:8080/api/almacenes
```

### Listar zonas de un almacén
```bash
curl http://localhost:8080/api/zonas/almacen/{almacenId}
```

### Listar ubicaciones disponibles
```bash
curl http://localhost:8080/api/ubicaciones/disponibles
```

### Listar palets de un lote
```bash
curl http://localhost:8080/api/palets/lote/{loteId}
```

### Listar cajas de un cliente
```bash
curl http://localhost:8080/api/cajas/cliente/{clienteId}
```

### Listar cajas próximas a vencer
```bash
curl http://localhost:8080/api/cajas/proximas-vencer?dias=30
```

## Valores Válidos

### Tipos de Zona
- `ULT` - Ultra congelado (-80°C a -60°C)
- `CONGELADO` - Congelado (-25°C a -15°C)
- `REFRIGERADO` - Refrigerado (2°C a 8°C)
- `SECO` - Temperatura ambiente

### Tamaños de Caja
- `PEQUENA`
- `MEDIANA`
- `GRANDE`
- `EXTRA_GRANDE`

### Condiciones de Almacén
- `ULT`
- `CONGELADO`
- `REFRIGERADO`
- `SECO`

### Tipos de Cliente
- `CORPORATIVO`
- `INDIVIDUAL`
- `GOBIERNO`

### Formas de Pago
- `CONTADO`
- `CREDITO_30_DIAS`
- `CREDITO_60_DIAS`
- `CREDITO_90_DIAS`

## Notas Importantes

1. **Orden de Ejecución**: Los pasos deben ejecutarse en orden (1-9) debido a las dependencias entre entidades
2. **IDs**: Guardar los IDs de cada respuesta para usarlos en los siguientes pasos
3. **Servidor**: Asegurarse de que el servidor Spring Boot esté corriendo en `http://localhost:8080`
4. **Base de Datos**: Verificar que la base de datos esté configurada correctamente

## Troubleshooting

### Error 404 - Not Found
- Verificar que el servidor esté corriendo
- Verificar la URL base

### Error 500 - Internal Server Error
- Revisar los logs del servidor
- Verificar que los IDs de las relaciones existan
- Verificar que la base de datos esté accesible

### Error 400 - Bad Request
- Verificar el formato JSON
- Verificar que todos los campos requeridos estén presentes
- Verificar que los valores sean del tipo correcto
