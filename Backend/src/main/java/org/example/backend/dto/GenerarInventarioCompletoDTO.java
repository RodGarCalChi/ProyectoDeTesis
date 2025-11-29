package org.example.backend.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.example.backend.enumeraciones.CondicionAlmacen;
import org.example.backend.enumeraciones.TipoProducto;
import org.example.backend.enumeraciones.TipoZona;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class GenerarInventarioCompletoDTO {
    
    @NotEmpty(message = "La lista de clientes es obligatoria")
    private List<UUID> clienteIds;
    
    @NotNull(message = "El operador logístico es obligatorio")
    private OperadorLogisticoDTO operadorLogistico;
    
    @NotNull(message = "El almacén es obligatorio")
    private AlmacenDTO almacen;
    
    @NotEmpty(message = "La lista de zonas es obligatoria")
    private List<ZonaDTO> zonas;
    
    @NotEmpty(message = "La lista de productos con inventario es obligatoria")
    private List<ProductoInventarioDTO> productosInventario;
    
    // Clase interna para OperadorLogistico
    public static class OperadorLogisticoDTO {
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100)
        private String nombre;
        
        @NotBlank(message = "El RUC es obligatorio")
        @Size(max = 20)
        private String ruc;
        
        @Size(max = 200)
        private String direccion;
        
        @Size(max = 20)
        private String telefono;
        
        @Size(max = 100)
        private String email;
        
        // Getters y Setters
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getRuc() { return ruc; }
        public void setRuc(String ruc) { this.ruc = ruc; }
        public String getDireccion() { return direccion; }
        public void setDireccion(String direccion) { this.direccion = direccion; }
        public String getTelefono() { return telefono; }
        public void setTelefono(String telefono) { this.telefono = telefono; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
    
    // Clase interna para Almacen
    public static class AlmacenDTO {
        @NotBlank(message = "El nombre del almacén es obligatorio")
        @Size(max = 100)
        private String nombre;
        
        @NotBlank(message = "La dirección del almacén es obligatoria")
        @Size(max = 200)
        private String direccion;
        
        private Boolean tieneAreaControlados = false;
        
        // Getters y Setters
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getDireccion() { return direccion; }
        public void setDireccion(String direccion) { this.direccion = direccion; }
        public Boolean getTieneAreaControlados() { return tieneAreaControlados; }
        public void setTieneAreaControlados(Boolean tieneAreaControlados) { this.tieneAreaControlados = tieneAreaControlados; }
    }
    
    // Clase interna para Zona
    public static class ZonaDTO {
        @NotBlank(message = "El nombre de la zona es obligatorio")
        @Size(max = 100)
        private String nombre;
        
        @NotNull(message = "El tipo de zona es obligatorio")
        private TipoZona tipo;
        
        @NotEmpty(message = "La lista de ubicaciones es obligatoria")
        private List<UbicacionDTO> ubicaciones;
        
        // Getters y Setters
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public TipoZona getTipo() { return tipo; }
        public void setTipo(TipoZona tipo) { this.tipo = tipo; }
        public List<UbicacionDTO> getUbicaciones() { return ubicaciones; }
        public void setUbicaciones(List<UbicacionDTO> ubicaciones) { this.ubicaciones = ubicaciones; }
    }
    
    // Clase interna para Ubicacion
    public static class UbicacionDTO {
        @NotBlank(message = "El código de ubicación es obligatorio")
        @Size(max = 30)
        private String codigo;
        
        private Integer capacidadMaxima;
        
        private Float tempObjetivoMin;
        
        private Float tempObjetivoMax;
        
        private Boolean disponible = true;
        
        // Getters y Setters
        public String getCodigo() { return codigo; }
        public void setCodigo(String codigo) { this.codigo = codigo; }
        public Integer getCapacidadMaxima() { return capacidadMaxima; }
        public void setCapacidadMaxima(Integer capacidadMaxima) { this.capacidadMaxima = capacidadMaxima; }
        public Float getTempObjetivoMin() { return tempObjetivoMin; }
        public void setTempObjetivoMin(Float tempObjetivoMin) { this.tempObjetivoMin = tempObjetivoMin; }
        public Float getTempObjetivoMax() { return tempObjetivoMax; }
        public void setTempObjetivoMax(Float tempObjetivoMax) { this.tempObjetivoMax = tempObjetivoMax; }
        public Boolean getDisponible() { return disponible; }
        public void setDisponible(Boolean disponible) { this.disponible = disponible; }
    }
    
    // Clase interna para Producto con Inventario
    public static class ProductoInventarioDTO {
        // Datos del producto
        @NotBlank(message = "El código SKU es obligatorio")
        @Size(max = 20)
        private String codigoSKU;
        
        @NotBlank(message = "El nombre del producto es obligatorio")
        @Size(max = 100)
        private String nombre;
        
        @NotNull(message = "El tipo de producto es obligatorio")
        private TipoProducto tipo;
        
        @NotNull(message = "La condición de almacén es obligatoria")
        private CondicionAlmacen condicionAlmacen;
        
        private Boolean requiereCadenaFrio = false;
        
        @Size(max = 30)
        private String registroSanitario;
        
        @Size(max = 20)
        private String unidadMedida;
        
        private Integer vidaUtilMeses;
        
        private Float tempMin;
        
        private Float tempMax;
        
        // Datos del lote
        @NotBlank(message = "El número de lote es obligatorio")
        private String numeroLote;
        
        private LocalDate fechaFabricacion;
        
        @NotNull(message = "La fecha de vencimiento es obligatoria")
        private LocalDate fechaVencimiento;
        
        @NotNull(message = "La cantidad inicial es obligatoria")
        private Integer cantidadInicial;
        
        private String proveedor;
        
        private String observacionesLote;
        
        // Datos del palet
        @NotBlank(message = "El código del palet es obligatorio")
        @Size(max = 50)
        private String codigoPalet;
        
        private Integer capacidadMaximaPalet;
        
        private Integer cajasActuales = 0;
        
        private Float pesoMaximoKg;
        
        private Float pesoActualKg = 0.0f;
        
        private String observacionesPalet;
        
        // Datos del inventario
        @NotNull(message = "La cantidad disponible es obligatoria")
        private Integer cantidadDisponible;
        
        private String codigoBarras;
        
        private Float temperaturaAlmacenamiento;
        
        private String observacionesInventario;
        
        // Getters y Setters
        public String getCodigoSKU() { return codigoSKU; }
        public void setCodigoSKU(String codigoSKU) { this.codigoSKU = codigoSKU; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public TipoProducto getTipo() { return tipo; }
        public void setTipo(TipoProducto tipo) { this.tipo = tipo; }
        public CondicionAlmacen getCondicionAlmacen() { return condicionAlmacen; }
        public void setCondicionAlmacen(CondicionAlmacen condicionAlmacen) { this.condicionAlmacen = condicionAlmacen; }
        public Boolean getRequiereCadenaFrio() { return requiereCadenaFrio; }
        public void setRequiereCadenaFrio(Boolean requiereCadenaFrio) { this.requiereCadenaFrio = requiereCadenaFrio; }
        public String getRegistroSanitario() { return registroSanitario; }
        public void setRegistroSanitario(String registroSanitario) { this.registroSanitario = registroSanitario; }
        public String getUnidadMedida() { return unidadMedida; }
        public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }
        public Integer getVidaUtilMeses() { return vidaUtilMeses; }
        public void setVidaUtilMeses(Integer vidaUtilMeses) { this.vidaUtilMeses = vidaUtilMeses; }
        public Float getTempMin() { return tempMin; }
        public void setTempMin(Float tempMin) { this.tempMin = tempMin; }
        public Float getTempMax() { return tempMax; }
        public void setTempMax(Float tempMax) { this.tempMax = tempMax; }
        public String getNumeroLote() { return numeroLote; }
        public void setNumeroLote(String numeroLote) { this.numeroLote = numeroLote; }
        public LocalDate getFechaFabricacion() { return fechaFabricacion; }
        public void setFechaFabricacion(LocalDate fechaFabricacion) { this.fechaFabricacion = fechaFabricacion; }
        public LocalDate getFechaVencimiento() { return fechaVencimiento; }
        public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
        public Integer getCantidadInicial() { return cantidadInicial; }
        public void setCantidadInicial(Integer cantidadInicial) { this.cantidadInicial = cantidadInicial; }
        public String getProveedor() { return proveedor; }
        public void setProveedor(String proveedor) { this.proveedor = proveedor; }
        public String getObservacionesLote() { return observacionesLote; }
        public void setObservacionesLote(String observacionesLote) { this.observacionesLote = observacionesLote; }
        public String getCodigoPalet() { return codigoPalet; }
        public void setCodigoPalet(String codigoPalet) { this.codigoPalet = codigoPalet; }
        public Integer getCapacidadMaximaPalet() { return capacidadMaximaPalet; }
        public void setCapacidadMaximaPalet(Integer capacidadMaximaPalet) { this.capacidadMaximaPalet = capacidadMaximaPalet; }
        public Integer getCajasActuales() { return cajasActuales; }
        public void setCajasActuales(Integer cajasActuales) { this.cajasActuales = cajasActuales; }
        public Float getPesoMaximoKg() { return pesoMaximoKg; }
        public void setPesoMaximoKg(Float pesoMaximoKg) { this.pesoMaximoKg = pesoMaximoKg; }
        public Float getPesoActualKg() { return pesoActualKg; }
        public void setPesoActualKg(Float pesoActualKg) { this.pesoActualKg = pesoActualKg; }
        public String getObservacionesPalet() { return observacionesPalet; }
        public void setObservacionesPalet(String observacionesPalet) { this.observacionesPalet = observacionesPalet; }
        public Integer getCantidadDisponible() { return cantidadDisponible; }
        public void setCantidadDisponible(Integer cantidadDisponible) { this.cantidadDisponible = cantidadDisponible; }
        public String getCodigoBarras() { return codigoBarras; }
        public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }
        public Float getTemperaturaAlmacenamiento() { return temperaturaAlmacenamiento; }
        public void setTemperaturaAlmacenamiento(Float temperaturaAlmacenamiento) { this.temperaturaAlmacenamiento = temperaturaAlmacenamiento; }
        public String getObservacionesInventario() { return observacionesInventario; }
        public void setObservacionesInventario(String observacionesInventario) { this.observacionesInventario = observacionesInventario; }
    }
    
    // Getters y Setters
    public List<UUID> getClienteIds() { return clienteIds; }
    public void setClienteIds(List<UUID> clienteIds) { this.clienteIds = clienteIds; }
    public OperadorLogisticoDTO getOperadorLogistico() { return operadorLogistico; }
    public void setOperadorLogistico(OperadorLogisticoDTO operadorLogistico) { this.operadorLogistico = operadorLogistico; }
    public AlmacenDTO getAlmacen() { return almacen; }
    public void setAlmacen(AlmacenDTO almacen) { this.almacen = almacen; }
    public List<ZonaDTO> getZonas() { return zonas; }
    public void setZonas(List<ZonaDTO> zonas) { this.zonas = zonas; }
    public List<ProductoInventarioDTO> getProductosInventario() { return productosInventario; }
    public void setProductosInventario(List<ProductoInventarioDTO> productosInventario) { this.productosInventario = productosInventario; }
}





