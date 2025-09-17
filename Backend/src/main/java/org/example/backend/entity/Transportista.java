package org.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.backend.enumeraciones.Rol;

@Entity
@Table(name = "transportistas")
@DiscriminatorValue("TRANSPORTISTA")
public class Transportista extends Usuario {
    
    @NotBlank(message = "La licencia es obligatoria")
    @Size(max = 20, message = "La licencia no puede exceder 20 caracteres")
    @Column(name = "licencia", nullable = false, length = 20)
    private String licencia;
    
    @NotBlank(message = "La categoría es obligatoria")
    @Size(max = 10, message = "La categoría no puede exceder 10 caracteres")
    @Column(name = "categoria", nullable = false, length = 10)
    private String categoria;
    
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    @Column(name = "cert_cadena_frio", nullable = false)
    private Boolean certCadenaFrio = false;
    
    // Constructores
    public Transportista() {
        super();
    }
    
    public Transportista(String nombres, String apellidos, String documento, 
                        String email, String passwordHash, Rol rol,
                        String licencia, String categoria, String telefono, 
                        Boolean certCadenaFrio) {
        super(nombres, apellidos, documento, email, passwordHash, rol);
        this.licencia = licencia;
        this.categoria = categoria;
        this.telefono = telefono;
        this.certCadenaFrio = certCadenaFrio;
    }
    
    // Getters y Setters
    public String getLicencia() {
        return licencia;
    }
    
    public void setLicencia(String licencia) {
        this.licencia = licencia;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public Boolean getCertCadenaFrio() {
        return certCadenaFrio;
    }
    
    public void setCertCadenaFrio(Boolean certCadenaFrio) {
        this.certCadenaFrio = certCadenaFrio;
    }
    
    // toString
    @Override
    public String toString() {
        return "Transportista{" +
                "id=" + getId() +
                ", nombres='" + getNombres() + '\'' +
                ", apellidos='" + getApellidos() + '\'' +
                ", documento='" + getDocumento() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", rol='" + getRol() + '\'' +
                ", licencia='" + licencia + '\'' +
                ", categoria='" + categoria + '\'' +
                ", telefono='" + telefono + '\'' +
                ", certCadenaFrio=" + certCadenaFrio +
                ", activo=" + getActivo() +
                '}';
    }
}