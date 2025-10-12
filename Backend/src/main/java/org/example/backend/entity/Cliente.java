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
@Table(name = "clientes")
public class Cliente {

    @Id
    @UuidGenerator
    private UUID id;

    @NotBlank(message = "La razón social es obligatoria")
    @Size(max = 100, message = "La razón social no puede exceder 100 caracteres")
    @Column(name = "razon_social", nullable = false, length = 100)
    private String razonSocial;

    @NotBlank(message = "El RUC/DNI es obligatorio")
    @Size(max = 20, message = "El RUC/DNI no puede exceder 20 caracteres")
    @Column(name = "ruc_dni", nullable = false, length = 20, unique = true)
    private String rucDni;

    @NotBlank(message = "La dirección de entrega es obligatoria")
    @Size(max = 200, message = "La dirección de entrega no puede exceder 200 caracteres")
    @Column(name = "direccion_entrega", nullable = false, length = 200)
    private String direccionEntrega;

    @NotBlank(message = "El distrito es obligatorio")
    @Size(max = 50, message = "El distrito no puede exceder 50 caracteres")
    @Column(name = "distrito", nullable = false, length = 50)
    private String distrito;

    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Column(name = "telefono", length = 20)
    private String telefono;

    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    @Column(name = "email", length = 100)
    private String email;

    @Size(max = 50, message = "El tipo de cliente no puede exceder 50 caracteres")
    @Column(name = "tipo_cliente", length = 50)
    private String tipoCliente;

    @Size(max = 50, message = "La forma de pago no puede exceder 50 caracteres")
    @Column(name = "forma_pago", length = 50)
    private String formaPago;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    // Constructores
    public Cliente() {
    }

    public Cliente(String razonSocial, String rucDni, String direccionEntrega,
            String distrito, String telefono, String email,
            String tipoCliente, String formaPago) {
        this.razonSocial = razonSocial;
        this.rucDni = rucDni;
        this.direccionEntrega = direccionEntrega;
        this.distrito = distrito;
        this.telefono = telefono;
        this.email = email;
        this.tipoCliente = tipoCliente;
        this.formaPago = formaPago;
        this.activo = true;
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

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id) && Objects.equals(rucDni, cliente.rucDni);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rucDni);
    }

    // toString
    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", razonSocial='" + razonSocial + '\'' +
                ", rucDni='" + rucDni + '\'' +
                ", direccionEntrega='" + direccionEntrega + '\'' +
                ", distrito='" + distrito + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", tipoCliente='" + tipoCliente + '\'' +
                ", formaPago='" + formaPago + '\'' +
                ", activo=" + activo +
                '}';
    }
}