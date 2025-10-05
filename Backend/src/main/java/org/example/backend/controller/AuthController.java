package org.example.backend.controller;

import org.example.backend.dto.LoginRequestDTO;
import org.example.backend.dto.LoginResponseDTO;
import org.example.backend.dto.UsuarioDTO;
import org.example.backend.entity.Usuario;
import org.example.backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:9002"})
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();
            
            // Validar que los campos no estén vacíos
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new LoginResponseDTO("El email es obligatorio"));
            }
            
            if (password == null || password.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new LoginResponseDTO("La contraseña es obligatoria"));
            }
            
            // Buscar usuario en la base de datos
            Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(email.toLowerCase());
            
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new LoginResponseDTO("Usuario no encontrado"));
            }
            
            Usuario usuario = usuarioOpt.get();
            
            // Verificar contraseña y que el usuario esté activo
            if (!usuarioService.validarCredenciales(email.toLowerCase(), password)) {
                return ResponseEntity.badRequest()
                    .body(new LoginResponseDTO("Contraseña incorrecta o usuario inactivo"));
            }
            
            // Crear DTO del usuario
            UsuarioDTO usuarioDTO = usuarioService.convertirADTO(usuario);
            
            // Generar token simple (en producción usar JWT)
            String token = "token_" + (usuario.getId().hashCode() & Integer.MAX_VALUE) + "_" + System.currentTimeMillis();
            
            // Respuesta exitosa
            LoginResponseDTO response = new LoginResponseDTO(token, usuarioDTO);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new LoginResponseDTO("Error interno del servidor: " + e.getMessage()));
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logout exitoso");
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> registerRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String nombres = registerRequest.get("nombres");
            String apellidos = registerRequest.get("apellidos");
            String documento = registerRequest.get("documento");
            String email = registerRequest.get("email");
            String password = registerRequest.get("password");
            String rol = registerRequest.get("rol");
            
            // Validaciones básicas
            if (nombres == null || nombres.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Los nombres son obligatorios");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (apellidos == null || apellidos.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Los apellidos son obligatorios");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (documento == null || documento.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "El documento es obligatorio");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (email == null || email.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "El email es obligatorio");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (password == null || password.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "La contraseña es obligatoria");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (rol == null || rol.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "El rol es obligatorio");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Crear usuario usando el servicio (que usa la base de datos real)
            Usuario nuevoUsuario = usuarioService.crearUsuario(
                nombres, apellidos, documento, email.toLowerCase(), password, rol
            );
            
            // Crear respuesta exitosa
            response.put("success", true);
            response.put("message", "Usuario registrado exitosamente");
            response.put("usuario", Map.of(
                "id", nuevoUsuario.getId().toString(),
                "nombres", nuevoUsuario.getNombres(),
                "apellidos", nuevoUsuario.getApellidos(),
                "documento", nuevoUsuario.getDocumento(),
                "email", nuevoUsuario.getEmail(),
                "rol", nuevoUsuario.getRol().name()
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error interno del servidor: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}