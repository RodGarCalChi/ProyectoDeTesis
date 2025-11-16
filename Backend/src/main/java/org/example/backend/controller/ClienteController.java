package org.example.backend.controller;

import org.example.backend.entity.Cliente;
import org.example.backend.entity.Producto;
import org.example.backend.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:9002"})
public class ClienteController {
    
    @Autowired
    private ClienteService clienteService;
    
    // ==================== CRUD BÁSICO DE CLIENTES ====================
    
    /**
     * Obtener todos los clientes
     * GET /api/clientes
     */
    @GetMapping
    public ResponseEntity<List<Cliente>> obtenerTodosLosClientes() {
        List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
        return ResponseEntity.ok(clientes);
    }
    
    /**
     * Obtener clientes activos
     * GET /api/clientes/activos
     */
    @GetMapping("/activos")
    public ResponseEntity<List<Cliente>> obtenerClientesActivos() {
        List<Cliente> clientes = clienteService.obtenerClientesActivos();
        return ResponseEntity.ok(clientes);
    }
    
    /**
     * Obtener cliente por ID
     * GET /api/clientes/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerClientePorId(@PathVariable UUID id) {
        Cliente cliente = clienteService.obtenerClientePorId(id);
        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * Buscar clientes por razón social
     * GET /api/clientes/buscar?razonSocial=nombre
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Cliente>> buscarClientes(@RequestParam String razonSocial) {
        List<Cliente> clientes = clienteService.buscarPorRazonSocial(razonSocial);
        return ResponseEntity.ok(clientes);
    }
    
    /**
     * Crear nuevo cliente
     * POST /api/clientes
     */
    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.crearCliente(cliente);
        return ResponseEntity.status(201).body(nuevoCliente);
    }
    
    /**
     * Actualizar cliente
     * PUT /api/clientes/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizarCliente(
            @PathVariable UUID id,
            @RequestBody Cliente cliente) {
        Cliente clienteActualizado = clienteService.actualizarCliente(id, cliente);
        if (clienteActualizado != null) {
            return ResponseEntity.ok(clienteActualizado);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * Eliminar cliente
     * DELETE /api/clientes/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable UUID id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }
    
    // ==================== GESTIÓN DE PRODUCTOS DEL CLIENTE ====================
    
    /**
     * Asignar múltiples productos a un cliente
     * POST /api/clientes/{clienteId}/productos
     */
    @PostMapping("/{clienteId}/productos")
    public ResponseEntity<Map<String, Object>> asignarProductos(
            @PathVariable UUID clienteId,
            @RequestBody List<UUID> productosIds) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Cliente cliente = clienteService.asignarProductos(clienteId, productosIds);
            
            response.put("success", true);
            response.put("message", "Productos asignados exitosamente");
            response.put("cliente", Map.of(
                "id", cliente.getId(),
                "nombre", cliente.getNombre(),
                "ruc", cliente.getRuc()
            ));
            response.put("total_productos", cliente.getProductos().size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Asignar un solo producto a un cliente
     * POST /api/clientes/{clienteId}/productos/{productoId}
     */
    @PostMapping("/{clienteId}/productos/{productoId}")
    public ResponseEntity<Map<String, Object>> asignarProducto(
            @PathVariable UUID clienteId,
            @PathVariable UUID productoId) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Cliente cliente = clienteService.asignarProducto(clienteId, productoId);
            
            response.put("success", true);
            response.put("message", "Producto asignado exitosamente");
            response.put("total_productos", cliente.getProductos().size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Obtener todos los productos de un cliente
     * GET /api/clientes/{clienteId}/productos
     */
    @GetMapping("/{clienteId}/productos")
    public ResponseEntity<Map<String, Object>> obtenerProductos(@PathVariable UUID clienteId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Set<Producto> productos = clienteService.obtenerProductosDeCliente(clienteId);
            
            List<Map<String, Object>> productosDTO = new ArrayList<>();
            for (Producto producto : productos) {
                productosDTO.add(Map.of(
                    "id", producto.getId(),
                    "codigoSKU", producto.getCodigoSKU(),
                    "nombre", producto.getNombre(),
                    "tipo", producto.getTipo().name(),
                    "requiereCadenaFrio", producto.getRequiereCadenaFrio()
                ));
            }
            
            response.put("success", true);
            response.put("total", productos.size());
            response.put("productos", productosDTO);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Remover un producto de un cliente
     * DELETE /api/clientes/{clienteId}/productos/{productoId}
     */
    @DeleteMapping("/{clienteId}/productos/{productoId}")
    public ResponseEntity<Map<String, Object>> removerProducto(
            @PathVariable UUID clienteId,
            @PathVariable UUID productoId) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            clienteService.removerProducto(clienteId, productoId);
            
            response.put("success", true);
            response.put("message", "Producto removido exitosamente");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Remover todos los productos de un cliente
     * DELETE /api/clientes/{clienteId}/productos
     */
    @DeleteMapping("/{clienteId}/productos")
    public ResponseEntity<Map<String, Object>> removerTodosLosProductos(@PathVariable UUID clienteId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            clienteService.removerTodosLosProductos(clienteId);
            
            response.put("success", true);
            response.put("message", "Todos los productos han sido removidos del cliente");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Obtener todos los clientes que tienen un producto específico
     * GET /api/clientes/por-producto/{productoId}
     */
    @GetMapping("/por-producto/{productoId}")
    public ResponseEntity<Map<String, Object>> obtenerClientesPorProducto(@PathVariable UUID productoId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Set<Cliente> clientes = clienteService.obtenerClientesDeProducto(productoId);
            
            List<Map<String, Object>> clientesDTO = new ArrayList<>();
            for (Cliente cliente : clientes) {
                clientesDTO.add(Map.of(
                    "id", cliente.getId(),
                    "nombre", cliente.getNombre(),
                    "ruc", cliente.getRuc(),
                    "email", cliente.getEmail() != null ? cliente.getEmail() : ""
                ));
            }
            
            response.put("success", true);
            response.put("total", clientes.size());
            response.put("clientes", clientesDTO);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Verificar si un cliente tiene un producto específico
     * GET /api/clientes/{clienteId}/tiene-producto/{productoId}
     */
    @GetMapping("/{clienteId}/tiene-producto/{productoId}")
    public ResponseEntity<Map<String, Object>> verificarProducto(
            @PathVariable UUID clienteId,
            @PathVariable UUID productoId) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean tieneProducto = clienteService.clienteTieneProducto(clienteId, productoId);
            
            response.put("success", true);
            response.put("tieneProducto", tieneProducto);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // ==================== OPERACIONES AVANZADAS ====================
    
    /**
     * Crear cliente con productos (existentes y/o nuevos)
     * POST /api/clientes/crear-con-productos
     */
    @PostMapping("/crear-con-productos")
    public ResponseEntity<Map<String, Object>> crearClienteConProductos(
            @RequestBody Map<String, Object> request) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Extraer datos del request
            @SuppressWarnings("unchecked")
            Map<String, Object> clienteData = (Map<String, Object>) request.get("cliente");
            
            @SuppressWarnings("unchecked")
            List<String> productosExistentesIds = (List<String>) request.get("productosExistentesIds");
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> productosNuevosData = (List<Map<String, Object>>) request.get("productosNuevos");
            
            String observaciones = (String) request.get("observaciones");
            
            // Crear cliente
            Cliente cliente = new Cliente();
            cliente.setRazonSocial((String) clienteData.get("razonSocial"));
            cliente.setRucDni((String) clienteData.get("rucDni"));
            cliente.setDireccionEntrega((String) clienteData.get("direccionEntrega"));
            cliente.setDistrito((String) clienteData.get("distrito"));
            cliente.setTelefono((String) clienteData.get("telefono"));
            cliente.setEmail((String) clienteData.get("email"));
            cliente.setTipoCliente((String) clienteData.get("tipoCliente"));
            cliente.setFormaPago((String) clienteData.get("formaPago"));
            cliente.setActivo(true);
            
            // Convertir IDs de productos existentes
            List<UUID> productosExistentesUUIDs = new ArrayList<>();
            if (productosExistentesIds != null) {
                productosExistentesIds.forEach(id -> productosExistentesUUIDs.add(UUID.fromString(id)));
            }
            
            // Crear productos nuevos
            List<Producto> productosNuevos = new ArrayList<>();
            if (productosNuevosData != null) {
                productosNuevosData.forEach(prodData -> {
                    Producto producto = new Producto();
                    producto.setCodigoSKU((String) prodData.get("codigoSKU"));
                    producto.setNombre((String) prodData.get("nombre"));
                    producto.setTipo(org.example.backend.enumeraciones.TipoProducto.valueOf((String) prodData.get("tipo")));
                    producto.setCondicionAlmacen(org.example.backend.enumeraciones.CondicionAlmacen.valueOf((String) prodData.get("condicionAlmacen")));
                    producto.setRequiereCadenaFrio((Boolean) prodData.get("requiereCadenaFrio"));
                    producto.setRegistroSanitario((String) prodData.get("registroSanitario"));
                    producto.setUnidadMedida((String) prodData.get("unidadMedida"));
                    producto.setVidaUtilMeses((Integer) prodData.get("vidaUtilMeses"));
                    
                    Object tempMin = prodData.get("tempMin");
                    if (tempMin instanceof Double) {
                        producto.setTempMin(((Double) tempMin).floatValue());
                    } else if (tempMin instanceof Integer) {
                        producto.setTempMin(((Integer) tempMin).floatValue());
                    }
                    
                    Object tempMax = prodData.get("tempMax");
                    if (tempMax instanceof Double) {
                        producto.setTempMax(((Double) tempMax).floatValue());
                    } else if (tempMax instanceof Integer) {
                        producto.setTempMax(((Integer) tempMax).floatValue());
                    }
                    
                    productosNuevos.add(producto);
                });
            }
            
            // Crear cliente con productos
            Cliente clienteCreado = clienteService.crearClienteConProductos(
                cliente, productosExistentesUUIDs, productosNuevos
            );
            
            response.put("success", true);
            response.put("message", "Cliente creado exitosamente con sus productos");
            response.put("data", Map.of(
                "cliente", Map.of(
                    "id", clienteCreado.getId(),
                    "razonSocial", clienteCreado.getRazonSocial(),
                    "rucDni", clienteCreado.getRucDni()
                ),
                "totalAsignaciones", clienteCreado.getProductos().size(),
                "observaciones", observaciones != null ? observaciones : ""
            ));
            
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Asignar múltiples productos a un cliente (masivo)
     * POST /api/clientes/asignar-productos-masivo
     */
    @PostMapping("/asignar-productos-masivo")
    public ResponseEntity<Map<String, Object>> asignarProductosMasivo(
            @RequestBody Map<String, Object> request) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String clienteIdStr = (String) request.get("clienteId");
            UUID clienteId = UUID.fromString(clienteIdStr);
            
            @SuppressWarnings("unchecked")
            List<String> productosIdsStr = (List<String>) request.get("productosIds");
            
            List<UUID> productosIds = new ArrayList<>();
            productosIdsStr.forEach(id -> productosIds.add(UUID.fromString(id)));
            
            Cliente cliente = clienteService.asignarProductosMasivo(clienteId, productosIds);
            
            response.put("success", true);
            response.put("message", "Productos asignados masivamente");
            response.put("total_productos", cliente.getProductos().size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Listar todos los clientes con sus productos
     * GET /api/clientes/con-productos
     */
    @GetMapping("/con-productos")
    public ResponseEntity<Map<String, Object>> listarClientesConProductos() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Cliente> clientes = clienteService.obtenerClientesConProductos();
            
            List<Map<String, Object>> clientesDTO = new ArrayList<>();
            for (Cliente cliente : clientes) {
                List<Map<String, Object>> productosDTO = new ArrayList<>();
                for (Producto producto : cliente.getProductos()) {
                    productosDTO.add(Map.of(
                        "id", producto.getId(),
                        "codigoSKU", producto.getCodigoSKU(),
                        "nombre", producto.getNombre(),
                        "tipo", producto.getTipo().name()
                    ));
                }
                
                clientesDTO.add(Map.of(
                    "id", cliente.getId(),
                    "razonSocial", cliente.getRazonSocial(),
                    "rucDni", cliente.getRucDni(),
                    "email", cliente.getEmail() != null ? cliente.getEmail() : "",
                    "activo", cliente.getActivo(),
                    "productos", productosDTO,
                    "totalProductos", cliente.getProductos().size()
                ));
            }
            
            response.put("success", true);
            response.put("total", clientes.size());
            response.put("clientes", clientesDTO);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Generar datos de prueba si no hay clientes
     * POST /api/clientes/generar-datos-prueba
     */
    @PostMapping("/generar-datos-prueba")
    public ResponseEntity<Map<String, Object>> generarDatosPrueba() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Cliente> clientesExistentes = clienteService.obtenerTodosLosClientes();
            
            if (!clientesExistentes.isEmpty()) {
                response.put("success", false);
                response.put("message", "Ya existen clientes en la base de datos");
                response.put("total_existentes", clientesExistentes.size());
                return ResponseEntity.badRequest().body(response);
            }
            
            // Generar 3 clientes de prueba con productos
            List<Cliente> clientesCreados = new ArrayList<>();
            
            // Cliente 1: Farmacia Central
            Cliente cliente1 = new Cliente();
            cliente1.setRazonSocial("Farmacia Central SAC");
            cliente1.setRucDni("20123456789");
            cliente1.setDireccionEntrega("Av. Arequipa 1234, Lima");
            cliente1.setDistrito("Lima");
            cliente1.setTelefono("01-4567890");
            cliente1.setEmail("ventas@farmaciacentral.com");
            cliente1.setTipoCliente("MAYORISTA");
            cliente1.setFormaPago("CREDITO_30_DIAS");
            cliente1.setActivo(true);
            
            Producto prod1 = new Producto();
            prod1.setCodigoSKU("MED-PAR-500");
            prod1.setNombre("Paracetamol 500mg");
            prod1.setTipo(org.example.backend.enumeraciones.TipoProducto.Medicamento);
            prod1.setCondicionAlmacen(org.example.backend.enumeraciones.CondicionAlmacen.Ambiente_15_25);
            prod1.setRequiereCadenaFrio(false);
            prod1.setRegistroSanitario("EE-12345-2024");
            prod1.setUnidadMedida("TABLETA");
            prod1.setVidaUtilMeses(36);
            prod1.setTempMin(15.0f);
            prod1.setTempMax(30.0f);
            
            Cliente c1 = clienteService.crearClienteConProductos(cliente1, new ArrayList<>(), List.of(prod1));
            clientesCreados.add(c1);
            
            // Cliente 2: Boticas del Pueblo
            Cliente cliente2 = new Cliente();
            cliente2.setRazonSocial("Boticas del Pueblo EIRL");
            cliente2.setRucDni("20987654321");
            cliente2.setDireccionEntrega("Jr. Cusco 567, Lima");
            cliente2.setDistrito("Cercado de Lima");
            cliente2.setTelefono("01-3456789");
            cliente2.setEmail("compras@boticasdelpueblo.com");
            cliente2.setTipoCliente("MINORISTA");
            cliente2.setFormaPago("CONTADO");
            cliente2.setActivo(true);
            
            Producto prod2 = new Producto();
            prod2.setCodigoSKU("MED-INS-100");
            prod2.setNombre("Insulina Humana 100UI");
            prod2.setTipo(org.example.backend.enumeraciones.TipoProducto.Biologico);
            prod2.setCondicionAlmacen(org.example.backend.enumeraciones.CondicionAlmacen.Refrigerado_2_8);
            prod2.setRequiereCadenaFrio(true);
            prod2.setRegistroSanitario("EE-67890-2024");
            prod2.setUnidadMedida("AMPOLLA");
            prod2.setVidaUtilMeses(24);
            prod2.setTempMin(2.0f);
            prod2.setTempMax(8.0f);
            
            Cliente c2 = clienteService.crearClienteConProductos(cliente2, new ArrayList<>(), List.of(prod2));
            clientesCreados.add(c2);
            
            // Cliente 3: Clínica San Juan
            Cliente cliente3 = new Cliente();
            cliente3.setRazonSocial("Clínica San Juan SA");
            cliente3.setRucDni("20456789123");
            cliente3.setDireccionEntrega("Av. Brasil 890, Lima");
            cliente3.setDistrito("Breña");
            cliente3.setTelefono("01-2345678");
            cliente3.setEmail("logistica@clinicasanjuan.com");
            cliente3.setTipoCliente("INSTITUCIONAL");
            cliente3.setFormaPago("CREDITO_60_DIAS");
            cliente3.setActivo(true);
            
            Producto prod3 = new Producto();
            prod3.setCodigoSKU("INS-ALC-500");
            prod3.setNombre("Alcohol en Gel 500ml");
            prod3.setTipo(org.example.backend.enumeraciones.TipoProducto.Cosmetico);
            prod3.setCondicionAlmacen(org.example.backend.enumeraciones.CondicionAlmacen.Ambiente_15_25);
            prod3.setRequiereCadenaFrio(false);
            prod3.setRegistroSanitario("NS-11111-2024");
            prod3.setUnidadMedida("FRASCO");
            prod3.setVidaUtilMeses(24);
            prod3.setTempMin(10.0f);
            prod3.setTempMax(35.0f);
            
            Cliente c3 = clienteService.crearClienteConProductos(cliente3, new ArrayList<>(), List.of(prod3));
            clientesCreados.add(c3);
            
            response.put("success", true);
            response.put("message", "Datos de prueba generados exitosamente");
            response.put("total_creados", clientesCreados.size());
            response.put("clientes", clientesCreados.stream().map(c -> Map.of(
                "id", c.getId(),
                "razonSocial", c.getRazonSocial(),
                "rucDni", c.getRucDni(),
                "totalProductos", c.getProductos().size()
            )).toList());
            
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(response);
        }
    }
}