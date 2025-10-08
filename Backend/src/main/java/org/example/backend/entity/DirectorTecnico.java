package org.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.backend.enumeraciones.Rol;

@Entity
@Table(name = "directores_tecnicos")
public class DirectorTecnico extends Usuario {
    
    @NotBlank(message = "La colegiatura es obligatoria")
    @Size(max = 20, message = "La colegiatura no puede exceder 20 caracteres")
    @Column(name = "colegiatura", nullable = false, length = 20)
    private String colegiatura;
    
    @NotBlank(message = "El registro DIGEMID es obligatorio")
    @Size(max = 30, message = "El registro DIGEMID no puede exceder 30 caracteres")
    @Column(name = "registro_digemid", nullable = false, length = 30)
    private String registroDIGEMID;
    
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    // Constructores
    public DirectorTecnico() {
        super();
    }
    
    public DirectorTecnico(String nombres, String apellidos, String documento, 
                          String email, String passwordHash, Rol rol,
                          String colegiatura, String registroDIGEMID, String telefono) {
        super(nombres, apellidos, documento, email, passwordHash, rol);
        this.colegiatura = colegiatura;
        this.registroDIGEMID = registroDIGEMID;
        this.telefono = telefono;
    }
    
    // Getters y Setters
    public String getColegiatura() {
        return colegiatura;
    }
    
    public void setColegiatura(String colegiatura) {
        this.colegiatura = colegiatura;
    }
    
    public String getRegistroDIGEMID() {
        return registroDIGEMID;
    }
    
    public void setRegistroDIGEMID(String registroDIGEMID) {
        this.registroDIGEMID = registroDIGEMID;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    // toString
    @Override
    public String toString() {
        return "DirectorTecnico{" +
                "id=" + getId() +
                ", nombres='" + getNombres() + '\'' +
                ", apellidos='" + getApellidos() + '\'' +
                ", documento='" + getDocumento() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", rol='" + getRol() + '\'' +
                ", colegiatura='" + colegiatura + '\'' +
                ", registroDIGEMID='" + registroDIGEMID + '\'' +
                ", telefono='" + telefono + '\'' +
                ", activo=" + getActivo() +
                '}';
    }
}