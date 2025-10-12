package org.example.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

@Component
public class RoleValidationInterceptor implements HandlerInterceptor {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        
        // Solo validar rutas de movimientos
        String requestURI = request.getRequestURI();
        if (!requestURI.startsWith("/api/movimientos")) {
            return true; // Permitir otras rutas
        }
        
        // Permitir OPTIONS requests (CORS preflight)
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }
        
        try {
            // Extraer token del header Authorization
            String authHeader = request.getHeader("Authorization");
            String token = null;
            
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }
            
            if (token == null || token.trim().isEmpty()) {
                return sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, 
                    "Token de autorización requerido");
            }
            
            // Extraer rol del token (simulación)
            String rol = extraerRolDeToken(token);
            
            if (rol == null) {
                return sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, 
                    "Token inválido");
            }
            
            // Validar que el usuario tenga rol de Recepcion
            if (!"Recepcion".equals(rol)) {
                Map<String, Object> errorData = new HashMap<>();
                errorData.put("success", false);
                errorData.put("message", "Acceso denegado. Solo usuarios con rol de Recepción pueden acceder a esta funcionalidad");
                errorData.put("rolActual", rol);
                errorData.put("rolRequerido", "Recepcion");
                
                return sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, errorData);
            }
            
            // Si llegamos aquí, el acceso está autorizado
            return true;
            
        } catch (Exception e) {
            return sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "Error interno del servidor");
        }
    }
    
    private String extraerRolDeToken(String token) {
        if (token == null) return null;
        
        // Simulación: extraer rol del token
        if (token.contains("_1_")) return "DirectorTecnico";
        if (token.contains("_2_")) return "Recepcion";
        if (token.contains("_3_")) return "Operaciones";
        if (token.contains("_4_")) return "Calidad";
        if (token.contains("_5_")) return "Despacho";
        if (token.contains("_6_")) return "Cliente";
        
        return null;
    }
    
    private boolean sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws Exception {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("message", message);
        
        return sendErrorResponse(response, statusCode, errorResponse);
    }
    
    private boolean sendErrorResponse(HttpServletResponse response, int statusCode, Map<String, Object> errorData) throws Exception {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // Agregar headers CORS
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:9002");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        
        String jsonResponse = objectMapper.writeValueAsString(errorData);
        response.getWriter().write(jsonResponse);
        
        return false; // No continuar con el procesamiento
    }
}