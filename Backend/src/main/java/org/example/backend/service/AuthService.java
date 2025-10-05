package org.example.backend.service;

import org.example.backend.enumeraciones.Rol;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    /**
     * Extrae el rol del token de autorización
     * En una implementación real, esto sería un JWT parser
     */
    public String extraerRolDeToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return null;
        }
        
        // Simulación: extraer rol del token basado en el ID de usuario
        if (token.contains("_1_")) return Rol.DirectorTecnico.name();
        if (token.contains("_2_")) return Rol.Recepcion.name();
        if (token.contains("_3_")) return Rol.Operaciones.name();
        if (token.contains("_4_")) return Rol.Calidad.name();
        if (token.contains("_5_")) return Rol.Despacho.name();
        if (token.contains("_6_")) return Rol.Cliente.name();
        
        return null;
    }
    
    /**
     * Extrae el ID de usuario del token
     */
    public Long extraerUserIdDeToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return null;
        }
        
        try {
            // Simulación: extraer ID del token
            if (token.startsWith("token_")) {
                String[] parts = token.split("_");
                if (parts.length >= 2) {
                    return Long.parseLong(parts[1]);
                }
            }
        } catch (NumberFormatException e) {
            return null;
        }
        
        return null;
    }
    
    /**
     * Valida si el token es válido
     */
    public boolean esTokenValido(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        
        // Simulación: validar formato del token
        return token.startsWith("token_") && extraerUserIdDeToken(token) != null;
    }
    
    /**
     * Valida si el usuario tiene el rol requerido
     */
    public boolean tieneRol(String token, Rol rolRequerido) {
        String rolUsuario = extraerRolDeToken(token);
        return rolRequerido.name().equals(rolUsuario);
    }
    
    /**
     * Valida si el usuario tiene rol de Recepción
     */
    public boolean esUsuarioRecepcion(String token) {
        return tieneRol(token, Rol.Recepcion);
    }
    
    /**
     * Valida acceso completo para movimientos (solo recepción)
     */
    public ValidationResult validarAccesoMovimientos(String authHeader) {
        // Extraer token del header Authorization
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
        
        if (token == null || token.trim().isEmpty()) {
            return new ValidationResult(false, "Token de autorización requerido", null);
        }
        
        if (!esTokenValido(token)) {
            return new ValidationResult(false, "Token inválido", null);
        }
        
        String rol = extraerRolDeToken(token);
        if (rol == null) {
            return new ValidationResult(false, "No se pudo determinar el rol del usuario", null);
        }
        
        if (!esUsuarioRecepcion(token)) {
            return new ValidationResult(false, 
                "Acceso denegado. Solo usuarios con rol de Recepción pueden acceder a esta funcionalidad", 
                rol);
        }
        
        return new ValidationResult(true, "Acceso autorizado", rol);
    }
    
    /**
     * Clase para encapsular el resultado de la validación
     */
    public static class ValidationResult {
        private final boolean valido;
        private final String mensaje;
        private final String rol;
        
        public ValidationResult(boolean valido, String mensaje, String rol) {
            this.valido = valido;
            this.mensaje = mensaje;
            this.rol = rol;
        }
        
        public boolean isValido() {
            return valido;
        }
        
        public String getMensaje() {
            return mensaje;
        }
        
        public String getRol() {
            return rol;
        }
    }
}