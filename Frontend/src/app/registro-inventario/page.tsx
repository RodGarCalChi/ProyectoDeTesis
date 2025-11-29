'use client';

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { ProtectedRoute } from '@/components/ProtectedRoute';
import { useAuth } from '@/contexts/AuthContext';
import { Navigation } from '@/components/Navigation';

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

function RegistroInventarioContent() {
  const router = useRouter();
  const { user, logout } = useAuth();
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
    observaciones: ''
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
      console.log('Cargando clientes...');
      const response = await fetch('http://localhost:8080/api/clientes');
      console.log('Response status:', response.status);
      
      if (!response.ok) {
        console.error('Error en respuesta:', response.statusText);
        return;
      }
      
      const data = await response.json();
      console.log('Datos de clientes:', data);
      
      // Manejar diferentes formatos de respuesta
      if (data.success && data.data) {
        setClientes(Array.isArray(data.data) ? data.data : []);
      } else if (Array.isArray(data)) {
        setClientes(data);
      } else {
        console.warn('Formato de respuesta inesperado para clientes');
      }
    } catch (error) {
      console.error('Error al cargar clientes:', error);
    }
  };

  const cargarZonas = async () => {
    try {
      console.log('Cargando zonas...');
      const response = await fetch('http://localhost:8080/api/zonas');
      
      if (!response.ok) {
        console.error('Error en respuesta de zonas:', response.statusText);
        return;
      }
      
      const data = await response.json();
      console.log('Datos de zonas:', data);
      
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
      console.log('Cargando unidades de medida...');
      const response = await fetch('http://localhost:8080/api/productos');
      
      if (!response.ok) {
        console.error('Error en respuesta de productos:', response.statusText);
        return;
      }
      
      const data = await response.json();
      console.log('Datos de productos para unidades:', data);
      
      let productosArray: Producto[] = [];
      
      if (data.success && data.data) {
        productosArray = Array.isArray(data.data) ? data.data : [];
      } else if (Array.isArray(data)) {
        productosArray = data;
      }
      
      const unidades = [...new Set(productosArray.map((p: Producto) => p.unidadMedida))].filter(Boolean);
      console.log('Unidades de medida encontradas:', unidades);
      setUnidadesMedida(unidades as string[]);
    } catch (error) {
      console.error('Error al cargar unidades de medida:', error);
    }
  };

  const cargarProductos = async () => {
    setLoading(true);
    try {
      let url = `http://localhost:8080/api/productos/buscar?page=${pagination.currentPage}&size=${pagination.size}`;
      
      if (filtros.clienteId) {
        url += `&clienteId=${filtros.clienteId}`;
      }
      if (filtros.nombre) {
        url += `&nombre=${encodeURIComponent(filtros.nombre)}`;
      }
      if (filtros.codigoSKU) {
        url += `&codigoSKU=${encodeURIComponent(filtros.codigoSKU)}`;
      }

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
    setFiltros(prev => ({
      ...prev,
      [name]: value
    }));
    setPagination(prev => ({ ...prev, currentPage: 0 })); // Reset a primera página
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
      console.log('Registrando mercadería del cliente:', formData);
      
      // 1. Crear el producto y asignarlo al cliente en una sola operación
      const requestData = {
        cliente: {
          id: formData.clienteId
        },
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

      console.log('Enviando datos:', requestData);

      const response = await fetch('http://localhost:8080/api/clientes/crear-con-productos', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          cliente: null, // No crear cliente nuevo
          productosExistentesIds: [],
          productosNuevos: [requestData.productoNuevo],
          clienteId: formData.clienteId,
          observaciones: formData.observaciones
        })
      });

      const result = await response.json();
      console.log('Respuesta del servidor:', result);

      if (response.ok && result.success) {
        // 2. Registrar en el inventario del cliente
        const inventarioData = {
          clienteId: formData.clienteId,
          productoId: result.data?.productos?.[0]?.id || 'temp-id',
          cantidad: parseInt(formData.cantidadRecibida),
          lote: formData.lote,
          fechaVencimiento: formData.fechaVencimiento,
          ubicacion: formData.ubicacionAlmacen,
          observaciones: formData.observaciones
        };

        console.log('Registrando en inventario:', inventarioData);

        // Mostrar mensaje de éxito
        alert(`✅ Mercadería registrada exitosamente!\n\n` +
              `📦 Producto: ${formData.nombre}\n` +
              `👤 Cliente: ${clientes.find(c => c.id === formData.clienteId)?.razonSocial}\n` +
              `📊 Cantidad: ${formData.cantidadRecibida} ${formData.unidadMedida}\n` +
              `📍 Ubicación: ${formData.ubicacionAlmacen}\n` +
              `🏷️ Lote: ${formData.lote}`);

        // Limpiar formulario
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
          observaciones: ''
        });

        // Recargar datos
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
    <div className="bg-gray-50 min-h-screen">
      <Navigation />
      
      {/* Main Content */}
      <div className="p-6">
        <div className="mb-6">
          <h1 className="mb-2 font-bold text-gray-900 text-2xl">PharmaFlow</h1>
          <h2 className="font-semibold text-gray-800 text-xl">Registro de Inventario</h2>
        </div>

        {/* Tabs */}
        <div className="mb-6">
          <div className="flex w-full max-w-lg">
            <button
              onClick={() => setActiveTab('productos')}
              className={`flex-1 flex items-center justify-center gap-2 py-2 px-4 rounded-l-lg border-2 transition-colors ${
                activeTab === 'productos'
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
              className={`flex-1 flex items-center justify-center gap-2 py-2 px-4 rounded-r-lg border-2 border-l-0 transition-colors ${
                activeTab === 'registro'
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
                    {clientes.length === 0 ? (
                      <option disabled>Cargando clientes...</option>
                    ) : (
                      clientes.map(cliente => (
                        <option key={cliente.id} value={cliente.id}>
                          {cliente.razonSocial}
                        </option>
                      ))
                    )}
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
                    {unidadesMedida.length === 0 ? (
                      <option disabled>Cargando unidades...</option>
                    ) : (
                      unidadesMedida.map(unidad => (
                        <option key={unidad} value={unidad}>
                          {unidad}
                        </option>
                      ))
                    )}
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

              {/* Información de resultados y debug */}
              <div className="flex justify-between items-center">
                <div className="text-gray-600 text-sm">
                  Mostrando {productos.length} de {pagination.totalElements} productos
                </div>
                <div className="text-gray-500 text-xs">
                  Clientes: {clientes.length} | Unidades: {unidadesMedida.length} | Zonas: {zonas.length}
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
                      <th className="px-6 py-3 font-medium text-gray-500 text-xs text-left uppercase tracking-wider">Acciones</th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                    {productos.map((producto) => {
                      // Determinar zona según condición de almacenamiento
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
                          <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                            {producto.codigoSKU}
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                            {producto.nombre}
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                            <span className="inline-flex px-2 py-1 bg-blue-100 rounded-full font-semibold text-blue-800 text-xs">
                              {producto.tipo}
                            </span>
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                            {producto.unidadMedida}
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap text-sm">
                            <span className={`inline-flex items-center gap-1 px-2 py-1 bg-${zona.color}-100 rounded-full font-semibold text-${zona.color}-800 text-xs`}>
                              <span>{zona.icono}</span>
                              {zona.tipo}
                            </span>
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap text-sm">
                            {producto.requiereCadenaFrio ? (
                              <span className="inline-flex items-center gap-1 px-2 py-1 bg-cyan-100 rounded-full font-semibold text-cyan-800 text-xs">
                                <svg className="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
                                </svg>
                                Cadena Frío
                              </span>
                            ) : (
                              <span className="inline-flex px-2 py-1 bg-green-100 rounded-full font-semibold text-green-800 text-xs">
                                Normal
                              </span>
                            )}
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                            <button className="text-blue-600 hover:text-blue-900">
                              Ver Detalle
                            </button>
                          </td>
                        </tr>
                      );
                    })}
                  </tbody>
                </table>
              )}
            </div>

            {/* Paginación */}
            {!loading && productos.length > 0 && (
              <div className="flex justify-between items-center px-6 py-4 border-gray-200 border-t">
                <div className="text-gray-700 text-sm">
                  Página {pagination.currentPage + 1} de {pagination.totalPages}
                </div>
                <div className="flex gap-2">
                  <button
                    onClick={() => setPagination(prev => ({ ...prev, currentPage: Math.max(0, prev.currentPage - 1) }))}
                    disabled={pagination.currentPage === 0}
                    className="disabled:opacity-50 bg-gray-200 hover:bg-gray-300 disabled:hover:bg-gray-200 px-4 py-2 rounded-lg text-gray-700 text-sm transition-colors disabled:cursor-not-allowed"
                  >
                    Anterior
                  </button>
                  <button
                    onClick={() => setPagination(prev => ({ ...prev, currentPage: Math.min(prev.totalPages - 1, prev.currentPage + 1) }))}
                    disabled={pagination.currentPage >= pagination.totalPages - 1}
                    className="disabled:opacity-50 bg-gray-200 hover:bg-gray-300 disabled:hover:bg-gray-200 px-4 py-2 rounded-lg text-gray-700 text-sm transition-colors disabled:cursor-not-allowed"
                  >
                    Siguiente
                  </button>
                </div>
              </div>
            )}
          </div>
        )}

        {/* Registro de Mercadería del Cliente */}
        {activeTab === 'registro' && (
          <div className="bg-white shadow-sm border border-gray-200 rounded-lg">
            <div className="p-6 border-gray-200 border-b">
              <div className="flex items-center gap-3 mb-2">
                <div className="flex justify-center items-center bg-green-100 rounded-full w-8 h-8">
                  <svg className="w-4 h-4 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
                  </svg>
                </div>
                <h3 className="font-semibold text-green-700 text-lg">Registrar Mercadería del Cliente</h3>
              </div>
              <p className="text-gray-600 text-sm">
                Registre mercadería nueva que un cliente ha traído para almacenar en nuestras instalaciones
              </p>
            </div>

            <form onSubmit={handleSubmit} className="p-6">
              {/* Sección 1: Información del Cliente */}
              <div className="mb-8">
                <h4 className="flex items-center gap-2 mb-4 font-semibold text-gray-800 text-lg">
                  <svg className="w-5 h-5 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                  </svg>
                  Cliente Propietario
                </h4>
                <div className="gap-6 grid grid-cols-1 md:grid-cols-2">
                  <div>
                    <label htmlFor="clienteId" className="block mb-2 font-medium text-gray-700 text-sm">
                      Cliente *
                    </label>
                    <select
                      id="clienteId"
                      name="clienteId"
                      value={formData.clienteId}
                      onChange={handleInputChange}
                      required
                      className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded focus:outline-none w-full text-gray-700 transition-colors appearance-none cursor-pointer"
                    >
                      <option value="">Seleccione el cliente propietario</option>
                      {clientes.map(cliente => (
                        <option key={cliente.id} value={cliente.id}>
                          {cliente.razonSocial}
                        </option>
                      ))}
                    </select>
                  </div>
                </div>
              </div>

              {/* Sección 2: Información del Producto */}
              <div className="mb-8">
                <h4 className="flex items-center gap-2 mb-4 font-semibold text-gray-800 text-lg">
                  <svg className="w-5 h-5 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
                  </svg>
                  Información del Producto
                </h4>
                <div className="gap-6 grid grid-cols-1 md:grid-cols-2">
                  {/* Código SKU */}
                  <div>
                    <label htmlFor="codigoSKU" className="block mb-2 font-medium text-gray-700 text-sm">
                      Código SKU *
                    </label>
                    <input
                      type="text"
                      id="codigoSKU"
                      name="codigoSKU"
                      value={formData.codigoSKU}
                      onChange={handleInputChange}
                      placeholder="Ej: MED-PAR-500"
                      required
                      className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded focus:outline-none w-full text-gray-700 transition-colors placeholder-gray-500"
                    />
                  </div>

                  {/* Nombre del Producto */}
                  <div>
                    <label htmlFor="nombre" className="block mb-2 font-medium text-gray-700 text-sm">
                      Nombre del Producto *
                    </label>
                    <input
                      type="text"
                      id="nombre"
                      name="nombre"
                      value={formData.nombre}
                      onChange={handleInputChange}
                      placeholder="Ej: Paracetamol 500mg"
                      required
                      className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded focus:outline-none w-full text-gray-700 transition-colors placeholder-gray-500"
                    />
                  </div>

                  {/* Tipo de Producto */}
                  <div>
                    <label htmlFor="tipo" className="block mb-2 font-medium text-gray-700 text-sm">
                      Tipo de Producto *
                    </label>
                    <select
                      id="tipo"
                      name="tipo"
                      value={formData.tipo}
                      onChange={handleInputChange}
                      required
                      className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded focus:outline-none w-full text-gray-700 transition-colors appearance-none cursor-pointer"
                    >
                      <option value="">Seleccione el tipo</option>
                      <option value="Medicamento">Medicamento</option>
                      <option value="Biologico">Biológico</option>
                      <option value="Dispositivo">Dispositivo Médico</option>
                      <option value="Controlado">Controlado</option>
                      <option value="Cosmetico">Cosmético</option>
                      <option value="Suplemento">Suplemento</option>
                    </select>
                  </div>

                  {/* Condición de Almacén */}
                  <div>
                    <label htmlFor="condicionAlmacen" className="block mb-2 font-medium text-gray-700 text-sm">
                      Condición de Almacén *
                    </label>
                    <select
                      id="condicionAlmacen"
                      name="condicionAlmacen"
                      value={formData.condicionAlmacen}
                      onChange={handleInputChange}
                      required
                      className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded focus:outline-none w-full text-gray-700 transition-colors appearance-none cursor-pointer"
                    >
                      <option value="">Seleccione condición</option>
                      <option value="Ambiente_15_25">🌡️ Ambiente (15-25°C)</option>
                      <option value="Refrigerado_2_8">❄️ Refrigerado (2-8°C)</option>
                      <option value="Congelado_m20">🧊 Congelado (-20°C)</option>
                      <option value="ULT_m70">🥶 Ultra Congelado (-70°C)</option>
                    </select>
                  </div>

                  {/* Unidad de Medida */}
                  <div>
                    <label htmlFor="unidadMedida" className="block mb-2 font-medium text-gray-700 text-sm">
                      Unidad de Medida *
                    </label>
                    <input
                      type="text"
                      id="unidadMedida"
                      name="unidadMedida"
                      value={formData.unidadMedida}
                      onChange={handleInputChange}
                      placeholder="Ej: TABLETA, AMPOLLA, FRASCO"
                      required
                      className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded focus:outline-none w-full text-gray-700 transition-colors placeholder-gray-500"
                    />
                  </div>

                  {/* Registro Sanitario */}
                  <div>
                    <label htmlFor="registroSanitario" className="block mb-2 font-medium text-gray-700 text-sm">
                      Registro Sanitario
                    </label>
                    <input
                      type="text"
                      id="registroSanitario"
                      name="registroSanitario"
                      value={formData.registroSanitario}
                      onChange={handleInputChange}
                      placeholder="Ej: EE-12345-2024"
                      className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded focus:outline-none w-full text-gray-700 transition-colors placeholder-gray-500"
                    />
                  </div>

                  {/* Vida Útil */}
                  <div>
                    <label htmlFor="vidaUtilMeses" className="block mb-2 font-medium text-gray-700 text-sm">
                      Vida Útil (meses)
                    </label>
                    <input
                      type="number"
                      id="vidaUtilMeses"
                      name="vidaUtilMeses"
                      value={formData.vidaUtilMeses}
                      onChange={handleInputChange}
                      placeholder="36"
                      min="1"
                      className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded focus:outline-none w-full text-gray-700 transition-colors placeholder-gray-500"
                    />
                  </div>

                  {/* Temperatura Mínima */}
                  <div>
                    <label htmlFor="tempMin" className="block mb-2 font-medium text-gray-700 text-sm">
                      Temperatura Mínima (°C)
                    </label>
                    <input
                      type="number"
                      id="tempMin"
                      name="tempMin"
                      value={formData.tempMin}
                      onChange={handleInputChange}
                      placeholder="15"
                      step="0.1"
                      className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded focus:outline-none w-full text-gray-700 transition-colors placeholder-gray-500"
                    />
                  </div>

                  {/* Temperatura Máxima */}
                  <div>
                    <label htmlFor="tempMax" className="block mb-2 font-medium text-gray-700 text-sm">
                      Temperatura Máxima (°C)
                    </label>
                    <input
                      type="number"
                      id="tempMax"
                      name="tempMax"
                      value={formData.tempMax}
                      onChange={handleInputChange}
                      placeholder="30"
                      step="0.1"
                      className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded focus:outline-none w-full text-gray-700 transition-colors placeholder-gray-500"
                    />
                  </div>
                </div>

                {/* Checkbox Cadena de Frío */}
                <div className="mt-4">
                  <label className="flex items-center gap-2">
                    <input
                      type="checkbox"
                      name="requiereCadenaFrio"
                      checked={formData.requiereCadenaFrio}
                      onChange={handleInputChange}
                      className="w-4 h-4 text-blue-600 border-gray-300 rounded focus:ring-blue-500"
                    />
                    <span className="text-gray-700 text-sm">
                      Este producto requiere cadena de frío (Normativa DIGEMID)
                    </span>
                  </label>
                </div>
              </div>

              {/* Sección 3: Información de Recepción */}
              <div className="mb-8">
                <h4 className="flex items-center gap-2 mb-4 font-semibold text-gray-800 text-lg">
                  <svg className="w-5 h-5 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                  </svg>
                  Información de Recepción
                </h4>
                <div className="gap-6 grid grid-cols-1 md:grid-cols-2">
                  {/* Cantidad Recibida */}
                  <div>
                    <label htmlFor="cantidadRecibida" className="block mb-2 font-medium text-gray-700 text-sm">
                      Cantidad Recibida *
                    </label>
                    <input
                      type="number"
                      id="cantidadRecibida"
                      name="cantidadRecibida"
                      value={formData.cantidadRecibida}
                      onChange={handleInputChange}
                      placeholder="100"
                      min="1"
                      required
                      className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded focus:outline-none w-full text-gray-700 transition-colors placeholder-gray-500"
                    />
                  </div>

                  {/* Lote */}
                  <div>
                    <label htmlFor="lote" className="block mb-2 font-medium text-gray-700 text-sm">
                      Número de Lote *
                    </label>
                    <input
                      type="text"
                      id="lote"
                      name="lote"
                      value={formData.lote}
                      onChange={handleInputChange}
                      placeholder="L2024001"
                      required
                      className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded focus:outline-none w-full text-gray-700 transition-colors placeholder-gray-500"
                    />
                  </div>

                  {/* Fecha de Vencimiento */}
                  <div>
                    <label htmlFor="fechaVencimiento" className="block mb-2 font-medium text-gray-700 text-sm">
                      Fecha de Vencimiento *
                    </label>
                    <input
                      type="date"
                      id="fechaVencimiento"
                      name="fechaVencimiento"
                      value={formData.fechaVencimiento}
                      onChange={handleInputChange}
                      required
                      className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded focus:outline-none w-full text-gray-700 transition-colors"
                    />
                  </div>

                  {/* Ubicación en Almacén */}
                  <div>
                    <label htmlFor="ubicacionAlmacen" className="block mb-2 font-medium text-gray-700 text-sm">
                      Ubicación en Almacén *
                    </label>
                    <select
                      id="ubicacionAlmacen"
                      name="ubicacionAlmacen"
                      value={formData.ubicacionAlmacen}
                      onChange={handleInputChange}
                      required
                      className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded focus:outline-none w-full text-gray-700 transition-colors appearance-none cursor-pointer"
                    >
                      <option value="">Seleccione ubicación</option>
                      <option value="A1-E1-P1">🌡️ Zona Seca - A1-E1-P1</option>
                      <option value="A1-E2-P1">🌡️ Zona Seca - A1-E2-P1</option>
                      <option value="B1-E1-P1">❄️ Zona Refrigerada - B1-E1-P1</option>
                      <option value="B1-E2-P1">❄️ Zona Refrigerada - B1-E2-P1</option>
                      <option value="C1-E1-P1">🧊 Zona Congelada - C1-E1-P1</option>
                      <option value="D1-E1-P1">🥶 Zona ULT - D1-E1-P1</option>
                      <option value="Q1-E1-P1">⚠️ Cuarentena - Q1-E1-P1</option>
                    </select>
                  </div>
                </div>

                {/* Observaciones */}
                <div className="mt-4">
                  <label htmlFor="observaciones" className="block mb-2 font-medium text-gray-700 text-sm">
                    Observaciones
                  </label>
                  <textarea
                    id="observaciones"
                    name="observaciones"
                    value={formData.observaciones}
                    onChange={handleInputChange}
                    placeholder="Observaciones adicionales sobre la mercadería recibida..."
                    rows={3}
                    className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded focus:outline-none w-full text-gray-700 transition-colors placeholder-gray-500 resize-none"
                  />
                </div>
              </div>

              {/* Botones de Acción */}
              <div className="flex justify-end gap-4 pt-6 border-gray-200 border-t">
                <button
                  type="button"
                  onClick={() => setFormData({
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
                    observaciones: ''
                  })}
                  className="bg-gray-200 hover:bg-gray-300 px-6 py-2 rounded-lg text-gray-700 transition-colors"
                >
                  Limpiar Formulario
                </button>
                <button
                  type="submit"
                  className="flex items-center gap-2 bg-green-600 hover:bg-green-700 px-6 py-2 rounded-lg text-white transition-colors"
                >
                  <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
                  </svg>
                  Registrar Mercadería
                </button>
              </div>
              <div className="mb-6">
                <label className="flex items-center">
                  <input
                    type="checkbox"
                    name="requiereReceta"
                    checked={formData.requiereReceta}
                    onChange={handleInputChange}
                    className="border-gray-300 rounded focus:ring-blue-500 w-4 h-4 text-blue-600"
                  />
                  <span className="ml-2 text-gray-700 text-sm">
                    Este producto requiere receta médica (Normativa DIGEMID)
                  </span>
                </label>
              </div>

              {/* Descripción */}
              <div className="mb-6">
                <label htmlFor="descripcion" className="block mb-2 font-medium text-gray-700 text-sm">
                  Descripción del Producto
                </label>
                <textarea
                  id="descripcion"
                  name="descripcion"
                  value={formData.descripcion}
                  onChange={handleInputChange}
                  placeholder="Ingrese una descripción detallada del producto, indicaciones, contraindicaciones, etc."
                  rows={4}
                  className="bg-gray-200 focus:bg-white px-3 py-2 border-2 border-gray-400 focus:border-blue-500 rounded focus:outline-none w-full text-gray-700 transition-colors resize-vertical placeholder-gray-500"
                />
              </div>

              {/* Verificación BPAs */}
              <div className="bg-yellow-50 mb-6 p-4 border border-yellow-200 rounded-lg">
                <h4 className="mb-3 font-medium text-yellow-800 text-sm">Verificación BPAs - DIGEMID</h4>
                <div className="space-y-2">
                  <label className="flex items-center">
                    <input type="checkbox" required className="border-gray-300 rounded focus:ring-blue-500 w-4 h-4 text-blue-600" />
                    <span className="ml-2 text-gray-700 text-sm">Producto cumple con normativas DIGEMID</span>
                  </label>
                  <label className="flex items-center">
                    <input type="checkbox" required className="border-gray-300 rounded focus:ring-blue-500 w-4 h-4 text-blue-600" />
                    <span className="ml-2 text-gray-700 text-sm">Documentación de registro sanitario completa</span>
                  </label>
                  <label className="flex items-center">
                    <input type="checkbox" required className="border-gray-300 rounded focus:ring-blue-500 w-4 h-4 text-blue-600" />
                    <span className="ml-2 text-gray-700 text-sm">Condiciones de almacenamiento verificadas</span>
                  </label>
                </div>
              </div>

              {/* Botones de acción */}
              <div className="flex gap-4">
                <button
                  type="submit"
                  className="bg-green-600 hover:bg-green-700 px-6 py-2 rounded font-medium text-white transition-colors"
                >
                  Registrar Producto
                </button>
                <button
                  type="button"
                  onClick={() => setFormData({
                    codigo: '',
                    nombre: '',
                    categoria: '',
                    proveedor: '',
                    precio: '',
                    stock: '',
                    stockMinimo: '',
                    ubicacion: '',
                    requiereReceta: false,
                    descripcion: ''
                  })}
                  className="bg-gray-300 hover:bg-gray-400 px-6 py-2 rounded font-medium text-gray-700 transition-colors"
                >
                  Limpiar
                </button>
              </div>
            </form>
          </div>
        )}
      </div>
    </div>
  );
}

export default function RegistroInventarioPage() {
  return (
    <ProtectedRoute requiredRole={['Operaciones', 'AreaAdministrativa', 'DirectorTecnico']}>
      <RegistroInventarioContent />
    </ProtectedRoute>
  );
}