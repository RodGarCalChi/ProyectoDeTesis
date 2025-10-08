package org.example.backend.service;

import org.example.backend.dto.UsuarioDTO;
import org.example.backend.entity.Usuario;
import org.example.backend.enumeraciones.Rol;
import org.example.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    public Usuario crearUsuario(String nombres, String apellidos, String documento, 
                               String email, String password, String rolStr) {
        
        // Validar que el email no exista
        if (usuarioRepository.existsByEmail(email)) {
            throw new RuntimeException("Ya existe un usuario con este email");
        }
        
        // Validar que el documento no exista
        if (usuarioRepository.existsByDocumento(documento)) {
            throw new RuntimeException("Ya existe un usuario con este documento");
        }
        
        // Convertir string a enum Rol
        Rol rol;
        try {
            rol = Rol.valueOf(rolStr);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Rol inválido: " + rolStr);
        }
        
        // Hashear la contraseña antes de guardar
        String passwordHash = passwordEncoder.encode(password);
        
        // Crear nuevo usuario con contraseña hasheada
        Usuario usuario = new Usuario(nombres, apellidos, documento, email, passwordHash, rol);
        
        return usuarioRepository.save(usuario);
    }
    
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    public boolean validarCredenciales(String email, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            
            // Verificar que el usuario esté activo
            if (!usuario.getActivo()) {
                return false;
            }
            
            String storedHash = usuario.getPasswordHash();
            boolean passwordValido = false;
            
            // Verificar si el hash almacenado es un hash BCrypt válido
            if (storedHash.startsWith("$2a$") || storedHash.startsWith("$2b$") || storedHash.startsWith("$2y$")) {
                // Es un hash BCrypt, usar matches
                passwordValido = passwordEncoder.matches(password, storedHash);
            } else {
                // No es un hash BCrypt, comparar directamente (para compatibilidad con datos existentes)
                passwordValido = storedHash.equals(password);
            }
            
            if (passwordValido) {
                // Actualizar último acceso
                usuario.setUltimoAcceso(LocalDateTime.now());
                usuarioRepository.save(usuario);
                return true;
            }
        }
        
        return false;
    }
    
    public UsuarioDTO convertirADTO(Usuario usuario) {
        return new UsuarioDTO(
            (long) (usuario.getId().hashCode() & Integer.MAX_VALUE), // Convertir UUID a Long
            usuario.getNombreCompleto(),
            usuario.getEmail(),
            usuario.getRol().name()
        );
    }
}