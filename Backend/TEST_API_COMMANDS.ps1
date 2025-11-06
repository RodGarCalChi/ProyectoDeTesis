# Script de prueba para APIs del sistema de almacén farmacéutico (PowerShell)
# Ejecutar en orden para crear la jerarquía completa

$BaseUrl = "http://localhost:8080/api"

Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "PASO 1: Crear Operador Logístico" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan

$operadorBody = @{
    nombre = "DHL Supply Chain Perú"
    ruc = "20123456789"
    direccion = "Av. Industrial 1234, Callao"
    telefono = "+51 1 234-5678"
    email = "contacto@dhl.com.pe"
    activo = $true
} | ConvertTo-Json

$operadorResponse = Invoke-RestMethod -Uri "$BaseUrl/operadores-logisticos" -Method Post -Body $operadorBody -ContentType "application/json"
$OPERADOR_ID = $operadorResponse.data.id
Write-Host "OPERADOR_ID: $OPERADOR_ID" -ForegroundColor Green
Write-Host ""

Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "PASO 2: Crear Cliente" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan

$clienteBody = @{
    razonSocial = "Ministerio de Salud del Perú"
    rucDni = "20987654321"
    direccionEntrega = "Av. Salaverry 801, Jesús María, Lima"
    distrito = "Jesús María"
    telefono = "+51 1 315-6600"
    email = "logistica@minsa.gob.pe"
    tipoCliente = "CORPORATIVO"
    formaPago = "CREDITO_30_DIAS"
    activo = $true
} | ConvertTo-Json

$clienteResponse = Invoke-RestMethod -Uri "$BaseUrl/clientes" -Method Post -Body $clienteBody -ContentType "application/json"
$CLIENTE_ID = $clienteResponse.data.id
Write-Host "CLIENTE_ID: $CLIENTE_ID" -ForegroundColor Green
Write-Host ""

Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "PASO 3: Crear Producto" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan

$productoBody = @{
    nombre = "Vacuna COVID-19 Pfizer-BioNTech"
    descripcion = "Vacuna de ARNm contra COVID-19"
    codigoBarras = "7501234567890"
    unidadMedida = "DOSIS"
    requiereCadenaFrio = $true
    temperaturaMinima = -80.0
    temperaturaMaxima = -60.0
} | ConvertTo-Json

$productoResponse = Invoke-RestMethod -Uri "$BaseUrl/productos" -Method Post -Body $productoBody -ContentType "application/json"
$PRODUCTO_ID = $productoResponse.data.id
Write-Host "PRODUCTO_ID: $PRODUCTO_ID" -ForegroundColor Green
Write-Host ""

Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "PASO 4: Crear Almacén" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan

$almacenBody = @{
    nombre = "Almacén Central Lima - MINSA"
    direccion = "Av. Los Frutales 456, Ate, Lima"
    operadorLogisticoId = $OPERADOR_ID
    clienteId = $CLIENTE_ID
    tieneAreaControlados = $true
} | ConvertTo-Json

$almacenResponse = Invoke-RestMethod -Uri "$BaseUrl/almacenes" -Method Post -Body $almacenBody -ContentType "application/json"
$ALMACEN_ID = $almacenResponse.data.id
Write-Host "ALMACEN_ID: $ALMACEN_ID" -ForegroundColor Green
Write-Host ""

Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "PASO 5: Crear Zona" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan

$zonaBody = @{
    nombre = "Zona Ultra Congelada A"
    tipo = "ULT"
    almacenId = $ALMACEN_ID
} | ConvertTo-Json

$zonaResponse = Invoke-RestMethod -Uri "$BaseUrl/zonas" -Method Post -Body $zonaBody -ContentType "application/json"
$ZONA_ID = $zonaResponse.data.id
Write-Host "ZONA_ID: $ZONA_ID" -ForegroundColor Green
Write-Host ""

Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "PASO 6: Crear Ubicación" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan

$ubicacionBody = @{
    codigo = "ULT-A-01-01"
    capacidadMaxima = 50
    disponible = $true
    zonaId = $ZONA_ID
} | ConvertTo-Json

$ubicacionResponse = Invoke-RestMethod -Uri "$BaseUrl/ubicaciones" -Method Post -Body $ubicacionBody -ContentType "application/json"
$UBICACION_ID = $ubicacionResponse.data.id
Write-Host "UBICACION_ID: $UBICACION_ID" -ForegroundColor Green
Write-Host ""

Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "PASO 7: Crear Lote" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan

$loteBody = @{
    numeroLote = "LOTE-COVID-2025-001"
    fechaIngreso = "2025-02-01"
    clienteId = $CLIENTE_ID
    productoId = $PRODUCTO_ID
    cantidadTotal = 10000
    condicionAlmacen = "ULT"
} | ConvertTo-Json

$loteResponse = Invoke-RestMethod -Uri "$BaseUrl/lotes" -Method Post -Body $loteBody -ContentType "application/json"
$LOTE_ID = $loteResponse.data.id
Write-Host "LOTE_ID: $LOTE_ID" -ForegroundColor Green
Write-Host ""

Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "PASO 8: Crear Palet" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan

$paletBody = @{
    codigo = "PAL-2025-001"
    loteId = $LOTE_ID
    ubicacionId = $UBICACION_ID
    capacidadMaxima = 20
    cajasActuales = 0
    pesoMaximoKg = 500.0
    pesoActualKg = 0.0
    disponible = $true
    observaciones = "Palet para vacunas COVID-19"
} | ConvertTo-Json

$paletResponse = Invoke-RestMethod -Uri "$BaseUrl/palets" -Method Post -Body $paletBody -ContentType "application/json"
$PALET_ID = $paletResponse.data.id
Write-Host "PALET_ID: $PALET_ID" -ForegroundColor Green
Write-Host ""

Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "PASO 9: Crear Caja" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan

$cajaBody = @{
    codigo = "CAJA-2025-001"
    paletId = $PALET_ID
    clienteId = $CLIENTE_ID
    productoId = $PRODUCTO_ID
    cantidad = 500
    loteProducto = "PFIZER-2025-A"
    fechaVencimiento = "2026-12-31"
    tamano = "MEDIANA"
    pesoKg = 25.5
    largoCm = 60.0
    anchoCm = 40.0
    altoCm = 30.0
    temperaturaRequeridaMin = -80.0
    temperaturaRequeridaMax = -60.0
    requiereCadenaFrio = $true
    sellada = $true
    observaciones = "Caja sellada con 500 dosis de vacuna COVID-19"
} | ConvertTo-Json

$cajaResponse = Invoke-RestMethod -Uri "$BaseUrl/cajas" -Method Post -Body $cajaBody -ContentType "application/json"
Write-Host "Caja creada exitosamente" -ForegroundColor Green
Write-Host ""

Write-Host "=========================================" -ForegroundColor Yellow
Write-Host "RESUMEN DE IDs CREADOS" -ForegroundColor Yellow
Write-Host "=========================================" -ForegroundColor Yellow
Write-Host "OPERADOR_ID: $OPERADOR_ID"
Write-Host "CLIENTE_ID: $CLIENTE_ID"
Write-Host "PRODUCTO_ID: $PRODUCTO_ID"
Write-Host "ALMACEN_ID: $ALMACEN_ID"
Write-Host "ZONA_ID: $ZONA_ID"
Write-Host "UBICACION_ID: $UBICACION_ID"
Write-Host "LOTE_ID: $LOTE_ID"
Write-Host "PALET_ID: $PALET_ID"
Write-Host ""

Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "CONSULTAS DE VERIFICACIÓN" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan

Write-Host "Listar todos los almacenes:" -ForegroundColor Yellow
$almacenes = Invoke-RestMethod -Uri "$BaseUrl/almacenes" -Method Get
$almacenes | ConvertTo-Json -Depth 5
Write-Host ""

Write-Host "Listar zonas del almacén:" -ForegroundColor Yellow
$zonas = Invoke-RestMethod -Uri "$BaseUrl/zonas/almacen/$ALMACEN_ID" -Method Get
$zonas | ConvertTo-Json -Depth 5
Write-Host ""

Write-Host "Listar ubicaciones disponibles:" -ForegroundColor Yellow
$ubicaciones = Invoke-RestMethod -Uri "$BaseUrl/ubicaciones/disponibles" -Method Get
$ubicaciones | ConvertTo-Json -Depth 5
Write-Host ""

Write-Host "=========================================" -ForegroundColor Green
Write-Host "PRUEBAS COMPLETADAS" -ForegroundColor Green
Write-Host "=========================================" -ForegroundColor Green
