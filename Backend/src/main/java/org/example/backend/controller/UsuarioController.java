package org.example.backend.controller;

import org.example.backend.dto.UsuarioDTO;
import org.example.backend.dto.UsuarioCreateDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:9002")
public class UsuarioController {

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        // Por ahora devolvemos datos de ejemplo
        List<UsuarioDTO> usuarios = new ArrayList<>();
        usuarios.add(new UsuarioDTO(1L, "admin", "admin@example.com", "ADMIN"));
        usuarios.add(new UsuarioDTO(2L, "user1", "user1@example.com", "USER"));
        usuarios.add(new UsuarioDTO(3L, "manager", "manager@example.com", "MANAGER"));
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Long id) {
        UsuarioDTO usuario = new UsuarioDTO(id, "user" + id, "user" + id + "@example.com", "USER");
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> createUsuario(@RequestBody UsuarioCreateDTO usuarioCreateDTO) {
        UsuarioDTO nuevoUsuario = new UsuarioDTO(
            System.currentTimeMillis(), 
            usuarioCreateDTO.getUsername(), 
            usuarioCreateDTO.getEmail(), 
            "USER"
        );
        return ResponseEntity.ok(nuevoUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable Long id, @RequestBody UsuarioCreateDTO usuarioCreateDTO) {
        UsuarioDTO usuarioActualizado = new UsuarioDTO(
            id, 
            usuarioCreateDTO.getUsername(), 
            usuarioCreateDTO.getEmail(), 
            "USER"
        );
        return ResponseEntity.ok(usuarioActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }
}