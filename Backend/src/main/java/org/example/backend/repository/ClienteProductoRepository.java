package org.example.backend.repository;

import org.example.backend.entity.ClienteProducto;
import org.example.backend.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteProductoRepository extends JpaRepository<ClienteProducto, UUID> {
    
    // Buscar productos de un cliente
    List<ClienteProducto> findByClienteId(UUID clienteId);
    
    // Buscar productos activos de un cliente
    List<ClienteProducto> findByClienteIdAndActivoTrue(UUID clienteId);
    
    // Buscar clientes de un producto
    List<ClienteProducto> findByProductoId(UUID productoId);
    
    // Buscar clientes activos de un producto
    List<ClienteProducto> findByProductoIdAndActivoTrue(UUID productoId);
    
    // Verificar si existe relación
    boolean existsByClienteIdAndProductoId(UUID clienteId, UUID productoId);
    
    // Buscar relación específica
    Optional<ClienteProducto> findByClienteIdAndProductoId(UUID clienteId, UUID productoId);
    
    // Obtener solo los productos (entidades) de un cliente
    @Query("SELECT cp.producto FROM ClienteProducto cp WHERE cp.cliente.id = :clienteId AND cp.activo = true")
    List<Producto> findProductosByClienteId(@Param("clienteId") UUID clienteId);
    
    // Contar productos por cliente
    @Query("SELECT COUNT(cp) FROM ClienteProducto cp WHERE cp.cliente.id = :clienteId AND cp.activo = true")
    Long countProductosByClienteId(@Param("clienteId") UUID clienteId);
    
    // Contar clientes por producto
    @Query("SELECT COUNT(cp) FROM ClienteProducto cp WHERE cp.producto.id = :productoId AND cp.activo = true")
    Long countClientesByProductoId(@Param("productoId") UUID productoId);
    
    // Listar todas las relaciones activas
    List<ClienteProducto> findByActivoTrue();
}
