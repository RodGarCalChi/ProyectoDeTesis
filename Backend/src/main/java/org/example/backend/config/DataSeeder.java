package org.example.backend.config;

import org.example.backend.entity.*;
import org.example.backend.enumeraciones.*;
import org.example.backend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final OperadorLogisticoRepository operadorLogisticoRepository;
    private final AlmacenRepository almacenRepository;
    private final ZonaRepository zonaRepository;
    private final UbicacionRepository ubicacionRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final LoteRepository loteRepository;
    private final RecepcionMercaderiaRepository recepcionMercaderiaRepository;
    private final InventarioClienteRepository inventarioClienteRepository;

    public DataSeeder(UsuarioRepository usuarioRepository,
                      OperadorLogisticoRepository operadorLogisticoRepository,
                      AlmacenRepository almacenRepository,
                      ZonaRepository zonaRepository,
                      UbicacionRepository ubicacionRepository,
                      ClienteRepository clienteRepository,
                      ProductoRepository productoRepository,
                      LoteRepository loteRepository,
                      RecepcionMercaderiaRepository recepcionMercaderiaRepository,
                      InventarioClienteRepository inventarioClienteRepository) {
        this.usuarioRepository = usuarioRepository;
        this.operadorLogisticoRepository = operadorLogisticoRepository;
        this.almacenRepository = almacenRepository;
        this.zonaRepository = zonaRepository;
        this.ubicacionRepository = ubicacionRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.loteRepository = loteRepository;
        this.recepcionMercaderiaRepository = recepcionMercaderiaRepository;
        this.inventarioClienteRepository = inventarioClienteRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() > 0) {
            System.out.println("La base de datos ya contiene datos. Saltando DataSeeder.");
            return;
        }

        System.out.println("Iniciando DataSeeder...");

        // 1. Crear Usuarios del Sistema
        Usuario admin = new Usuario("Admin", "Sistema", "00000001", "admin@sistema.com", "admin123", Rol.AreaAdministrativa);
        Usuario operador = new Usuario("Juan", "Perez", "12345678", "juan.perez@logistica.com", "operador123", Rol.Operaciones);
        Usuario recepcion = new Usuario("Ana", "Torres", "87654321", "ana.torres@logistica.com", "recepcion123", Rol.Recepcion);
        Usuario calidad = new Usuario("Carlos", "Ruiz", "11223344", "carlos.ruiz@logistica.com", "calidad123", Rol.Calidad);
        
        usuarioRepository.saveAll(Arrays.asList(admin, operador, recepcion, calidad));
        System.out.println("Usuarios de sistema creados.");

        // 2. Crear Operador Logístico
        OperadorLogistico opLogistico = new OperadorLogistico("Logística Segura SAC", "20123456789");
        opLogistico.setDireccion("Av. Industrial 123, Lima");
        opLogistico.setTelefono("01-555-1234");
        opLogistico.setEmail("contacto@logisticasegura.com");
        operadorLogisticoRepository.save(opLogistico);

        // 3. Crear Almacén
        Almacen almacen = new Almacen("Almacén Central", "Av. Industrial 123, Nave A", true);
        almacen.setOperadorLogistico(opLogistico);
        // Nota: Almacen requiere Cliente, asignaremos uno por defecto o null si la entidad lo permite (revisar entidad)
        // Asumiremos que se asignará el primer cliente creado.

        // 4. Crear Clientes (Variados)
        List<Cliente> clientes = Arrays.asList(
            new Cliente("FarmaSalud SAC", "20555555551", "Av. Principal 456, Miraflores", "Miraflores", "999888777", "contacto@farmasalud.com", "Farmacia", "Crédito 30 días"),
            new Cliente("Hospital Central", "20444444441", "Av. Brasil 100, Jesus Maria", "Jesus Maria", "999111222", "logistica@hospital.gob.pe", "Hospital", "Contado"),
            new Cliente("Laboratorios Biolife", "20666666661", "Av. Javier Prado 2000, San Borja", "San Borja", "999333444", "ventas@biolife.com", "Laboratorio", "Crédito 60 días"),
            new Cliente("Clínica San Felipe", "20777777771", "Av. Gregorio Escobedo 650, Jesus Maria", "Jesus Maria", "999555666", "compras@clinicasanfelipe.com", "Clínica", "Crédito 45 días"),
            new Cliente("Boticas Perú", "20888888881", "Jr. de la Unión 100, Lima", "Lima", "999777888", "contacto@boticasperu.com", "Cadena Boticas", "Contado")
        );
        clienteRepository.saveAll(clientes);
        System.out.println("Clientes creados.");

        // Asignar cliente al almacén
        almacen.setCliente(clientes.get(0)); 
        almacenRepository.save(almacen);

        // Crear usuarios para clientes
        List<Usuario> usuariosClientes = Arrays.asList(
            new Usuario("User", "FarmaSalud", "C0000001", "usuario@farmasalud.com", "cliente123", Rol.Cliente),
            new Usuario("User", "Hospital", "C0000002", "usuario@hospital.gob.pe", "cliente123", Rol.Cliente)
        );
        usuarioRepository.saveAll(usuariosClientes);

        // 5. Crear Zonas
        Zona zonaRecepcion = new Zona("Zona Recepción", "RECEPCION", almacen);
        Zona zonaAlmacenamiento = new Zona("Zona Almacenamiento A", "ALMACENAMIENTO", almacen);
        Zona zonaFrio = new Zona("Cámara Fría 1", "REFRIGERADO", almacen);
        Zona zonaDespacho = new Zona("Zona Despacho", "DESPACHO", almacen);
        Zona zonaCuarentena = new Zona("Zona Cuarentena", "CUARENTENA", almacen);
        
        zonaRepository.saveAll(Arrays.asList(zonaRecepcion, zonaAlmacenamiento, zonaFrio, zonaDespacho, zonaCuarentena));

        // 6. Crear Ubicaciones
        List<Ubicacion> ubicaciones = Arrays.asList(
            new Ubicacion("A-01-01", 100, 15.0f, 25.0f, true, zonaAlmacenamiento),
            new Ubicacion("A-01-02", 100, 15.0f, 25.0f, true, zonaAlmacenamiento),
            new Ubicacion("A-01-03", 100, 15.0f, 25.0f, true, zonaAlmacenamiento),
            new Ubicacion("F-01-01", 50, 2.0f, 8.0f, true, zonaFrio),
            new Ubicacion("F-01-02", 50, 2.0f, 8.0f, true, zonaFrio),
            new Ubicacion("C-01-01", 50, 15.0f, 25.0f, true, zonaCuarentena)
        );
        ubicacionRepository.saveAll(ubicaciones);

        // 7. Crear Productos (Variados)
        List<Producto> productos = Arrays.asList(
            new Producto("MED-001", "Paracetamol 500mg", TipoProducto.Medicamento, CondicionAlmacen.Ambiente_15_25, false, "RS-1001", "Caja x 100", 24, 15.0f, 30.0f),
            new Producto("MED-002", "Ibuprofeno 400mg", TipoProducto.Medicamento, CondicionAlmacen.Ambiente_15_25, false, "RS-1002", "Caja x 50", 24, 15.0f, 30.0f),
            new Producto("MED-003", "Amoxicilina 500mg", TipoProducto.Medicamento, CondicionAlmacen.Ambiente_15_25, false, "RS-1003", "Caja x 100", 24, 15.0f, 30.0f),
            new Producto("VAC-001", "Vacuna Influenza", TipoProducto.Biologico, CondicionAlmacen.Refrigerado_2_8, true, "RS-2001", "Vial", 12, 2.0f, 8.0f),
            new Producto("VAC-002", "Vacuna COVID-19", TipoProducto.Biologico, CondicionAlmacen.Refrigerado_2_8, true, "RS-2002", "Vial Multi", 6, 2.0f, 8.0f),
            new Producto("INS-001", "Jeringas 5ml", TipoProducto.Dispositivo, CondicionAlmacen.Ambiente_15_25, false, "RS-3001", "Caja x 50", 60, 10.0f, 35.0f),
            new Producto("INS-002", "Guantes Nitrilo M", TipoProducto.Dispositivo, CondicionAlmacen.Ambiente_15_25, false, "RS-3002", "Caja x 100", 60, 10.0f, 35.0f),
            new Producto("CTR-001", "Morfina 10mg", TipoProducto.Controlado, CondicionAlmacen.Ambiente_15_25, false, "RS-4001", "Ampolla", 24, 15.0f, 25.0f)
        );
        
        // Asignar productos a clientes (distribución aleatoria/manual)
        productos.get(0).getClientes().addAll(Arrays.asList(clientes.get(0), clientes.get(1), clientes.get(4))); // Paracetamol
        productos.get(1).getClientes().addAll(Arrays.asList(clientes.get(0), clientes.get(4))); // Ibuprofeno
        productos.get(3).getClientes().addAll(Arrays.asList(clientes.get(1), clientes.get(3))); // Vacuna Influenza
        productos.get(7).getClientes().addAll(Arrays.asList(clientes.get(1), clientes.get(3))); // Morfina (Hospital/Clínica)
        
        productoRepository.saveAll(productos);
        System.out.println("Productos creados.");

        // 8. Crear Lotes e Inventario Inicial
        // Creamos una recepción "Inicial" para justificar el stock
        RecepcionMercaderia recepcionInicial = new RecepcionMercaderia("OC-INIT", "GR-INIT", clientes.get(0), LocalDateTime.now().minusMonths(1), "Sistema", EstadoRecepcion.APROBADO);
        recepcionMercaderiaRepository.save(recepcionInicial);

        for (Producto prod : productos) {
            // Crear 2 lotes por producto
            for (int i = 1; i <= 2; i++) {
                Lote lote = new Lote();
                lote.setNumero("L-" + prod.getCodigoSKU() + "-00" + i);
                lote.setProducto(prod);
                lote.setFechaFabricacion(LocalDate.now().minusMonths(3));
                lote.setFechaVencimiento(LocalDate.now().plusMonths(12 + (i * 6)));
                lote.setCantidadInicial(1000);
                lote.setCantidadDisponible(1000);
                lote.setEstado(EstadoLote.DISPONIBLE);
                lote.setProveedor("Proveedor Genérico");
                loteRepository.save(lote);

                // Crear Inventario para clientes que tienen este producto
                for (Cliente cliente : prod.getClientes()) {
                    InventarioCliente inv = new InventarioCliente(
                        cliente, prod, lote, recepcionInicial, 
                        500, // Stock inicial arbitrario
                        lote.getFechaVencimiento(), 
                        EstadoInventario.ALMACENADO
                    );
                    
                    // Asignar ubicación según tipo
                    if (prod.getRequiereCadenaFrio()) {
                        inv.setUbicacion(ubicaciones.get(3)); // Frio
                    } else {
                        inv.setUbicacion(ubicaciones.get(0)); // Ambiente
                    }
                    
                    inv.setCodigoBarras("CB-" + cliente.getId().toString().substring(0,4) + "-" + lote.getNumero());
                    inv.setUsuarioRegistro(operador);
                    inventarioClienteRepository.save(inv);
                }
            }
        }
        System.out.println("Lotes e Inventario creados.");

        System.out.println("DataSeeder completado exitosamente.");
    }
}
