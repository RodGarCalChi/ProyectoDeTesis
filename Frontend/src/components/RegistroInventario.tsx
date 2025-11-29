import React, { useState, useEffect } from 'react';

interface Cliente {
    id: string;
    razonSocial: string;
}

interface Producto {
    id: string;
    codigoSKU: string;
    nombre: string;
    tipo: string;
    requiereCadenaFrio: boolean;
    unidadMedida: string;
    condicionAlmacen: string;
}

interface Zona {
    id: string;
    nombre: string;
    tipo: string;
}

export function RegistroInventario() {
    const [activeTab, setActiveTab] = useState('productos');
    const [formData, setFormData] = useState({
        clienteId: '',
        codigoSKU: '',
        nombre: '',
        tipo: '',
        condicionAlmacen: '',
        requiereCadenaFrio: false,
        registroSanitario: '',
        unidadMedida: '',
        vidaUtilMeses: '',
        tempMin: '',
        tempMax: '',
        cantidadRecibida: '',
        lote: '',
        fechaVencimiento: '',
        ubicacionAlmacen: '',
        observaciones: '',
        requiereReceta: false,
        descripcion: ''
    });

    // Estados para el catálogo
    const [productos, setProductos] = useState<Producto[]>([]);
    const [clientes, setClientes] = useState<Cliente[]>([]);
    const [zonas, setZonas] = useState<Zona[]>([]);
    const [unidadesMedida, setUnidadesMedida] = useState<string[]>([]);
    const [loading, setLoading] = useState(false);
    const [filtros, setFiltros] = useState({
        clienteId: '',
        nombre: '',
        codigoSKU: '',
        unidadMedida: '',
        tipoZona: ''
    });
    const [pagination, setPagination] = useState({
        currentPage: 0,
        totalPages: 0,
        totalElements: 0,
        size: 10
    });

    // Cargar datos iniciales al montar el componente
    useEffect(() => {
        cargarClientes();
        cargarZonas();
        cargarUnidadesMedida();
    }, []);

    // Cargar productos cuando cambian los filtros
    useEffect(() => {
        if (activeTab === 'productos') {
            cargarProductos();
        }
    }, [activeTab, filtros, pagination.currentPage]);

    const cargarClientes = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/clientes');
            if (!response.ok) return;
            const data = await response.json();
            if (data.success && data.data) {
                setClientes(Array.isArray(data.data) ? data.data : []);
            } else if (Array.isArray(data)) {
                setClientes(data);
            }
        } catch (error) {
            console.error('Error al cargar clientes:', error);
        }
    };

    const cargarZonas = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/zonas');
            if (!response.ok) return;
            const data = await response.json();
            if (data.success && data.data) {
                setZonas(Array.isArray(data.data) ? data.data : []);
            } else if (Array.isArray(data)) {
                setZonas(data);
            }
        } catch (error) {
            console.error('Error al cargar zonas:', error);
        }
    };

    const cargarUnidadesMedida = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/productos');
            if (!response.ok) return;
            const data = await response.json();
            let productosArray: Producto[] = [];
            if (data.success && data.data) {
                productosArray = Array.isArray(data.data) ? data.data : [];
            } else if (Array.isArray(data)) {
                productosArray = data;
            }
            const unidades = [...new Set(productosArray.map((p: Producto) => p.unidadMedida))].filter(Boolean);
            setUnidadesMedida(unidades as string[]);
        } catch (error) {
            console.error('Error al cargar unidades de medida:', error);
        }
    };

    const cargarProductos = async () => {
        setLoading(true);
        try {
            let url = `http://localhost:8080/api/productos/buscar?page=${pagination.currentPage}&size=${pagination.size}`;
            if (filtros.clienteId) url += `&clienteId=${filtros.clienteId}`;
            if (filtros.nombre) url += `&nombre=${encodeURIComponent(filtros.nombre)}`;
            if (filtros.codigoSKU) url += `&codigoSKU=${encodeURIComponent(filtros.codigoSKU)}`;

            const response = await fetch(url);
            const data = await response.json();

            if (data.success) {
                setProductos(data.data);
                setPagination(prev => ({
                    ...prev,
                    totalPages: data.totalPages,
                    totalElements: data.totalElements,
                    currentPage: data.currentPage
                }));
            }
        } catch (error) {
            console.error('Error al cargar productos:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleFiltroChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFiltros(prev => ({ ...prev, [name]: value }));
        setPagination(prev => ({ ...prev, currentPage: 0 }));
    };

    const limpiarFiltros = () => {
        setFiltros({
            clienteId: '',
            nombre: '',
            codigoSKU: '',
            unidadMedida: '',
            tipoZona: ''
        });
        setPagination(prev => ({ ...prev, currentPage: 0 }));
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
        const { name, value, type } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? (e.target as HTMLInputElement).checked : value
        }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);

        try {
            const requestData = {
                cliente: { id: formData.clienteId },
                productoNuevo: {
                    codigoSKU: formData.codigoSKU,
                    nombre: formData.nombre,
                    tipo: formData.tipo,
                    condicionAlmacen: formData.condicionAlmacen,
                    requiereCadenaFrio: formData.requiereCadenaFrio,
                    registroSanitario: formData.registroSanitario,
                    unidadMedida: formData.unidadMedida,
                    vidaUtilMeses: formData.vidaUtilMeses ? parseInt(formData.vidaUtilMeses) : null,
                    tempMin: formData.tempMin ? parseFloat(formData.tempMin) : null,
                    tempMax: formData.tempMax ? parseFloat(formData.tempMax) : null
                },
                observaciones: formData.observaciones
            };

            const response = await fetch('http://localhost:8080/api/clientes/crear-con-productos', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    cliente: null,
                    productosExistentesIds: [],
                    productosNuevos: [requestData.productoNuevo],
                    clienteId: formData.clienteId,
                    observaciones: formData.observaciones
                })
            });

            const result = await response.json();

            if (response.ok && result.success) {
                alert(`✅ Mercadería registrada exitosamente!\n\n` +
                    `📦 Producto: ${formData.nombre}\n` +
                    `👤 Cliente: ${clientes.find(c => c.id === formData.clienteId)?.razonSocial}\n` +
                    `📊 Cantidad: ${formData.cantidadRecibida} ${formData.unidadMedida}\n` +
                    `📍 Ubicación: ${formData.ubicacionAlmacen}\n` +
                    `🏷️ Lote: ${formData.lote}`);

                setFormData({
                    clienteId: '',
                    codigoSKU: '',
                    nombre: '',
                    tipo: '',
                    condicionAlmacen: '',
                    requiereCadenaFrio: false,
                    registroSanitario: '',
                    unidadMedida: '',
                    vidaUtilMeses: '',
                    tempMin: '',
                    tempMax: '',
                    cantidadRecibida: '',
                    lote: '',
                    fechaVencimiento: '',
                    ubicacionAlmacen: '',
                    observaciones: '',
                    requiereReceta: false,
                    descripcion: ''
                });

                cargarProductos();
            } else {
                throw new Error(result.message || 'Error al registrar la mercadería');
            }
        } catch (error) {
            console.error('Error al registrar mercadería:', error);
            alert(`❌ Error al registrar la mercadería:\n${error instanceof Error ? error.message : 'Error desconocido'}`);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="bg-white shadow-sm border border-gray-200 rounded-lg">
            <div className="p-6 border-b border-gray-200">
                <h2 className="font-semibold text-gray-800 text-xl">Registro de Inventario</h2>
            </div>

            <div className="p-6">
                {/* Tabs */}
                <div className="mb-6">
                    <div className="flex w-full max-w-lg">
                        <button
                            onClick={() => setActiveTab('productos')}
                            className={`flex-1 flex items-center justify-center gap-2 py-2 px-4 rounded-l-lg border-2 transition-colors ${activeTab === 'productos'
                                    ? 'bg-gray-200 border-gray-400 text-gray-900'
                                    : 'bg-white border-gray-300 text-gray-600 hover:bg-gray-50'
                                }`}
                        >
                            <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
                            </svg>
                            Catálogo de Productos
                        </button>
                        <button
                            onClick={() => setActiveTab('registro')}
                            className={`flex-1 flex items-center justify-center gap-2 py-2 px-4 rounded-r-lg border-2 border-l-0 transition-colors ${activeTab === 'registro'
                                    ? 'bg-gray-200 border-gray-400 text-gray-900'
                                    : 'bg-white border-gray-300 text-gray-600 hover:bg-gray-50'
                                }`}
                        >
                            <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                            </svg>
                            Registrar Producto
                        </button>
                    </div>
                </div>

                {/* Catálogo de Productos */}
                {activeTab === 'productos' && (
                    <div className="bg-white shadow-sm border border-gray-200 rounded-lg">
                        <div className="p-6 border-gray-200 border-b">
                            <div className="flex items-center gap-3 mb-4">
                                <div className="flex justify-center items-center bg-blue-100 rounded-full w-8 h-8">
                                    <svg className="w-4 h-4 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
                                    </svg>
                                </div>
                                <h3 className="font-semibold text-blue-700 text-lg">Catálogo de Productos Farmacéuticos</h3>
                            </div>

                            {/* Filtros */}
                            <div className="gap-4 grid grid-cols-1 md:grid-cols-3 lg:grid-cols-6 mb-4">
                                {/* Filtro por Cliente */}
                                <div>
                                    <label className="block mb-2 font-medium text-gray-700 text-sm">
                                        Cliente {clientes.length > 0 && <span className="text-gray-400">({clientes.length})</span>}
                                    </label>
                                    <select
                                        name="clienteId"
                                        value={filtros.clienteId}
                                        onChange={handleFiltroChange}
                                        className="px-3 py-2 border border-gray-300 focus:border-blue-500 rounded-lg focus:outline-none w-full text-sm"
                                    >
                                        <option value="">Todos los clientes</option>
                                        {clientes.map(cliente => (
                                            <option key={cliente.id} value={cliente.id}>
                                                {cliente.razonSocial}
                                            </option>
                                        ))}
                                    </select>
                                </div>

                                {/* Filtro por Nombre */}
                                <div>
                                    <label className="block mb-2 font-medium text-gray-700 text-sm">
                                        Producto
                                    </label>
                                    <input
                                        type="text"
                                        name="nombre"
                                        value={filtros.nombre}
                                        onChange={handleFiltroChange}
                                        placeholder="Buscar..."
                                        className="px-3 py-2 border border-gray-300 focus:border-blue-500 rounded-lg focus:outline-none w-full text-sm"
                                    />
                                </div>

                                {/* Filtro por Código SKU */}
                                <div>
                                    <label className="block mb-2 font-medium text-gray-700 text-sm">
                                        Código SKU
                                    </label>
                                    <input
                                        type="text"
                                        name="codigoSKU"
                                        value={filtros.codigoSKU}
                                        onChange={handleFiltroChange}
                                        placeholder="Buscar..."
                                        className="px-3 py-2 border border-gray-300 focus:border-blue-500 rounded-lg focus:outline-none w-full text-sm"
                                    />
                                </div>

                                {/* Filtro por Unidad de Medida */}
                                <div>
                                    <label className="block mb-2 font-medium text-gray-700 text-sm">
                                        Unidad Medida {unidadesMedida.length > 0 && <span className="text-gray-400">({unidadesMedida.length})</span>}
                                    </label>
                                    <select
                                        name="unidadMedida"
                                        value={filtros.unidadMedida}
                                        onChange={handleFiltroChange}
                                        className="px-3 py-2 border border-gray-300 focus:border-blue-500 rounded-lg focus:outline-none w-full text-sm"
                                    >
                                        <option value="">Todas las unidades</option>
                                        {unidadesMedida.map(unidad => (
                                            <option key={unidad} value={unidad}>
                                                {unidad}
                                            </option>
                                        ))}
                                    </select>
                                </div>

                                {/* Filtro por Tipo de Zona */}
                                <div>
                                    <label className="block mb-2 font-medium text-gray-700 text-sm">
                                        Zona Asignada
                                    </label>
                                    <select
                                        name="tipoZona"
                                        value={filtros.tipoZona}
                                        onChange={handleFiltroChange}
                                        className="px-3 py-2 border border-gray-300 focus:border-blue-500 rounded-lg focus:outline-none w-full text-sm"
                                    >
                                        <option value="">Todas</option>
                                        <option value="CONGELADO">🧊 Congelado (-20°C)</option>
                                        <option value="REFRIGERADO">❄️ Refrigerado (2-8°C)</option>
                                        <option value="SECO">🌡️ Seco (15-25°C)</option>
                                        <option value="ULT">🥶 ULT (-70°C)</option>
                                    </select>
                                </div>

                                {/* Botón Limpiar */}
                                <div className="flex items-end">
                                    <button
                                        onClick={limpiarFiltros}
                                        className="bg-gray-200 hover:bg-gray-300 px-4 py-2 rounded-lg w-full text-gray-700 text-sm transition-colors"
                                    >
                                        Limpiar
                                    </button>
                                </div>
                            </div>

                            {/* Información de resultados */}
                            <div className="flex justify-between items-center">
                                <div className="text-gray-600 text-sm">
                                    Mostrando {productos.length} de {pagination.totalElements} productos
                                </div>
                            </div>
                        </div>

                        <div className="overflow-x-auto">
                            {loading ? (
                                <div className="flex justify-center items-center py-12">
                                    <div className="border-4 border-blue-200 border-t-blue-600 rounded-full w-12 h-12 animate-spin"></div>
                                </div>
                            ) : productos.length === 0 ? (
                                <div className="py-12 text-center text-gray-500">
                                    No se encontraron productos con los filtros seleccionados
                                </div>
                            ) : (
                                <table className="w-full">
                                    <thead className="bg-gray-50">
                                        <tr>
                                            <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">Código</th>
                                            <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">Producto</th>
                                            <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">Categoría</th>
                                            <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">Unidad</th>
                                            <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">Zona Requerida</th>
                                            <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">Estado</th>
                                        </tr>
                                    </thead>
                                    <tbody className="bg-white divide-y divide-gray-200">
                                        {productos.map((producto) => {
                                            const getZonaRequerida = () => {
                                                if (producto.requiereCadenaFrio) {
                                                    if (producto.condicionAlmacen?.includes('ULT') || producto.condicionAlmacen?.includes('-70')) {
                                                        return { tipo: 'ULT', icono: '🥶', color: 'purple' };
                                                    } else if (producto.condicionAlmacen?.includes('CONGELADO') || producto.condicionAlmacen?.includes('-20')) {
                                                        return { tipo: 'CONGELADO', icono: '🧊', color: 'blue' };
                                                    } else {
                                                        return { tipo: 'REFRIGERADO', icono: '❄️', color: 'cyan' };
                                                    }
                                                }
                                                return { tipo: 'SECO', icono: '🌡️', color: 'green' };
                                            };
                                            const zona = getZonaRequerida();
                                            return (
                                                <tr key={producto.id} className="hover:bg-gray-50">
                                                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{producto.codigoSKU}</td>
                                                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{producto.nombre}</td>
                                                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{producto.tipo}</td>
                                                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{producto.unidadMedida}</td>
                                                    <td className="px-6 py-4 whitespace-nowrap text-sm">
                                                        <span className={`inline-flex items-center gap-1 px-2 py-1 bg-${zona.color}-100 rounded-full font-semibold text-${zona.color}-800 text-xs`}>
                                                            <span>{zona.icono}</span>{zona.tipo}
                                                        </span>
                                                    </td>
                                                    <td className="px-6 py-4 whitespace-nowrap text-sm">
                                                        {producto.requiereCadenaFrio ? 'Cadena Frío' : 'Normal'}
                                                    </td>
                                                </tr>
                                            );
                                        })}
                                    </tbody>
                                </table>
                            )}
                        </div>
                    </div>
                )}

                {/* Registro de Mercadería */}
                {activeTab === 'registro' && (
                    <div className="bg-white shadow-sm border border-gray-200 rounded-lg">
                        <div className="p-6 border-gray-200 border-b">
                            <h3 className="font-semibold text-green-700 text-lg">Registrar Mercadería del Cliente</h3>
                        </div>
                        <form onSubmit={handleSubmit} className="p-6">
                            <div className="gap-6 grid grid-cols-1 md:grid-cols-2">
                                <div>
                                    <label className="block mb-2 font-medium text-gray-700 text-sm">Cliente *</label>
                                    <select name="clienteId" value={formData.clienteId} onChange={handleInputChange} required className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded focus:outline-none w-full text-gray-700">
                                        <option value="">Seleccione el cliente</option>
                                        {clientes.map(c => <option key={c.id} value={c.id}>{c.razonSocial}</option>)}
                                    </select>
                                </div>
                                <div>
                                    <label className="block mb-2 font-medium text-gray-700 text-sm">Código SKU *</label>
                                    <input type="text" name="codigoSKU" value={formData.codigoSKU} onChange={handleInputChange} required className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded w-full" />
                                </div>
                                <div>
                                    <label className="block mb-2 font-medium text-gray-700 text-sm">Nombre *</label>
                                    <input type="text" name="nombre" value={formData.nombre} onChange={handleInputChange} required className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded w-full" />
                                </div>
                                <div>
                                    <label className="block mb-2 font-medium text-gray-700 text-sm">Tipo *</label>
                                    <select name="tipo" value={formData.tipo} onChange={handleInputChange} required className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded w-full">
                                        <option value="">Seleccione tipo</option>
                                        <option value="Medicamento">Medicamento</option>
                                        <option value="Biologico">Biológico</option>
                                        <option value="Dispositivo">Dispositivo</option>
                                    </select>
                                </div>
                                <div>
                                    <label className="block mb-2 font-medium text-gray-700 text-sm">Condición *</label>
                                    <select name="condicionAlmacen" value={formData.condicionAlmacen} onChange={handleInputChange} required className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded w-full">
                                        <option value="">Seleccione condición</option>
                                        <option value="Ambiente_15_25">Ambiente</option>
                                        <option value="Refrigerado_2_8">Refrigerado</option>
                                        <option value="Congelado_m20">Congelado</option>
                                    </select>
                                </div>
                                <div>
                                    <label className="block mb-2 font-medium text-gray-700 text-sm">Unidad Medida *</label>
                                    <input type="text" name="unidadMedida" value={formData.unidadMedida} onChange={handleInputChange} required className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded w-full" />
                                </div>
                                <div>
                                    <label className="block mb-2 font-medium text-gray-700 text-sm">Cantidad Recibida *</label>
                                    <input type="number" name="cantidadRecibida" value={formData.cantidadRecibida} onChange={handleInputChange} required className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded w-full" />
                                </div>
                                <div>
                                    <label className="block mb-2 font-medium text-gray-700 text-sm">Lote *</label>
                                    <input type="text" name="lote" value={formData.lote} onChange={handleInputChange} required className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded w-full" />
                                </div>
                                <div>
                                    <label className="block mb-2 font-medium text-gray-700 text-sm">Vencimiento *</label>
                                    <input type="date" name="fechaVencimiento" value={formData.fechaVencimiento} onChange={handleInputChange} required className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded w-full" />
                                </div>
                                <div>
                                    <label className="block mb-2 font-medium text-gray-700 text-sm">Ubicación *</label>
                                    <select name="ubicacionAlmacen" value={formData.ubicacionAlmacen} onChange={handleInputChange} required className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded w-full">
                                        <option value="">Seleccione ubicación</option>
                                        <option value="A1-E1-P1">Zona Seca</option>
                                        <option value="B1-E1-P1">Zona Refrigerada</option>
                                    </select>
                                </div>
                            </div>
                            <div className="mt-6 flex justify-end">
                                <button type="submit" className="bg-green-600 hover:bg-green-700 text-white px-6 py-2 rounded-lg">Registrar</button>
                            </div>
                        </form>
                    </div>
                )}
            </div>
        </div>
    );
}
