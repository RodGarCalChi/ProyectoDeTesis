package org.example.backend.service;

import org.example.backend.dto.*;
import org.example.backend.entity.*;
import org.example.backend.enumeraciones.EstadoInventario;
import org.example.backend.enumeraciones.EstadoLote;
import org.example.backend.enumeraciones.EstadoRecepcion;
import org.example.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class GenerarInventarioCompletoService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private OperadorLogisticoRepository operadorLogisticoRepository;
    
    @Autowired
    private AlmacenRepository almacenRepository;
    
    @Autowired
    private ZonaRepository zonaRepository;
    
    @Autowired
    private UbicacionRepository ubicacionRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private LoteRepository loteRepository;
    
    @Autowired
    private PaletRepository paletRepository;
    
    @Autowired
    private InventarioClienteRepository inventarioClienteRepository;
    
    @Autowired
    private RecepcionMercaderiaRepository recepcionMercaderiaRepository;
    
    /**
     * Genera el inventario completo jerárquico para uno o más clientes
     * Jerarquía: Cliente -> OperadorLogistico -> Almacen -> Zona -> Ubicacion -> Producto -> Lote -> Palet -> InventarioCliente
     */
    public Map<String, Object> generarInventarioCompleto(GenerarInventarioCompletoDTO request) {
        System.out.println("=== INICIO GENERACIÓN DE INVENTARIO COMPLETO ===");
        
        Map<String, Object> resultado = new HashMap<>();
        List<String> errores = new ArrayList<>();
        
        try {
            // 1. Validar que todos los clientes existan
            List<Cliente> clientes = new ArrayList<>();
            for (UUID clienteId : request.getClienteIds()) {
                Cliente cliente = clienteRepository.findById(clienteId)
                        .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clienteId));
                clientes.add(cliente);
                System.out.println("✓ Cliente validado: " + cliente.getRazonSocial());
            }
            
            // 2. Crear o buscar OperadorLogistico
            OperadorLogistico operadorLogistico;
            Optional<OperadorLogistico> operadorExistente = operadorLogisticoRepository.findByRuc(request.getOperadorLogistico().getRuc());
            if (operadorExistente.isPresent()) {
                operadorLogistico = operadorExistente.get();
                System.out.println("✓ Operador logístico existente: " + operadorLogistico.getNombre());
            } else {
                operadorLogistico = new OperadorLogistico();
                operadorLogistico.setNombre(request.getOperadorLogistico().getNombre());
                operadorLogistico.setRuc(request.getOperadorLogistico().getRuc());
                operadorLogistico.setDireccion(request.getOperadorLogistico().getDireccion());
                operadorLogistico.setTelefono(request.getOperadorLogistico().getTelefono());
                operadorLogistico.setEmail(request.getOperadorLogistico().getEmail());
                operadorLogistico = operadorLogisticoRepository.save(operadorLogistico);
                System.out.println("✓ Operador logístico creado: " + operadorLogistico.getNombre());
            }
            
            // 3. Crear Almacenes para cada cliente
            Map<UUID, Almacen> almacenesPorCliente = new HashMap<>();
            for (Cliente cliente : clientes) {
                Almacen almacen = new Almacen();
                almacen.setNombre(request.getAlmacen().getNombre() + " - " + cliente.getRazonSocial());
                almacen.setDireccion(request.getAlmacen().getDireccion());
                almacen.setOperadorLogistico(operadorLogistico);
                almacen.setCliente(cliente);
                almacen.setTieneAreaControlados(request.getAlmacen().getTieneAreaControlados());
                almacen = almacenRepository.save(almacen);
                almacenesPorCliente.put(cliente.getId(), almacen);
                System.out.println("✓ Almacén creado para cliente: " + cliente.getRazonSocial());
            }
            
            // 4. Crear Zonas y Ubicaciones para cada almacén
            Map<UUID, List<Zona>> zonasPorAlmacen = new HashMap<>();
            Map<UUID, List<Ubicacion>> ubicacionesPorZona = new HashMap<>();
            
            for (Map.Entry<UUID, Almacen> entry : almacenesPorCliente.entrySet()) {
                Almacen almacen = entry.getValue();
                List<Zona> zonas = new ArrayList<>();
                
                for (GenerarInventarioCompletoDTO.ZonaDTO zonaDTO : request.getZonas()) {
                    Zona zona = new Zona();
                    zona.setNombre(zonaDTO.getNombre());
                    zona.setTipo(zonaDTO.getTipo().name()); // Convertir enum TipoZona a String
                    zona.setAlmacen(almacen);
                    zona = zonaRepository.save(zona);
                    zonas.add(zona);
                    System.out.println("  ✓ Zona creada: " + zona.getNombre());
                    
                    // Crear ubicaciones para esta zona
                    List<Ubicacion> ubicaciones = new ArrayList<>();
                    for (GenerarInventarioCompletoDTO.UbicacionDTO ubicacionDTO : zonaDTO.getUbicaciones()) {
                        Ubicacion ubicacion = new Ubicacion();
                        ubicacion.setCodigo(ubicacionDTO.getCodigo());
                        ubicacion.setCapacidad(ubicacionDTO.getCapacidadMaxima());
                        ubicacion.setTempObjetivoMin(ubicacionDTO.getTempObjetivoMin());
                        ubicacion.setTempObjetivoMax(ubicacionDTO.getTempObjetivoMax());
                        ubicacion.setDisponible(ubicacionDTO.getDisponible());
                        ubicacion.setZona(zona);
                        ubicacion = ubicacionRepository.save(ubicacion);
                        ubicaciones.add(ubicacion);
                        System.out.println("    ✓ Ubicación creada: " + ubicacion.getCodigo());
                    }
                    ubicacionesPorZona.put(zona.getId(), ubicaciones);
                }
                zonasPorAlmacen.put(almacen.getId(), zonas);
            }
            
            // 5. Crear Productos, Lotes, Palets e InventarioCliente
            List<ProductoDTO> productosCreados = new ArrayList<>();
            List<Lote> lotesCreados = new ArrayList<>();
            List<Palet> paletsCreados = new ArrayList<>();
            List<InventarioCliente> inventariosCreados = new ArrayList<>();
            
            // Obtener primera ubicación disponible para asignar palets
            Ubicacion primeraUbicacion = null;
            for (List<Ubicacion> ubicaciones : ubicacionesPorZona.values()) {
                if (!ubicaciones.isEmpty()) {
                    primeraUbicacion = ubicaciones.get(0);
                    break;
                }
            }
            
            for (GenerarInventarioCompletoDTO.ProductoInventarioDTO productoDTO : request.getProductosInventario()) {
                try {
                    // 5.1 Crear o buscar Producto
                    Producto producto;
                    Optional<Producto> productoExistente = productoRepository.findByCodigoSKU(productoDTO.getCodigoSKU());
                    if (productoExistente.isPresent()) {
                        producto = productoExistente.get();
                        System.out.println("  ✓ Producto existente: " + producto.getNombre());
                    } else {
                        ProductoCreateDTO productoCreateDTO = new ProductoCreateDTO(
                                productoDTO.getCodigoSKU(),
                                productoDTO.getNombre(),
                                productoDTO.getTipo(),
                                productoDTO.getCondicionAlmacen(),
                                productoDTO.getRequiereCadenaFrio(),
                                productoDTO.getRegistroSanitario(),
                                productoDTO.getUnidadMedida(),
                                productoDTO.getVidaUtilMeses(),
                                productoDTO.getTempMin(),
                                productoDTO.getTempMax()
                        );
                        ProductoDTO productoCreadoDTO = productoService.crearProducto(productoCreateDTO);
                        producto = productoRepository.findById(productoCreadoDTO.getId())
                                .orElseThrow(() -> new RuntimeException("Error al obtener producto creado"));
                        productosCreados.add(productoCreadoDTO);
                        System.out.println("  ✓ Producto creado: " + producto.getNombre());
                    }
                    
                    // 5.2 Crear Lote
                    Lote lote;
                    Optional<Lote> loteExistente = loteRepository.findByNumero(productoDTO.getNumeroLote());
                    if (loteExistente.isPresent()) {
                        lote = loteExistente.get();
                        System.out.println("    ✓ Lote existente: " + lote.getNumero());
                    } else {
                        lote = new Lote();
                        lote.setNumero(productoDTO.getNumeroLote());
                        lote.setProducto(producto);
                        lote.setFechaFabricacion(productoDTO.getFechaFabricacion());
                        lote.setFechaVencimiento(productoDTO.getFechaVencimiento());
                        lote.setCantidadInicial(productoDTO.getCantidadInicial());
                        lote.setCantidadDisponible(productoDTO.getCantidadInicial());
                        lote.setEstado(EstadoLote.DISPONIBLE);
                        lote.setProveedor(productoDTO.getProveedor());
                        lote.setObservaciones(productoDTO.getObservacionesLote());
                        lote = loteRepository.save(lote);
                        lotesCreados.add(lote);
                        System.out.println("    ✓ Lote creado: " + lote.getNumero());
                    }
                    
                    // 5.3 Crear Palet
                    Palet palet;
                    Optional<Palet> paletExistente = paletRepository.findByCodigo(productoDTO.getCodigoPalet());
                    if (paletExistente.isPresent()) {
                        palet = paletExistente.get();
                        System.out.println("      ✓ Palet existente: " + palet.getCodigo());
                    } else {
                        palet = new Palet();
                        palet.setCodigo(productoDTO.getCodigoPalet());
                        palet.setLote(lote);
                        if (primeraUbicacion != null) {
                            palet.setUbicacion(primeraUbicacion);
                        }
                        palet.setCapacidadMaxima(productoDTO.getCapacidadMaximaPalet());
                        palet.setCajasActuales(productoDTO.getCajasActuales());
                        palet.setPesoMaximoKg(productoDTO.getPesoMaximoKg());
                        palet.setPesoActualKg(productoDTO.getPesoActualKg());
                        palet.setDisponible(true);
                        palet.setObservaciones(productoDTO.getObservacionesPalet());
                        palet = paletRepository.save(palet);
                        paletsCreados.add(palet);
                        System.out.println("      ✓ Palet creado: " + palet.getCodigo());
                    }
                    
                    // 5.4 Crear InventarioCliente para cada cliente
                    // Nota: InventarioCliente requiere RecepcionMercaderia, así que creamos una recepción básica
                    for (Cliente cliente : clientes) {
                        try {
                            // Crear una recepción básica para este cliente y producto
                            String numeroOrden = "GEN-" + cliente.getId().toString().substring(0, 8) + "-" + System.currentTimeMillis();
                            String numeroGuia = "GUIA-" + cliente.getId().toString().substring(0, 8) + "-" + System.currentTimeMillis();
                            
                            // Verificar si ya existe una recepción con este número
                            RecepcionMercaderia recepcion;
                            Optional<RecepcionMercaderia> recepcionExistente = recepcionMercaderiaRepository.findByNumeroOrdenCompra(numeroOrden);
                            if (recepcionExistente.isPresent()) {
                                recepcion = recepcionExistente.get();
                            } else {
                                recepcion = new RecepcionMercaderia();
                                recepcion.setNumeroOrdenCompra(numeroOrden);
                                recepcion.setNumeroGuiaRemision(numeroGuia);
                                recepcion.setCliente(cliente);
                                recepcion.setFechaRecepcion(LocalDateTime.now());
                                recepcion.setResponsableRecepcion("Sistema - Generación Automática");
                                recepcion.setEstado(EstadoRecepcion.ALMACENADO); // Ya está almacenado directamente
                                recepcion.setVerificacionDocumentos(true);
                                recepcion.setVerificacionFisica(true);
                                recepcion.setVerificacionTemperatura(true);
                                recepcion.setAprobadoPorCalidad(true);
                                recepcion.setObservaciones("Recepción generada automáticamente para inventario inicial");
                                recepcion = recepcionMercaderiaRepository.save(recepcion);
                                System.out.println("        ✓ Recepción creada: " + numeroOrden);
                            }
                            
                            // Crear inventario
                            InventarioCliente inventario = new InventarioCliente();
                            inventario.setCliente(cliente);
                            inventario.setProducto(producto);
                            inventario.setLote(lote);
                            inventario.setRecepcion(recepcion);
                            inventario.setCantidadDisponible(productoDTO.getCantidadDisponible());
                            inventario.setCantidadReservada(0);
                            inventario.setCantidadDespachada(0);
                            inventario.setFechaIngreso(LocalDateTime.now());
                            inventario.setFechaVencimiento(productoDTO.getFechaVencimiento());
                            inventario.setEstado(EstadoInventario.ALMACENADO);
                            if (productoDTO.getTemperaturaAlmacenamiento() != null) {
                                inventario.setTemperaturaAlmacenamiento(BigDecimal.valueOf(productoDTO.getTemperaturaAlmacenamiento()));
                            }
                            inventario.setCodigoBarras(productoDTO.getCodigoBarras());
                            inventario.setObservaciones(productoDTO.getObservacionesInventario());
                            if (primeraUbicacion != null) {
                                inventario.setUbicacion(primeraUbicacion);
                            }
                            inventario = inventarioClienteRepository.save(inventario);
                            inventariosCreados.add(inventario);
                            System.out.println("        ✓ Inventario creado para cliente: " + cliente.getRazonSocial());
                        } catch (Exception e) {
                            String error = "Error creando inventario para cliente " + cliente.getRazonSocial() + ": " + e.getMessage();
                            errores.add(error);
                            System.err.println("        ✗ " + error);
                            e.printStackTrace();
                        }
                    }
                    
                } catch (Exception e) {
                    String error = "Error procesando producto " + productoDTO.getNombre() + ": " + e.getMessage();
                    errores.add(error);
                    System.err.println("  ✗ " + error);
                    e.printStackTrace();
                }
            }
            
            // Construir respuesta
            resultado.put("operadorLogistico", operadorLogistico);
            resultado.put("almacenes", new ArrayList<>(almacenesPorCliente.values()));
            resultado.put("zonas", zonasPorAlmacen);
            resultado.put("ubicaciones", ubicacionesPorZona);
            resultado.put("productosCreados", productosCreados);
            resultado.put("lotesCreados", lotesCreados);
            resultado.put("paletsCreados", paletsCreados);
            resultado.put("inventariosCreados", inventariosCreados);
            resultado.put("totalProductos", productosCreados.size());
            resultado.put("totalLotes", lotesCreados.size());
            resultado.put("totalPalets", paletsCreados.size());
            resultado.put("totalInventarios", inventariosCreados.size());
            resultado.put("errores", errores);
            resultado.put("exitoso", errores.isEmpty());
            
            if (!errores.isEmpty()) {
                resultado.put("mensaje", "Se generó el inventario con " + errores.size() + " errores");
            } else {
                resultado.put("mensaje", "Inventario completo generado exitosamente");
            }
            
            System.out.println("=== FIN GENERACIÓN ===");
            System.out.println("Productos: " + productosCreados.size());
            System.out.println("Lotes: " + lotesCreados.size());
            System.out.println("Palets: " + paletsCreados.size());
            System.out.println("Inventarios: " + inventariosCreados.size());
            System.out.println("Errores: " + errores.size());
            
        } catch (Exception e) {
            System.err.println("Error crítico: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al generar inventario completo: " + e.getMessage(), e);
        }
        
        return resultado;
    }
}

