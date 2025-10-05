package org.example.backend.service;

import org.example.backend.dto.UsuarioDTO;
import org.example.backend.entity.Usuario;
import org.example.backend.enumeraciones.Rol;
import org.example.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
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
        
        // Crear nuevo usuario
        Usuario usuario = new Usuario(nombres, apellidos, documento, email, password, rol);
        
        return usuarioRepository.save(usuario);
    }
    
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    public boolean validarCredenciales(String email, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            // En producción, aquí deberías usar BCrypt para comparar passwords hasheados
            boolean passwordValido = usuario.getPasswordHash().equals(password);
            
            if (passwordValido && usuario.getActivo()) {
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