package org.example.backend.service;

import org.example.backend.entity.Cliente;
import org.example.backend.entity.Producto;
import org.example.backend.repository.ClienteRepository;
import org.example.backend.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    /**
     * Asignar múltiples productos a un cliente
     */
    public Cliente asignarProductos(UUID clienteId, List<UUID> productosIds) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clienteId));
        
        List<Producto> productos = productoRepository.findAllById(productosIds);
        
        if (productos.isEmpty()) {
            throw new RuntimeException("No se encontraron productos con los IDs proporcionados");
        }
        
        // Agregar productos al cliente
        productos.forEach(producto -> {
            cliente.getProductos().add(producto);
            producto.getClientes().add(cliente);
        });
        
        return clienteRepository.save(cliente);
    }
    
    /**
     * Asignar un solo producto a un cliente
     */
    public Cliente asignarProducto(UUID clienteId, UUID productoId) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clienteId));
        
        Producto producto = productoRepository.findById(productoId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoId));
        
        cliente.getProductos().add(producto);
        producto.getClientes().add(cliente);
        
        return clienteRepository.save(cliente);
    }
    
    /**
     * Obtener todos los productos de un cliente
     */
    @Transactional(readOnly = true)
    public Set<Producto> obtenerProductosDeCliente(UUID clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clienteId));
        
        return cliente.getProductos();
    }
    
    /**
     * Remover un producto de un cliente
     */
    public Cliente removerProducto(UUID clienteId, UUID productoId) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clienteId));
        
        Producto producto = productoRepository.findById(productoId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoId));
        
        cliente.getProductos().remove(producto);
        producto.getClientes().remove(cliente);
        
        return clienteRepository.save(cliente);
    }
    
    /**
     * Remover todos los productos de un cliente
     */
    public Cliente removerTodosLosProductos(UUID clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clienteId));
        
        // Remover la relación bidireccional
        cliente.getProductos().forEach(producto -> producto.getClientes().remove(cliente));
        cliente.getProductos().clear();
        
        return clienteRepository.save(cliente);
    }
    
    /**
     * Obtener todos los clientes que tienen un producto específico
     */
    @Transactional(readOnly = true)
    public Set<Cliente> obtenerClientesDeProducto(UUID productoId) {
        Producto producto = productoRepository.findById(productoId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoId));
        
        return producto.getClientes();
    }
    
    /**
     * Verificar si un cliente tiene un producto específico
     */
    @Transactional(readOnly = true)
    public boolean clienteTieneProducto(UUID clienteId, UUID productoId) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clienteId));
        
        return cliente.getProductos().stream()
            .anyMatch(p -> p.getId().equals(productoId));
    }
}