-- =====================================================
-- INSERTS PARA USUARIOS DE PHARMAFLOW
-- =====================================================

-- Crear tabla usuarios si no existe (estructura basada en la entidad Usuario)
CREATE TABLE IF NOT EXISTS usuarios (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    documento VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT true,
    ultimo_acceso TIMESTAMP,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo_usuario VARCHAR(50) -- Para herencia
);

-- Limpiar datos existentes (opcional)
-- DELETE FROM usuarios;

-- =====================================================
-- INSERTAR USUARIOS DE PRUEBA
-- =====================================================

-- 1. Usuario Administrador / Director Técnico
INSERT INTO usuarios (
    id, nombres, apellidos, documento, email, password_hash, rol, activo, fecha_creacion, fecha_actualizacion
) VALUES (
    '550e8400-e29b-41d4-a716-446655440001',
    'Administrador',
    'Sistema',
    '12345678',
    'admin@pharmaflow.com',
    'password', -- En producción debería ser un hash
    'DirectorTecnico',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- 2. Usuario de Recepción (PRINCIPAL PARA PRUEBAS)
INSERT INTO usuarios (
    id, nombres, apellidos, documento, email, password_hash, rol, activo, fecha_creacion, fecha_actualizacion
) VALUES (
    '550e8400-e29b-41d4-a716-446655440002',
    'María',
    'García',
    '87654321',
    'recepcion@pharmaflow.com',
    'password', -- En producción debería ser un hash
    'Recepcion',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- 3. Usuario de Operaciones
INSERT INTO usuarios (
    id, nombres, apellidos, documento, email, password_hash, rol, activo, fecha_creacion, fecha_actualizacion
) VALUES (
    '550e8400-e29b-41d4-a716-446655440003',
    'Juan',
    'Pérez',
    '11223344',
    'operaciones@pharmaflow.com',
    'password',
    'Operaciones',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- 4. Usuario de Calidad
INSERT INTO usuarios (
    id, nombres, apellidos, documento, email, password_hash, rol, activo, fecha_creacion, fecha_actualizacion
) VALUES (
    '550e8400-e29b-41d4-a716-446655440004',
    'Ana',
    'López',
    '55667788',
    'calidad@pharmaflow.com',
    'password',
    'Calidad',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- 5. Usuario de Despacho
INSERT INTO usuarios (
    id, nombres, apellidos, documento, email, password_hash, rol, activo, fecha_creacion, fecha_actualizacion
) VALUES (
    '550e8400-e29b-41d4-a716-446655440005',
    'Carlos',
    'Rodríguez',
    '99887766',
    'despacho@pharmaflow.com',
    'password',
    'Despacho',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- 6. Usuario Cliente
INSERT INTO usuarios (
    id, nombres, apellidos, documento, email, password_hash, rol, activo, fecha_creacion, fecha_actualizacion
) VALUES (
    '550e8400-e29b-41d4-a716-446655440006',
    'Pedro',
    'Martínez',
    '44332211',
    'cliente@pharmaflow.com',
    'password',
    'Cliente',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- =====================================================
-- USUARIOS ADICIONALES DE RECEPCIÓN PARA PRUEBAS
-- =====================================================

-- Usuario de Recepción 2
INSERT INTO usuarios (
    id, nombres, apellidos, documento, email, password_hash, rol, activo, fecha_creacion, fecha_actualizacion
) VALUES (
    '550e8400-e29b-41d4-a716-446655440007',
    'Laura',
    'Fernández',
    '12121212',
    'recepcion2@pharmaflow.com',
    'password',
    'Recepcion',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Usuario de Recepción 3
INSERT INTO usuarios (
    id, nombres, apellidos, documento, email, password_hash, rol, activo, fecha_creacion, fecha_actualizacion
) VALUES (
    '550e8400-e29b-41d4-a716-446655440008',
    'Roberto',
    'Silva',
    '34343434',
    'recepcion3@pharmaflow.com',
    'password',
    'Recepcion',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- =====================================================
-- VERIFICAR INSERTS
-- =====================================================

-- Consultar todos los usuarios insertados
SELECT 
    id,
    nombres,
    apellidos,
    documento,
    email,
    rol,
    activo,
    fecha_creacion
FROM usuarios 
ORDER BY rol, nombres;

-- Consultar solo usuarios de Recepción
SELECT 
    nombres || ' ' || apellidos as nombre_completo,
    email,
    documento,
    rol
FROM usuarios 
WHERE rol = 'Recepcion' 
AND activo = true;

-- =====================================================
-- COMANDOS ÚTILES PARA ADMINISTRACIÓN
-- =====================================================

-- Activar/Desactivar usuario
-- UPDATE usuarios SET activo = false WHERE email = 'usuario@ejemplo.com';

-- Cambiar rol de usuario
-- UPDATE usuarios SET rol = 'NuevoRol' WHERE email = 'usuario@ejemplo.com';

-- Actualizar último acceso
-- UPDATE usuarios SET ultimo_acceso = CURRENT_TIMESTAMP WHERE email = 'usuario@ejemplo.com';

-- Eliminar usuario (NO recomendado, mejor desactivar)
-- DELETE FROM usuarios WHERE email = 'usuario@ejemplo.com';