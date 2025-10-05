package org.example.backend.dto;

public class LoginResponseDTO {
    
    private String token;
    private UsuarioDTO usuario;
    private String message;
    
    // Constructores
    public LoginResponseDTO() {}
    
    public LoginResponseDTO(String token, UsuarioDTO usuario, String message) {
        this.token = token;
        this.usuario = usuario;
        this.message = message;
    }
    
    // Constructor para respuesta exitosa
    public LoginResponseDTO(String token, UsuarioDTO usuario) {
        this.token = token;
        this.usuario = usuario;
        this.message = "Login exitoso";
    }
    
    // Constructor para respuesta de error
    public LoginResponseDTO(String message) {
        this.message = message;
    }
    
    // Getters y Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public UsuarioDTO getUsuario() {
        return usuario;
    }
    
    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}