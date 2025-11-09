package org.example.backend.service;

import org.example.backend.dto.*;
import org.example.backend.entity.Cliente;
import org.example.backend.entity.ClienteProducto;
import org.example.backend.entity.Producto;
import org.example.backend.repository.ClienteProductoRepository;
import org.example.backend.repository.ClienteRepository;
import org.example.backend.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClienteProductoService {
    
    @Autowired
    private ClienteProductoRepository clienteProductoRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private ProductoService productoService;
    
    // Asignar producto a cliente
    public ClienteProductoDTO asignarProductoACliente(ClienteProductoDTO dto) {
        // Verificar que no exista ya la relación
        if (clienteProductoRepository.existsByClienteIdAndProductoId(dto.getClienteId(), dto.getProductoId())) {
            throw new RuntimeException("El producto ya está asignado a este cliente");
        }
        
        // Buscar cliente y producto
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        // Crear relación
        ClienteProducto clienteProducto = new ClienteProducto(cliente, producto, dto.getObservaciones());
        clienteProducto = clienteProductoRepository.save(clienteProducto);
        
        return convertirADTO(clienteProducto);
    }
    
    // Obtener productos de un cliente
    @Transactional(readOnly = true)
    public List<ProductoDTO> obtenerProductosDeCliente(UUID clienteId) {
        List<Producto> productos = clienteProductoRepository.findProductosByClienteId(clienteId);
        return productos.stream()
                .map(this::convertirProductoADTO)
                .collect(Collectors.toList());
    }
    
    // Obtener relaciones de un cliente
    @Transactional(readOnly = true)
    public List<ClienteProductoDTO> obtenerRelacionesDeCliente(UUID clienteId) {
        return clienteProductoRepository.findByClienteIdAndActivoTrue(clienteId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    // Obtener relaciones de un producto
    @Transactional(readOnly = true)
    public List<ClienteProductoDTO> obtenerRelacionesDeProducto(UUID productoId) {
        return clienteProductoRepository.findByProductoIdAndActivoTrue(productoId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    // Desactivar relación
    public void desactivarRelacion(UUID clienteId, UUID productoId) {
        ClienteProducto relacion = clienteProductoRepository.findByClienteIdAndProductoId(clienteId, productoId)
                .orElseThrow(() -> new RuntimeException("Relación no encontrada"));
        
        relacion.setActivo(false);
        clienteProductoRepository.save(relacion);
    }
    
    // Activar relación
    public void activarRelacion(UUID clienteId, UUID productoId) {
        ClienteProducto relacion = clienteProductoRepository.findByClienteIdAndProductoId(clienteId, productoId)
                .orElseThrow(() -> new RuntimeException("Relación no encontrada"));
        
        relacion.setActivo(true);
        clienteProductoRepository.save(relacion);
    }
    
    // Contar productos de un cliente
    @Transactional(readOnly = true)
    public Long contarProductosDeCliente(UUID clienteId) {
        return clienteProductoRepository.countProductosByClienteId(clienteId);
    }
    

    
    // Listar todas las relaciones cliente-producto
    @Transactional(readOnly = true)
    public List<ClienteProductoDTO> listarTodasLasRelaciones() {
        return clienteProductoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    // Listar solo relaciones activas
    @Transactional(readOnly = true)
    public List<ClienteProductoDTO> listarRelacionesActivas() {
        return clienteProductoRepository.findByActivoTrue().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    // Asignación masiva de múltiples productos a múltiples clientes
    public List<ClienteProductoDTO> asignarMasivo(List<ClienteProductoDTO> asignaciones) {
        System.out.println("=== INICIO ASIGNACIÓN MASIVA ===");
        System.out.println("Total de asignaciones recibidas: " + asignaciones.size());
        
        List<ClienteProductoDTO> resultados = new ArrayList<>();
        List<String> errores = new ArrayList<>();
        
        for (int i = 0; i < asignaciones.size(); i++) {
            final int indice = i;
            ClienteProductoDTO dto = asignaciones.get(i);
            
            System.out.println("Procesando asignación " + indice + ": Cliente=" + dto.getClienteId() + ", Producto=" + dto.getProductoId());
            
            try {
                // Verificar que los IDs no sean nulos
                if (dto.getClienteId() == null || dto.getProductoId() == null) {
                    errores.add("Índice " + indice + ": ClienteId o ProductoId es nulo");
                    continue;
                }
                
                // Verificar que no exista ya la relación
                boolean existe = clienteProductoRepository.existsByClienteIdAndProductoId(dto.getClienteId(), dto.getProductoId());
                System.out.println("  - Relación existe: " + existe);
                
                if (existe) {
                    errores.add("Índice " + indice + ": El producto ya está asignado a este cliente");
                    continue;
                }
                
                // Buscar cliente
                System.out.println("  - Buscando cliente...");
                Cliente cliente = clienteRepository.findById(dto.getClienteId())
                        .orElseThrow(() -> new RuntimeException("Índice " + indice + ": Cliente no encontrado"));
                System.out.println("  - Cliente encontrado: " + cliente.getRazonSocial());
                
                // Buscar producto
                System.out.println("  - Buscando producto...");
                Producto producto = productoRepository.findById(dto.getProductoId())
                        .orElseThrow(() -> new RuntimeException("Índice " + indice + ": Producto no encontrado"));
                System.out.println("  - Producto encontrado: " + producto.getNombre());
                
                // Crear relación
                System.out.println("  - Creando relación...");
                ClienteProducto clienteProducto = new ClienteProducto(cliente, producto, dto.getObservaciones());
                clienteProducto = clienteProductoRepository.save(clienteProducto);
                System.out.println("  - Relación guardada con ID: " + clienteProducto.getId());
                
                resultados.add(convertirADTO(clienteProducto));
                System.out.println("  - ✓ Asignación " + indice + " completada");
                
            } catch (Exception e) {
                System.err.println("  - ✗ Error en asignación " + indice + ": " + e.getMessage());
                e.printStackTrace();
                errores.add("Índice " + indice + ": " + e.getMessage());
            }
        }
        
        System.out.println("=== FIN ASIGNACIÓN MASIVA ===");
        System.out.println("Exitosas: " + resultados.size() + ", Errores: " + errores.size());
        
        // Si hubo errores, lanzar excepción con el resumen
        if (!errores.isEmpty()) {
            String mensajeError = "Se procesaron " + resultados.size() + " de " + asignaciones.size() + 
                    " asignaciones. Errores: " + String.join("; ", errores);
            System.err.println(mensajeError);
            throw new RuntimeException(mensajeError);
        }
        
        return resultados;
    }
    
    // Métodos auxiliares
    private ClienteProductoDTO convertirADTO(ClienteProducto cp) {
        return new ClienteProductoDTO(
                cp.getId(),
                cp.getCliente().getId(),
                cp.getCliente().getRazonSocial(),
                cp.getProducto().getId(),
                cp.getProducto().getNombre(),
                cp.getProducto().getCodigoSKU(),
                cp.getFechaAsignacion(),
                cp.getActivo(),
                cp.getObservaciones()
        );
    }
    
    private ProductoDTO convertirProductoADTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setCodigoSKU(producto.getCodigoSKU());
        dto.setNombre(producto.getNombre());
        dto.setTipo(producto.getTipo());
        dto.setCondicionAlmacen(producto.getCondicionAlmacen());
        dto.setRequiereCadenaFrio(producto.getRequiereCadenaFrio());
        dto.setRegistroSanitario(producto.getRegistroSanitario());
        dto.setUnidadMedida(producto.getUnidadMedida());
        dto.setVidaUtilMeses(producto.getVidaUtilMeses());
        dto.setTempMin(producto.getTempMin());
        dto.setTempMax(producto.getTempMax());
        dto.setFechaCreacion(producto.getFechaCreacion());
        dto.setFechaActualizacion(producto.getFechaActualizacion());
        return dto;
    }
    
    private ClienteDTO convertirClienteADTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setRazonSocial(cliente.getRazonSocial());
        dto.setRucDni(cliente.getRucDni());
        dto.setDireccionEntrega(cliente.getDireccionEntrega());
        dto.setDistrito(cliente.getDistrito());
        dto.setTelefono(cliente.getTelefono());
        dto.setEmail(cliente.getEmail());
        dto.setTipoCliente(cliente.getTipoCliente());
        dto.setFormaPago(cliente.getFormaPago());
        dto.setActivo(cliente.getActivo());
        dto.setFechaCreacion(cliente.getFechaCreacion());
        dto.setFechaActualizacion(cliente.getFechaActualizacion());
        return dto;
    }
}
