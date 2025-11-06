#!/bin/bash

# Script de prueba para APIs del sistema de almacén farmacéutico
# Ejecutar en orden para crear la jerarquía completa

BASE_URL="http://localhost:8080/api"

echo "========================================="
echo "PASO 1: Crear Operador Logístico"
echo "========================================="
OPERADOR_RESPONSE=$(curl -X POST "$BASE_URL/operadores-logisticos" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "DHL Supply Chain Perú",
    "ruc": "20123456789",
    "direccion": "Av. Industrial 1234, Callao",
    "telefono": "+51 1 234-5678",
    "email": "contacto@dhl.com.pe",
    "activo": true
  }')
echo "$OPERADOR_RESPONSE"
OPERADOR_ID=$(echo "$OPERADOR_RESPONSE" | grep -o '"id":"[^"]*' | cut -d'"' -f4)
echo "OPERADOR_ID: $OPERADOR_ID"
echo ""

echo "========================================="
echo "PASO 2: Crear Cliente"
echo "========================================="
CLIENTE_RESPONSE=$(curl -X POST "$BASE_URL/clientes" \
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
  }')
echo "$CLIENTE_RESPONSE"
CLIENTE_ID=$(echo "$CLIENTE_RESPONSE" | grep -o '"id":"[^"]*' | cut -d'"' -f4)
echo "CLIENTE_ID: $CLIENTE_ID"
echo ""

echo "========================================="
echo "PASO 3: Crear Producto"
echo "========================================="
PRODUCTO_RESPONSE=$(curl -X POST "$BASE_URL/productos" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Vacuna COVID-19 Pfizer-BioNTech",
    "descripcion": "Vacuna de ARNm contra COVID-19",
    "codigoBarras": "7501234567890",
    "unidadMedida": "DOSIS",
    "requiereCadenaFrio": true,
    "temperaturaMinima": -80.0,
    "temperaturaMaxima": -60.0
  }')
echo "$PRODUCTO_RESPONSE"
PRODUCTO_ID=$(echo "$PRODUCTO_RESPONSE" | grep -o '"id":"[^"]*' | cut -d'"' -f4)
echo "PRODUCTO_ID: $PRODUCTO_ID"
echo ""

echo "========================================="
echo "PASO 4: Crear Almacén"
echo "========================================="
ALMACEN_RESPONSE=$(curl -X POST "$BASE_URL/almacenes" \
  -H "Content-Type: application/json" \
  -d "{
    \"nombre\": \"Almacén Central Lima - MINSA\",
    \"direccion\": \"Av. Los Frutales 456, Ate, Lima\",
    \"operadorLogisticoId\": \"$OPERADOR_ID\",
    \"clienteId\": \"$CLIENTE_ID\",
    \"tieneAreaControlados\": true
  }")
echo "$ALMACEN_RESPONSE"
ALMACEN_ID=$(echo "$ALMACEN_RESPONSE" | grep -o '"id":"[^"]*' | cut -d'"' -f4)
echo "ALMACEN_ID: $ALMACEN_ID"
echo ""

echo "========================================="
echo "PASO 5: Crear Zona"
echo "========================================="
ZONA_RESPONSE=$(curl -X POST "$BASE_URL/zonas" \
  -H "Content-Type: application/json" \
  -d "{
    \"nombre\": \"Zona Ultra Congelada A\",
    \"tipo\": \"ULT\",
    \"almacenId\": \"$ALMACEN_ID\"
  }")
echo "$ZONA_RESPONSE"
ZONA_ID=$(echo "$ZONA_RESPONSE" | grep -o '"id":"[^"]*' | cut -d'"' -f4)
echo "ZONA_ID: $ZONA_ID"
echo ""

echo "========================================="
echo "PASO 6: Crear Ubicación"
echo "========================================="
UBICACION_RESPONSE=$(curl -X POST "$BASE_URL/ubicaciones" \
  -H "Content-Type: application/json" \
  -d "{
    \"codigo\": \"ULT-A-01-01\",
    \"capacidadMaxima\": 50,
    \"disponible\": true,
    \"zonaId\": \"$ZONA_ID\"
  }")
echo "$UBICACION_RESPONSE"
UBICACION_ID=$(echo "$UBICACION_RESPONSE" | grep -o '"id":"[^"]*' | cut -d'"' -f4)
echo "UBICACION_ID: $UBICACION_ID"
echo ""

echo "========================================="
echo "PASO 7: Crear Lote"
echo "========================================="
LOTE_RESPONSE=$(curl -X POST "$BASE_URL/lotes" \
  -H "Content-Type: application/json" \
  -d "{
    \"numeroLote\": \"LOTE-COVID-2025-001\",
    \"fechaIngreso\": \"2025-02-01\",
    \"clienteId\": \"$CLIENTE_ID\",
    \"productoId\": \"$PRODUCTO_ID\",
    \"cantidadTotal\": 10000,
    \"condicionAlmacen\": \"ULT\"
  }")
echo "$LOTE_RESPONSE"
LOTE_ID=$(echo "$LOTE_RESPONSE" | grep -o '"id":"[^"]*' | cut -d'"' -f4)
echo "LOTE_ID: $LOTE_ID"
echo ""

echo "========================================="
echo "PASO 8: Crear Palet"
echo "========================================="
PALET_RESPONSE=$(curl -X POST "$BASE_URL/palets" \
  -H "Content-Type: application/json" \
  -d "{
    \"codigo\": \"PAL-2025-001\",
    \"loteId\": \"$LOTE_ID\",
    \"ubicacionId\": \"$UBICACION_ID\",
    \"capacidadMaxima\": 20,
    \"cajasActuales\": 0,
    \"pesoMaximoKg\": 500.0,
    \"pesoActualKg\": 0.0,
    \"disponible\": true,
    \"observaciones\": \"Palet para vacunas COVID-19\"
  }")
echo "$PALET_RESPONSE"
PALET_ID=$(echo "$PALET_RESPONSE" | grep -o '"id":"[^"]*' | cut -d'"' -f4)
echo "PALET_ID: $PALET_ID"
echo ""

echo "========================================="
echo "PASO 9: Crear Caja"
echo "========================================="
CAJA_RESPONSE=$(curl -X POST "$BASE_URL/cajas" \
  -H "Content-Type: application/json" \
  -d "{
    \"codigo\": \"CAJA-2025-001\",
    \"paletId\": \"$PALET_ID\",
    \"clienteId\": \"$CLIENTE_ID\",
    \"productoId\": \"$PRODUCTO_ID\",
    \"cantidad\": 500,
    \"loteProducto\": \"PFIZER-2025-A\",
    \"fechaVencimiento\": \"2026-12-31\",
    \"tamano\": \"MEDIANA\",
    \"pesoKg\": 25.5,
    \"largoCm\": 60.0,
    \"anchoCm\": 40.0,
    \"altoCm\": 30.0,
    \"temperaturaRequeridaMin\": -80.0,
    \"temperaturaRequeridaMax\": -60.0,
    \"requiereCadenaFrio\": true,
    \"sellada\": true,
    \"observaciones\": \"Caja sellada con 500 dosis de vacuna COVID-19\"
  }")
echo "$CAJA_RESPONSE"
echo ""

echo "========================================="
echo "RESUMEN DE IDs CREADOS"
echo "========================================="
echo "OPERADOR_ID: $OPERADOR_ID"
echo "CLIENTE_ID: $CLIENTE_ID"
echo "PRODUCTO_ID: $PRODUCTO_ID"
echo "ALMACEN_ID: $ALMACEN_ID"
echo "ZONA_ID: $ZONA_ID"
echo "UBICACION_ID: $UBICACION_ID"
echo "LOTE_ID: $LOTE_ID"
echo "PALET_ID: $PALET_ID"
echo ""

echo "========================================="
echo "CONSULTAS DE VERIFICACIÓN"
echo "========================================="

echo "Listar todos los almacenes:"
curl -X GET "$BASE_URL/almacenes"
echo ""

echo "Listar zonas del almacén:"
curl -X GET "$BASE_URL/zonas/almacen/$ALMACEN_ID"
echo ""

echo "Listar ubicaciones disponibles:"
curl -X GET "$BASE_URL/ubicaciones/disponibles"
echo ""

echo "Listar palets del lote:"
curl -X GET "$BASE_URL/palets/lote/$LOTE_ID"
echo ""

echo "Listar cajas del cliente:"
curl -X GET "$BASE_URL/cajas/cliente/$CLIENTE_ID"
echo ""

echo "========================================="
echo "PRUEBAS COMPLETADAS"
echo "========================================="
