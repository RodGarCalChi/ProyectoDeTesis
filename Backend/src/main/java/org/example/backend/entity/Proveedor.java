package org.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "proveedores")
public class Proveedor {
    
    @Id
    @UuidGenerator
    private UUID id;
    
    @NotBlank(message = "La razón social es obligatoria")
    @Size(max = 100, message = "La razón social no puede exceder 100 caracteres")
    @Column(name = "razon_social", nullable = false, length = 100)
    private String razonSocial;
    
    @NotBlank(message = "El RUC es obligatorio")
    @Size(max = 20, message = "El RUC no puede exceder 20 caracteres")
    @Column(name = "ruc", nullable = false, length = 20, unique = true)
    private String ruc;
    
    @Size(max = 100, message = "El contacto no puede exceder 100 caracteres")
    @Column(name = "contacto", length = 100)
    private String contacto;
    
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    @Column(name = "email", length = 100)
    private String email;
    
    @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
    @Column(name = "direccion", length = 200)
    private String direccion;
    
    @Column(name = "habilitado", nullable = false)
    private Boolean habilitado = true;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public Proveedor() {}
    
    public Proveedor(String razonSocial, String ruc, String contacto, 
                    String telefono, String email, String direccion, Boolean habilitado) {
        this.razonSocial = razonSocial;
        this.ruc = ruc;
        this.contacto = contacto;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.habilitado = habilitado;
    }
    
    // Getters y Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getRazonSocial() {
        return razonSocial;
    }
    
    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }
    
    public String getRuc() {
        return ruc;
    }
    
    public void setRuc(String ruc) {
        this.ruc = ruc;
    }
    
    public String getContacto() {
        return contacto;
    }
    
    public void setContacto(String contacto) {
        this.contacto = contacto;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public Boolean getHabilitado() {
        return habilitado;
    }
    
    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
    
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
    
    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proveedor proveedor = (Proveedor) o;
        return Objects.equals(id, proveedor.id) && Objects.equals(ruc, proveedor.ruc);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, ruc);
    }
    
    // toString
    @Override
    public String toString() {
        return "Proveedor{" +
                "id=" + id +
                ", razonSocial='" + razonSocial + '\'' +
                ", ruc='" + ruc + '\'' +
                ", contacto='" + contacto + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", direccion='" + direccion + '\'' +
                ", habilitado=" + habilitado +
                '}';
    }
}