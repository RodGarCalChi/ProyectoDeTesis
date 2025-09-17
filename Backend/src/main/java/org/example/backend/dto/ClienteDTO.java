package org.example.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public class ClienteDTO {
    
    private UUID id;
    
    @NotBlank(message = "La razón social es obligatoria")
    @Size(max = 100, message = "La razón social no puede exceder 100 caracteres")
    private String razonSocial;
    
    @NotBlank(message = "El RUC/DNI es obligatorio")
    @Size(max = 20, message = "El RUC/DNI no puede exceder 20 caracteres")
    private String rucDni;
    
    @NotBlank(message = "La dirección de entrega es obligatoria")
    @Size(max = 200, message = "La dirección de entrega no puede exceder 200 caracteres")
    private String direccionEntrega;
    
    @NotBlank(message = "El distrito es obligatorio")
    @Size(max = 50, message = "El distrito no puede exceder 50 caracteres")
    private String distrito;
    
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;
    
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    private String email;
    
    @Size(max = 50, message = "El tipo de cliente no puede exceder 50 caracteres")
    private String tipoCliente;
    
    @Size(max = 50, message = "La forma de pago no puede exceder 50 caracteres")
    private String formaPago;
    
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public ClienteDTO() {}
    
    public ClienteDTO(UUID id, String razonSocial, String rucDni, String direccionEntrega, 
                     String distrito, String telefono, String email, 
                     String tipoCliente, String formaPago, Boolean activo,
                     LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.id = id;
        this.razonSocial = razonSocial;
        this.rucDni = rucDni;
        this.direccionEntrega = direccionEntrega;
        this.distrito = distrito;
        this.telefono = telefono;
        this.email = email;
        this.tipoCliente = tipoCliente;
        this.formaPago = formaPago;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
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
    
    public String getRucDni() {
        return rucDni;
    }
    
    public void setRucDni(String rucDni) {
        this.rucDni = rucDni;
    }
    
    public String getDireccionEntrega() {
        return direccionEntrega;
    }
    
    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }
    
    public String getDistrito() {
        return distrito;
    }
    
    public void setDistrito(String distrito) {
        this.distrito = distrito;
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
    
    public String getTipoCliente() {
        return tipoCliente;
    }
    
    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }
    
    public String getFormaPago() {
        return formaPago;
    }
    
    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }
    
    public Boolean getActivo() {
        return activo;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
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
}