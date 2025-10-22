package org.example.backend.service;

import org.example.backend.dto.ClienteProductoDTO;
import org.example.backend.dto.ProductoDTO;
import org.example.backend.entity.Cliente;
import org.example.backend.entity.ClienteProducto;
import org.example.backend.entity.Producto;
import org.example.backend.repository.ClienteProductoRepository;
import org.example.backend.repository.ClienteRepository;
import org.example.backend.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
