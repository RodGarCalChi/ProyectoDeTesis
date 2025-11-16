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
    
    // ==================== CRUD BÁSICO DE CLIENTES ====================
    
    /**
     * Obtener todos los clientes
     */
    @Transactional(readOnly = true)
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }
    
    /**
     * Obtener clientes activos
     */
    @Transactional(readOnly = true)
    public List<Cliente> obtenerClientesActivos() {
        return clienteRepository.findByActivoTrue();
    }
    
    /**
     * Obtener cliente por ID
     */
    @Transactional(readOnly = true)
    public Cliente obtenerClientePorId(UUID id) {
        return clienteRepository.findById(id).orElse(null);
    }
    
    /**
     * Buscar clientes por razón social
     */
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorRazonSocial(String razonSocial) {
        return clienteRepository.findByRazonSocialContainingIgnoreCase(razonSocial);
    }
    
    /**
     * Crear nuevo cliente
     */
    public Cliente crearCliente(Cliente cliente) {
        // Verificar que no exista el RUC/DNI
        if (clienteRepository.existsByRucDni(cliente.getRucDni())) {
            throw new RuntimeException("Ya existe un cliente con el RUC/DNI: " + cliente.getRucDni());
        }
        return clienteRepository.save(cliente);
    }
    
    /**
     * Actualizar cliente
     */
    public Cliente actualizarCliente(UUID id, Cliente clienteActualizado) {
        Cliente cliente = clienteRepository.findById(id).orElse(null);
        if (cliente == null) {
            return null;
        }
        
        // Actualizar campos
        cliente.setRazonSocial(clienteActualizado.getRazonSocial());
        cliente.setDireccionEntrega(clienteActualizado.getDireccionEntrega());
        cliente.setDistrito(clienteActualizado.getDistrito());
        cliente.setTelefono(clienteActualizado.getTelefono());
        cliente.setEmail(clienteActualizado.getEmail());
        cliente.setTipoCliente(clienteActualizado.getTipoCliente());
        cliente.setFormaPago(clienteActualizado.getFormaPago());
        cliente.setActivo(clienteActualizado.getActivo());
        
        return clienteRepository.save(cliente);
    }
    
    /**
     * Eliminar cliente
     */
    public void eliminarCliente(UUID id) {
        clienteRepository.deleteById(id);
    }
    
    // ==================== GESTIÓN DE PRODUCTOS DEL CLIENTE ====================
    
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
    
    // ==================== OPERACIONES AVANZADAS ====================
    
    /**
     * Crear cliente con productos (existentes y/o nuevos)
     */
    public Cliente crearClienteConProductos(Cliente cliente, List<UUID> productosExistentesIds, 
                                           List<Producto> productosNuevos) {
        // 1. Crear el cliente
        Cliente nuevoCliente = crearCliente(cliente);
        
        // 2. Asignar productos existentes si hay
        if (productosExistentesIds != null && !productosExistentesIds.isEmpty()) {
            List<Producto> productosExistentes = productoRepository.findAllById(productosExistentesIds);
            productosExistentes.forEach(producto -> {
                nuevoCliente.getProductos().add(producto);
                producto.getClientes().add(nuevoCliente);
            });
        }
        
        // 3. Crear y asignar productos nuevos si hay
        if (productosNuevos != null && !productosNuevos.isEmpty()) {
            productosNuevos.forEach(producto -> {
                Producto nuevoProducto = productoRepository.save(producto);
                nuevoCliente.getProductos().add(nuevoProducto);
                nuevoProducto.getClientes().add(nuevoCliente);
            });
        }
        
        return clienteRepository.save(nuevoCliente);
    }
    
    /**
     * Asignar productos masivamente a un cliente
     */
    public Cliente asignarProductosMasivo(UUID clienteId, List<UUID> productosIds) {
        return asignarProductos(clienteId, productosIds);
    }
    
    /**
     * Obtener todos los clientes con sus productos
     */
    @Transactional(readOnly = true)
    public List<Cliente> obtenerClientesConProductos() {
        List<Cliente> clientes = clienteRepository.findAll();
        // Forzar la carga de productos (lazy loading)
        clientes.forEach(cliente -> cliente.getProductos().size());
        return clientes;
    }
}
