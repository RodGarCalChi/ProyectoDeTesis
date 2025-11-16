package org.example.backend.service;

import org.example.backend.dto.ProductoCreateDTO;
import org.example.backend.dto.ProductoDTO;
import org.example.backend.dto.ProductoUpdateDTO;
import org.example.backend.entity.Producto;
import org.example.backend.enumeraciones.TipoProducto;
import org.example.backend.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    // Crear producto
    public ProductoDTO crearProducto(ProductoCreateDTO createDTO) {
        // Verificar que no exista el código SKU
        if (productoRepository.existsByCodigoSKU(createDTO.getCodigoSKU())) {
            throw new RuntimeException("Ya existe un producto con el código SKU: " + createDTO.getCodigoSKU());
        }

        Producto producto = new Producto(
                createDTO.getCodigoSKU(),
                createDTO.getNombre(),
                createDTO.getTipo(),
                createDTO.getCondicionAlmacen(),
                createDTO.getRequiereCadenaFrio(),
                createDTO.getRegistroSanitario(),
                createDTO.getUnidadMedida(),
                createDTO.getVidaUtilMeses(),
                createDTO.getTempMin(),
                createDTO.getTempMax()
        );

        Producto savedProducto = productoRepository.save(producto);
        return convertToDTO(savedProducto);
    }

    // Obtener todos los productos
    @Transactional(readOnly = true)
    public List<ProductoDTO> obtenerTodosLosProductos() {
        return productoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener productos con paginación
    @Transactional(readOnly = true)
    public Page<ProductoDTO> obtenerProductosPaginados(Pageable pageable) {
        return productoRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    // Obtener producto por ID
    @Transactional(readOnly = true)
    public Optional<ProductoDTO> obtenerProductoPorId(UUID id) {
        return productoRepository.findById(id)
                .map(this::convertToDTO);
    }

    // Obtener producto por código SKU
    @Transactional(readOnly = true)
    public Optional<ProductoDTO> obtenerProductoPorSKU(String codigoSKU) {
        return productoRepository.findByCodigoSKU(codigoSKU)
                .map(this::convertToDTO);
    }

    // Buscar productos con filtros
    @Transactional(readOnly = true)
    public Page<ProductoDTO> buscarProductosConFiltros(String nombre, String codigoSKU,
            TipoProducto tipo, Boolean requiereCadenaFrio,
            Pageable pageable) {
        return productoRepository.findProductosWithFilters(nombre, codigoSKU, tipo, requiereCadenaFrio, pageable)
                .map(this::convertToDTO);
    }

    // Buscar por nombre
    @Transactional(readOnly = true)
    public List<ProductoDTO> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener productos por tipo
    @Transactional(readOnly = true)
    public List<ProductoDTO> obtenerProductosPorTipo(TipoProducto tipo) {
        return productoRepository.findByTipo(tipo).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener productos que requieren cadena de frío
    @Transactional(readOnly = true)
    public List<ProductoDTO> obtenerProductosCadenaFrio(Boolean requiereCadenaFrio) {
        return productoRepository.findByRequiereCadenaFrio(requiereCadenaFrio).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Actualizar producto
    public ProductoDTO actualizarProducto(UUID id, ProductoUpdateDTO updateDTO) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        // Verificar código SKU si se está actualizando
        if (updateDTO.getCodigoSKU() != null && !updateDTO.getCodigoSKU().equals(producto.getCodigoSKU())) {
            if (productoRepository.existsByCodigoSKU(updateDTO.getCodigoSKU())) {
                throw new RuntimeException("Ya existe un producto con el código SKU: " + updateDTO.getCodigoSKU());
            }
            producto.setCodigoSKU(updateDTO.getCodigoSKU());
        }

        // Actualizar campos si no son null
        if (updateDTO.getNombre() != null) {
            producto.setNombre(updateDTO.getNombre());
        }
        if (updateDTO.getTipo() != null) {
            producto.setTipo(updateDTO.getTipo());
        }
        if (updateDTO.getCondicionAlmacen() != null) {
            producto.setCondicionAlmacen(updateDTO.getCondicionAlmacen());
        }
        if (updateDTO.getRequiereCadenaFrio() != null) {
            producto.setRequiereCadenaFrio(updateDTO.getRequiereCadenaFrio());
        }
        if (updateDTO.getRegistroSanitario() != null) {
            producto.setRegistroSanitario(updateDTO.getRegistroSanitario());
        }
        if (updateDTO.getUnidadMedida() != null) {
            producto.setUnidadMedida(updateDTO.getUnidadMedida());
        }
        if (updateDTO.getVidaUtilMeses() != null) {
            producto.setVidaUtilMeses(updateDTO.getVidaUtilMeses());
        }
        if (updateDTO.getTempMin() != null) {
            producto.setTempMin(updateDTO.getTempMin());
        }
        if (updateDTO.getTempMax() != null) {
            producto.setTempMax(updateDTO.getTempMax());
        }

        Producto updatedProducto = productoRepository.save(producto);
        return convertToDTO(updatedProducto);
    }

    // Eliminar producto
    public void eliminarProducto(UUID id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }

    // Estadísticas
    @Transactional(readOnly = true)
    public List<Object[]> obtenerEstadisticasPorTipo() {
        return productoRepository.countProductosByTipo();
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> obtenerProductosProximosAVencer(Integer mesesLimite) {
        return productoRepository.findProductosProximosAVencer(mesesLimite).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Método auxiliar para convertir Entity a DTO
    private ProductoDTO convertToDTO(Producto producto) {
        return new ProductoDTO(
                producto.getId(),
                producto.getCodigoSKU(),
                producto.getNombre(),
                producto.getTipo(),
                producto.getCondicionAlmacen(),
                producto.getRequiereCadenaFrio(),
                producto.getRegistroSanitario(),
                producto.getUnidadMedida(),
                producto.getVidaUtilMeses(),
                producto.getTempMin(),
                producto.getTempMax(),
                producto.getFechaCreacion(),
                producto.getFechaActualizacion()
        );
    }

    // Obtener productos por cliente
    @Transactional(readOnly = true)
    public Page<ProductoDTO> obtenerProductosPorCliente(UUID clienteId, Pageable pageable) {
        return productoRepository.findByClientesId(clienteId, pageable)
                .map(this::convertToDTO);
    }

    // Buscar productos por cliente con filtros
    @Transactional(readOnly = true)
    public Page<ProductoDTO> buscarProductosPorClienteConFiltros(UUID clienteId, String nombre, 
            String codigoSKU, TipoProducto tipo, Boolean requiereCadenaFrio, Pageable pageable) {
        return productoRepository.findByClientesIdWithFilters(clienteId, nombre, codigoSKU, 
                tipo, requiereCadenaFrio, pageable)
                .map(this::convertToDTO);
    }
}
