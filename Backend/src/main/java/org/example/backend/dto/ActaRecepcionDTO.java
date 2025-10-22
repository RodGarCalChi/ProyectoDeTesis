package org.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.backend.enumeraciones.EstadoDocumento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ActaRecepcionDTO {

    private UUID id;

    @NotBlank(message = "El número de acta es obligatorio")
    @Size(max = 50, message = "El número de acta no puede exceder 50 caracteres")
    private String numeroActa;

    @NotNull(message = "La fecha de recepción es obligatoria")
    private LocalDateTime fechaRecepcion;

    @NotNull(message = "El ID del cliente es obligatorio")
    private UUID clienteId;

    private String clienteNombre;

    @NotBlank(message = "El responsable de recepción es obligatorio")
    @Size(max = 100, message = "El nombre del responsable no puede exceder 100 caracteres")
    private String responsableRecepcion;

    @Size(max = 200, message = "El lugar de recepción no puede exceder 200 caracteres")
    private String lugarRecepcion;

    private BigDecimal temperaturaLlegada;

    @Size(max = 500, message = "Las condiciones de transporte no pueden exceder 500 caracteres")
    private String condicionesTransporte;

    @Size(max = 1000, message = "Las observaciones no pueden exceder 1000 caracteres")
    private String observaciones;

    @NotNull(message = "El estado es obligatorio")
    private EstadoDocumento estado;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private String creadoPor;
    private String actualizadoPor;

    private List<DetalleActaRecepcionDTO> detalles;

    // Constructores
    public ActaRecepcionDTO() {
    }

    public ActaRecepcionDTO(UUID id, String numeroActa, LocalDateTime fechaRecepcion,
            UUID clienteId, String clienteNombre, String responsableRecepcion,
            String lugarRecepcion, BigDecimal temperaturaLlegada,
            String condicionesTransporte, String observaciones,
            EstadoDocumento estado, LocalDateTime fechaCreacion,
            LocalDateTime fechaActualizacion, String creadoPor, String actualizadoPor) {
        this.id = id;
        this.numeroActa = numeroActa;
        this.fechaRecepcion = fechaRecepcion;
        this.clienteId = clienteId;
        this.clienteNombre = clienteNombre;
        this.responsableRecepcion = responsableRecepcion;
        this.lugarRecepcion = lugarRecepcion;
        this.temperaturaLlegada = temperaturaLlegada;
        this.condicionesTransporte = condicionesTransporte;
        this.observaciones = observaciones;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
        this.creadoPor = creadoPor;
        this.actualizadoPor = actualizadoPor;
    }

    // Getters y Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNumeroActa() {
        return numeroActa;
    }

    public void setNumeroActa(String numeroActa) {
        this.numeroActa = numeroActa;
    }

    public LocalDateTime getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(LocalDateTime fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public UUID getClienteId() {
        return clienteId;
    }

    public void setClienteId(UUID clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getResponsableRecepcion() {
        return responsableRecepcion;
    }

    public void setResponsableRecepcion(String responsableRecepcion) {
        this.responsableRecepcion = responsableRecepcion;
    }

    public String getLugarRecepcion() {
        return lugarRecepcion;
    }

    public void setLugarRecepcion(String lugarRecepcion) {
        this.lugarRecepcion = lugarRecepcion;
    }

    public BigDecimal getTemperaturaLlegada() {
        return temperaturaLlegada;
    }

    public void setTemperaturaLlegada(BigDecimal temperaturaLlegada) {
        this.temperaturaLlegada = temperaturaLlegada;
    }

    public String getCondicionesTransporte() {
        return condicionesTransporte;
    }

    public void setCondicionesTransporte(String condicionesTransporte) {
        this.condicionesTransporte = condicionesTransporte;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public EstadoDocumento getEstado() {
        return estado;
    }

    public void setEstado(EstadoDocumento estado) {
        this.estado = estado;
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

    public String getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }

    public String getActualizadoPor() {
        return actualizadoPor;
    }

    public void setActualizadoPor(String actualizadoPor) {
        this.actualizadoPor = actualizadoPor;
    }

    public List<DetalleActaRecepcionDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleActaRecepcionDTO> detalles) {
        this.detalles = detalles;
    }
}