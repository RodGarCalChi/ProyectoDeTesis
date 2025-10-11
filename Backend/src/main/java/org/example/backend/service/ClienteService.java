package org.example.backend.service;

import org.example.backend.dto.ClienteCreateDTO;
import org.example.backend.dto.ClienteDTO;
import org.example.backend.dto.ClienteUpdateDTO;
import org.example.backend.entity.Cliente;
import org.example.backend.repository.ClienteRepository;
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
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    // Crear cliente
    public ClienteDTO crearCliente(ClienteCreateDTO createDTO) {
        // Verificar que no exista el RUC/DNI
        if (clienteRepository.existsByRucDni(createDTO.getRucDni())) {
            throw new RuntimeException("Ya existe un cliente con el RUC/DNI: " + createDTO.getRucDni());
        }
        
        Cliente cliente = new Cliente(
                createDTO.getRazonSocial(),
                createDTO.getRucDni(),
                createDTO.getDireccionEntrega(),
                createDTO.getDistrito(),
                createDTO.getTelefono(),
                createDTO.getEmail(),
                createDTO.getTipoCliente(),
                createDTO.getFormaPago()
        );
        
        Cliente savedCliente = clienteRepository.save(cliente);
        return convertToDTO(savedCliente);
    }
    
    // Obtener todos los clientes
    @Transactional(readOnly = true)
    public List<ClienteDTO> obtenerTodosLosClientes() {
        return clienteRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Obtener clientes activos
    @Transactional(readOnly = true)
    public List<ClienteDTO> obtenerClientesActivos() {
        return clienteRepository.findByActivoTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Obtener clientes con paginación
    @Transactional(readOnly = true)
    public Page<ClienteDTO> obtenerClientesPaginados(Pageable pageable) {
        return clienteRepository.findAll(pageable)
                .map(this::convertToDTO);
    }
    
    // Obtener cliente por ID
    @Transactional(readOnly = true)
    public Optional<ClienteDTO> obtenerClientePorId(UUID id) {
        return clienteRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    // Obtener cliente por RUC/DNI
    @Transactional(readOnly = true)
    public Optional<ClienteDTO> obtenerClientePorRucDni(String rucDni) {
        return clienteRepository.findByRucDni(rucDni)
                .map(this::convertToDTO);
    }
    
    // Buscar clientes con filtros
    @Transactional(readOnly = true)
    public Page<ClienteDTO> buscarClientesConFiltros(String razonSocial, String rucDni, 
                                                    String distrito, String tipoCliente, 
                                                    Boolean activo, Pageable pageable) {
        return clienteRepository.findClientesWithFilters(razonSocial, rucDni, distrito, tipoCliente, activo, pageable)
                .map(this::convertToDTO);
    }
    
    // Buscar por razón social
    @Transactional(readOnly = true)
    public List<ClienteDTO> buscarPorRazonSocial(String razonSocial) {
        return clienteRepository.findByRazonSocialContainingIgnoreCase(razonSocial).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Obtener clientes por tipo
    @Transactional(readOnly = true)
    public List<ClienteDTO> obtenerClientesPorTipo(String tipoCliente) {
        return clienteRepository.findByTipoCliente(tipoCliente).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Obtener clientes por distrito
    @Transactional(readOnly = true)
    public List<ClienteDTO> obtenerClientesPorDistrito(String distrito) {
        return clienteRepository.findByDistrito(distrito).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Actualizar cliente
    public ClienteDTO actualizarCliente(UUID id, ClienteUpdateDTO updateDTO) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        
        // Verificar RUC/DNI si se está actualizando
        if (updateDTO.getRucDni() != null && !updateDTO.getRucDni().equals(cliente.getRucDni())) {
            if (clienteRepository.existsByRucDni(updateDTO.getRucDni())) {
                throw new RuntimeException("Ya existe un cliente con el RUC/DNI: " + updateDTO.getRucDni());
            }
            cliente.setRucDni(updateDTO.getRucDni());
        }
        
        // Actualizar campos si no son null
        if (updateDTO.getRazonSocial() != null) {
            cliente.setRazonSocial(updateDTO.getRazonSocial());
        }
        if (updateDTO.getDireccionEntrega() != null) {
            cliente.setDireccionEntrega(updateDTO.getDireccionEntrega());
        }
        if (updateDTO.getDistrito() != null) {
            cliente.setDistrito(updateDTO.getDistrito());
        }
        if (updateDTO.getTelefono() != null) {
            cliente.setTelefono(updateDTO.getTelefono());
        }
        if (updateDTO.getEmail() != null) {
            cliente.setEmail(updateDTO.getEmail());
        }
        if (updateDTO.getTipoCliente() != null) {
            cliente.setTipoCliente(updateDTO.getTipoCliente());
        }
        if (updateDTO.getFormaPago() != null) {
            cliente.setFormaPago(updateDTO.getFormaPago());
        }
        if (updateDTO.getActivo() != null) {
            cliente.setActivo(updateDTO.getActivo());
        }
        
        Cliente updatedCliente = clienteRepository.save(cliente);
        return convertToDTO(updatedCliente);
    }
    
    // Activar/Desactivar cliente
    public ClienteDTO cambiarEstadoCliente(UUID id, Boolean activo) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        
        cliente.setActivo(activo);
        Cliente updatedCliente = clienteRepository.save(cliente);
        return convertToDTO(updatedCliente);
    }
    
    // Eliminar cliente (soft delete - marcar como inactivo)
    public void eliminarCliente(UUID id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }
    
    // Eliminar cliente permanentemente
    public void eliminarClientePermanente(UUID id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado con ID: " + id);
        }
        clienteRepository.deleteById(id);
    }
    
    // Estadísticas
    @Transactional(readOnly = true)
    public List<Object[]> obtenerEstadisticasPorTipo() {
        return clienteRepository.countClientesByTipo();
    }
    
    @Transactional(readOnly = true)
    public List<Object[]> obtenerEstadisticasPorDistrito() {
        return clienteRepository.countClientesByDistrito();
    }
    
    @Transactional(readOnly = true)
    public List<ClienteDTO> obtenerClientesConEmail() {
        return clienteRepository.findClientesWithEmail().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Método auxiliar para convertir Entity a DTO
    private ClienteDTO convertToDTO(Cliente cliente) {
        return new ClienteDTO(
                cliente.getId(),
                cliente.getRazonSocial(),
                cliente.getRucDni(),
                cliente.getDireccionEntrega(),
                cliente.getDistrito(),
                cliente.getTelefono(),
                cliente.getEmail(),
                cliente.getTipoCliente(),
                cliente.getFormaPago(),
                cliente.getActivo(),
                cliente.getFechaCreacion(),
                cliente.getFechaActualizacion()
        );
    }
}