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
    
    // Asignar múltiples productos a un cliente existente
    public ResultadoAsignacionDTO asignarVariosProductos(AsignarProductosDTO dto) {
        Cliente cliente = clienteRepository.findById(UUID.fromString(dto.getClienteId()))
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        List<ProductoDTO> productosAsignados = new ArrayList<>();
        int totalAsignaciones = 0;
        
        for (String productoId : dto.getProductosIds()) {
            UUID prodId = UUID.fromString(productoId);
            
            // Verificar si ya existe la relación
            if (!clienteProductoRepository.existsByClienteIdAndProductoId(cliente.getId(), prodId)) {
                Producto producto = productoRepository.findById(prodId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + productoId));
                
                ClienteProducto relacion = new ClienteProducto(cliente, producto, dto.getObservaciones());
                clienteProductoRepository.save(relacion);
                
                productosAsignados.add(convertirProductoADTO(producto));
                totalAsignaciones++;
            }
        }
        
        ClienteDTO clienteDTO = convertirClienteADTO(cliente);
        return new ResultadoAsignacionDTO(
                clienteDTO,
                productosAsignados,
                totalAsignaciones,
                "Se asignaron " + totalAsignaciones + " productos al cliente"
        );
    }
    
    // Crear cliente con productos (existentes y/o nuevos)
    public ResultadoAsignacionDTO crearClienteConProductos(ClienteConProductosDTO dto) {
        // Crear el cliente
        ClienteDTO clienteCreado = clienteService.crearCliente(dto.getCliente());
        UUID clienteId = clienteCreado.getId();
        
        List<ProductoDTO> productosAsignados = new ArrayList<>();
        int totalAsignaciones = 0;
        
        // Asignar productos existentes
        if (dto.getProductosExistentesIds() != null && !dto.getProductosExistentesIds().isEmpty()) {
            for (String productoId : dto.getProductosExistentesIds()) {
                UUID prodId = UUID.fromString(productoId);
                Producto producto = productoRepository.findById(prodId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + productoId));
                
                Cliente cliente = clienteRepository.findById(clienteId).get();
                ClienteProducto relacion = new ClienteProducto(cliente, producto, dto.getObservaciones());
                clienteProductoRepository.save(relacion);
                
                productosAsignados.add(convertirProductoADTO(producto));
                totalAsignaciones++;
            }
        }
        
        // Crear y asignar productos nuevos
        if (dto.getProductosNuevos() != null && !dto.getProductosNuevos().isEmpty()) {
            for (ProductoCreateDTO productoNuevo : dto.getProductosNuevos()) {
                ProductoDTO productoCreado = productoService.crearProducto(productoNuevo);
                
                Cliente cliente = clienteRepository.findById(clienteId).get();
                Producto producto = productoRepository.findById(productoCreado.getId()).get();
                
                ClienteProducto relacion = new ClienteProducto(cliente, producto, dto.getObservaciones());
                clienteProductoRepository.save(relacion);
                
                productosAsignados.add(productoCreado);
                totalAsignaciones++;
            }
        }
        
        return new ResultadoAsignacionDTO(
                clienteCreado,
                productosAsignados,
                totalAsignaciones,
                "Cliente creado con " + totalAsignaciones + " productos asignados"
        );
    }
    
    // Crear/asociar producto con cliente (maneja todos los casos)
    public ResultadoAsignacionDTO crearOAsociarProductoConCliente(ProductoConClienteDTO dto) {
        Cliente cliente;
        Producto producto;
        
        // Manejar cliente (existente o nuevo)
        if (dto.getClienteId() != null && !dto.getClienteId().isEmpty()) {
            cliente = clienteRepository.findById(UUID.fromString(dto.getClienteId()))
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        } else if (dto.getClienteNuevo() != null) {
            ClienteDTO clienteCreado = clienteService.crearCliente(dto.getClienteNuevo());
            cliente = clienteRepository.findById(clienteCreado.getId()).get();
        } else {
            throw new RuntimeException("Debe proporcionar un clienteId o datos de clienteNuevo");
        }
        
        // Manejar producto (existente o nuevo)
        if (dto.getProductoId() != null && !dto.getProductoId().isEmpty()) {
            producto = productoRepository.findById(UUID.fromString(dto.getProductoId()))
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        } else if (dto.getProductoNuevo() != null) {
            ProductoDTO productoCreado = productoService.crearProducto(dto.getProductoNuevo());
            producto = productoRepository.findById(productoCreado.getId()).get();
        } else {
            throw new RuntimeException("Debe proporcionar un productoId o datos de productoNuevo");
        }
        
        // Verificar si ya existe la relación
        if (clienteProductoRepository.existsByClienteIdAndProductoId(cliente.getId(), producto.getId())) {
            throw new RuntimeException("El producto ya está asignado a este cliente");
        }
        
        // Crear la relación
        ClienteProducto relacion = new ClienteProducto(cliente, producto, dto.getObservaciones());
        clienteProductoRepository.save(relacion);
        
        List<ProductoDTO> productos = new ArrayList<>();
        productos.add(convertirProductoADTO(producto));
        
        return new ResultadoAsignacionDTO(
                convertirClienteADTO(cliente),
                productos,
                1,
                "Producto asociado exitosamente al cliente"
        );
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
